
package acme.features.flightCrewMember.flightCrewMember;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.FlightCrewMember;

@GuiService
public class CrewListService extends AbstractGuiService<FlightCrewMember, FlightCrewMember> {

	@Autowired
	private CrewRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Integer id = super.getRequest().getData("id", int.class);
		Collection<FlightCrewMember> crew = this.repository.findCrewByAssignmentId(id);
		System.out.println("Número de crew encontrados: " + crew.size());
		super.getBuffer().addData(crew);
	}

	@Override
	public void unbind(final FlightCrewMember crew) {
		Dataset dataset;

		dataset = super.unbindObject(crew, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience");

		super.getResponse().addData(dataset);

	}

}
