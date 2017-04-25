<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h2>Your Uploads</h2>


 


<ul class="list-group">
<c:forEach items="${files}" var="file">

<a href="<c:url value="/files/image/${file.id}" />">
 <li class="list-group-item">
    <span class="badge" title="OCR has been run ${fn:length(file.imageFile.ocrRuns)} time(s).">${fn:length(file.imageFile.ocrRuns)}</span>
    ${file.id} (${file.filename})
  </li>
</a>

</c:forEach>
</ul>

<c:set var="prev" value="${currentPageValue-1 >= 0 ? currentPageValue-1 : 0 }" />
<c:set var="next" value="${currentPageValue+1 < totalPages ? currentPageValue+1 : totalPages-1 }" />

<nav aria-label="Page navigation" class="pull-right">
  <ul class="pagination">
    <li>
      <a href="<c:url value="?page=${prev}" />" aria-label="Previous">
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>
<c:forEach begin="1" end="${totalPages}" varStatus="loop">
<li <c:if test="${loop.index-1 == currentPageValue}">class="active"</c:if> ><a href="<c:url value="?page=${loop.index-1}" />">${loop.index}</a></li>
</c:forEach>
   <li>
      <a href="<c:url value="?page=${next}" />" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  </ul>
</nav>