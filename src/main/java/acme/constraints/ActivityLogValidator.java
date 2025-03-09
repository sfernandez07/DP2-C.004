/*
 * package acme.constraints;
 * 
 * import java.util.Date;
 * 
 * import javax.validation.ConstraintValidator;
 * import javax.validation.ConstraintValidatorContext;
 * 
 * import acme.entities.flightAssignments.ActivityLog;
 * import acme.entities.flightAssignments.FlightAssignment;
 * 
 * public class ActivityLogValidator implements ConstraintValidator<ValidActivityLog, ActivityLog> {
 * 
 * @Override
 * public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
 * if (activityLog == null || activityLog.getFlightAssignment() == null)
 * return true; // Skip validation if data is missing
 * 
 * FlightAssignment flightAssignment = activityLog.getFlightAssignment();
 * 
 * // Assuming FlightLeg is properly mapped within FlightAssignment
 * if (flightAssignment.getFlightLeg() == null || flightAssignment.getFlightLeg().getScheduleArrival() == null)
 * return true; // Skip if there's no scheduled arrival
 * 
 * Date scheduledArrival = flightAssignment.getFlightLeg().getScheduleArrival();
 * Date registrationMoment = activityLog.getRegistrationMoment();
 * 
 * if (registrationMoment == null || scheduledArrival == null)
 * return true; // Skip validation if any field is missing
 * 
 * return registrationMoment.after(scheduledArrival);
 * }
 * }
 * 
 */
