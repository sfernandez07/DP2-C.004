
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
import acme.features.airlineManager.flight.AirlineManagerFlightRepository;
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
	@Valid
	@Mandatory
	private SelfTransfer		selfTransfer;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				cost;

	@ValidString
	@Automapped
	@Optional
	private String				description;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		AirlineManagerFlightRepository repository = SpringHelper.getBean(AirlineManagerFlightRepository.class);
		return repository.findMinScheduledDeparture(this.getId());
	}

	@Transient
	public Date getScheduledArrival() {
		AirlineManagerFlightRepository repository = SpringHelper.getBean(AirlineManagerFlightRepository.class);
		return repository.findMaxScheduledArrival(this.getId());
	}

	@Transient
	public String getOriginCity() {
		AirlineManagerFlightRepository repository = SpringHelper.getBean(AirlineManagerFlightRepository.class);
		List<String> cities = repository.findOriginCity(this.getId());
		return cities.isEmpty() ? null : cities.get(0);
	}

	@Transient
	public String getDestinationCity() {
		AirlineManagerFlightRepository repository = SpringHelper.getBean(AirlineManagerFlightRepository.class);
		List<String> cities = repository.findDestinationCity(this.getId());
		return cities.isEmpty() ? null : cities.get(0);
	}

	@Transient
	public Integer getLayovers() {
		AirlineManagerFlightRepository repository = SpringHelper.getBean(AirlineManagerFlightRepository.class);
		int totalLegs = repository.countLegs(this.getId());
		return totalLegs > 1 ? totalLegs - 1 : 0;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager manager;

}
