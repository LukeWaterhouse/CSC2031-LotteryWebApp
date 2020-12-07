<%--
  Created by IntelliJ IDEA.
  User: FiercePC
  Date: 10/11/2020
  Time: 01:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>

<h1>Admin Homepage</h1>


<%--stops a person accessing adminHome page directly--%>
<%
    response.setHeader("Cache-Constrol", "no-cache,no_store, must-revalidate");

    if (session.getAttribute("role")!="admin"){
        response.sendRedirect("../index.jsp");

    }
%>
<body>


<form action="DisplayData" id=DisplayData method="post">
    <input type="submit" value="Get All Data">
</form>


<form action="Logout" id=Logout method="post">
    <input type="submit" value="Logout">
</form><br>

<div id="table">
    <%= request.getAttribute("data") %>
</div>

<script type="text/javascript">
    const table = document.getElementById('table')

    if (table.innerText==="null") {
        table.style.display = "none";
    }
</script>

</body>
</html>
