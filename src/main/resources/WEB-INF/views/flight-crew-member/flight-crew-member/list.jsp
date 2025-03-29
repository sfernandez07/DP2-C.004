<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.flight-crew-member.list.label.employeeCode" path="employeeCode" width="17%"/>
	<acme:list-column code="flight-crew-member.flight-crew-member.list.label.phoneNumber" path="phoneNumber" width="17%"/>
	<acme:list-column code="flight-crew-member.flight-crew-member.list.label.languageSkills" path="languageSkills" width="17%"/>
	<acme:list-column code="flight-crew-member.flight-crew-member.list.label.availabilityStatus" path="availabilityStatus" width="17%"/>
	<acme:list-column code="flight-crew-member.flight-crew-member.list.label.salary" path="salary" width="16%"/>
	<acme:list-column code="flight-crew-member.flight-crew-member.list.label.yearsOfExperience" path="yearsOfExperience" width="16%"/>
</acme:list>