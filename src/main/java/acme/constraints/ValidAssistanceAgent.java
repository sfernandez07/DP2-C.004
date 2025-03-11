
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = AssistanceAgentValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidAssistanceAgent {

	// Standard validation properties -----------------------------------------

	String message() default "The first two or three letters of the employee code have to correspond to their initials";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
