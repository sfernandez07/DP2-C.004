
package acme.entities.flightAssignments;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActivityLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

	@Mandatory
	@Automapped
	@ManyToOne(optional = false)
	private FlightAssignment	flightAssignment;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
	private String				typeOfIncident;

	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String				description;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 10)
	private Integer				severityLevel;


	@Override
	public int hashCode() {
		return Objects.hash(this.flightCrewMember, this.flightAssignment, this.registrationMoment, this.typeOfIncident, this.description, this.severityLevel);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ActivityLog))
			return false;
		ActivityLog other = (ActivityLog) obj;
		return Objects.equals(this.flightCrewMember, other.flightCrewMember) && Objects.equals(this.flightAssignment, other.flightAssignment) && Objects.equals(this.registrationMoment, other.registrationMoment)
			&& Objects.equals(this.typeOfIncident, other.typeOfIncident) && Objects.equals(this.description, other.description) && Objects.equals(this.severityLevel, other.severityLevel);
	}
}
