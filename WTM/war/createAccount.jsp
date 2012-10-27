<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Account</title>
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
        if(trim(document.frmCreateAcct.username.value)=="")
        {
          alert("Username empty!");
          document.frmCreateAcct.username.focus();
          error = "You must have a username";
          return false;
        }
        else if(trim(document.frmCreateAcct.password1.value)==""
        		|| trim(document.frmCreateAcct.password2.value)=="" )
        {
          alert("Passwords cannot be empty!");
          document.frmCreateAcct.password1.focus();
          return false;
        }
        else if( trim(document.frmCreateAcct.password1.value) !=
    		 trim(document.frmCreateAcct.password2.value) )
        {
        	alert("Passwords do not match!");
        	document.frmCreateAcct.password1.value="";
        	document.frmCreateAcct.password2.value="";
            document.frmCreateAcct.password1.focus();
            return false;
        }
    }
</script>

<body>
	<form name="frmCreateAcct" onSubmit="return validate();" action="/createAcct" method="post">
		<center>
			<h1 class="titleStyle">Do-D-Due</h1>
		</center>
		<table align="center">
			<tr>
				<td colspan="2"><div><%=error%></div></td>
			</tr>
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