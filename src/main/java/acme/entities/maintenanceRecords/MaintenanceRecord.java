
package acme.entities.maintenanceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.aircrafts.Aircraft;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date					maintenanceMoment;

	@Mandatory
	@Automapped
	@Valid
	private MaintenanceRecordStatus	status;

	@Mandatory
	@Automapped
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date					inspectionDueDate;

	@Mandatory
	@Automapped
	@ValidNumber
	private Double					estimatedCost;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String					notes;

	@Mandatory
	@Valid
	@ManyToOne(optional = true)
	private Aircraft				aircraft;

	@Mandatory
	@ManyToOne
	@Valid
	private Technician				technician;

}
