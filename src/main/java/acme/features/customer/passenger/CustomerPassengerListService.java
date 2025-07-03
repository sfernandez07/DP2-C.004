
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status = true;

		try {
			if (!super.getRequest().getData().isEmpty()) {
				Integer bookingId = super.getRequest().getData("bookingId", Integer.class);
				Booking booking = this.repository.findBookingById(bookingId);
				Integer customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
				status = booking.getCustomer().getId() == customerId;
			}
		} catch (Throwable E) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Passenger> passengers;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().getData().isEmpty())
			passengers = this.repository.findPassengersByCustomer(customerId);
		else {
			int bookingId = super.getRequest().getData("bookingId", int.class);
			passengers = this.repository.findAllPassengerByBookingId(bookingId);
		}

		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "passportNumber", "dateOfBirth", "draftMode");

		super.getResponse().addData(dataset);
	}

}
