<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forgot Password</title>
<link rel="stylesheet" type="text/css" href="../Resources/wtmStyles.css">
</head>

<%
	String error=request.getParameter("error");
	if(error==null || error=="null"){
 		error="";
	}
%>

<script>

	function trim(s) {
		return s.replace(/^\s*/, "").replace(/\s*$/, "");
	}

	function validate() {
		if (trim(document.frmForgotPassword.username.value) == "") {
			alert("Username empty!");
			document.frmForgotPassword.username.focus();
			return false;
		} else if( trim(document.frmForgotPassword.password.value) == "" ){
			alert("New password cannot be empty!");
			document.frmForgotPassword.password.focus();
			return false;
		}
		var r = confirm("Are you sure you want to do this?");
		if (r == false ) {
			document.frmForgotPassword.username.value = "";
			document.frmForgotPassword.username.focus();
			return false;
		} 
	}
</script>

<body>
	<form name="frmForgotPassword" action="/resetPassword" method="post">
		<center>
			<h1 class="titleStyle">Do-D-Due</h1>
		</center>
		<table align="center">
			<tr>
				<td colspan="2"><%=error%></td>
			</tr>
			<tr>
				<td>Username:</td>
				<td><input name="username" type="text"  /></td>
			</tr>
			<tr>
				<td>New Password:</td>
				<td><input name="password" type="password"  /></td>
			</tr>
			<tr>
				<td><input type="submit" name="button" onclick = "return validate()" value="Submit"/></td>
				<td><input type="submit" name="button" value="Cancel"/></td>
			</tr>
		</table>
		</form>
</body>
</html>