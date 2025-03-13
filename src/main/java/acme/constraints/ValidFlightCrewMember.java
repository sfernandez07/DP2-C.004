
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = FlightCrewMemberValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFlightCrewMember {

	String message() default "The employee code must start with the crew member's initials and follow the required format.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
