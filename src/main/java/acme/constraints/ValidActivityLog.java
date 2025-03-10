
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ActivityLogValidator.class)
@Target({
	ElementType.TYPE
})

@Retention(RetentionPolicy.RUNTIME)
public @interface ValidActivityLog {

	String message() default "An ActivityLog entry must be registered after the FlightLeg has taken place.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
