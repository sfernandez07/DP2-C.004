
package acme.realms;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$", message = "The employee code must follow the correct pattern")
	@Column(unique = true)
	@Automapped
	private String				employeeCode;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$", message = "The phone number must follow the correct pattern")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				languageSkills;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private AvailabilityStatus	availabilityStatus;

	@Mandatory
	@ValidString
	@Automapped
	private String				airline;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidNumber(min = 0)
	@Automapped
	private Integer				yearsOfExperience;


	@Override
	public int hashCode() {
		return Objects.hash(this.employeeCode, this.phoneNumber, this.languageSkills, this.availabilityStatus, this.airline, this.salary, this.yearsOfExperience);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof FlightCrewMember))
			return false;
		FlightCrewMember other = (FlightCrewMember) obj;
		return Objects.equals(this.employeeCode, other.employeeCode) && Objects.equals(this.phoneNumber, other.phoneNumber) && Objects.equals(this.languageSkills, other.languageSkills) && this.availabilityStatus == other.availabilityStatus
			&& Objects.equals(this.airline, other.airline) && Objects.equals(this.salary, other.salary) && Objects.equals(this.yearsOfExperience, other.yearsOfExperience);
	}

}
