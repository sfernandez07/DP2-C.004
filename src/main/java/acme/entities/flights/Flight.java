
package acme.entities.flights;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;

public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Column(nullable = false)
	private String				tag;

	@Mandatory
	private Boolean				selfTransfer;

	@Mandatory
	@DecimalMin(value = "1.00")
	@DecimalMax(value = "1000000.00")
	private Double				cost;

	@ValidString
	@Optional
	private String				description;

	// Derived attributes -----------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Mandatory
	private Date				scheduleDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				scheduleArrival;

	@Mandatory
	private String				originCity;

	@Mandatory
	private String				destinationCity;

	@Mandatory
	private Integer				layovers;

	// Relationships ----------------------------------------------------------

	@OneToMany
	@Mandatory
	@Valid
	private List<FlightLeg>		flightLegs;

}
