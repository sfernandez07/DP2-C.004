
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (assistanceAgent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctEmployeeCode;

			correctEmployeeCode = assistanceAgent.getEmployeeCode().startsWith("" + assistanceAgent.getIdentity().getName().charAt(0) + assistanceAgent.getIdentity().getSurname().charAt(0));
			super.state(context, correctEmployeeCode, "employeeCode", "Employee code's first two letters correspond to his initials");
		}
		result = !super.hasErrors(context);

		return result;
	}

}
