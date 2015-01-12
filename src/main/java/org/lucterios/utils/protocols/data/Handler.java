package org.lucterios.utils.protocols.data;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {

	public final static String PROTOCOL="data";

	public Handler() {
	}

	protected URLConnection openConnection(URL url) {
	    return new DataURLConnection(url);
	}

}
