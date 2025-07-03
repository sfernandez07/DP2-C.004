
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getMethod().equals("POST");

		try {
			Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Integer passengerId = super.getRequest().getData("id", Integer.class);
			Passenger passenger = this.repository.findPassengerById(passengerId);
			status = status && customerId == passenger.getCustomer().getId() && passenger.isDraftMode();
		} catch (Throwable E) {
			status = false;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		Collection<BookingRecord> bookingRecords = this.repository.findAllBookingRecordsByPassengerId(passenger.getId());
		super.state(bookingRecords.isEmpty(), "*", "customer.passenger.form.error.associatedBookings");
	}

	@Override
	public void perform(final Passenger passenger) {

		Passenger deletedPassenger = this.repository.findPassengerById(passenger.getId());
		this.repository.delete(deletedPassenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}

}
