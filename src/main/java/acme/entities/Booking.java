
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	private Date				purchaseMoment;

	/*
	 * @Mandatory
	 * private TravelClass travelClass;
	 */

	@Mandatory
	@ValidNumber(min = 0)
	private Double				price;

	@Optional
	private String				lastCreditNibble;
}
