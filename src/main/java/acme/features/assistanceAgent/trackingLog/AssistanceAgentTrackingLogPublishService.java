
package acme.features.assistanceAgent.trackingLog;

import java.util.Arrays;
import java.util.List;

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
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {
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
		String claimStatus;
		String method;

		method = super.getRequest().getMethod();

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		claim = trackingLog == null ? null : trackingLog.getClaim();
		status = claim != null && trackingLog.isDraftMode() && !claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent());

		if (status)
			if (method.equals("GET"))
				status = true;
			else {
				claimStatus = super.getRequest().getData("status", String.class);

				status = claimStatus.equals("0") || Arrays.stream(TrackingLogStatus.values()).map(t -> t.name()).anyMatch(t -> t.equals(claimStatus));
			}

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
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "step", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		Claim claim = trackingLog.getClaim();
		{ // ResolutionPercentage creciente
			if (!super.getBuffer().getErrors().hasErrors("resolutionPercentage")) {
				boolean correctResolutionPercentaje = true;
				Double lastResolutionPercentage = this.repository.findLastResolutionPercentagePublished(claim.getId());

				if (lastResolutionPercentage != null)
					correctResolutionPercentaje = trackingLog.getResolutionPercentage() >= lastResolutionPercentage;

				super.state(correctResolutionPercentaje, "resolutionPercentage", "acme.validation.tracking-log.resolutionPercentage.message");
			}

		}
		{ // Solo 1 tracking log excepcional
			if (trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100) {
				Long numberOfFinalTrackingLog = this.repository.countFinalTrackingLogs(claim.getId());
				boolean correctFinalTrackingLog = numberOfFinalTrackingLog < 2;
				super.state(correctFinalTrackingLog, "*", "acme.validation.tracking-log.exceptional.already-created.message");
			}

		}
		{ // Mismo status de trackingLog excepcional
			if (!super.getBuffer().getErrors().hasErrors("status"))
				if (trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100 && !trackingLog.getClaim().isDraftMode()) {
					boolean correctExceptionalTrackingLogStatus = true;
					List<TrackingLog> trackingLogs = this.repository.findPublishedTrackingLogOrderedByPercentage(claim.getId());

					TrackingLog lastTrackingLog = trackingLogs.isEmpty() ? null : trackingLogs.get(0);

					if (lastTrackingLog.getResolutionPercentage() == 100)
						correctExceptionalTrackingLogStatus = lastTrackingLog.getStatus().equals(trackingLog.getStatus());

					super.state(correctExceptionalTrackingLogStatus, "status", "acme.validation.tracking-log.exceptional.status.message");
				}

		}
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setDraftMode(false);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices choicesStatus;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());
		dataset = super.unbindObject(trackingLog, "updateMoment", "step", "resolutionPercentage", "status", "resolution", "draftMode");
		dataset.put("masterId", trackingLog.getClaim().getId());
		dataset.put("claimDraftMode", trackingLog.getClaim().isDraftMode());
		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}
}
