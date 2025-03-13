
package acme.entities.airlines;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				name;

	@Mandatory
	@Column(unique = true)
	@Valid
	private String				iataCode;

	@Mandatory
	@Automapped
	@ValidUrl
	private String				website;

	@Mandatory
	@Automapped
	@Valid
	private AirlineType			type;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				foundationMoment;

	@Optional
	@Automapped
	@ValidEmail
	private String				emailAddress;

	@Optional
	@Automapped
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

}
