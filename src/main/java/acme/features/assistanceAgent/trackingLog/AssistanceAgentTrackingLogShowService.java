
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;
import acme.entities.claims.TrackingLogRepository;
import acme.entities.claims.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId;
		TrackingLog trackingLog;
		Claim claim;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		claim = trackingLog == null ? null : trackingLog.getClaim();
		status = claim != null && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices choicesStatus;
		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		dataset = super.unbindObject(trackingLog, "creationOrder", "updateMoment", "step", "resolutionPercentage", "status", "resolution", "draftMode");
		dataset.put("masterId", trackingLog.getClaim().getId());
		dataset.put("claimDraftMode", trackingLog.getClaim().isDraftMode());
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}
}
