
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@ValidAirlineManager
@Getter
@Setter
public class AirlineManager extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				identifierNumber;

	@Mandatory
	@ValidNumber(min = 0, max = 70)
	@Automapped
	private Integer				yearsOfExperience;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.DATE)
	@Automapped
	private Date				dateOfBirth;

	@Optional
	@ValidUrl
	@Automapped
	private String				picture;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
