<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h2>Edit page ${page.page}</h2>

<p>
<a class="btn btn-default btn-sm" href="<c:url value="/files/image/${imageId}/${runId}/page/${page.page}/lines" />">
  Cancel
</a>
</p>

<c:url value="/files/image/${imageId}/${runId}/page/${page.page}/lines/edit" var="actionUrl" />

<form:form modelAttribute="textEditForm" action="${actionUrl}" method="POST">

<p>
<button class="btn btn-primary btn-sm" type="submit">Save Corrections</button>
</p>

<c:forEach items="${page.lines}" var="line" varStatus="i">
<div class="panel panel-default">
  <div class="panel-body">
    <img src="<c:url value="/files/image/${imageId}/${runId}/line/${page.page}/${line.imageFilename}" />"
    <br>
    <form:input type="hidden" path="lineCorrections[${i.index}].lineName" value="${lineCorrections[i.index].lineName}" />
    <form:input type="text" class="form-control" path="lineCorrections[${i.index}].text" value="${lineCorrections[i.index].text}" style="margin-top: 10px" />
  </div>
</div>
</c:forEach>

</form:form>