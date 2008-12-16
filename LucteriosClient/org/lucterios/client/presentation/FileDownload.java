package org.lucterios.client.presentation;

import java.io.File;
import java.io.IOException;

import org.lucterios.client.transport.HttpTransport;
import org.lucterios.client.transport.TransportException;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;
import org.lucterios.utils.ZipManager;

public class FileDownload implements Runnable {
	
	public interface FileDownloadCallBack {
		void success(String aLocalFile);
		void failure(String aMessage);
	}
	
	private HttpTransport mTransport=null;
	private String m_FileName;
	private String m_RemoteFileName;
	private boolean m_isCompress=false; 
	private FileDownloadCallBack m_CallBack;
	
	public FileDownload(){
		mTransport=Singletons.Transport();
	}
	
	private String getTempFileName(String aName){
		String val=Tools.replace(aName,"\\","%"); 
		val=Tools.replace(val,"/","%");
		return Singletons.TEMP_DIR+"/"+val;
	}
	
	private boolean loadFile(String aRemoteFileName,String aLocalFileName) throws LucteriosException{
		File file=new File(aLocalFileName);
		File tmp_file;
		if (file.exists())
			file.delete();
		try {
			tmp_file=File.createTempFile("tmp","",new File(Singletons.TEMP_DIR));
		} catch (IOException e) {
			throw new LucteriosException("Fichier temporaire non créable",e);
		}
		mTransport.saveFiles(aRemoteFileName,tmp_file.getPath());
		if (m_isCompress) {
			ZipManager zip_manager=new ZipManager(tmp_file);
			zip_manager.extractFirstFile(file);
		}
		else
			tmp_file.renameTo(file);
		return file.exists();
	}
	
	public void extractFile(String aFileName,String aRemoteFileName,boolean aIsCompress,FileDownloadCallBack aCallBack) {
		m_FileName=aFileName;
		m_RemoteFileName=aRemoteFileName;
		m_CallBack=aCallBack;
		m_isCompress=aIsCompress;
		Thread extract = new Thread(this);
		extract.setPriority(Thread.MIN_PRIORITY);
		extract.start();
	}

	public void run() {
		String temp_file=getTempFileName(m_FileName);
		try {
			if (loadFile(m_RemoteFileName,temp_file))
				m_CallBack.success(temp_file);
			else
				m_CallBack.failure("Echec de l'import");
		} catch (TransportException te) {
			if (te.mCode==404)
				m_CallBack.failure("Fichier inexistant");
			else
				m_CallBack.failure(te.getMessage());
		} catch (LucteriosException le) {
			m_CallBack.failure(le.getMessage());
		}
	}
}
