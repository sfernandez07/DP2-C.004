
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AirlineManager;

@Validator
public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (airlineManager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctIdentifierNumber;

			correctIdentifierNumber = airlineManager.getIdentifierNumber().startsWith("" + airlineManager.getIdentity().getName().charAt(0) + airlineManager.getIdentity().getSurname().charAt(0));
			super.state(context, correctIdentifierNumber, "managerCode", "Manager code's first two letters correspond to his initials");
		}
		result = !super.hasErrors(context);

		return result;
	}

}
