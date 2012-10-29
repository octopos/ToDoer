<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="../Resources/wtmStyles.css">
</head>

<%
	String error=request.getParameter("error");
	if(error==null || error=="null"){
 		error="";
	}
%>

<script>
    function trim(s) 
    {
        return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
    }

    function validate()
    {
        if(trim(document.frmLogin.username.value)=="")
        {
          alert("Username empty!");
          document.frmLogin.username.focus();
          return false;
        }
        else if(trim(document.frmLogin.password.value)=="" )
        {
          alert("Password cannot be empty!");
          document.frmLogin.password.focus();
          return false;
        }
    }
</script>

<body>
	<form action="/login" name="frmLogin" onsubmit="return validate()" method="get">
		<center>
			<h1 class="titleStyle">Do-D-Due</h1>
		</center>
		<table align="center">
			<tr>
				<td colspan="2"><%=error%></td>
			</tr>
			<tr>
				<td>Username:</td>
				<td><input name="username" type="text" /></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input name="password" type="password" /></td>
			</tr>
			<tr align="right">
				<td colspan="2"><input type="submit" value="Login"/></td>
			</tr>
			<tr align="right">
				<td colspan="2"><a href="forgotPassword.jsp">Forgot Password</a></td>
			</tr>
			<tr align="right">
				<td colspan="2"><a href="createAccount.jsp">Create Account</a></td>
			</tr>
			<tr align="right">
				<td colspan="2"><a href="showUser.jsp">Show user table</a></td>
			</tr>
		</table>
	</form>
</body>
</html>