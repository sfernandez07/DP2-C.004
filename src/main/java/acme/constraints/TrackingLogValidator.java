
package acme.constraints;

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
	private TrackingLogRepository repository;


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (trackingLog == null) {
			result = true;
			super.state(context, false, "*", "");
		} else {
			{
				boolean correctStatus = true;
				if (trackingLog.getResolutionPercentage() != null)
					if (trackingLog.getResolutionPercentage() == 100)
						correctStatus = trackingLog.getStatus() == TrackingLogStatus.ACCEPTED || trackingLog.getStatus() == TrackingLogStatus.REJECTED;
					else
						correctStatus = trackingLog.getStatus() == TrackingLogStatus.PENDING;
				super.state(context, correctStatus, "status", "acme.validation.tracking-log.resolutionPercentage.status.message");
			}
			{
				boolean correctResolution;

				correctResolution = trackingLog.getStatus() == TrackingLogStatus.PENDING || trackingLog.getResolution() != null && !trackingLog.getResolution().isBlank();

				super.state(context, correctResolution, "resolution", "acme.validation.tracking-log.resolutionPercentage.resolution.message");
			}
			result = !super.hasErrors(context);
		}

		return result;
	}

}
