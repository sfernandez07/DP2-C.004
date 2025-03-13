
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claims.ClaimStatus;
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
				boolean correctStatus;

				if (trackingLog.getResolutionPercentage() < 100)
					correctStatus = trackingLog.getStatus() == TrackingLogStatus.PENDING;
				else
					correctStatus = trackingLog.getStatus() == TrackingLogStatus.ACCEPTED || trackingLog.getStatus() == TrackingLogStatus.REJECTED;

				super.state(context, correctStatus, "*", "Si el procentaje de resolucion es 100, el TrackingLog debe aceptar o rechazar la Claim");
			}
			{
				boolean correctResolution;

				correctResolution = trackingLog.getStatus() == TrackingLogStatus.PENDING || trackingLog.getResolution() != null && !trackingLog.getResolution().isBlank();

				super.state(context, correctResolution, "*", "Si el estado del TrackingLog no es PENDING, debe tener una resolucion");
			}
			{
				boolean sameClaimStatus;

				if (trackingLog.getStatus() == TrackingLogStatus.PENDING)
					sameClaimStatus = true;
				else if (trackingLog.getStatus() == TrackingLogStatus.ACCEPTED)
					sameClaimStatus = trackingLog.getClaim().getStatus() == ClaimStatus.ACCEPTED;
				else
					sameClaimStatus = trackingLog.getClaim().getStatus() == ClaimStatus.REJECTED;

				super.state(context, sameClaimStatus, "*", "Si el estado del TrackingLog no es PENDING, la claim debe estar rechazada o aceptada al igual que el TrackingLog asociado");
			}
			{
				boolean correctResolutionPercentaje;
				List<TrackingLog> pastTrackingLogs = this.respository.findPreviousTrackingLogs(trackingLog.getClaim().getId(), trackingLog.getUpdateMoment());
				if (pastTrackingLogs.isEmpty())
					correctResolutionPercentaje = true;
				else
					correctResolutionPercentaje = pastTrackingLogs.get(0).getResolutionPercentage() <= trackingLog.getResolutionPercentage();
				super.state(context, correctResolutionPercentaje, "*", "El porcentaje de resolucion debe crecer monotonicamente");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
