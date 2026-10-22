export interface BoardRequest {
  name: string;
  description?: string;
}

export interface BoardResponse {
  id: string;
  name: string;
  description: string;
}