export interface MultiBarModel {
  labels: string[];
  list: MultiBarItem[];
}

export interface MultiBarItem {
  name: string;
  color: string;
  data: number[];
}
