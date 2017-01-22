<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Uploaded Models</h2>

<c:forEach items="${models}" var="model" varStatus="loop">
<div class="panel-group" id="accordion${loop.index}" role="tablist" aria-multiselectable="true">
  <div class="panel panel-default">
    <div class="panel-heading" role="tab">
      <h4 class="panel-title">
        <a role="button" data-toggle="collapse" data-parent="#accordion${loop.index}" href="#description${loop.index}" aria-expanded="true" aria-controls="description${loop.index}">
          <b>${model.title}</b> (${model.filename})
        </a>
      </h4>
    </div>
    <div id="description${loop.index}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
      <div class="panel-body">
        ${model.description}
      </div>
    </div>
  </div>
</div> 
</c:forEach>
