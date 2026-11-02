import { UserResponse } from "./user";

export interface TaskRequest {
  title: string;
  description?: string;
  dueDate?: Date;
  assignedUserIds?: string[];
}

export interface TaskResponse {
  id: string;
  title: string;
  description: string;
  completed: boolean;
  position: number;
  createdAt: Date;
  updatedAt: Date;
  dueDate: Date;
  completedAt: Date;
  assignedUsers: UserResponse[];
}