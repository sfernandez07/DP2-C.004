
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		BookingRecord br = new BookingRecord();
		super.getBuffer().addData(br);
	}

	@Override
	public void bind(final BookingRecord br) {
		int passengerId;
		Passenger passenger;
		int bookingId;
		Booking booking;

		passengerId = super.getRequest().getData("passenger", int.class);
		passenger = this.repository.findPassengerById(passengerId);
		bookingId = super.getRequest().getData("passenger", int.class);
		booking = this.repository.findBookingById(bookingId);

		super.bindObject(br);
		br.setPassenger(passenger);
		br.setBooking(booking);
	}

	@Override
	public void validate(final BookingRecord br) {
		;
	}

	@Override
	public void perform(final BookingRecord br) {
		this.repository.save(br);
	}

	@Override
	public void unbind(final BookingRecord br) {
		Dataset dataset;
		SelectChoices choicesPassengers;
		SelectChoices choicesBookings;
		Collection<Passenger> passengers = this.repository.findAllPassengers();
		Integer id = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.repository.findBookingsByCustomerId(id);

		choicesPassengers = SelectChoices.from(passengers, "fullName", br.getPassenger());
		choicesBookings = SelectChoices.from(bookings, "locatorCode", br.getBooking());

		dataset = super.unbindObject(br);
		dataset.put("passenger", choicesPassengers.getSelected().getKey());
		dataset.put("passengers", choicesPassengers);
		dataset.put("booking", choicesBookings.getSelected().getKey());
		dataset.put("bookings", choicesBookings);

		super.getResponse().addData(dataset);
	}
}
