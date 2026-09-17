import { SpaceResponse } from './space';

export interface UserResponse {
  id: string;
  username: string;
  email: string;
  lastVisitedSpace: string;
  createdAt: Date;
}

export interface UserSignupRequest {
  username: string;
  email: string;
  password: string;
}

export interface UserSigninRequest {
  email: string;
  password: string;
}

export interface UpdateLastVisitedSpaceRequest {
  spaceId: string;
}
