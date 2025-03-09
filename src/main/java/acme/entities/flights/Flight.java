
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;

public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Mandatory
	@Size(min = 1, max = 50)
	@Column(nullable = false)
	private String				tag;

	@Column(nullable = false)
	@Mandatory
	private Boolean				selfTransfer;

	@Mandatory
	@DecimalMin(value = "1.00")
	@DecimalMax(value = "1000000.00")
	@Column(nullable = false)
	private Double				cost;

	@Size(max = 255)
	@Column
	@Optional
	private String				description;

	// Derived attributes -----------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Mandatory
	private Date				scheduleDeparture;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Mandatory
	private Date				scheduleArrival;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	@Mandatory
	private String				originCity;

	@NotBlank
	@Size(max = 100)
	@Column(nullable = false, length = 100)
	@Mandatory
	private String				destinationCity;

	@Column(nullable = false)
	@Mandatory
	private Integer				layovers;

	// Relationships ----------------------------------------------------------

}
