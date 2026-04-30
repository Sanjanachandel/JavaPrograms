import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OperatorApiService } from '../../../core/services/operator-api.service';
import { OperatorDto, PlanDto } from '../../../shared/models/models';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-admin-operators',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-operators.component.html',
  styleUrls: ['./admin-operators.component.css']
})
export class AdminOperatorsComponent implements OnInit {
  private operatorApi = inject(OperatorApiService);
  private toast       = inject(ToastService);

  operators = signal<OperatorDto[]>([]);
  operatorPlans = signal<PlanDto[]>([]);
  
  // Pagination for plans
  currentPlanPage = signal(0);
  totalPlanPages  = signal(0);
  planPageSize    = 10;
  
  // Forms
  showOperatorForm = false;
  isEditingOperator = false;
  editOperatorId: number | null = null;
  operatorForm = { name: '', circle: 'All India' };

  showPlanForm = false;
  isEditingPlan = false;
  editPlanId: number | null = null;
  planForm: Partial<PlanDto> & { category: string; amount: number; validity: number; description: string; type: string } = { 
    category: 'All-in-One', 
    amount: 0, 
    validity: 0, 
    description: '',
    type: 'Prepaid'
  };

  selectedOperatorId: number | null = null;
  selectedOperatorName = '';

  operatorTypes = ['Prepaid', 'Postpaid'];
  planCategories = ['All-in-One', 'Data', 'Talktime', 'Entertainment'];

  // Delete Confirmation Modal State
  showDeleteConfirm = false;
  itemToDelete: { id: number, type: 'OPERATOR' | 'PLAN' } | null = null;


  ngOnInit() {
    this.loadOperators();
  }

  loadOperators() {
    this.operatorApi.getAllOperators().subscribe({
      next: (data) => this.operators.set(data),
      error: () => this.toast.error('Failed to load operators')
    });
  }

  toggleOperatorForm() {
    this.showOperatorForm = !this.showOperatorForm;
    if (!this.showOperatorForm) this.resetOperatorForm();
  }

  editOperator(op: OperatorDto) {
    this.showOperatorForm = true;
    this.isEditingOperator = true;
    this.editOperatorId = op.id || null;
    this.operatorForm = { name: op.name, circle: op.circle };
  }

  saveOperator() {
    console.log('Submitting Operator Form:', this.operatorForm);

    if (!this.operatorForm.name || this.operatorForm.name.trim().length < 2) {
      this.toast.error('Operator name must be at least 2 characters');
      return;
    }
    if (!this.operatorForm.circle) {
      this.toast.error('Operating circle is required');
      return;
    }

    // Duplicate Check (Only for New Operators)
    if (!this.isEditingOperator) {
      const isDuplicate = this.operators().some(op => 
        op.name.toLowerCase() === this.operatorForm.name.toLowerCase() && 
        op.circle.toLowerCase() === this.operatorForm.circle.toLowerCase()
      );

      if (isDuplicate) {
        this.toast.error('An operator with this name, type, and circle already exists!');
        return;
      }
    }

    if (this.isEditingOperator && this.editOperatorId) {
      console.log('Updating Operator ID:', this.editOperatorId);
      this.operatorApi.updateOperator(this.editOperatorId, this.operatorForm).subscribe({
        next: () => {
          this.loadOperators();
          this.resetOperatorForm();
          this.toast.success('Operator updated successfully!');
        },
        error: (err) => {
          console.error('Update Operator Error:', err);
          const msg = err?.error?.message || err?.message || 'Failed to update operator';
          this.toast.error(msg);
        }
      });
    } else {
      console.log('Creating New Operator...');
      this.operatorApi.createOperator(this.operatorForm).subscribe({
        next: (res) => {
          console.log('Operator Created Successfully:', res);
          this.loadOperators();
          this.resetOperatorForm();
          this.toast.success('Operator created successfully!');
        },
        error: (err) => {
          console.error('Create Operator Error:', err);
          const msg = err?.error?.message || err?.message || 'Failed to create operator';
          this.toast.error(msg);
        }
      });
    }
  }

  deleteOperator(id: number | undefined) {
    if (!id) return;
    this.itemToDelete = { id, type: 'OPERATOR' };
    this.showDeleteConfirm = true;
  }

  resetOperatorForm() {
    this.showOperatorForm = false;
    this.isEditingOperator = false;
    this.editOperatorId = null;
    this.operatorForm = { name: '', circle: 'All India' };
  }

  viewPlans(op: OperatorDto) {
    this.selectedOperatorId = op.id || null;
    this.selectedOperatorName = op.name;
    this.loadPlans(0);
  }

  loadPlans(page: number = 0) {
    if (!this.selectedOperatorId) return;
    this.operatorApi.getPlansByOperatorPaginated(this.selectedOperatorId, page, this.planPageSize).subscribe({
      next: (res) => {
        this.operatorPlans.set(res.content);
        this.totalPlanPages.set(res.totalPages);
        this.currentPlanPage.set(res.number);
      },
      error: () => this.toast.error('Failed to load plans')
    });
  }

  nextPlanPage() {
    if (this.currentPlanPage() < this.totalPlanPages() - 1) {
      this.loadPlans(this.currentPlanPage() + 1);
    }
  }

  prevPlanPage() {
    if (this.currentPlanPage() > 0) {
      this.loadPlans(this.currentPlanPage() - 1);
    }
  }

  togglePlanForm() {
    this.showPlanForm = !this.showPlanForm;
    if (!this.showPlanForm) this.resetPlanForm();
  }

  editPlan(plan: PlanDto) {
    this.showPlanForm = true;
    this.isEditingPlan = true;
    this.editPlanId = plan.id || null;
    this.planForm = { ...plan } as any;
  }

  savePlan() {
    console.log('Submitting Plan Form:', this.planForm);
    console.log('Selected Operator ID:', this.selectedOperatorId);

    if (!this.selectedOperatorId) {
      this.toast.error('No operator selected');
      return;
    }

    // Form Validation
    if (!this.planForm.amount || this.planForm.amount <= 0) {
      this.toast.error('Please enter a valid amount (> 0)');
      return;
    }
    if (!this.planForm.validity || this.planForm.validity <= 0) {
      this.toast.error('Please enter a valid validity (> 0)');
      return;
    }
    if (!this.planForm.description || this.planForm.description.trim().length < 5) {
      this.toast.error('Description must be at least 5 characters');
      return;
    }

    // Duplicate Check (Only for New Plans)
    if (!this.isEditingPlan) {
      const isDuplicate = this.operatorPlans().some(p => 
        p.amount === this.planForm.amount && 
        p.validity === this.planForm.validity && 
        p.category === this.planForm.category &&
        p.type === this.planForm.type
      );

      if (isDuplicate) {
        this.toast.error('A plan with this amount, validity, and category already exists!');
        return;
      }
    }

    if (this.isEditingPlan && this.editPlanId) {
      console.log('Updating Plan ID:', this.editPlanId);
      this.operatorApi.updatePlan(this.editPlanId, this.planForm).subscribe({
        next: () => {
          this.loadPlans(this.currentPlanPage());
          this.resetPlanForm();
          this.toast.success('Plan updated successfully!');
        },
        error: (err) => {
          console.error('Update Plan Error:', err);
          const msg = err?.error?.message || err?.message || 'Failed to update plan';
          this.toast.error(msg);
        }
      });
    } else {
      console.log('Creating New Plan...');
      this.operatorApi.createPlan(this.selectedOperatorId, this.planForm).subscribe({
        next: (res) => {
          console.log('Plan Created Successfully:', res);
          this.loadPlans(0);
          this.resetPlanForm();
          this.toast.success('Plan added successfully!');
        },
        error: (err) => {
          console.error('Create Plan Error:', err);
          const msg = err?.error?.message || err?.message || 'Failed to create plan';
          this.toast.error(msg);
        }
      });
    }
  }

  deletePlan(id: number | undefined) {
    if (!id) return;
    this.itemToDelete = { id, type: 'PLAN' };
    this.showDeleteConfirm = true;
  }

  confirmDelete() {
    if (!this.itemToDelete) return;
    
    if (this.itemToDelete.type === 'OPERATOR') {
      this.operatorApi.deleteOperator(this.itemToDelete.id).subscribe(() => {
        if (this.selectedOperatorId === this.itemToDelete?.id) this.selectedOperatorId = null;
        this.loadOperators();
        this.toast.success('Operator deleted');
        this.cancelDelete();
      });
    } else if (this.itemToDelete.type === 'PLAN') {
      this.operatorApi.deletePlan(this.itemToDelete.id).subscribe(() => {
        this.loadPlans();
        this.toast.success('Plan deleted');
        this.cancelDelete();
      });
    }
  }

  cancelDelete() {
    this.showDeleteConfirm = false;
    this.itemToDelete = null;
  }

  consoleLog(msg: string) {
    console.log('[DEBUG]:', msg);
  }

  resetPlanForm() {
    this.showPlanForm = false;
    this.isEditingPlan = false;
    this.editPlanId = null;
    this.planForm = { category: 'All-in-One', amount: 0, validity: 0, description: '', type: 'Prepaid' };
  }
}
