<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
//# sourceURL=date.js
$(function() {
   $.each($(".date"), function(index, elem) {
       elem = $(elem);
       var dateText = elem.text();
       if (dateText.endsWith(']')) {
           dateText = dateText.substring(dateText.indexOf('['));
       }
       var date = new Date(dateText);
       elem.text(date.toLocaleDateString());
   });
});
</script>

<style>
pre {
  overflow: auto;
  word-wrap: normal;
  white-space: pre;
  max-height: 200px;
  font-size: 9px;
}

</style>

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

<c:forEach items="${image.trainings}" var="entry">
<c:set value="${entry.key}" var="run" />
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">OCR run on <span class="date">${run.date}</span> <a href="<c:url value="/files/image/${image.id}/${run.id}/download" />"><div class="pull-right"><i class="fa fa-download" aria-hidden="true"></i>  Download</a></div></h3>
  </div>
  <div class="panel-body">
    Model used: <b>${run.model.title}</b> (${run.model.filename})
    <br>
    Steps completed:
    <ul>
    <c:forEach items="${run.steps}" var="step">
        <li>
            <c:if test="${step.status == 'SUCCEEDED'}"><span class="label label-success">Succeeded</span></c:if><c:if test="${step.status == 'FAILED' }"><span class="label label-danger">Failed</span></c:if> ${step.stepType} at <span class="date">${step.date}</span> 
        </li>
    </c:forEach>
    </ul>
    
    <a href="<c:url value="/files/image/${image.id}/${run.id}/text" />"><i class="fa fa-font" aria-hidden="true"></i> OCR Result as HOCR</a>
    <br><a href="<c:url value="/files/image/${image.id}/${run.id}/pages" />"><i class="fa fa-eye" aria-hidden="true"></i> Correct Pages</a>
    <span class="pull-right">
    <a class="btn btn-link trainingDialogLink" data-url="${actionUrlTrain}" data-runid="${run.id}" data-imageid="${image.id}">
	   <i class="fa fa-cog" aria-hidden="true"></i> Train Model
	</a>
	</span>
	
	<div class="panel-group" style="margin-top: 10px;" id="trainingListPanel" role="tablist" aria-multiselectable="true">
	  <div class="panel panel-default">
	    <div class="panel-heading" role="tab" id="headingOne" style="padding: 4px;">
	      <span class="panel-title">
	        <a role="button" data-toggle="collapse" data-parent="#trainingListPanel" href="#trainingList" aria-expanded="false" aria-controls="trainingList">
	          <small>Trainings</small>
	        </a>
	      </span>
	    </div>
	    <div id="trainingList" class="panel-collapse collapse out" role="tabpanel" aria-labelledby="headingOne">
        <c:forEach items="${entry.value}" var="training">
	      <div class="list-group">
	        ${training.id} on ${training.date}
	        <c:if test="${training.done}"><span class="label label-success">Done</span></c:if>
	        <c:if test="${not training.done}"><span class="label label-info">Running</span></c:if>
	        
	        <!-- logs for finished trainings -->
	        <c:if test="${training.done and not empty training.log}">
	        <div class="panel-heading" role="tab" id="${training.id}" style="padding: 4px;">
            <div class="panel-group" style="margin-top: 10px;" id="Panel${training.id}" role="tablist" aria-multiselectable="true">
		      <div class="panel panel-default" style="background-color: #edf4ff;">
			      <span class="panel-title" style="padding-left: 5px; font-size: 14px;">
		            <a role="button" data-toggle="collapse" data-parent="#Panel${training.id}" href="#List${training.id}" aria-expanded="false" aria-controls="List${training.id}">
		              <small>Log <i class="fa fa-caret-down" aria-hidden="true"></i></small>
		            </a>
		          </span>
	           </div>
		       <div id="List${training.id}" class="panel-collapse collapse out" role="tabpanel" aria-labelledby="${training.id}">
		          <small><pre>
		            ${training.log}
		          </pre></small>
		       </div>
		    </div>
		    </div>
	        </c:if>
	        
	        <!-- log for running trainings -->
	        <c:if test="${not training.done and not empty training.log}">
	        <small><pre>
	        ${training.log}
	        </pre></small>
	        </c:if>
	      </div>
        </c:forEach>
        </div>
	  </div>
	
  </div>
</div>
</c:forEach>

<c:url value="/files/image/" var="baseUrl" />
<script>
    //@ sourceURL=test.js
    $(function() {
        $('.trainingDialogLink').click(function() { 
            var runId = $(this).data('runid');
            var imageId = $(this).data('imageid');
            
            $('#trainingForm').attr('action', '${baseUrl}' + imageId + "/" + runId + "/train");
            $.get("${baseUrl}/" + imageId + "/" + runId + "/pages.json", function( data ) {
                $.each(data, function(index, elem) {
                    var li = $('<li></li>');
                    var checkbox = $('<input type="checkbox" checked name="pages" style="padding-right: 10px;"></input>');
                    checkbox.attr('value', elem.page)
                    li.append(checkbox);
                    li.append("<label>Page " + elem.page + "</label>");
                    $("#trainingPages").append(li);
                });
             });
            
            $('#trainingModal').modal('show');
        });
        
        $('#trainingModal').on('hide.bs.modal', function (event) {
            $("#trainingPages").empty();
        });
    });
    </script>

<!-- Modal -->
<div class="modal fade" id="trainingModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Train Model</h4>
      </div>
      <div class="modal-body">
        <form id="trainingForm" action="${actionUrlTrain}" class="form-inline" method="POST">
        <input id="csrfInput" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <p>
            <label>Lines to train: </label> <input name="linesToTrain" type="number" class="form-control input-sm" value="100000" />
        </p>
        <p>
            <label>Saving frequency: </label> <input name="savingFreq" type="number" class="form-control input-sm" value="1000" />
            
        </p>
        <p>
            <label>Select pages to train:</label>
            <ul style="list-style-type: none; padding-left: 5px; max-height: 500px; overflow-y:scroll" id="trainingPages">
            
            </ul>
        </p>
    </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        <button type="button" id="submitTrainingForm" class="btn btn-primary">Train</button>
      </div>
    </div>
  </div>
</div>

<script>
$(function() {
    $('#submitTrainingForm').click(function() {
       $('#trainingForm').submit(); 
    });
});
</script>

</div>

