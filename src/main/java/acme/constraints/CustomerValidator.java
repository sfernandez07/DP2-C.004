
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.UserAccount;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		if (!this.isIdentifierValid(customer, context))
			return false;

		UserAccount userAccount = customer.getUserAccount();
		if (!this.isUserAccountValid(userAccount, context))
			return false;

		if (!this.doInitialsMatchIdentifier(customer, userAccount, context))
			return false;

		return true;
	}

	private boolean isIdentifierValid(final Customer customer, final ConstraintValidatorContext context) {
		String identifier = customer.getIdentifier();
		boolean valid = identifier != null && !identifier.isBlank() && identifier.matches("^[A-Z]{2,3}\\d{6}$");

		if (!valid)
			this.buildViolation(context, "{acme.validation.identifier.invalidIdentifier.message}");

		return valid;
	}

	private boolean isUserAccountValid(final UserAccount account, final ConstraintValidatorContext context) {
		if (account == null || account.getIdentity() == null) {
			this.buildViolation(context, "");
			return false;
		}

		String name = account.getIdentity().getName();
		String surname = account.getIdentity().getSurname();

		if (name == null || name.isBlank() || surname == null || surname.isBlank()) {
			this.buildViolation(context, "");
			return false;
		}

		return true;
	}

	private boolean doInitialsMatchIdentifier(final Customer customer, final UserAccount userAccount, final ConstraintValidatorContext context) {
		String name = userAccount.getIdentity().getName();
		String[] surnames = userAccount.getIdentity().getSurname().split(" ");

		String initials = String.valueOf(name.charAt(0)).toUpperCase() + String.valueOf(surnames[0].charAt(0)).toUpperCase();

		if (surnames.length > 1)
			initials += String.valueOf(surnames[1].charAt(0)).toUpperCase();

		String identifier = customer.getIdentifier();
		String expectedPrefix = identifier.substring(0, initials.length());

		if (!initials.equals(expectedPrefix)) {
			this.buildViolation(context, "{acme.validation.customer.startIdentifier.message}");
			return false;
		}

		return true;
	}

	private void buildViolation(final ConstraintValidatorContext context, final String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
