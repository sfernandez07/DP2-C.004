<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="customer.passenger.form.label.fullName" path="fullName"/>
	<acme:input-textbox code="customer.passenger.form.label.email" path="email"/>
	<acme:input-textbox code="customer.passenger.label.passportNumber" path="passportNumber"/>
	<acme:input-textbox code="customer.passenger.form.label.dateOfBirth" path="dateOfBirth"/>
	<acme:input-textbox code="customer.passenger.form.label.specialNeeds" path="specialNeeds"/>
</acme:form>