package org.lucterios.utils.protocols.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;

import org.lucterios.utils.DecodeBase64ToInputStream;
import org.lucterios.utils.StringList;

public class DataURLConnection extends URLConnection {

	private boolean base64 = false;
	private boolean connected = false;
	private String data;
	private String mediaType;
	private Map<String, java.util.List<String>> headerFields = new TreeMap<String, java.util.List<String>>();

	public DataURLConnection(URL url) {
	    super(url);
	}

	public void connect() throws IOException{
	    if( connected )
	    	return;
	    connected = true;
	    String data = getURL().getPath();
	    int dataStart = data.indexOf(',');
	    if (dataStart==-1)
	    	throw new IOException("Expecting ',' introducing data");
	    String prefix = data.substring(0, dataStart);
	    if (prefix.trim().endsWith(";base64")){
	    	base64 = true;
	    	mediaType = prefix.substring(0, prefix.length()-7).trim();
	    }
	    else{
	    	mediaType = prefix.trim();
	    }
	
	    this.data = data.substring(dataStart+1);
	    if (mediaType==null || mediaType.length()==0)
	    	mediaType="text/plain";
	
	    StringList media_list=new StringList();
	    media_list.add(mediaType);
	    headerFields.put("content-type",media_list);
	}


	public String getHeaderField(String name) {
		return headerFields.get(name).get(0);
	}


	public Map<String, java.util.List<String>> getHeaderFields() {
		return headerFields;
	}


	public InputStream getInputStream() throws IOException {
	    connect();
	    if (base64)
	    	return (InputStream) new DecodeBase64ToInputStream(data);
	    else
	    	return (InputStream) new DefaultInputStream();
	}

	private class DefaultInputStream extends InputStream{

	    int dataIdx = 0;

	    public int available() throws IOException{
	      return
	          dataIdx+1<data.length() ? 1 : 0;
	    }

	    public int read() throws IOException {
	      try {
	        char c = data.charAt(dataIdx++);
	        if (c != '%') {
	          return c;
	        }
	        else {
	          char h1 = data.charAt(dataIdx++);
	          char h2 = data.charAt(dataIdx++);
	          int value = Character.digit(h1, 16) * 16 + Character.digit(h2, 16);
	          return (char) value;
	        }
	      }
	      catch (IndexOutOfBoundsException ex) {
	        return -1;
	      }
	    }

	}
}
