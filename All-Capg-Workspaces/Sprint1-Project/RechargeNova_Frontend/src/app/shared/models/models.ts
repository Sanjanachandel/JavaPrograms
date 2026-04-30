/* ============================================================
   RechargeNova – Shared Models (match backend DTOs exactly)
   ============================================================ */

// ---- Auth / User ----
export interface UserRegistrationRequest {
  name: string;
  email: string;
  password: string;
  phoneNumber: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface UserResponse {
  id: number;
  name: string;
  email: string;
  role: string;
  phoneNumber: string;
  createdAt: string;
  profilePictureUrl?: string;
}

export interface AuthResponse {
  token: string;
  user: UserResponse;
}

// ---- Operator & Plans ----
export interface PlanDto {
  id: number;
  operatorId: number;
  amount: number;
  validity: number;
  description: string;
  category: string;
  type: string; // Prepaid or Postpaid
}

export interface OperatorDto {
  id: number;
  name: string;
  circle: string;
  plans?: PlanDto[];
}

// ---- Recharge ----
export interface RechargeRequest {
  operatorId: number;
  planId: number;
  mobileNumber: string;
  paymentMethod: string;
  rechargeType: string;
}

export interface RechargeResponse {
  id: number;
  userId: number;
  operatorId: number;
  planId: number;
  mobileNumber: string;
  amount: number;
  status: string;
  paymentMethod: string;
  rechargeType: string;
  createdAt: string;
  message: string;
}

// ---- Payment ----
export interface PaymentRequest {
  rechargeId: number;
  userId: number;
  amount: number;
  paymentMethod: string;
}

export interface PaymentResponse {
  id: number;
  rechargeId: number;
  userId: number;
  amount: number;
  paymentMethod: string;
  status: string;
  transactionTime: string;
}

// ---- UI State ----
export type ToastType = 'success' | 'error' | 'info';

export interface Toast {
  id: string;
  type: ToastType;
  message: string;
}

export type PlanCategory = 'ALL' | 'All-in-One' | 'Data' | 'Talktime';

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}
