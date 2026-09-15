import { spaceRole } from '../constants/roles';

export interface spaceRequest {
  name: string;
}

export interface SpaceResponse {
  id: string;
  name: string;
  members: SpaceMember[];
}

export interface SpaceResponseWithDetails {
  id: string;
  name: string;
  members: SpaceMemberWithDetails[];
}

export interface SpaceMember {
  id: string;
  role: spaceRole;
}

export interface SpaceMemberWithDetails {
  id: string;
  username: string;
  email: string;
  role: spaceRole;
}

export interface SpaceMemberRequest {
  userId: string;
  role: spaceRole;
}

export interface RemoveSpaceMemberRequest {
  userId: string;
}
