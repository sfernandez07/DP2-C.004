
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation

@NotBlank
@Pattern(regexp = "^[A-Z0-9]{6,8}$")

public @interface ValidLocatorCode {

	String message() default "{acme.validation.booking.locatorCode.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
