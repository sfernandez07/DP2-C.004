
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

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
	@ValidNumber(min = 0, max = 100)
	@Mandatory
	private Double				durationHours;

	@Automapped
	@Valid
	@Mandatory
	private LegStatus			status;

	@ValidString(max = 100)
	@Automapped
	@Mandatory
	private String				departureAirport;

	@Automapped
	@ValidString(max = 100)
	@Mandatory
	private String				arrivalAirport;

	@Automapped
	@ValidString(max = 100)
	@Mandatory
	private String				aircraft;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@ManyToOne(optional = false)
	@Valid
	@Mandatory
	private Flight				flight;
}
