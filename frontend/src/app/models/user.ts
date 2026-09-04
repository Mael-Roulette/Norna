export interface UserResponse {
  username: string,
  email: string,
  createdAt: Date
}

export interface UserRequest {
  username: string,
  email: string,
  password: string
}