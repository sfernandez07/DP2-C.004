<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenance-record.list.label.maintenanceMoment" path="maintenanceMoment" width="20%"/>
	<acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="20%"/>
	<acme:list-column code="technician.maintenance-record.list.label.nextInspectionDueDate" path="nextInspectionDueDate" width="30%"/>
	<acme:list-column code="technician.maintenance-record.list.label.estimatedCost" path="estimatedCost" width="20%"/>
	<jstl:if test="${_command == 'list'}">
		<acme:list-column code="technician.maintenance-record.list.label.aircraft" path="aircraft.registrationNumber" width="10%"/>
	</jstl:if>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>