
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent assistanceAgent;

		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		claim = new Claim();
		//claim.setDraftMode(true);
		claim.setAssistanceAgent(assistanceAgent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		FlightLeg leg;

		legId = super.getRequest().getData("flightLeg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "status");
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
		Dataset dataset;

		flightLegs = this.repository.findLegsThatOccurred();
		choices = SelectChoices.from(flightLegs, "flightNumber", claim.getFlightLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "status");
		dataset.put("flightLeg", choices.getSelected().getKey());
		dataset.put("flightLegs", choices);

		super.getResponse().addData(dataset);
	}

}
