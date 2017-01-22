<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h2>Upload new model</h2>

<c:url value="/models/upload" var="actionUrl" />


<form:form modelAttribute="modelForm" enctype="multipart/form-data" action="${actionUrl}?${_csrf.parameterName}=${_csrf.token}" method="POST"> 
	 <div class="form-group">
	   <label for="title">Title:</label>
	   <form:input class="form-control" type="text" path="title" id="title" />
	   <form:errors path="title" />
	 </div>
	 
	 <div class="form-group">
       <label for="title">Description:</label>
       <form:textarea class="form-control" path="description" id="description" />
       <form:errors path="description" />
     </div>
     
     <div class="form-group">
        <label for="modelfile">Model file:</label>
        <input type="file" class="form-control" name="modelfile" />
     </div>
 
    <button type="submit" class="btn btn-primary btn-md">Upload</button>
</form:form>