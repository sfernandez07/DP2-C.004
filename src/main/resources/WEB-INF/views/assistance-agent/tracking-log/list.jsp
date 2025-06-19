<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistance-agent.tracking-log.list.label.creationOrder" path="creationOrder" width="5%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.updateMoment" path="updateMoment" width="19%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.step" path="step" width="19%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.resolutionPercentage" path="resolutionPercentage" width="19%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.status" path="status" width="19%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.resolution" path="resolution" width="19%"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="assistance-agent.tracking-log.list.button.create" action="/assistance-agent/tracking-log/create?masterId=${masterId}"/>
</jstl:if>