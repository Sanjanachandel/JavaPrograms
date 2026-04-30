import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = 'http://localhost:8989/api/payments';

  constructor(private http: HttpClient) {}

  createOrder(orderData: { 
    amount: number, 
    currency: string, 
    receipt: string, 
    rechargeId: number, 
    userId: number,
    rechargeType: string 
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/create-order`, orderData);
  }

  verifyPayment(paymentData: { 
    razorpayOrderId: string, 
    razorpayPaymentId: string, 
    razorpaySignature: string,
    rechargeId: number,
    userId: number,
    amount: number,
    rechargeType: string
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/verify-payment`, paymentData);
  }
}
