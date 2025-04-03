
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking = new Booking();
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		Integer flightId;
		flightId = super.getRequest().getData("flight", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCreditNibble");
		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		Collection<Flight> flights = this.repository.findAllFlights();
		SelectChoices choices2;

		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		choices2 = SelectChoices.from(flights, "description", booking.getFlight());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCreditNibble");
		dataset.put("flight", choices2.getSelected().getKey());
		dataset.put("travelClassChoices", choices);
		dataset.put("flightChoices", choices2);

		super.getResponse().addData(dataset);
	}
}
