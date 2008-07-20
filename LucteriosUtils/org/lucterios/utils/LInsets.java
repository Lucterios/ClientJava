/*
*    This file is part of Lucterios.
*
*    Lucterios is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Lucterios is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Lucterios; if not, write to the Free Software
*    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*
*	Contributeurs: 
* 		A. Tres Finocchiaro
*		Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
*/
package org.lucterios.utils;

/* Linsets
 *
 * 	A class for getting the screen
 * 	insets for non-Windows Operating Systems
 *
 * 	By:
 * 		A. Tres Finocchiaro
 * 	Liscense:
 * 		http://www.gnu.org/copyleft/gpl.html
 *	Compatible with:
 *		Java 1.5 SE, Java 1.6 SE
 *	Keywords:
 *		insets, osx, linux, unix, 
 *		solaris, kde, gnome, xfce, 
 *		ubuntu, java, darwin
 */
import java.io.*;
import java.util.*;
import java.awt.*;
 
public class LInsets {
	private static final String DESKTOP_ENVIRONMENTS = "kdesktop|gnome-panel|xfce|darwin";
	
	private static final String KDE_CONFIG = System.getProperty("user.home") + "/.kde/share/config/kickerrc";
		
	private static final String OS_NAME = System.getProperty("os.name");
		
	public static Insets getInsets() {
		switch (isCompatibleOS()) {
			case  0: return getKDEInsets();
			case  1: return getGnomeInsets();
			case  2: return getXfceInsets();
			case  3: return getDarwinInsets();
			default: return getDefaultInsets();
		}
	}
	
	/*
	 * Default insets if an error occured
	 */
	private static Insets getDefaultInsets() {
		System.err.println("\nPlease see: http://www.google.com/search?q=gnome+kde+insets\n\n"); 
		System.err.println("\nTrying default insets for " + OS_NAME);
		try {
			GraphicsDevice[] gdev_list=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
			for (int gs_index=0;gs_index<gdev_list.length;gs_index++)
			{
				GraphicsDevice gs=gdev_list[gs_index];
				GraphicsConfiguration[] gconf_list=gs.getConfigurations();
				if (gconf_list.length>0)
					return (Toolkit.getDefaultToolkit().getScreenInsets(gconf_list[0]));
			}
		}
		catch (HeadlessException h) {
			System.err.println("Error: Headless error (you aren't running a GUI)");
		}
		return new Insets(0,0,0,0);
	}
	
	/*
	 * Created for Mac OS 10.x.x
	 */
	private static Insets getDarwinInsets() {
		return getDefaultInsets();
	}
	
	/*
	 * Created for Gnome 2.12.x.x
	 */
	private static Insets getGnomeInsets() {
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
            
            return new Insets(n, w, s, e);
	}
	
	private static Insets getXfceInsets() {
		return getDefaultInsets();
	}
	
	/* 
	 * Created for KDE 3.5 
	 */
	private static Insets getKDEInsets() {
		/* KDE:		2 Top			|	JAVA:		0 Top
		 *	0  Left		1  Right	|		1 Left 		3 Right
		 *		3 Bottom		|			2 Bottom
		 */
		int[] sizes = {24, 30, 46, 58, 0};	// 	xSmall, Small, Medium, Large, xLarge, Null
		int[] i = {0, 0, 0, 0, 0}; 		// 	Left, Right, Top, Bottom, Null
		
		
		/* Needs to be fixed.  Doesn't know the difference between CustomSize and Size */
		int customSiz = getKdeINI("General", "CustomSize");
		int siz = getKdeINI("General", "Size");
		int pos = getKdeINI("General", "Position");
		int position = pos==-1?3:pos;
		int size = (customSiz==-1||siz!=4)?siz:customSiz;
		size = size<24?sizes[size]:size;
		i[position]=size;
		return new Insets(i[2],i[0],i[3],i[1]);
	}
	
	/*
	 * Determine if current Operating System is a *nix flavor 
	 */
	private static int isCompatibleOS() {
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
	
	private static int getKdeINI(String category, String component) {
		try {	
			File f = new File(KDE_CONFIG);
			if (f.exists() && (category != null) && (component != null)){		
				FileReader reader = new FileReader(f);
				BufferedReader buffer = new BufferedReader(reader);
				String value = null;
				String temp = buffer.readLine();
				while ((value == null) && (temp != null)) {
					value = getValueInSection(category, component, buffer, temp);
					temp = buffer.readLine();
				}
				buffer.close();	
				reader.close();
				if (value!=null)
					return Integer.parseInt(value);
			}
		}
		catch (Exception e) {}
		return -1;
	}

	private static String getValueInSection(String category, String component, 
			BufferedReader buffer, String currentLine) throws IOException 
	{
		String value=null; 
		String current_temp=currentLine;
		if (current_temp.trim().equals("[" + category + "]")) {
			current_temp = buffer.readLine();
			while ((value == null) && (current_temp != null) && !current_temp.trim().startsWith("[")) {
				if (current_temp.startsWith(component + "=")) 
					value = current_temp.substring(component.length() + 1);
				current_temp = buffer.readLine();	
			}
		}
		return value;
	}
}
