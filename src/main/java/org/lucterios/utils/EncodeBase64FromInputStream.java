package org.lucterios.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.util.Base64EncoderStream;

public class EncodeBase64FromInputStream {
	private byte[] outStream=null;
	private Base64EncoderStream bes;
	public EncodeBase64FromInputStream(InputStream input) {
		
		try {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bes = new Base64EncoderStream(baos);		
	        byte[] buffer = new byte[4096];
	        int len;
	        while((len = input.read(buffer)) != -1) 
	        	bes.write(buffer, 0, len);
	        input.close();
	        outStream=baos.toByteArray();
	        bes.close();
    	} catch(IOException e) {}
	}

	public String encodeString() 
	{
	   return new String(encode()); 
	}

	public byte[] encode () 
	{
		return outStream; 
	}	
}
