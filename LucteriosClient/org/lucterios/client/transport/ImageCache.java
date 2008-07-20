package org.lucterios.client.transport;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;

import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;

public class ImageCache {
	
	private HttpTransport mTransport=null;
	private static final String CACHE_DIR=".LucteriosCache";
	
	public ImageCache(HttpTransport aTransport){
		mTransport=aTransport;
		clear();
	}
	
	private void clear(){
		File cache_dir=new File(CACHE_DIR);
		if (!cache_dir.isDirectory())
			cache_dir.mkdir();
	}
	
	private String getCacheFileName(String aName){
		String val=Tools.replace(aName,"\\","%"); 
		val=Tools.replace(val,"/","%");
		return CACHE_DIR+"/"+val;
	}

	private long getFileSize(String aFileName)
	{
		File f=new File(aFileName);
		return f.length();
		/*try {
			long size = 0;
			FileReader reader;
			reader = new FileReader(aFileName);
			while (reader.read()!=-1)
			    size++;
			return size;
		} 
		catch (FileNotFoundException e) {
			return 0;
		}
		catch (IOException e) {
			return 0;
		}*/
	}
	
	public boolean isInCache(String aIconName)
	{
		try {
			long file_size=getFileSize(getCacheFileName(aIconName));
			if (file_size!=0) {
				long size = mTransport.getFileLength(aIconName);
				return (file_size==size);
			}
			else 
				return false; 
		} catch (LucteriosException e) {
			return true;
		}
	}
	
	public ImageIcon getImage(String aIconName){
		String file_cache=getCacheFileName(aIconName);
		File file=new File(file_cache);
		if (file.exists()){
			try {
				FileImageInputStream fiis = new FileImageInputStream(file);
				BufferedImage img=ImageIO.read(fiis);			
				ImageIcon icon=new ImageIcon(img);
				return icon;
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
		else
			return null;
	}
	
	public ImageIcon addImage(String aIconName,InputStream aImageStream) {
		if (aImageStream != null)
			try {			
				File file_cache=new File(getCacheFileName(aIconName));
				if (file_cache.isFile())
					file_cache.delete();
				Tools.saveFileStream(file_cache, aImageStream);
			} catch (LucteriosException e) {
				e.printStackTrace();
			}
		return getImage(aIconName);
	}
}
