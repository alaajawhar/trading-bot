import {DropdownItem} from "../services/payloads";
import {DisabledFieldItem, DisabledFieldsCard} from "../components/disabled-field-card/models";

// Use this: getValueById = (dropdowns: DropdownItem[], id: string) => getDropdownValueFromKey(dropdowns, id);
export function getDropdownValueFromKey(dropdowns: DropdownItem[], id: string): string | undefined {
  const item = dropdowns.find(item => item.id == id);
  return item?.value;
}

export function convertResponseToDisabledFieldsCard(response: any): DisabledFieldsCard {
  const disabledFieldsList: DisabledFieldItem[] = [];

  // Loop through each key-value pair in the response
  for (const key in response) {
    if (response.hasOwnProperty(key)) {
      const label = convertCamelCaseToLabel(key);  // Convert field name to label
      const value = String(response[key]);         // Get the field value as a string
      disabledFieldsList.push({label, value});   // Add to the list
    }
  }

  // Return the DisabledFieldsCard object
  return {
    list: disabledFieldsList
  };
}

export function convertCamelCaseToLabel(fieldName: string): string {
  return fieldName
    .replace(/([A-Z])/g, ' $1')  // Insert space before capital letters
    .replace(/^./, str => str.toUpperCase());  // Capitalize the first letter
}
