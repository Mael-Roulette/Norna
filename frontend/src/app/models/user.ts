export interface UserResponse {
  username: string,
  email: string,
  createdAt: Date
}

export interface UserSignupRequest {
  username: string,
  email: string,
  password: string
}

export interface UserSigninRequest {
  email: string,
  password: string
}