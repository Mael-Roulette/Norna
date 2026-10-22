import { BoardResponse } from "./board";

export interface ProjectRequest {
  name: string;
  boardsId: string[];
}

export interface ProjectNameUpdateRequest {
  name: string;
}

export interface ProjectBoardsUpdateRequest {
  boardIds: string[];
}

export interface ProjectResponse {
  id: string;
  name: string;
  boards: BoardResponse[] | [];
}