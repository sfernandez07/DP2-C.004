
package acme.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightAssignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ManyToOne
	private FlightCrewMember	flightCrewMember;

	@Mandatory
	@Enumerated(EnumType.STRING)
	private Duty				duty;

	@Mandatory
	private LocalDateTime		lastUpdate;

	@Mandatory
	@Enumerated(EnumType.STRING)
	private AssignmentStatus	status;

	@Optional
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


	public enum Duty {
		PILOT, CO_PILOT, LEAD_ATTENDANT, CABIN_ATTENDANT;
	}

	public enum AssignmentStatus {
		CONFIRMED, PENDING, CANCELLED;
	}
}
