import {DropdownItem} from "../services/payloads";

// Use this: getValueById = (dropdowns: DropdownItem[], id: string) => getDropdownValueFromKey(dropdowns, id);
export function getDropdownValueFromKey(dropdowns: DropdownItem[], id: string): string | undefined {
  const item = dropdowns.find(item => item.id == id);
  return item?.value;
}
