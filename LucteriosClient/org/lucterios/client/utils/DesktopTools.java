package org.lucterios.client.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.IniFileManager;
import org.lucterios.utils.IniFileReader;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools.InfoDescription;

public class DesktopTools extends DesktopInterface {

	class ProcessExitDetector extends Thread {

		private Process process;
		private PrintStream outStream;

	    private boolean isExited() {
	        try {
	            process.exitValue();
	            return false; 
	        } catch (IllegalThreadStateException exc) {
	            return true; 
	        }
	    }
		
	    public ProcessExitDetector(String[] aArgs) throws LucteriosException {
	    	outStream=System.out;
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
					outStream.println(inputLine);
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

	private int[] current_coord=null;
	
	private IniFileManager m_ApplicationsSettingFile=null;
	
	public DesktopTools() {
		super();
		DesktopInterface.gInstance=this;
	}
		
	/* (non-Javadoc)
	 * @see org.lucterios.utils.graphic.DesktopInterface#initApplicationsSetting(org.lucterios.utils.IniFileManager)
	 */
	public void initApplicationsSetting(IniFileManager aApplicationsSettingFile) {
		m_ApplicationsSettingFile=aApplicationsSettingFile;
	}

	/* (non-Javadoc)
	 * @see org.lucterios.utils.graphic.DesktopInterface#launch(java.lang.String)
	 */
	public void launch(String aUrl) throws LucteriosException
	{
		if (aUrl.startsWith("mailto:"))
			openInMail(aUrl);
		else if (aUrl.startsWith("http://") || aUrl.startsWith("https://"))
			openInWeb(aUrl);
		else
			openFile(aUrl);
	}

	/* (non-Javadoc)
	 * @see org.lucterios.utils.graphic.DesktopInterface#getInsets()
	 */
	public int[] getCoord(GUIGenerator generator) {
		if (current_coord==null)
		switch (isCompatibleOS()) {
			case 0: 
				extractKDEInsets();
				break;
			case 1: 
				extractGnomeInsets();
				break;
			default: 
				current_coord=generator.getDefaultInsets();
				break;
		}
		return current_coord;
	}
	
	public void printFilePDF(String aUrl) throws LucteriosException {
		String[] args;
        Logging.getInstance().writeLog("OS to open URL",OS_NAME,1);
		if (OS_NAME.toLowerCase().startsWith("windows")) {
			String file_path = convertWinFilePath(aUrl);
			args = new String[]{"rundll32","url.dll","FileProtocolHandler",file_path};
		}
		else if (OS_NAME.toLowerCase().startsWith("mac os")) // MAC OS-X
			args = new String[] {"print",aUrl};
		else
			args=new String[] {"lpr", aUrl};				
		new ProcessExitDetector(args);			
	}
	
	public void openFile(String aUrl) throws LucteriosException {
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
		else {
			String file_path = convertWinFilePath(aUrl);
			openInWeb(file_path);
		}
	}

	private String convertWinFilePath(String aUrl) throws LucteriosException {
		String file_path=aUrl;
		if (OS_NAME.toLowerCase().startsWith("windows")) {
			try {
				URL url = new URL(aUrl);
				File current_file=new File(url.toURI());
				file_path='"'+current_file.getAbsolutePath()+'"';
			} catch (Exception e) {
				throw new LucteriosException("Fichier '"+aUrl+"' non ouvrable!",e);
			}
		}
		return file_path;
	}
	
	private void openInWeb(String aUrl) throws LucteriosException {
		String[] args;
        Logging.getInstance().writeLog("OS to open URL",OS_NAME,1);
		if (OS_NAME.toLowerCase().startsWith("windows"))
			args = new String[]{"rundll32","url.dll","FileProtocolHandler",aUrl};
		else if (OS_NAME.toLowerCase().startsWith("mac os")) // MAC OS-X
			args = new String[] {"open",aUrl};
		else
			args=new String[] {searchBrowserFromUnix(), aUrl};				
		new ProcessExitDetector(args);			
	}
	
	private void openInMail(String aUrl) throws LucteriosException {
		String[] args;
        Logging.getInstance().writeLog("OS to open URL",OS_NAME,1);
        if (OS_NAME.toLowerCase().startsWith("windows"))
			args = new String[]{"rundll32","url.dll","FileProtocolHandler",aUrl};
		else if (OS_NAME.toLowerCase().startsWith("mac os")) // MAC OS-X
			args = new String[] {"open",aUrl};
		else
			args = new String[] {searchMailerFromUnix(),aUrl};				
		new ProcessExitDetector(args);			
	}

	private String searchBrowserFromUnix() throws LucteriosException 
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
		} catch (InterruptedException e) {
			throw new LucteriosException("Navigateur web non trouvé",e);
		} catch (IOException e) {
			throw new LucteriosException("Navigateur web non trouvé",e);
		} 
	}

	private String searchMailerFromUnix() throws LucteriosException {
		String[] browsers = {"thunderbird", "xdg-open", "kmail", "evolution", "mozilla", "opera", "netscape" }; 
		String browser = null; 
		try {
			for (int count = 0; count < browsers.length && browser == null; count++)
			if (Runtime.getRuntime().exec( new String[] {"which", browsers[count]}).waitFor() == 0)
				browser = browsers[count]; 
			if (browser == null) 
				throw new LucteriosException("Impossible de trouver un gestionnaire de courriels."); 
			return browser;
		} catch (InterruptedException e) {
			throw new LucteriosException("Gestionnaire de courriels non trouvé",e);
		} catch (IOException e) {
			throw new LucteriosException("Gestionnaire de courriels non trouvé",e);
		} 
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
            current_coord=new int[]{n, w, s, e};
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
		current_coord=new int[]{i[2],i[0],i[3],i[1]};
	}
	
	/*
	 * Determine if current Operating System is a *nix flavor 
	 */
	private int isCompatibleOS() {
		if (!OS_NAME.toLowerCase().startsWith("windows")) {
			try {
				Process p = Runtime.getRuntime().exec("ps ax");
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				java.util.List<String> desktopList = Arrays.asList(DESKTOP_ENVIRONMENTS.split("\\|"));
				
				String line = r.readLine();
				while (line != null) {
					for (int index=0;index<desktopList.size();index++)
					{
						String s=desktopList.get(index);
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

	public void throwException(Exception e) {
		ExceptionDlg.throwException(e);
	}

	public void setInfoDescription(InfoDescription infoDescription) {
		ExceptionDlg.mInfoDescription=infoDescription;
	}
}
