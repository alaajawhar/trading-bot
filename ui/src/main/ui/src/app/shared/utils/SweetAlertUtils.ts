import Swal from "sweetalert2";

export class SweetAlertUtils {

  static confirmDelete(title: string, subtitle: string, onConfirmAction: () => void) {
    Swal.fire({
      title: title,
      text: subtitle,
      icon: 'error',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel',
      showCancelButton: true,
      confirmButtonColor: '#ec5135',
      cancelButtonColor: '#a89c9c',
      reverseButtons: true
    }).then(result => {
      if (result.isConfirmed) {
        onConfirmAction();
      }
    })
  }
}
