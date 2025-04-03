<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="technician.maintenance-record.form.label.maintenanceMoment" path="maintenanceMoment"/>
	<acme:input-select code="technician.maintenance-record.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-moment code="technician.maintenance-record.form.label.nextInspectionDueDate" path="nextInspectionDueDate"/>
	<acme:input-money code="technician.maintenance-record.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textarea code="technician.maintenance-record.form.label.notes" path="notes"/>
	<acme:input-select code="technician.maintenance-record.form.label.aircraft" path="aircraft"  choices="${aircrafts}"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'create'}">

			<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>	
			<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>
			<acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete"/>
			<acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>
