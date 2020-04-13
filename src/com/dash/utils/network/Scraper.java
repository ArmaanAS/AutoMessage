package com.dash.utils.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.The5zigMod;

public class Scraper {
	public static String readURL(String uri) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		The5zigAPI.getLogger().info("Accessing webpage: " + uri);
		
		HttpsURLConnection con = (HttpsURLConnection) new URL(uri).openConnection();
		con.setSSLSocketFactory(RelaxedSSLContext.getInstance().getSocketFactory());
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		InputStream in = null;
		String encoding, body = "";
		try {
		    in = con.getInputStream();
		} catch (IOException ioe) {
		    if (con instanceof HttpURLConnection) {
		        HttpURLConnection httpConn = (HttpURLConnection) con;
		        int statusCode = httpConn.getResponseCode();
		        if (statusCode != 200) {
		            in = httpConn.getErrorStream();
		        }
		    }
		    return "";
		}
		
		encoding = con.getContentEncoding();  // ** WRONG: should use "con.getContentType()" instead but it returns something like "text/html; charset=UTF-8" so this value must be parsed to extract the actual encoding
		encoding = encoding == null ? "UTF-8" : encoding.split(";\\s*")[1].split("=")[1];
	
		return IOUtils.toString(in, encoding);
	}
}
