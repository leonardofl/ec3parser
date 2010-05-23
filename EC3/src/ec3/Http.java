package ec3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*  
	Copyright (C) Leonardo Leite
	
	This code is free software: you can
	redistribute it and/or modify it under the terms of the GNU
	General Public License (GNU GPL) as published by the Free Software
	Foundation, either version 3 of the License, or
	any later version.  The code is distributed WITHOUT ANY WARRANTY;
	without even the implied warranty of MERCHANTABILITY or FITNESS
	FOR A PARTICULAR PURPOSE.  See the GNU GPL for more details.
*/

public class Http {
	
	/**
	 * make a http requisition and return the response
	 * if you need manipulate headers, don't use this method
	 * @param link the URI
	 * @return the web page html code
	 * @throws IOException
	 */
	public static String httpRequisition(String link) throws IOException {

		String urlString = new String(link);

		//better with throw than try/except
		URL url = new URL(urlString);
		HttpURLConnection connection;
		connection = (HttpURLConnection) url.openConnection();

		connection.setRequestProperty("Request-Method", "GET");
		connection.setRequestProperty("Content-Length", "0");
		connection.setRequestProperty("Content-Type", "text/plain");

		connection.setDoInput(true);
		connection.setDoOutput(false);

		//make the connection
		connection.connect();

		//open to input to read
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		//read until the end
		StringBuffer newData = new StringBuffer();
		String str = "";
		while (null != ((str = br.readLine()))) {
			newData.append(str);
		}
		br.close();
		String result = new String(newData);
		connection.disconnect();

		return result;
	}



}
