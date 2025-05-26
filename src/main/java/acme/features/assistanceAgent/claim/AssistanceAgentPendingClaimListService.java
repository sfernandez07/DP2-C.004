
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentPendingClaimListService extends AbstractGuiService<AssistanceAgent, Claim> {
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
		Collection<Claim> claims;
		int assistanceAgentId;
		Collection<Claim> filteredClaims = ArgumentMatchers.anyCollection();

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findAllClaimsByAssistanceAgentId(assistanceAgentId);
		for (Claim c : claims)
			if (!c.isCompleted())
				filteredClaims.add(c);

		super.getBuffer().addData(filteredClaims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "type", "status", "flightLeg.flightNumber");
		super.addPayload(dataset, claim, "description");
		super.getResponse().addData(dataset);
	}
}
