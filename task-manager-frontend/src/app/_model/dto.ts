export interface APIResponseDTO<T> {
  success: boolean;
  message: string;
  data: T;
  statusCode: number;
  timestamp: string;
}

export interface AuthenticationResponseDTO {
  access_token: string;
  refresh_token: string;
}

export interface PageableResponseDTO<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ConfirmDataDTO {
    title: string;
    message: string;
    confirmText: string;
    cancelText: string;
}