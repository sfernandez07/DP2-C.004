
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;
import acme.entities.claims.TrackingLogRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Claim claim;
		int claimId;

		claimId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(claimId);
		status = claim != null && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrackingLog> trackingLogs;
		int claimId;
		claimId = super.getRequest().getData("masterId", int.class);
		trackingLogs = this.repository.findTrackingLogByClaimId(claimId);

		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "status", "draftMode");
		super.addPayload(dataset, trackingLog, "resolution");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrackingLog> trackingLogs) {
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		super.getResponse().addGlobal("masterId", masterId);
	}
}
