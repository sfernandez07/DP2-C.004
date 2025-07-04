
package acme.features.assistanceAgent.claim;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.flights.FlightLeg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Collection<FlightLeg> flightLegs;
		int flightLegId;
		String claimType;
		String method;

		flightLegs = this.repository.findLegsThatOccurred();

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = true;
		else {
			claimType = super.getRequest().getData("type", String.class);
			flightLegId = super.getRequest().getData("flightLeg", int.class);
			status = (flightLegId == 0 || flightLegs.stream().map(f -> f.getId()).anyMatch(f -> f == flightLegId)) &&//
				(claimType.equals("0") || Arrays.stream(ClaimType.values()).map(t -> t.name()).anyMatch(t -> t.equals(claimType)));
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent assistanceAgent;

		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		claim = new Claim();
		claim.setDraftMode(true);
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());
		claim.setAssistanceAgent(assistanceAgent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		FlightLeg leg;

		legId = super.getRequest().getData("flightLeg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "passengerEmail", "description", "type");
		claim.setFlightLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<FlightLeg> flightLegs;
		SelectChoices choices;
		SelectChoices choiceType;
		Dataset dataset;

		flightLegs = this.repository.findLegsThatOccurred();
		choices = SelectChoices.from(flightLegs, "flightNumber", claim.getFlightLeg());
		choiceType = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "draftMode");
		dataset.put("status", claim.getStatus());
		dataset.put("flightLeg", choices.getSelected().getKey());
		dataset.put("flightLegs", choices);
		dataset.put("types", choiceType);

		super.getResponse().addData(dataset);
	}

}
