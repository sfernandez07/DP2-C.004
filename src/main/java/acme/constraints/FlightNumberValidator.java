
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.FlightLeg;
import acme.entities.flights.FlightRepository;

@Validator
public class FlightNumberValidator extends AbstractValidator<ValidFlightNumber, FlightLeg> {

	@Autowired
	private FlightRepository repository;


	@Override
	protected void initialise(final ValidFlightNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightLeg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (leg == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else {
			String flightNumber = leg.getFlightNumber();
			String airlineIataCode = leg.getFlight().getManager().getAirline().getIATAcode();
			String pattern = "^" + airlineIataCode + "\\d{4}$";
			boolean validFormat = flightNumber.matches(pattern);

			super.state(context, validFormat, "flightNumber", "acme.validation.airline-manager.flightleg.flight-number.format.message");
			result = result && validFormat;

			FlightLeg existingLeg = this.repository.findLegByFlightNumber(flightNumber);
			boolean uniqueFlightNumber = existingLeg == null || existingLeg.equals(leg);

			super.state(context, uniqueFlightNumber, "flightNumber", "acme.validation.airline-manager.flightleg.flight-number.duplicate.message");
			result = result && uniqueFlightNumber;
		}

		return result;
	}
}
