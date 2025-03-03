
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	// Last five destinations visited by the customer
	private List<String>		lastFiveDestinations;

	// Money spent in bookings during the last year
	private Double				moneySpentLastYear;

	// Number of bookings grouped by travel class
	private Integer				numberOfBookingsEconomyClass;
	private Integer				numberOfBookingsBusinessClass;
	private Integer				numberOfBookingsFirstClass;

	// Count, average, minimum, maximum, and standard deviation of the cost of bookings in the last five years
	private Integer				countBookingsLastFiveYears;
	private Double				averageCostLastFiveYears;
	private Double				minCostLastFiveYears;
	private Double				maxCostLastFiveYears;
	private Double				stdDevCostLastFiveYears;

	// Count, average, minimum, maximum, and standard deviation of the number of passengers in the bookings
	private Integer				countPassengersLastFiveYears;
	private Double				averageNumberOfPassengers;
	private Integer				minNumberOfPassengers;
	private Integer				maxNumberOfPassengers;
	private Double				stdDevNumberOfPassengers;

}
