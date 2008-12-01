package org.lucterios.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipManager {

	private File mZipFileName;
	
	public ZipManager(File aZipFileName){
		mZipFileName=aZipFileName;
	}
	
	public void extractFirstFile(File aFileName) throws LucteriosException {
		int BUFFER=2048;
		byte data[]=new byte[BUFFER];
		try {
			ZipFile zf = new ZipFile(mZipFileName);
			Enumeration entries = zf.entries();
			BufferedOutputStream dest=null;
			if (entries.hasMoreElements())
			{
				ZipEntry e=(ZipEntry)entries.nextElement();
				if (aFileName.exists())
					aFileName.delete();
				InputStream is=zf.getInputStream(e);
				FileOutputStream fos= new FileOutputStream(aFileName);
				dest = new BufferedOutputStream(fos,BUFFER);
				int count;
				while ((count=is.read(data,0,BUFFER))!=-1)
				{
					dest.write(data,0,count);
				}
				dest.flush();
				dest.close();
			}
			zf.close();					
		} catch (FileNotFoundException fnfe) {
			throw new LucteriosException("Echec d'archivage",fnfe);
		}
		catch (IOException ioe) {
			throw new LucteriosException("Echec d'archivage",ioe);
		}
	}

	public void compressSimpleFile(File aFileName) throws LucteriosException {
		int BUFFER=2048;
		byte data[]=new byte[BUFFER];
		try {
			if (mZipFileName.exists())
				mZipFileName.delete();
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(mZipFileName));
	        FileInputStream in = new FileInputStream(aFileName);
	        out.putNextEntry(new ZipEntry(aFileName.getName()));	    
            int len;
            while ((len = in.read(data)) > 0) {
                out.write(data, 0, len);
            }	    
            out.closeEntry();
            in.close();
	        out.close();
		} catch (FileNotFoundException fnfe) {
			throw new LucteriosException("Echec d'archivage",fnfe);
		}
		catch (IOException ioe) {
			throw new LucteriosException("Echec d'archivage",ioe);
		}
	}

}
