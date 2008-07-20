package org.lucterios.Print;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;

public class ExtendableURLStreamHandlerFactory implements URLStreamHandlerFactory {
	
	private HashMap protocol2Handler = new HashMap();

	private static ExtendableURLStreamHandlerFactory instance=null;

	public final static void init() {
		try {
		    URL.setURLStreamHandlerFactory(getDefaultFactory());
		} catch(Error e) {		
		}
	}
	
	public final static ExtendableURLStreamHandlerFactory getDefaultFactory(){
		if (instance==null)
			instance=new ExtendableURLStreamHandlerFactory();
		return instance;
	}

	public ExtendableURLStreamHandlerFactory() {
		putHandler(DataURLStreamHandler.PROTOCOL, new DataURLStreamHandler());
	}

	public URLStreamHandler createURLStreamHandler(String string) {
		return (URLStreamHandler) protocol2Handler.get(string);
	}

	public void putHandler(String protocol, URLStreamHandler handler){
		this.protocol2Handler.put(protocol, handler);
	}

	public boolean handlesProtocol(String protocol){
		return protocol2Handler.containsKey(protocol);
	}

}
