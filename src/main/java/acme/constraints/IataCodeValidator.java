
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IataCodeValidator implements ConstraintValidator<ValidIataCode, String> {

	private static final String IATA_CODE_REGEX = "^[A-Z]{3}$";


	@Override
	public void initialize(final ValidIataCode constraintAnnotation) {
		// No initialization needed
	}

	@Override
	public boolean isValid(final String iataCode, final ConstraintValidatorContext context) {
		if (iataCode == null || iataCode.isEmpty())
			return false; // IATA code is mandatory, so it cannot be null or empty
		return Pattern.matches(IataCodeValidator.IATA_CODE_REGEX, iataCode);
	}
}
