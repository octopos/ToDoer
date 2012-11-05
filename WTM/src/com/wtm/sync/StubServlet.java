package com.wtm.sync;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wtm.database.ToDoItemDataSource;

@SuppressWarnings("serial")
public class StubServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		long id = Long.parseLong(req.getParameter("id"));
		String syncString = ToDoItemDataSource.getInstance()
				.getTracker().getTestingChanges(id);
		System.out.println(syncString);
		URL url;
		HttpURLConnection conn;
		StringBuffer home = req.getRequestURL();
		int pathEnd = home.lastIndexOf("/");
		String path = home.substring(0,pathEnd+1);
		path+="sync.do";
		url = new URL(path);
		conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		System.out.println("Got connection");
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		bout.write(syncString.toString().getBytes());
		byte[] bytes = bout.toByteArray();
		
		OutputStream out = conn.getOutputStream();
		out.write(bytes);
		System.out.println("Wrote bytes");
		
		conn.connect();
		System.out.println("Connection successful");
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String line;
		PrintWriter respWriter =resp.getWriter();
		
		while ((line = rd.readLine()) != null) {
			//columns = line.split("\t");
			respWriter.write(line+"<br/>");
		}
		//resp.getWriter().println("\n"+conn.getResponseMessage());
		conn.disconnect();

	}
	
	
}
