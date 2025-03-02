
package acme.entities.flightAssignments;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ManyToOne
	private FlightCrewMember	flightCrewMember;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private Duty				duty;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdate;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private AssignmentStatus	status;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String				remarks;


	@Override
	public int hashCode() {
		return Objects.hash(this.flightCrewMember, this.duty, this.lastUpdate, this.status, this.remarks);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof FlightAssignment))
			return false;
		FlightAssignment other = (FlightAssignment) obj;
		return Objects.equals(this.flightCrewMember, other.flightCrewMember) && this.duty == other.duty && Objects.equals(this.lastUpdate, other.lastUpdate) && this.status == other.status && Objects.equals(this.remarks, other.remarks);
	}

}
