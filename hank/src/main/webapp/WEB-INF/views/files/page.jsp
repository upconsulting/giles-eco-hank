<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Page ${page.page}</h2>

<c:forEach items="${page.lines}" var="line">
<div class="panel panel-default">
  <div class="panel-body">
    <img src="<c:url value="/files/image/${imageId}/${runId}/line/${page.page}/${line.imageFilename}" />"
    <br>
    <input type="text" class="form-control" value="${line.text}" readonly style="margin-top: 10px"></input>
  </div>
</div>
</c:forEach>