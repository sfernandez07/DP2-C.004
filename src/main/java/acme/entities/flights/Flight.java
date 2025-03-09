
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

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
	private Boolean				selfTransfer;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				cost;

	@ValidString(max = 255)
	@Automapped
	@Optional
	private String				description;

	// Derived attributes -----------------------------------------------------

	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@Mandatory
	private Date				scheduleDeparture;

	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	@Mandatory
	private Date				scheduleArrival;

	@ValidString(max = 100)
	@Automapped
	@Mandatory
	private String				originCity;

	@ValidString(max = 100)
	@Automapped
	@Mandatory
	private String				destinationCity;

	@Automapped
	@Mandatory
	@ValidNumber
	private Integer				layovers;

	// Relationships ----------------------------------------------------------

}
