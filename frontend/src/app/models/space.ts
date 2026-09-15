import { spaceRole } from '../constants/roles';

export interface SpaceRequest {
  spaceId: string;
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