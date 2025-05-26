
package acme.features.assistanceAgent.claim;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.claims.TrackingLog;
import acme.entities.claims.TrackingLogStatus;
import acme.entities.flights.FlightLeg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean authorize;
		boolean canPublish;
		int masterId;
		Claim claim;
		AssistanceAgent assistanceAgent;
		Collection<FlightLeg> flightLegs;
		int flightLegId;
		String claimType;
		String method;

		masterId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(masterId);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();

		flightLegs = this.repository.findLegsThatOccurred();

		method = super.getRequest().getMethod();

		authorize = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);
		canPublish = claim.getStatus() == TrackingLogStatus.ACCEPTED || claim.getStatus() == TrackingLogStatus.REJECTED;

		if (authorize)
			if (method.equals("GET"))
				authorize = true;
			else {
				claimType = super.getRequest().getData("type", String.class);
				flightLegId = super.getRequest().getData("flightLeg", int.class);
				authorize = (flightLegId == 0 || flightLegs.stream().map(f -> f.getId()).anyMatch(f -> f == flightLegId)) &&//
					(claimType.equals("0") || Arrays.stream(ClaimType.values()).map(t -> t.name()).anyMatch(t -> t.equals(claimType)));
			}

		super.state(canPublish, "status", "acme.validation.claim.status.message");
		super.getResponse().setAuthorised(authorize);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		FlightLeg flightLeg;

		legId = super.getRequest().getData("flightLeg", int.class);
		flightLeg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "status");
		claim.setFlightLeg(flightLeg);
	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		int id;
		Collection<TrackingLog> trackingLogs;

		id = super.getRequest().getData("id", int.class);
		trackingLogs = this.repository.findTrackingLogsByClaimId(id);
		for (TrackingLog t : trackingLogs) {
			t.setDraftMode(false);
			this.repository.save(t);
		}
		claim.setDraftMode(false);
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<FlightLeg> flightLegs;
		SelectChoices choices;
		Dataset dataset;
		SelectChoices choiceType;

		flightLegs = this.repository.findAllFlightLegs();
		choices = SelectChoices.from(flightLegs, "flightNumber", claim.getFlightLeg());
		choiceType = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", //
			"status", "draftMode");
		dataset.put("types", choiceType);
		dataset.put("flightLeg", choices.getSelected().getKey());
		dataset.put("flightLegs", choices);

		super.getResponse().addData(dataset);
	}
}
