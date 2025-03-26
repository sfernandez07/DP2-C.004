<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flightCrewMember.flightAssignment.form.duty" path="duty"/>
	<acme:input-textbox code="flightCrewMember.flightAssignment.form.label.lastUpdate" path="lastUpdate"/>
	<acme:input-textbox code="flightCrewMember.flightAssignment.label.status" path="status"/>
	<acme:input-textbox code="flightCrewMember.flightAssignment.form.label.remarks" path="remarks"/>
</acme:form>