
package acme.entities.flights;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Automapped
	@Mandatory
	private SelfTransfer		selfTransfer;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				cost;

	@ValidString(max = 255)
	@Automapped
	@Optional
	private String				description;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		Date departure = repository.findScheduledDeparture(this.getId());

		if (departure != null && !MomentHelper.isInRange(departure, MomentHelper.parse("2000-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"), MomentHelper.parse("2201-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss")))
			departure = null;

		return departure;
	}

	@Transient
	public Date getScheduledArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		Date arrival = repository.findScheduledArrival(this.getId());
		Date departure = this.getScheduledDeparture();
		Date minArrival = Date.from(departure.toInstant().plus(1, ChronoUnit.MINUTES));

		if (arrival == null || MomentHelper.isBefore(arrival, minArrival))
			arrival = minArrival;

		if (!MomentHelper.isInRange(arrival, MomentHelper.parse("2000-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"), MomentHelper.parse("2201-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss")))
			arrival = null;

		return arrival;
	}

	@Transient
	public String getOriginCity() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		return repository.findOriginCity(this.getId());
	}

	@Transient
	public String getDestinationCity() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		return repository.findDestinationCity(this.getId());
	}

	@Transient
	public Integer getLayovers() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		Integer legs = repository.countLegs(this.getId());

		if (legs == null || legs <= 1)
			return 0;
		return legs - 1;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	@JoinColumn(name = "airline_id")
	private Airline airline;

}
