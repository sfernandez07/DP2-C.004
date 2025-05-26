
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.entities.flights.Flight;
import acme.entities.flights.FlightLeg;
import acme.entities.flights.FlightRepository;

public class FlightLegValidator extends AbstractValidator<ValidFlightLeg, FlightLeg> {

	@Override
	public boolean isValid(final FlightLeg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (leg == null)
			return false;

		if (leg.getFlight() == null || leg.getScheduledDeparture() == null || leg.getScheduledArrival() == null)
			return true;
		else {
			Flight flight = leg.getFlight();
			FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
			List<FlightLeg> allLegs = repository.findLegsByFlight(flight.getId());

			for (FlightLeg otherLeg : allLegs) {
				if (Objects.equals(otherLeg.getId(), leg.getId()))
					continue;

				boolean overlaps = MomentHelper.isInRange(leg.getScheduledDeparture(), otherLeg.getScheduledDeparture(), otherLeg.getScheduledArrival());

				super.state(context, !overlaps, "scheduledDeparture", "acme.validation.airline-manager.flightleg.valid-flightleg.overlap");
				result = result && !overlaps;
			}

			boolean validDuration = !MomentHelper.isBefore(leg.getScheduledArrival(), MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 1, ChronoUnit.MINUTES));

			super.state(context, validDuration, "scheduledArrival", "acme.validation.airline-manager.flightleg.valid-flightleg.time-conflict");
			result = result && validDuration;
			allLegs.sort(Comparator.comparing(FlightLeg::getScheduledDeparture));

			for (int i = 0; i < allLegs.size() - 1; i++) {
				FlightLeg current = allLegs.get(i);
				FlightLeg next = allLegs.get(i + 1);

				if (current == null || next == null || current.getScheduledArrival() == null || next.getScheduledDeparture() == null)
					continue;
				boolean correctOrder = !MomentHelper.isAfter(current.getScheduledArrival(), next.getScheduledDeparture());

				super.state(context, correctOrder, "scheduledArrival", "acme.validation.airline-manager.flightleg.valid-flightleg.sequence-order");
				result = result && correctOrder;
			}
		}

		return result;
	}
}
