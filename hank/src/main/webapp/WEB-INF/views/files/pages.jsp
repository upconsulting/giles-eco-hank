<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h2>Pages</h2>

<ul class="list-group">
<c:forEach items="${pages}" var="page">
  <li class="list-group-item">
    <span class="badge">${fn:length(page.lines)} Lines</span>
    <span class="label label-primary">OCR Result</span> <a href="<c:url value="/files/image/${imageId}/${runId}/page/${page.page}/lines" />">
        <i class="fa fa-file-text-o" aria-hidden="true"></i> Page ${page.page}
    </a>
    <div>
        <span class="label label-warning">Correction</span>  <a href="<c:url value="/files/image/${imageId}/${runId}/${page.correction.id}/page/${page.page}/lines" />">
            Corrected on: ${page.correction.date}
        </a> 
    </div>
  </li>
</c:forEach>
</ul>