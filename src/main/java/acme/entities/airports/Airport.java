
package acme.entities.airports;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}$", message = "The IATA code must be exactly three uppercase letters")
	@Column(unique = true)
	@Automapped
	private String				iataCode;

	@Mandatory
	@Valid
	@Automapped
	private OperationalScope	operationalScope;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidUrl
	@Automapped
	private String				website;

	@Optional
	@ValidEmail
	@Automapped
	private String				emailAddress;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$", message = "The contact phone number must follow the correct pattern")
	@Automapped
	private String				contactPhoneNumber;


	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.iataCode, this.operationalScope, this.city, this.country, this.website, this.emailAddress, this.contactPhoneNumber);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Airport))
			return false;
		Airport other = (Airport) obj;
		return Objects.equals(this.name, other.name) && Objects.equals(this.iataCode, other.iataCode) && this.operationalScope == other.operationalScope && Objects.equals(this.city, other.city) && Objects.equals(this.country, other.country)
			&& Objects.equals(this.website, other.website) && Objects.equals(this.emailAddress, other.emailAddress) && Objects.equals(this.contactPhoneNumber, other.contactPhoneNumber);
	}

}
