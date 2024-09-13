export interface HeaderNotification {
  message: string;
  items: NotificationItem[]
}

export interface NotificationItem {
  id: string;
  bootstrapIcon: string;
  title: string;
  subtitle1: string;
  subtitle2: string;
}

export interface HeaderProfile {
  fullName: string;
  role: string;
  items: ProfileItem[]
}

export interface ProfileItem {
  id: string;
  bootstrapIcon: string;
  onClick: () => void;
  title: string;
}
