<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Page ${page.page}</h2>

<ol class="breadcrumb">
  <li><a href="<c:url value="/files/image/${imageId}/${runId}/pages" />"><i class="fa fa-angle-double-left" aria-hidden="true"></i>
   All Pages</a></li>
</ol>

<div class="text-right">
<p>
<a href="<c:url value="/files/image/${imageId}/${runId}/page/${page.page}/lines/edit" />" class="btn btn-primary btn-sm">Correct Extracted Text</a>
</p>
</div>

<c:forEach items="${page.lines}" var="line">
<div class="panel panel-default">
  <div class="panel-body">
    <img src="<c:url value="/files/image/${imageId}/${runId}/line/${page.page}/${line.imageFilename}" />"
    <br>
    <input type="text" class="form-control" value="${line.text}" readonly style="margin-top: 10px"></input>
  </div>
</div>
</c:forEach>