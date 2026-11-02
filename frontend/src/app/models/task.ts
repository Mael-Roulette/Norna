import { UserResponse } from "./user";

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