
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

	private static final String PHONE_REGEX = "^\\+?[0-9]{6,15}$";


	@Override
	public void initialize(final ValidPhoneNumber constraintAnnotation) {
		// No initialization needed
	}

	@Override
	public boolean isValid(final String phoneNumber, final ConstraintValidatorContext context) {
		if (phoneNumber == null || phoneNumber.isEmpty())
			return true; // Allow null or empty values as they may be optional
		return Pattern.matches(PhoneNumberValidator.PHONE_REGEX, phoneNumber);
	}
}
