package org.lucterios.utils.graphic;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FilesFilter extends FileFilter {
	private String[] mfiles = null;
	private String mDescription="";

	public FilesFilter(String[] afiles,String aDescription) {
		super();
		mfiles = afiles;
		mDescription=aDescription;
	}

	public boolean accept(File aFile) {
		if (aFile.isDirectory())
			return true;
		String name=aFile.getName();
		if ((mfiles != null) && (mfiles.length > 0)) {
			for (int index = 0; index < mfiles.length; index++)
				if (name.length() > mfiles[index].length()) {
					String ext = name.substring(
							name.length() - mfiles[index].length())
							.toLowerCase();
					if (ext.equalsIgnoreCase(mfiles[index]))
						return true;
				}
			return false;
		} else
			return true;
	}

	public String getDescription() {
		return mDescription;
	}
}
