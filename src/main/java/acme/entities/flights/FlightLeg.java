
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
import acme.constraints.ValidFlightLeg;
import acme.constraints.ValidFlightNumber;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightLeg
@ValidFlightNumber
public class FlightLeg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@Mandatory
	@Valid
	private String				flightNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	@Mandatory
	private Date				scheduledDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	@Mandatory
	private Date				scheduledArrival;

	@Automapped
	@Valid
	@Mandatory
	private LegStatus			status;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Double getDurationHours() {
		if (this.scheduledDeparture == null || this.scheduledArrival == null)
			return null;
		long millis = this.scheduledArrival.getTime() - this.scheduledDeparture.getTime();
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

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Flight		flight;
}
