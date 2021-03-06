package org.lucterios.engine.transport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.lucterios.gui.AbstractImage;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.StringList;
import org.lucterios.utils.Tools;

public class ImageCache {
	
	static public Class<? extends AbstractImage> ImageClass=null;
	
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
		checkCacheDir();
	}

	protected void checkCacheDir() {
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
	
	public AbstractImage getImage(String aIconName){
		String file_cache=getCacheFileName(aIconName);
		File file=new File(file_cache);
		if (file.exists()){
			try {
				AbstractImage icon=(AbstractImage)ImageClass.newInstance();
				if (icon.load(file)) {
					if (!mMiniImages.contains(file_cache) && (icon.getHeight()<=64) && (icon.getWidth()<=64))
						mMiniImages.add(file_cache);
					return icon;
				}
				else
					return AbstractImage.Null;
			} catch (InstantiationException e) {
				return AbstractImage.Null;
			} catch (IllegalAccessException e) {
				return AbstractImage.Null;
			}
		}
		else
			return AbstractImage.Null;
	}
	
	public AbstractImage addImage(String aIconName,InputStream aImageStream) {
		if (aImageStream != null)
			try {
				checkCacheDir();				
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
