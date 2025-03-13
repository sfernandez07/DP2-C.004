
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightLeg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@Mandatory
	@ValidNumber(max = 50)
	private Integer				flightNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@ValidMoment
	@Mandatory
	private Date				scheduleDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@ValidMoment
	@Mandatory
	private Date				scheduleArrival;

	@Automapped
	@Valid
	@Mandatory
	private LegStatus			status;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Double getDurationHours() {
		if (this.scheduleDeparture == null || this.scheduleArrival == null)
			return null;
		long millis = this.scheduleArrival.getTime() - this.scheduleDeparture.getTime();
		return millis / (1000.0 * 60 * 60);
	}

	// Relationships ----------------------------------------------------------


	@Valid
	@ManyToOne(optional = false)
	@Mandatory
	private Airport		departureAirport;

	@Valid
	@ManyToOne(optional = false)
	@Mandatory
	private Airport		arrivalAirport;

	@Valid
	@ManyToOne(optional = false)
	@Mandatory
	private Aircraft	aircraft;

	@ManyToOne(optional = false)
	@Valid
	@Mandatory
	private Flight		flight;
}
