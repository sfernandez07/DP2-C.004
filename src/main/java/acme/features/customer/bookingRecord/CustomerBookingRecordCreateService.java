
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

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
		int bookingId;
		int passengerId;
		Booking booking;
		Passenger passenger;

		bookingId = super.getRequest().getData("booking", int.class);
		passengerId = super.getRequest().getData("passenger", int.class);
		booking = this.repository.findBookingById(bookingId);
		passenger = this.repository.findPassengerById(passengerId);

		super.bindObject(br);
		br.setBooking(booking);
		br.setPassenger(passenger);
	}

	@Override
	public void validate(final BookingRecord br) {
		;
	}

	@Override
	public void perform(final BookingRecord br) {
		this.repository.save(br);
	}
	/*
	 * @Override
	 * public void unbind(final BookingRecord br) {
	 * Dataset dataset;
	 * SelectChoices choicesBooking;
	 * SelectChoices choicesPassenger;
	 * 
	 * 
	 * dataset = super.getResponse().addData(dataset);
	 * }
	 */
}
