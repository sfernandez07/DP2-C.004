
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = IataCodeValidator.class)
@Target({
	ElementType.FIELD, ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIataCode {

	String message() default "The IATA code must be exactly three uppercase letters.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
