
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
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		status = booking != null && booking.getDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking b;
		int id;

		id = super.getRequest().getData("id", int.class);
		b = this.repository.findBookingById(id);

		super.getBuffer().addData(b);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCreditNibble");
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.delete(booking);
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
