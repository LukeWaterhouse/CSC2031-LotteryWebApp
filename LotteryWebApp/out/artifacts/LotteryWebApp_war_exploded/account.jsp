<%--
  Created by IntelliJ IDEA.
  User: johnmace
  Date: 21/10/2020
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account</title>
    <script defer src="numbers.js"></script>

</head>
<body>

<%--Stops person accessing account directly--%>
<%
    response.setHeader("Cache-Constrol", "no-cache,no_store, must-revalidate");
    if (session.getAttribute("role")!="user"){
        response.sendRedirect("../index.jsp");
    }
%>

<h1>User Account</h1>
<p id="topMessage"><%= request.getAttribute("message") %></p><br>
<p>First name: <%= session.getAttribute("firstname") %></p>
<p>Last name: <%= session.getAttribute("lastname") %></p>
<p>Email: <%= session.getAttribute("email") %></p>
<p>Username: <%= session.getAttribute("username") %></p>



<form action = "AddUserNumbers" id="LotteryForm" name="LotteryForm" method="post">
    <label for="LotteryText"></label>
    <div id="inputError"></div><br>
    Lottery Number: <input type="text" name="LotteryText" id="LotteryText" onkeyleave="generate()"><br>
    <input type="button" id="LotteryGenerate" name="LotteryGenerate" value="Generate!">
    <input type="submit" id="LotterySubmit" name="LotterySubmit">
</form>

<form action="GetUserNumbers" id=GetUserNumbers method="post">
    <input type="submit" value="Get Draws" id="getDrawsButton">
</form>




<p id="draws">Draws: <%= request.getAttribute("draws") %></p><br>

<script type="text/javascript">
    const topMsg = document.getElementById('topMessage')
    if (topMsg.innerText==="null") {
        topMsg.style.display = "none";
    }

    let showDraws = <%=session.getAttribute("showDraws")%>
</script>

<form action="checkWin" id="checkWin" method="post">
    <input type="submit" value="check Win" id="checkWinButton">
</form>

<form action="Logout" id=Logout method="post">
    <input type="submit" value="Logout">
</form><br>

<p id="winningMessage"><%= session.getAttribute("winning message") %></p><br>


</body>
</html>
