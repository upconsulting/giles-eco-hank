<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>${image.filename}</h2>

<div class="row">
<div class="col-md-6">
<img src="<c:url value="/files/image/${image.id}/content" />" class="img-responsive">
</div>

<div class="col-md-6">
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">Processing Folder</h3>
  </div>
  <div class="panel-body">
    <ul>
    <c:forEach items="${image.processingFiles}" var="f">
        <li>${f}</li>
    </c:forEach>
    </ul>
  </div>
</div>

<c:forEach items="${image.lineFolders}" var="lineFolder">
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">Subfolder: ${lineFolder.key}</h3>
  </div>
  <div class="panel-body">
    <ul>
    <c:forEach items="${lineFolder.value}" var="f">
        <li>${f}</li>
    </c:forEach>
    </ul>
  </div>
</div>
</c:forEach>
</div>

</div>
