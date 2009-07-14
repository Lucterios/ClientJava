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

package org.lucterios.Print;

import java.io.File;

public class ExtensionFilter extends javax.swing.filechooser.FileFilter
{
	public static final String EXTENSION_EXPORT_PDF = ".pdf";
	public static final String EXTENSION_EXPORT_CSV = ".csv";

	private String FExtensionFile;
	
	public ExtensionFilter(String aExtensionFile){
		super();
		assert aExtensionFile!=null;
		assert aExtensionFile.length()>0;
		FExtensionFile=aExtensionFile.toLowerCase();
		if (FExtensionFile.charAt(0)!='.')
			FExtensionFile='.'+FExtensionFile;
	}
	
	public boolean accept(File aFile) {
		if (aFile.isDirectory())
			return true;
		String name=aFile.getName();
		if (name.length()>4)
		{
			String ext=name.substring(name.length()-4).toLowerCase();
			return FExtensionFile.equals( ext );
		}
		else
			return false;
	}

	public String getDescription() {
		return "Fichier "+FExtensionFile.substring(1).toUpperCase();
	}
}
