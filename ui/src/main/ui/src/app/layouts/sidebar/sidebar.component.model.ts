export interface SideBarItem {
  bootstrapIcon: string;
  routing: string;
  title: string;
  child: ChildSideBarItem[];
}

export interface ChildSideBarItem {
  routing: string;
  title: string;
}


