<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flightCrewMember.crew.list.label.employeeCode" path="employeeCode" width="20%"/>
	<acme:list-column code="flightCrewMember.crew.list.label.phoneNumber" path="phoneNumber" width="20%"/>
	<acme:list-column code="flightCrewMember.crew.list.label.languageSkills" path="languageSkills" width="20%"/>
	<acme:list-column code="flightCrewMember.crew.list.label.yearsOfExperience" path="yearsOfExperience" width="20%"/>
</acme:list>