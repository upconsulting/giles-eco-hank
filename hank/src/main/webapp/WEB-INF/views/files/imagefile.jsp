<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>${image.filename}</h2>

<div class="row">
<div class="col-md-6">
<img src="<c:url value="/files/image/${image.id}/content" />" class="img-responsive">
</div>

<div class="col-md-6">

<c:url value="/files/image/${image.id}/ocr/run" var="actionUrl" />

<div class="panel panel-default">
  <div class="panel-body">
    <form action="${actionUrl}" class="form-inline" method="POST">
      <input id="csrfInput" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input type="hidden" name="imageId" value="${image.id}">
	  <div class="form-group">
	    <label for="runOcr">Run OCR again with model: </label>
	    <select class="form-control input-sm" id="selectedModel" name="modelId">
	    <c:forEach items="${models}" var="ocrmodel">
            <option value="${ocrmodel.id}">${ocrmodel.filename}</option>
        </c:forEach>
        </select>
	  </div>
	  <button type="submit" class="btn btn-primary btn-sm">Run OCR</button>
	</form>
  </div>
</div>

<c:forEach items="${image.imageFile.ocrRuns}" var="run">
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">OCR run on ${run.date}</h3>
  </div>
  <div class="panel-body">
    Model used: <b>${run.model.title}</b> (${run.model.filename})
    <br>
    Steps completed:
    <ul>
    <c:forEach items="${run.steps}" var="step">
        <li>
            <c:if test="${step.status == 'SUCCEEDED'}"><span class="label label-success">Succeeded</span></c:if><c:if test="${step.status == 'FAILED' }"><span class="label label-danger">Failed</span></c:if> ${step.stepType} at ${step.date} 
        </li>
    </c:forEach>
    </ul>
    
    <b>OCR Result: </b><a href="<c:url value="/files/image/${image.id}/${run.id}/text" />">${run.hocrFile}</a>
    <br><a href="<c:url value="/files/image/${image.id}/${run.id}/pages" />">See Pages</a>
  </div>
</div>
</c:forEach>

</div>
