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
*	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
*/

package org.lucterios.update;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.JOptionPane;

public class Main 
{	
	public static void ShowHelp()
	{
		System.out.println("Syntax : LucteriosUpdate ArchiveFile DestinationPath ApplicationName");
	}
	
	public static void checkPathExists(File path)
	{
		if (!path.exists())
			path.mkdirs();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		if (args.length==3)
		{
			String zip_path=args[0];
			File zip_file=new File(zip_path);
			String target_path=args[1];
			if (target_path.charAt(target_path.length()-1)!='/')
				target_path+="/";
			File target_dir=new File(target_path);
			String ApplicationName=args[2];
			System.out.println("LucteriosUpdate '"+zip_path+"' '"+target_dir+"' '"+ApplicationName+"'");
			if (zip_file.isFile() && target_dir.isDirectory())
			{
				WaitWindow ww=new WaitWindow(ApplicationName);
				try
				{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					try 
					{
						deleteDirectory(new File(target_dir,"lib"));
						unzipArchive(zip_file, target_path);
					}
					finally
					{
						ww.dispose();
					}
					JOptionPane.showMessageDialog(null, "Mise à jour effectuée avec success.\nRelancement de l'application.", ApplicationName, JOptionPane.INFORMATION_MESSAGE);
					Runtime rt=Runtime.getRuntime();
					try 
					{
						String[] file_jar;
						boolean for_mac_osx=false;
						if (System.getProperty("os.name").toLowerCase().startsWith("mac os"))
							for_mac_osx=target_dir.getParentFile().getParentFile().getParent().endsWith(".app");
						
						if (for_mac_osx) {// MAC OS-X
							file_jar=new String[]{"open",target_dir.getParentFile().getParentFile().getParent()};
						}
						else {
							String java_dir=System.getProperty("java.home");
							file_jar=new String[]{java_dir+"/bin/java","-jar",target_dir.getAbsolutePath()+"/LucteriosClient.jar"};
						}
						System.out.println("Restart:"+file_jar.toString());
						rt.exec(file_jar);
					} 
					catch (IOException e){}					
					System.out.println("Exit");
					System.exit(0);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					ShowHelp();
				}
			}
			else
				ShowHelp();
		}
		else
			ShowHelp();
	}
	
	private static boolean deleteDirectory(File path) {
	    if(path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
	

	private static void unzipArchive(File zipFile, String targetPath)
			throws ZipException, IOException, FileNotFoundException {
		int BUFFER=2048;
		byte data[]=new byte[BUFFER];

		ZipFile zf = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> entries = zf.entries();
		BufferedOutputStream dest=null;
		while (entries.hasMoreElements())
		{
			ZipEntry e=entries.nextElement();
			String file_extracted=targetPath+e.getName();
			File new_file=new File(file_extracted);
			if (!new_file.isDirectory())
			{
				if (new_file.exists())
					new_file.delete();
				checkPathExists(new_file.getParentFile());
				InputStream is=zf.getInputStream(e);
				System.out.println(file_extracted);
				
				FileOutputStream fos=new FileOutputStream(file_extracted);
				dest = new BufferedOutputStream(fos,BUFFER);
				int count;
				while ((count=is.read(data,0,BUFFER))!=-1)
				{
					dest.write(data,0,count);
				}
				dest.flush();
				dest.close();
			}
		}
		zf.close();
	}

}
