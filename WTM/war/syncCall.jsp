<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream oostream = new ObjectOutputStream(bout);

			for (TaskChange item : changesList) {
				System.out.println(item);
				oostream.writeChars(item.toString());
			}
			oostream.close();
			byte[] bytes = bout.toByteArray();

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://www.yoursite.com/script.php");

			InputStream in = new ByteArrayInputStream(bytes);
			httppost.setEntity(new InputStreamEntity(in, bytes.length));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity outEntity = response.getEntity();
			outEntity.writeTo(bout);
			System.out.println(bout.toString());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			System.out.println("Client protocol exception");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IO exception");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("IO exception");
		}

		// clear changes table
		// changesDataStore.deleteAllChanges();
	%>

</body>
</html>