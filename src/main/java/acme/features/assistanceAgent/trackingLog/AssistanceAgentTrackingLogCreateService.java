
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;
import acme.entities.claims.TrackingLogStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		status = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);

		trackingLog = new TrackingLog();
		trackingLog.setUpdateMoment(MomentHelper.getCurrentMoment());
		trackingLog.setStep("");
		trackingLog.setResolutionPercentage(0.0);
		trackingLog.setStatus(TrackingLogStatus.PENDING);
		trackingLog.setDraftMode(true);
		trackingLog.setResolution("");
		trackingLog.setClaim(claim);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		;
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		/*
		 * int masterId;
		 * Claim claim;
		 * 
		 * masterId = super.getRequest().getData("masterId", int.class);
		 * claim = this.repository.findClaimById(masterId);
		 * if(trackingLog.getResolutionPercentage()==100)
		 * if (trackingLog.getStatus() == TrackingLogStatus.ACCEPTED)
		 * claim.setStatus(ClaimStatus.ACCEPTED);
		 * else if (trackingLog.getStatus() == TrackingLogStatus.REJECTED)
		 * claim.setStatus(ClaimStatus.REJECTED);
		 * 
		 * this.repository.save(claim);
		 */
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices choicesStatus;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "status", "resolution", "draftMode");
		dataset.put("masterId", trackingLog.getClaim().getId());
		dataset.put("claimDraftMode", false);
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}
}
