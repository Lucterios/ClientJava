package org.lucterios.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.util.Base64DecodeStream;

public class DecodeBase64ToInputStream extends InputStream {

	private Base64DecodeStream bds;

	public DecodeBase64ToInputStream(String data) {
		bds=new Base64DecodeStream(new ByteArrayInputStream(data.getBytes()));		
	}
	
	public int read() throws IOException{
	    return bds.read();
	}

	public byte[] readData() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            int len;
            int count = 0;
            while((len = read(buffer)) != -1) {
                baos.write(buffer, 0, len);
                count += len;
            }
            close();
        } catch(IOException e) {}
        return baos.toByteArray();
    }	
}
