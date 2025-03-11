
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation

@Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "The phone number must be in the correct format: optional '+' followed by 6 to 15 digits.")

public @interface ValidPhoneNumber {

	// Standard validation properties -----------------------------------------

	String message() default "{acme.validation.phone.message}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
