<%--
  Created by IntelliJ IDEA.
  User: johnmace
  Date: 21/10/2020
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>



  <head>
    <title>Home</title>
      <script defer src="script.js"></script>
  </head>
  <body>


  <h1>Home Page</h1>

  <p id="homeMessage"><%= request.getAttribute("homeMessage") %></p><br>

  <h2>Login</h2>


<%--  --%>


  <form action="UserLogin" id=UserLogin method="post">

      <div  id="userLoginError"></div>
      <label for="username">User name:</label><br>
      <input type="text" id="usernameLogin" name="username"><br>

      <div  id="passLoginError"></div>
      <label for="password">Password:</label><br>
      <input type="password" id="passwordLogin" name="password"><br><br>

      <input type="submit" value="Login" id="loginButton">


  </form>

  <h2>Register</h2>

  <form action="CreateAccount" id="Register" method="post">


      <div  id="firstError"></div>
      <label for="firstname">First name:</label><br>
      <input type="text" id="firstname" name="firstname"><br>

      <div  id="lastError"></div>
      <label for="lastname">Last name:</label><br>
      <input type="text" id="lastname" name="lastname"><br>


      <div  id="userError"></div>
      <label for="username">Username:</label><br>
      <input type="text" id="username" name="username"><br>

      <div  id="phoneError"></div>
      <label for="phone">Phone:</label><br>
      <input type="text" id="phone" name="phone"><br>

      <div id="mailError"></div><br>
      <label for="email">Email:</label><br>
      <input type="text" id="email" name="email"><br>

      <div id="passError"></div><br>
      <label for="password">Password:</label><br>
      <input type="password" id="password" name="password"><br><br>

      <div id="roleError"></div><br>
      <p>
          <label>Select Type</label>
          <select name="txt_rolereg" id="role">
              <option value="" selected="selected"> - select role - </option>
              <option value="user">User</option>
              <option value="admin">Admin</option>
          </select>
      </p>

      <input type="submit" value="Register">
  </form>
  
  <script type="text/javascript">


      let test = <%=request.getAttribute("tooManyTimes")%>

      console.log("Print in index.jsp:"+test)


  </script>

  </body>
</html>
