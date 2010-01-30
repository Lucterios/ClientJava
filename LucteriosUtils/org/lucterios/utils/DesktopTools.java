package org.lucterios.utils;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class DesktopTools {

	class ProcessExitDetector extends Thread {

		private Process process;

	    private boolean isExited() {
	        try {
	            process.exitValue();
	            return false; 
	        } catch (IllegalThreadStateException exc) {
	            return true; 
	        }
	    }
		
	    public ProcessExitDetector(String[] aArgs) throws LucteriosException {
    		String cmd="";
	    	try{
	    		for(String arg:aArgs)
	    			cmd+=arg+" ";
	    		process = Runtime.getRuntime().exec(aArgs);
	    		start();
				System.out.println("Commande '"+cmd+"' lancée");
			} catch (IOException e) {
				throw new LucteriosException("Commande '"+cmd+"' non lancée!",e);
			}
	    }

	    private void showInputStream(InputStream aStream){
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(aStream));
				String inputLine;
				while ((inputLine = in.readLine()) != null) 
					System.out.println(inputLine);
			} catch (IOException e) {
				e.printStackTrace();
			}		    	
	    }
	    
	    public void run() {
	        try {
	        	while (isExited()) {
	        		showInputStream(process.getInputStream());
	        		showInputStream(process.getErrorStream());
	        		sleep(1000);
	        	}
	        	System.out.println("Exit:"+process.exitValue());		
	        } catch (InterruptedException e) {}
	    }
	}
	
	private static final String DESKTOP_ENVIRONMENTS = "kde|gnome";
	
	private static final String KDE_CONFIG = System.getProperty("user.home") + "/.kde/share/config/kickerrc";
		
	public static final String OS_NAME = System.getProperty("os.name");

	public static final String OS_ARCH=System.getProperty("os.arch").toLowerCase();
	
	public static final String ASSOCIATION_SECTION="Application";
	
	private Insets current_insets=null;
	
	private IniFileManager m_ApplicationsSettingFile=null;
	
	private DesktopTools() {
		super();
	}
	
	private static DesktopTools mInstance=null;
	public static DesktopTools instance() {
		if (mInstance==null)
			mInstance=new DesktopTools();
		return mInstance;
	}
	
	public void initApplicationsSetting(IniFileManager aApplicationsSettingFile) {
		m_ApplicationsSettingFile=aApplicationsSettingFile;
	}

	public void launch(String aUrl) throws LucteriosException
	{
		if (aUrl.startsWith("mailto:"))
			openInMail(aUrl);
		else if (aUrl.startsWith("http://") || aUrl.startsWith("https://"))
			openInWeb(aUrl);
		else
			openInFile(aUrl);
	}

	public Insets getInsets() {
		if (current_insets==null)
		switch (isCompatibleOS()) {
			case 0: 
				extractKDEInsets();
				break;
			case 1: 
				extractGnomeInsets();
				break;
			default: 
				extractDefaultInsets();
				break;
		}
		return current_insets;
	}	

	private void openInFile(String aUrl) throws LucteriosException {
		int dot_pos=aUrl.lastIndexOf('.');
		String ext=aUrl.substring(dot_pos+1);
		String applic="";
		if (m_ApplicationsSettingFile!=null) {
			applic=m_ApplicationsSettingFile.getValueSection(ASSOCIATION_SECTION,ext);
		}
		if ((applic!=null) && (applic.length()>0)) {
			String[] args=new String[] {applic, aUrl};				
			new ProcessExitDetector(args);			
		} 
		else if ("x86".equals(OS_ARCH)) {
				try {
					URL url = new URL(aUrl);
					File current_file=new File(url.toURI());
					String[] args = new String[]{"rundll32","url.dll","FileProtocolHandler",'"'+current_file.getAbsolutePath()+'"'};
					new ProcessExitDetector(args);							
				} catch (MalformedURLException e) {
					throw new LucteriosException("Fichier '"+aUrl+"' non ouvrable!",e);
				} catch (URISyntaxException e) {
					throw new LucteriosException("Fichier '"+aUrl+"' non ouvrable!",e);
				}
		}
		else
			openInWeb(aUrl);
	}
	
	private void openInWeb(String aUrl) throws LucteriosException {
		try {
			String[] args;
	        Logging.getInstance().writeLog("OS to open URL",OS_ARCH,1);
			if ("x86".equals(OS_ARCH))
				args = new String[]{"rundll32","url.dll","FileProtocolHandler",aUrl};
			else if ("ppc".equals(OS_ARCH) || "powerpc".equals(OS_ARCH) || "i386".equals(OS_ARCH)) // MAC OS-X
				args = new String[] {"open",aUrl};
			else
				args=new String[] {searchBrowserFromUnix(), aUrl};				
			new ProcessExitDetector(args);			
		} catch (InterruptedException e) {
			throw new LucteriosException("Page non trouvée!",e);
		} 
	}
	
	private void openInMail(String aUrl) throws LucteriosException {
		try {
			String[] args;
			String os_arch=System.getProperty("os.arch").toLowerCase();
	        Logging.getInstance().writeLog("OS to open URL",os_arch,1);
			if ("x86".equals( os_arch ))
				args = new String[]{"rundll32","url.dll","FileProtocolHandler",aUrl};
			else if ("ppc".equals( os_arch ) || "powerpc".equals( os_arch ))
				args = new String[] {"open",aUrl};
			else
				args = new String[] {searchMailerFromUnix(),aUrl};				
			new ProcessExitDetector(args);			
		} catch (InterruptedException e) {
			throw new LucteriosException("Courriel non trouvée!",e);
		} 
	}

	private String searchBrowserFromUnix() throws InterruptedException, LucteriosException 
	{
		String[] browsers = {"xdg-open","firefox", "mozilla", "konqueror", "opera", "epiphany", "netscape" }; 
		String browser = null; 
		try {
			for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime().exec( new String[] {"which", browsers[count]}).waitFor() == 0)
						browser = browsers[count];
			if (browser == null) 
				throw new LucteriosException("Impossible de trouver un navigateur Web."); 
			return browser;
		} catch (IOException e) {
			throw new LucteriosException("Navigateur web non trouvé",e);
		} 
	}

	private String searchMailerFromUnix()
			throws InterruptedException,  LucteriosException {
		String[] browsers = {"thunderbird", "xdg-open", "kmail", "evolution", "mozilla", "opera", "netscape" }; 
		String browser = null; 
		try {
			for (int count = 0; count < browsers.length && browser == null; count++)
			if (Runtime.getRuntime().exec( new String[] {"which", browsers[count]}).waitFor() == 0)
				browser = browsers[count]; 
			if (browser == null) 
				throw new LucteriosException("Impossible de trouver un gestionnaire de courriels."); 
			return browser;
		} catch (IOException e) {
			throw new LucteriosException("Gestionnaire de courriels non trouvé",e);
		} 
	}
	
	/*
	 * Default insets if an error occured
	 */
	private void extractDefaultInsets() {
		System.err.println("Default Insets");
		try {
			GraphicsDevice[] gdev_list=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
			for (int gs_index=0;gs_index<gdev_list.length;gs_index++)
			{
				GraphicsDevice gs=gdev_list[gs_index];
				GraphicsConfiguration[] gconf_list=gs.getConfigurations();
				if (gconf_list.length>0)
					current_insets=(Toolkit.getDefaultToolkit().getScreenInsets(gconf_list[0]));
			}
		}
		catch (HeadlessException h) {
			System.err.println("Error: Headless error (you aren't running a GUI)");
		}
		current_insets=new Insets(0,0,0,0);
	}
	
	/*
	 * Created for Gnome 2.12.x.x
	 */
	private void extractGnomeInsets() {
            int n=0; int s=0; int e=0; int w=0;

            try {
                    // get the list of panels
                    Process p = Runtime.getRuntime().exec("gconftool --all-dirs /apps/panel/toplevels");
                    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    Process p2;
                    BufferedReader r2;
                    String line = r.readLine();
                    while (line != null) {
                            // get the orientation of the panel (top/bottom/left/right
                            p2 = Runtime.getRuntime().exec("gconftool -g "+line+"/orientation");
                            r2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                            String orientation = r2.readLine();
                            
                            // get the auto_hide value to know witch size to get
                            p2 = Runtime.getRuntime().exec("gconftool -g "+line+"/auto_hide");
                            r2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                            String auto_hide = r2.readLine();
                            
                            // get the right size
                            int theSize = 0;
                            if(auto_hide.toLowerCase().startsWith("true")) {
                                p2 = Runtime.getRuntime().exec("gconftool -g "+line+"/auto_hide_size");
                                r2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                                theSize = Integer.parseInt(r2.readLine());
                            }
                            else {
                                p2 = Runtime.getRuntime().exec("gconftool -g "+line+"/size");
                                r2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                                theSize = Integer.parseInt(r2.readLine());
                            }
                            
                            // affect the right size's value to the good variable (n, s, e, w)
                            if(orientation.toLowerCase().startsWith("top")) {
                                n += theSize;
                            }
                            else if(orientation.toLowerCase().startsWith("bottom")) {
                                s += theSize;
                            }
                            else if(orientation.toLowerCase().startsWith("left")) {
                                w += theSize;
                            }
                            else if(orientation.toLowerCase().startsWith("right")) {
                                e += theSize;
                            }

                            line = r.readLine();
                    }
            }
            catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Error: IO Exception during Runtime exec");
            }
            current_insets=new Insets(n, w, s, e);
	}
	
	/* 
	 * Created for KDE 3.5 
	 */
	private void extractKDEInsets() {
		/* KDE:		2 Top			|	JAVA:		0 Top
		 *	0  Left		1  Right	|		1 Left 		3 Right
		 *		3 Bottom		|			2 Bottom
		 */
		int[] sizes = {24, 30, 46, 58, 0};	// 	xSmall, Small, Medium, Large, xLarge, Null
		int[] i = {0, 0, 0, 0, 0}; 		// 	Left, Right, Top, Bottom, Null
		
		
		/* Needs to be fixed.  Doesn't know the difference between CustomSize and Size */
		File kde_file=new File(KDE_CONFIG);
		if (kde_file.exists()) {
			IniFileReader ini_file=new IniFileReader(KDE_CONFIG);
			int customSiz = ini_file.getValueSectionInt("General", "CustomSize");
			int siz = ini_file.getValueSectionInt("General", "Size");
			int pos = ini_file.getValueSectionInt("General", "Position");
			ini_file.close();
			int position = pos==-1?2:pos;
			if(siz == -1) siz = 3;
			int size = (customSiz==-1||siz!=4)?siz:customSiz;
			size = size<24?sizes[size]:size;
			i[position]=size;
		}
		else
			i[3]=sizes[3];
		current_insets=new Insets(i[2],i[0],i[3],i[1]);
	}
	
	/*
	 * Determine if current Operating System is a *nix flavor 
	 */
	private int isCompatibleOS() {
		if (!OS_NAME.toLowerCase().startsWith("windows")) {
			try {
				Process p = Runtime.getRuntime().exec("ps ax");
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				java.util.List desktopList = Arrays.asList(DESKTOP_ENVIRONMENTS.split("\\|"));
				
				String line = r.readLine();
				while (line != null) {
					for (int index=0;index<desktopList.size();index++)
					{
						String s=(String)desktopList.get(index);
						if (line.contains(s) && !line.contains("grep"))
							return desktopList.indexOf(s);
					}					
					line = r.readLine();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error: IO Exception during Runtime exec");
			}
		}
		return -1;
	}
}
