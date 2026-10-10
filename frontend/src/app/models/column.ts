export interface BoardColumnRequest {
  name: string;
  position: number;
}

export interface updateColumnNameRequest {
  name: string;
}

export interface updateColumnPositionRequest {
  position: number;
}

export interface BoardColumnResponse {
  id: string;
  name: string;
  position: number;
}