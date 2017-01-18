<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Your Uploads</h2>

<ul>
<c:forEach items="${files}" var="file">

<li>${file.id}</li>
<ul>
    <c:forEach items="${file.processingFiles}" var="f">
        <li>${file.processingFolder}/${f}</li>
    </c:forEach>
</ul>

</c:forEach>
</ul>