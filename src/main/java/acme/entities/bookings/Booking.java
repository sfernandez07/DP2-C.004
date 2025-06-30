
package acme.entities.bookings;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidCreditNibble;
import acme.constraints.ValidLocatorCode;
import acme.entities.flights.Flight;
import acme.features.authenticated.booking.BookingRepository;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidLocatorCode
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Optional
	@ValidCreditNibble
	@Automapped
	private String				lastCreditNibble;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;


	@Transient
	public Money getPrice() {
		Money result = new Money();

		if (this.getFlight() == null) {
			result.setAmount(0.0);
			result.setCurrency("EUR");
			return result;
		}

		Money flightCost = this.getFlight().getCost();
		BookingRepository bookingRepository = SpringHelper.getBean(BookingRepository.class);
		Integer passengerCount = bookingRepository.findAllPassengersByBookingId(this.getId());

		result.setCurrency(flightCost.getCurrency());
		result.setAmount(flightCost.getAmount() * passengerCount);

		return result;
	}
}
