<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="../Resources/wtmStyles.css">
</head>
<body>
	<form action="/createAcct" method="post">
		<center>
			<h1 class="titleStyle">Do-D-Due</h1>
		</center>
		<table align="center">
			<tr>
				<td>Username:</td>
				<td><input name="username" type="text"  /></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input name="password1" type="password" /></td>
			</tr>
			<tr>
				<td>Confirm Password:</td>
				<td><input name="password2" type="password"/></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit"/></td>
				<td><input type="submit" value="Cancel"/></td>
			</tr>
		</table>
		</form>
</body>
</html>