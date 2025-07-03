
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getMethod().equals("POST");

		try {

			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Integer bookingId = super.getRequest().getData("id", Integer.class);
			Booking booking = this.repository.findBookingById(bookingId);
			status = status && booking != null && customerId == booking.getCustomer().getId() && booking.isDraftMode();

		} catch (Throwable E) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "travelClass", "lastCreditNibble");
	}

	@Override
	public void validate(final Booking booking) {

		Collection<BookingRecord> bookingRecords = this.repository.findAllBookingRecordsByBookingId(booking.getId());
		boolean valid = bookingRecords.isEmpty();
		super.state(valid, "*", "customer.booking.form.error.stillPassengers");
	}

	@Override
	public void perform(final Booking booking) {

		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Flight> flights = this.repository.findAllPublishedFlights();

		dataset = super.unbindObject(booking, "flight", "locatorCode", "travelClass", "lastCreditNibble", "draftMode", "id", "price");
		dataset.put("travelClasses", travelClasses);
		SelectChoices flightChoices;
		Flight selectedFlight = this.repository.findBookingById(booking.getId()).getFlight();
		flightChoices = SelectChoices.from(flights, "flightDescription", selectedFlight);

		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);

	}

}
