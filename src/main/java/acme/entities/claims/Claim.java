
package acme.entities.claims;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.flights.FlightLeg;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	public TrackingLogStatus getStatus() {
		TrackingLogStatus result;
		TrackingLogRepository repository;
		Collection<TrackingLog> trackingLogs;

		repository = SpringHelper.getBean(TrackingLogRepository.class);
		trackingLogs = repository.findPublishedTrackingLogOrderedByPercentage(this.getId());
		if (trackingLogs.isEmpty())
			result = TrackingLogStatus.PENDING;
		else {
			TrackingLog lastTrackingLog = trackingLogs.stream().findFirst().orElse(null);
			if (lastTrackingLog != null && lastTrackingLog.getResolutionPercentage() == 100)
				result = lastTrackingLog.getStatus();
			else
				result = TrackingLogStatus.PENDING;

		}

		return result;
	}

	public boolean isCompleted() {
		return this.getStatus() != TrackingLogStatus.PENDING;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent	assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightLeg		flightLeg;

}
