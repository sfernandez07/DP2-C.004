
package acme.realms;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRole {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$", message = "The identifier must follow the correct pattern")
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$", message = "The phone number must follow the correct pattern")
	private String				phoneNumber;

	@Mandatory
	@ValidString
	private String				physicalAddress;

	@Mandatory
	@ValidString(max = 50)
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	private String				country;

	@Optional
	@ValidNumber(min = 0, max = 500000)
	private Integer				earnedPoints;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.city, this.country, this.earnedPoints, this.identifier, this.phoneNumber, this.physicalAddress);
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
		Customer other = (Customer) obj;
		return Objects.equals(this.city, other.city) && Objects.equals(this.country, other.country) && Objects.equals(this.earnedPoints, other.earnedPoints) && Objects.equals(this.identifier, other.identifier)
			&& Objects.equals(this.phoneNumber, other.phoneNumber) && Objects.equals(this.physicalAddress, other.physicalAddress);
	}

}
