
package acme.entities.flights;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Automapped
	@Mandatory
	private SelfTransfer		selfTransfer;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				cost;

	@ValidString(max = 255)
	@Automapped
	@Optional
	private String				description;

	@Mandatory
	@Automapped
	private Status				status;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		Date departure = repository.findScheduledDeparture(this.getId());
		return departure;
	}

	@Transient
	public Date getScheduledArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		Date arrival = repository.findScheduledArrival(this.getId());
		return arrival;
	}

	@Transient
	public String getOriginCity() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<String> cities = repository.findOriginCity(this.getId());
		return cities.isEmpty() ? null : cities.get(0);
	}

	@Transient
	public String getDestinationCity() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<String> cities = repository.findDestinationCity(this.getId());
		return cities.isEmpty() ? null : cities.get(0);
	}

	@Transient
	public Integer getLayovers() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		Integer legs = repository.countLegs(this.getId());

		if (legs == null || legs <= 1)
			return 0;
		return legs - 1;
	}

	@Transient
	public String getFlightDescription() {
		String origin = this.getOriginCity();
		String dest = this.getDestinationCity();
		String originCity = origin != null ? origin : "";
		String destCity = dest != null ? dest : "";
		return "Flight: " + originCity + " --> " + destCity;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager manager;

}
