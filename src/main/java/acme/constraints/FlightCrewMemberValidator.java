
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.FlightCrewMember;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Override
	public boolean isValid(final FlightCrewMember flightCrewMember, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (flightCrewMember == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctEmployeeCode;

			correctEmployeeCode = flightCrewMember.getEmployeeCode().startsWith("" + flightCrewMember.getIdentity().getName().charAt(0) + flightCrewMember.getIdentity().getSurname().charAt(0));
			super.state(context, correctEmployeeCode, "employeeCode", "Employee code's first two letters correspond to his initials");
		}
		result = !super.hasErrors(context);

		return result;
	}

}
