package com.wtm.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SyncServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		StringBuilder stringBuilder = new StringBuilder("Empty string");
		BufferedReader requestReader = null;
		PrintWriter responseWriter = resp.getWriter();
		ArrayList<TaskChange> changesList = new ArrayList<TaskChange>();
		TaskChange newChange;
		// System.out.println("Got writer");
		try {
			InputStream inputStream = req.getInputStream();
			if (inputStream != null) {
				requestReader = new BufferedReader(new InputStreamReader(
						inputStream));
				String line;
				String[] columns;
				while ((line = requestReader.readLine()) != null) {
					columns = line.split("\t");
					responseWriter.write("From Server\t"+columns.length+"\t"+line);
					//newChange = new TaskChange(columns[0],columns[1],columns[2],columns[3],columns[4],columns[5],columns[6],columns[7], columns[8],columns[9]);
					//changesList.add(newChange);
					//update changes list..
					
					//responseWriter.write(columns[1]+"\t"+columns[2]+"\t"+columns[3]+"\t"+columns[4]);
					responseWriter.write("\n");
				}
			} else {
				stringBuilder.append("No data");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (requestReader != null) {
				try {
					requestReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
					throw ex;
				}
			}
		}
		String body = stringBuilder.toString();
		//responseWriter.write("From Server : " + body);
		System.out.println(body);

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);

	}

}
