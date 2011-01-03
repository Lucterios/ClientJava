package org.lucterios.client.transport;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;

import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.StringList;
import org.lucterios.utils.Tools;

public class ImageCache {
	
	private HttpTransport mTransport=null;
	private static final String CACHE_DIR=".LucteriosCache";
	private static final String SUFIX_FOR_DUMMY="%DUMMY";
	private static final long TIME_PERINITY=1000*60*60*24*15; // 15j en millisecondes
	private static final long TIME_PERINITY_DUMMY=1000*60*60*12; // 12h en millisecondes
	private static StringList mMiniImages=new StringList();
	
	public static void clearMiniImages(){
		mMiniImages.clear();
	}
	
	public ImageCache(HttpTransport aTransport){
		mTransport=aTransport;
		File cache_dir=new File(CACHE_DIR);
		if (!cache_dir.isDirectory())
			cache_dir.mkdir();
	}
	
	private String getCacheFileName(String aName){
		String val=Tools.replace(aName,"\\","%"); 
		val=Tools.replace(val,":","%");
		val=Tools.replace(val,"/","%");
		return CACHE_DIR+"/"+val;
	}

	public boolean isInCache(String aIconName,int aSize)
	{
		try {
			String cache_file_name=getCacheFileName(aIconName);
			File cache_file=new File(cache_file_name);
			long file_size=cache_file.length();
			Date date_limit=new Date(cache_file.lastModified()+TIME_PERINITY);			
			if ((file_size!=0) && (date_limit.after(new Date()))) {
				if (mMiniImages.contains(cache_file_name))
					return true;
				else {
					long size;
					if (aSize==0)
						size = mTransport.getFileLength(aIconName);
					else
						size = aSize;
					return (file_size==size);
				}
			}
			else {
				File dummy_file=new File(cache_file_name+SUFIX_FOR_DUMMY); 
				Date dummy_limit=new Date(dummy_file.lastModified()+TIME_PERINITY_DUMMY);
				if (dummy_file.exists())
					return dummy_limit.after(new Date());
				else
					return false;
			}
		} catch (LucteriosException e) {
			return true;
		}
	}

	public void addDummy(String aIconName) {
		try {
			String cache_file_name=getCacheFileName(aIconName);
			File dummy_file=new File(cache_file_name+SUFIX_FOR_DUMMY);
			dummy_file.createNewFile();
		} catch (IOException e) {}
	}
	
	public ImageIcon getImage(String aIconName){
		String file_cache=getCacheFileName(aIconName);
		File file=new File(file_cache);
		if (file.exists()){
			try {
				FileImageInputStream fiis = new FileImageInputStream(file);
				BufferedImage img=ImageIO.read(fiis);			
				ImageIcon icon=new ImageIcon(img);
				if (!mMiniImages.contains(file_cache) && (icon.getIconHeight()<=64) && (icon.getIconWidth()<=64))
					mMiniImages.add(file_cache);
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
				String cache_file_name=getCacheFileName(aIconName);
				File dummy_file=new File(cache_file_name+SUFIX_FOR_DUMMY);
				if (dummy_file.isFile())
					dummy_file.delete();
				File file_cache=new File(cache_file_name);
				if (file_cache.isFile())
					file_cache.delete();
				Tools.saveFileStream(file_cache, aImageStream);
			} catch (LucteriosException e) {
				e.printStackTrace();
			}
		return getImage(aIconName);
	}

}
