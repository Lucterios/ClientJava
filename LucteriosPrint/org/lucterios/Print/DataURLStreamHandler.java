package org.lucterios.Print;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class DataURLStreamHandler extends URLStreamHandler {

	public final static String PROTOCOL="data";

	public DataURLStreamHandler() {
	}

	protected URLConnection openConnection(URL url) {
	    return new DataURLConnection(url);
	}

}
