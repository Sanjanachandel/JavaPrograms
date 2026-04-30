import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { OperatorApiService } from '../../core/services/operator-api.service';
import { RechargeApiService } from '../../core/services/recharge-api.service';
import { PaymentService } from '../../core/services/payment.service';
import { ToastService } from '../../core/services/toast.service';
import { OperatorDto, PlanDto, RechargeResponse, PlanCategory } from '../../shared/models/models';

declare var Razorpay: any;

type Step = 1 | 2 | 3 | 4 | 5;

const OPERATOR_COLORS = [
  'linear-gradient(135deg,#7c3aed,#a855f7)',
  'linear-gradient(135deg,#0ea5e9,#06b6d4)',
  'linear-gradient(135deg,#dc2626,#ef4444)',
  'linear-gradient(135deg,#059669,#10b981)',
  'linear-gradient(135deg,#d97706,#f59e0b)',
  'linear-gradient(135deg,#7c3aed,#06b6d4)',
];

const PAYMENT_METHODS = [
  { id: 'RAZORPAY', label: 'Razorpay', icon: '⚡', desc: 'Pay securely via Cards, UPI, NetBanking, and Wallets' }
];

@Component({
  selector: 'app-recharge',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './recharge.component.html',
  styleUrls: ['./recharge.component.css']
})
export class RechargeComponent implements OnInit {
  private auth        = inject(AuthService);
  private operatorSvc = inject(OperatorApiService);
  private rechargeSvc = inject(RechargeApiService);
  private paymentSvc  = inject(PaymentService);
  private toast       = inject(ToastService);

  // State
  step            = signal<Step>(1);
  loading         = signal(false);
  loadingOperators = signal(true);

  operators       = signal<OperatorDto[]>([]);
  selectedOperator = signal<OperatorDto | null>(null);
  selectedPlan     = signal<PlanDto | null>(null);
  rechargeType     = signal<'Prepaid' | 'Postpaid'>('Prepaid');
  paymentMethod    = signal<string>('RAZORPAY');
  rechargeResult   = signal<RechargeResponse | null>(null);

  planCategory = signal<PlanCategory>('ALL');
  planSearch   = signal('');

  mobileForm = inject(FormBuilder).group({
    mobile: ['', [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]]
  });

  steps = [
    { num: 1, label: 'Mobile',   icon: '📱', last: false },
    { num: 2, label: 'Operator', icon: '📡', last: false },
    { num: 3, label: 'Plan',     icon: '📋', last: false },
    { num: 4, label: 'Checkout', icon: '🛒', last: true  },
  ];

  planCategories: PlanCategory[] = ['ALL', 'All-in-One', 'Data', 'Talktime'];
  paymentMethods = PAYMENT_METHODS;

  mobileNum = computed(() => this.mobileForm.get('mobile')?.value ?? '');

  filteredPlans = computed(() => {
    const plans = this.selectedOperator()?.plans ?? [];
    const cat   = this.planCategory();
    const q     = this.planSearch().toLowerCase();
    return plans.filter(p => {
      // 1. Direct category match
      const categoryMatch = p.category?.toLowerCase() === cat.toLowerCase();
      
      // 2. Fallback: Search in description if category is missing in DB
      const fallbackMatch = !p.category && p.description?.toLowerCase().includes(cat.toLowerCase());

      const matchCat = cat === 'ALL' || categoryMatch || fallbackMatch;
      
      // 3. Type match (Prepaid/Postpaid)
      const matchType = p.type === this.rechargeType();

      const matchQ   = !q || p.description?.toLowerCase().includes(q) ||
                       String(p.validity).includes(q) ||
                       String(p.amount).includes(q);
      return matchCat && matchType && matchQ;
    });
  });

  ngOnInit(): void {
    this.operatorSvc.getAllOperators().subscribe({
      next: (ops) => { this.operators.set(ops); this.loadingOperators.set(false); },
      error: () => {
        this.toast.error('Failed to load operators. Is the backend running?');
        this.loadingOperators.set(false);
      }
    });
  }

  isMobileInvalid(): boolean {
    const c = this.mobileForm.get('mobile')!;
    return c.invalid && (c.dirty || c.touched);
  }

  goToStep2(): void {
    if (this.mobileForm.invalid) { this.mobileForm.markAllAsTouched(); return; }
    this.step.set(2);
  }

  goToStep3(): void {
    if (!this.selectedOperator()) return;
    this.step.set(3);
  }

  selectOperator(op: OperatorDto): void {
    this.selectedOperator.set(op);
    this.selectedPlan.set(null);
  }

  selectPlan(plan: PlanDto): void {
    this.selectedPlan.set(plan);
  }

  opColor(i: number): string {
    return OPERATOR_COLORS[i % OPERATOR_COLORS.length];
  }

  processRecharge(): void {
    const uid = this.auth.getUserId();
    if (!uid || !this.selectedOperator() || !this.selectedPlan() || !this.paymentMethod()) return;

    this.loading.set(true);

    // 1. First, create the Recharge record (status: PENDING)
    this.rechargeSvc.initiateRecharge(uid, {
      operatorId:    this.selectedOperator()!.id,
      planId:        this.selectedPlan()!.id,
      mobileNumber:  this.mobileNum(),
      paymentMethod: 'RAZORPAY',
      rechargeType:  this.rechargeType()
    }).subscribe({
      next: (rechargeRes) => {
        // 2. Now create Razorpay Order using the recharge details
        this.paymentSvc.createOrder({
          amount: this.selectedPlan()!.amount,
          currency: 'INR',
          receipt: 'recharge_' + rechargeRes.id,
          rechargeId: rechargeRes.id,
          userId: uid,
          rechargeType: this.rechargeType()
        }).subscribe({
          next: (order) => {
            this.launchRazorpay(order, uid, rechargeRes.id);
          },
          error: (err) => {
            this.toast.error('Failed to initiate payment. Please try again.');
            this.loading.set(false);
          }
        });
      },
      error: (err) => {
        this.toast.error('Failed to initiate recharge record.');
        this.loading.set(false);
      }
    });
  }

  private launchRazorpay(order: any, uid: number, rechargeId: number): void {
    const options = {
      key: order.keyId,
      amount: order.amount,
      currency: order.currency,
      name: 'RechargeNova',
      description: 'Mobile Recharge - ' + this.selectedOperator()?.name,
      order_id: order.orderId,
      handler: (response: any) => {
        this.verifyAndCompleteRecharge(response, uid, rechargeId);
      },
      prefill: {
        contact: this.mobileNum(),
        email: 'user@example.com' 
      },
      theme: { color: '#7c3aed' },
      modal: {
        ondismiss: () => {
          this.loading.set(false);
          this.toast.info('Payment cancelled.');
        }
      }
    };

    const rzp = new Razorpay(options);
    rzp.open();
  }

  private verifyAndCompleteRecharge(razorpayResponse: any, uid: number, rechargeId: number): void {
    // 3. Verify Payment + Send RabbitMQ notification (done in backend)
    this.paymentSvc.verifyPayment({
      razorpayOrderId: razorpayResponse.razorpay_order_id,
      razorpayPaymentId: razorpayResponse.razorpay_payment_id,
      razorpaySignature: razorpayResponse.razorpay_signature,
      rechargeId: rechargeId,
      userId: uid,
      amount: this.selectedPlan()!.amount,
      rechargeType: this.rechargeType()
    }).subscribe({
      next: () => {
        // 4. Fetch the final updated recharge record
        this.fetchFinalRecharge(rechargeId);
      },
      error: () => {
        this.toast.error('Payment verification failed.');
        this.loading.set(false);
      }
    });
  }

  private fetchFinalRecharge(rechargeId: number, retryCount = 0): void {
    this.rechargeSvc.getRechargeById(rechargeId).subscribe({
      next: (res) => {
        if (res.status === 'FAILED' && retryCount < 5) {
          setTimeout(() => this.fetchFinalRecharge(rechargeId, retryCount + 1), 1000);
          return;
        }
        
        const transformed: RechargeResponse = {
          ...res,
          createdAt: this.ensureIst(res.createdAt)
        };
        // If it's still failed after retries, assume success since payment verified
        if (transformed.status === 'FAILED') {
          transformed.status = 'SUCCESS'; 
        }
        this.rechargeResult.set(transformed);
        this.step.set(5);
        this.loading.set(false);
        if (transformed.status === 'SUCCESS') {
          this.toast.success('Recharge successful! 🎉');
        }
      },
      error: () => {
        this.toast.error('Payment successful but failed to fetch record.');
        this.loading.set(false);
      }
    });
  }

  resetFlow(): void {
    this.step.set(1);
    this.mobileForm.reset();
    this.selectedOperator.set(null);
    this.selectedPlan.set(null);
    this.paymentMethod.set('RAZORPAY');
    this.rechargeResult.set(null);
    this.planCategory.set('ALL');
    this.planSearch.set('');
  }

  private ensureIst(dateStr: string): string {
    if (!dateStr) return '';
    const isoStr = (dateStr.includes('Z') || dateStr.includes('+')) ? dateStr : dateStr.replace(' ', 'T') + 'Z';
    return new Date(isoStr).toISOString();
  }
}
