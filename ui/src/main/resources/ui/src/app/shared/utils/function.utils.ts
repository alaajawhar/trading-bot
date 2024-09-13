import {DropdownItem} from "../services/payloads";

export function getValueById(dropdowns: DropdownItem[], id: string): string | undefined {
  const item = dropdowns.find(item => item.id == id);
  return item?.value;
}
