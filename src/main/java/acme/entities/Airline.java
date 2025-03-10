
package acme.entities;

import java.util.Date;
import java.util.Objects;

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
	@Automapped
	@Column(unique = true)
	@Valid
	private String				IATA_code;

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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.IATA_code, this.emailAddress, this.foundationMoment, this.name, this.phoneNumber, this.type, this.website);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Airline other = (Airline) obj;
		return Objects.equals(this.IATA_code, other.IATA_code) && Objects.equals(this.emailAddress, other.emailAddress) && Objects.equals(this.foundationMoment, other.foundationMoment) && Objects.equals(this.name, other.name)
			&& Objects.equals(this.phoneNumber, other.phoneNumber) && this.type == other.type && Objects.equals(this.website, other.website);
	}

}
