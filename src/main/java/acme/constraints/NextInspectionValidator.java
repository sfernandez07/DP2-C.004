
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.maintenanceRecords.MaintenanceRecord;

@Validator
public class NextInspectionValidator extends AbstractValidator<ValidNextInspection, MaintenanceRecord> {

	@Override
	public boolean isValid(final MaintenanceRecord maintenanceRecord, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (maintenanceRecord == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else {
			boolean validNextInspection = true;

			// Validación: nextInspectionDueDate debe ser al menos 1 minuto después de moment
			Date maintenanceMoment = maintenanceRecord.getMaintenanceMoment();
			Date nextInspectionDueDate = maintenanceRecord.getNextInspectionDueDate();

			if (maintenanceMoment != null && nextInspectionDueDate != null) {
				validNextInspection = nextInspectionDueDate.after(maintenanceMoment);
				super.state(context, validNextInspection, "nextInspectionDueDate", "The next inspection date must be after the maintenance moment.");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}
}
