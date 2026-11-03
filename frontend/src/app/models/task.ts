import { UserResponse } from "./user";

export interface TaskFormModel {
  title: string;
  description: string;
  dueDate: Date | null;
  assignedUserIds: string[];
}

export interface TaskRequest {
  title: string;
  description: string;
  dueDate: string | null;
  assignedUserIds: string[] | null;
}

export interface TaskResponse {
  id: string;
  title: string;
  description: string;
  completed: boolean;
  position: number;
  createdAt: Date;
  updatedAt: Date;
  dueDate: string;
  completedAt: Date;
  assignedUsers: UserResponse[];
}