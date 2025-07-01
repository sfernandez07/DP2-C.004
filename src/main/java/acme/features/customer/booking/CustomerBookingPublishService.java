
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		try {
			status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

			super.getResponse().setAuthorised(status);
			if (!super.getRequest().getMethod().equals("POST"))
				super.getResponse().setAuthorised(false);
			else {

				int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
				int bookingId = super.getRequest().getData("id", int.class);
				Booking booking = this.repository.findBookingById(bookingId);
				Integer flightId = super.getRequest().getData("flight", Integer.class);
				if (flightId == null)
					status = false;
				else if (flightId != 0) {
					Flight flight = this.repository.getFlightById(flightId);
					status = status && flight != null && !flight.isDraftMode();
				}

				status = status && customerId == booking.getCustomer().getId();
				super.getResponse().setAuthorised(status);
			}
		} catch (Throwable t) {
			super.getResponse().setAuthorised(false);
		}

	}

	@Override
	public void load() {

		int id = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "travelClass", "lastCreditNibble");
	}

	@Override
	public void validate(final Booking booking) {
		Booking existing = this.repository.findBookingByLocator(booking.getLocatorCode());
		boolean valid = existing == null || existing.getId() == booking.getId();
		super.state(valid, "locatorCode", "customer.booking.form.error.duplicateLocatorCode");

		Collection<BookingRecord> bookingRecords = this.repository.findAllBookingRecordsByBookingId(booking.getId());
		valid = !bookingRecords.isEmpty();
		super.state(valid, "*", "customer.booking.form.error.noPassengers");

		valid = bookingRecords.stream().filter(br -> br.getPassenger().isDraftMode()).findFirst().isEmpty();
		super.state(valid, "*", "customer.booking.form.error.publishPassengers");

		valid = booking.getFlight() != null;
		super.state(valid, "flight", "customer.booking.form.error.invalidFlight");

		valid = booking.getLastCreditNibble() != null && !booking.getLastCreditNibble().isBlank();
		super.state(valid, "lastCreditNibble", "customer.booking.form.error.lastNibbleNeeded");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices travelClasses;

		travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Flight> flights = this.repository.findAllPublishedFlights();

		dataset = super.unbindObject(booking, "flight", "locatorCode", "travelClass", "price", "lastCreditNibble", "draftMode", "id");
		dataset.put("travelClasses", travelClasses);
		SelectChoices flightChoices;

		flightChoices = SelectChoices.from(flights, "flightDescription", booking.getFlight());

		dataset.put("flights", flightChoices);
		super.getResponse().addData(dataset);
	}

}
