package org.lucterios.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class DesktopTools {

	private Object awt_desktop=null;
	public DesktopTools() {
		super();
		initDesktop();
	}

	public void launch(String aUrl) throws LucteriosException
	{
		if (aUrl.startsWith("mailto:"))
			openInMail(aUrl);
		else if (aUrl.startsWith("http://"))
			openInWeb(aUrl);
		else
			openInFile(aUrl);
	}

	
	private void initDesktop()
	{
		try {
			Class desktop_class=Class.forName("java.awt.Desktop");
			Method get_desktop_meth=desktop_class.getMethod("getDesktop",null);
			awt_desktop=get_desktop_meth.invoke(null,null);
			Method is_desktop_meth=desktop_class.getMethod("isDesktopSupported",null);
			Boolean bool=(Boolean)is_desktop_meth.invoke(awt_desktop,null);
			if (!bool.booleanValue())
				awt_desktop=null;				
		} catch (ClassNotFoundException e) {
			awt_desktop=null;
		} catch (SecurityException e) {
			awt_desktop=null;
		} catch (NoSuchMethodException e) {
			awt_desktop=null;
		} catch (IllegalArgumentException e) {
			awt_desktop=null;
		} catch (IllegalAccessException e) {
			awt_desktop=null;
		} catch (InvocationTargetException e) {
			awt_desktop=null;
		}
	}
	
	private boolean invokeDesktopLaunch(String aMethodName,Class aType,String aUrl)
	{
		if (awt_desktop!=null)
		{
			try {
				Method desktop_launch=awt_desktop.getClass().getMethod(aMethodName,new Class[]{aType});
				Constructor construct=aType.getConstructor(new Class[]{String.class});
				Object param=construct.newInstance(new Object[]{aUrl});
				desktop_launch.invoke(awt_desktop,new Object[]{param});
				return true;
			} catch (SecurityException e) {
				return false;
			} catch (NoSuchMethodException e) {
				return false;
			} catch (IllegalArgumentException e) {
				return false;
			} catch (IllegalAccessException e) {
				return false;
			} catch (InvocationTargetException e) {
				return false;
			} catch (InstantiationException e) {
				return false;
			}			
		}
		else
			return false;
	}

	private void searchBrowser(String url) throws LucteriosException
	{
		try {
			String[] args;
			String os_arch=System.getProperty("os.arch").toLowerCase();
	        Logging.getInstance().writeLog("OS to open URL",os_arch,1);
			if ("x86".equals( os_arch ))
				args = new String[]{"rundll32","url.dll","FileProtocolHandler",url};
			else if ("ppc".equals( os_arch ) || "powerpc".equals( os_arch ))
				args = new String[] {"open",url};
			else
				args=new String[] {searchBrowserFromUnix(), url};				
			Runtime.getRuntime().exec(args);
		} catch (MalformedURLException e) 
		{
			throw new LucteriosException("Page non trouvée!",e);
		} catch (IOException e) {
			throw new LucteriosException("Page non trouvée!",e);
		} catch (InterruptedException e) {
			throw new LucteriosException("Page non trouvée!",e);
		} 
		
	}

	private String searchBrowserFromUnix() throws InterruptedException, IOException, LucteriosException 
	{
		String[] browsers = { "firefox", "mozilla", "konqueror", "opera", "epiphany", "netscape" }; 
		String browser = null; 
		for (int count = 0; count < browsers.length && browser == null; count++)
		if (Runtime.getRuntime().exec( new String[] {"which", browsers[count]}).waitFor() == 0)
			browser = browsers[count]; 
		if (browser == null) 
			throw new LucteriosException("Impossible de trouver un navigateur Web."); 
		return browser;
	}

	private void searchMailer(String url) throws LucteriosException
	{
		try {
			//String url_without_mailto=url.replaceAll("mailto:","");
			String[] args;
			String os_arch=System.getProperty("os.arch").toLowerCase();
	        Logging.getInstance().writeLog("OS to open URL",os_arch,1);
			if ("x86".equals( os_arch ))
				args = new String[]{"rundll32","url.dll","FileProtocolHandler",url};
			else if ("ppc".equals( os_arch ) || "powerpc".equals( os_arch ))
				args = new String[] {"open",url};
			else
			{
				args = new String[] {searchMailerFromUnix(),url};				
			}
			Runtime.getRuntime().exec(args);
		} catch (MalformedURLException e) 
		{
			throw new LucteriosException("Courriel non trouvée!",e);
		} catch (IOException e) {
			throw new LucteriosException("Courriel non trouvée!",e);
		} catch (InterruptedException e) {
			throw new LucteriosException("Courriel non trouvée!",e);
		} 
		
	}

	private String searchMailerFromUnix()
			throws InterruptedException, IOException, LucteriosException {
		String[] browsers = { "thunderbird", "mozilla", "kmail", "opera", "netscape" }; 
		String browser = null; 
		for (int count = 0; count < browsers.length && browser == null; count++)
		if (Runtime.getRuntime().exec( new String[] {"which", browsers[count]}).waitFor() == 0)
			browser = browsers[count]; 
		if (browser == null) 
			throw new LucteriosException("Impossible de trouver un editeur courriel."); 
		return browser;
	}
	
	private void openInFile(String aUrl) throws LucteriosException {
		if (!invokeDesktopLaunch("open",File.class,aUrl))
			searchBrowser(aUrl);
	}

	private void openInWeb(String aUrl) throws LucteriosException {
		if (!invokeDesktopLaunch("browse",URL.class,aUrl))
			searchBrowser(aUrl);
	}

	private void openInMail(String aUrl) throws LucteriosException {
		if (!invokeDesktopLaunch("mail",URL.class,aUrl))
			searchMailer(aUrl);
	}
}
