
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;

public class FlightLeg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true, nullable = false)
	@Mandatory
	private Integer				flightNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Mandatory
	private Date				scheduleDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Mandatory
	private Date				scheduleArrival;

	@DecimalMin(value = "0.0")
	@Column(nullable = false)
	@Mandatory
	private Double				durationHours;

	@Column(nullable = false)
	@Mandatory
	private LegStatus			status;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false)
	@Mandatory
	private String				departureAirport;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false)
	@Mandatory
	private String				arrivalAirport;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false)
	@Mandatory
	private String				aircraft;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@ManyToOne(optional = false)
	@Valid
	@Mandatory
	private Flight				flight;
}
