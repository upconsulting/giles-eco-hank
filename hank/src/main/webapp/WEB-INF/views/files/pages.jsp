<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h2>Pages</h2>

<ul class="list-group">
<c:forEach items="${pages}" var="page">
  <a href="<c:url value="/files/image/${imageId}/${runId}/page/${page.page}/lines" />">
    <li class="list-group-item">
    <span class="badge">${fn:length(page.lines)} Lines</span>
    <i class="fa fa-file-text-o" aria-hidden="true"></i> Page ${page.page}
  </li>
</a>
</c:forEach>
</ul>