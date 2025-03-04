
package acme.entities.flights;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;

public class FlightLeg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Integer				flightNumber;

	private Date				scheduleDeparture;

	private Date				scheduleArrival;

	private Double				durationHours;

	private LegStatus			status;

	private String				departureAirport;

	private String				arrivalAirport;

	private String				aircraft;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@ManyToOne(optional = false)
	@Valid
	@Mandatory
	private Flight				flight;
}
