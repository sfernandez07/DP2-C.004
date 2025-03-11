
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claims.TrackingLog;
import acme.entities.claims.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

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
			//TODO: Comprobar que el resolutionPercentage crezca monotonicamente. Que el procentaje de este 
			//TrackingLog se igual o mayor que el anterior
		}

		result = !super.hasErrors(context);

		return result;
	}

}
