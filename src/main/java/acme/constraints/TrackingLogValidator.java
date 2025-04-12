
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claims.TrackingLog;
import acme.entities.claims.TrackingLogRepository;
import acme.entities.claims.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private TrackingLogRepository respository;


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "");
		else {
			{
				boolean correctStatus = true;

				if (trackingLog.getResolutionPercentage() == 100)
					correctStatus = trackingLog.getStatus() == TrackingLogStatus.ACCEPTED || trackingLog.getStatus() == TrackingLogStatus.REJECTED;

				super.state(context, correctStatus, "status", "acme.validation.tracking-log.resolutionPercentage.status.message");
			}
			{
				boolean correctResolution;

				correctResolution = trackingLog.getStatus() == TrackingLogStatus.PENDING || trackingLog.getResolution() != null && !trackingLog.getResolution().isBlank();

				super.state(context, correctResolution, "resolution", "acme.validation.tracking-log.resolutionPercentage.resolution.message");
			}
			{
				boolean correctResolutionPercentaje;
				List<TrackingLog> pastTrackingLogs = this.respository.findPreviousTrackingLogs(trackingLog.getClaim().getId(), trackingLog.getUpdateMoment());
				List<TrackingLog> nextTrackingLogs = this.respository.findNextTrackingLogs(trackingLog.getClaim().getId(), trackingLog.getUpdateMoment());
				if (pastTrackingLogs.isEmpty())
					correctResolutionPercentaje = true;
				else
					correctResolutionPercentaje = pastTrackingLogs.get(0).getResolutionPercentage() <= trackingLog.getResolutionPercentage();
				if (correctResolutionPercentaje)
					if (nextTrackingLogs.isEmpty())
						correctResolutionPercentaje = true;
					else
						correctResolutionPercentaje = nextTrackingLogs.get(0).getResolutionPercentage() >= trackingLog.getResolutionPercentage();

				super.state(context, correctResolutionPercentaje, "resolutionPercentage", "acme.validation.tracking-log.resolutionPercentage.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
