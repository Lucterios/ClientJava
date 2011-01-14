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

package org.lucterios.client.setting;

import java.io.InputStream;

import org.lucterios.engine.resources.Resources;
import org.lucterios.utils.Tools;

public class Constants {
	public static final String Version() {
		InputStream is = Resources.class.getResourceAsStream("version.txt");
		try {
			return Tools.parseISToString(is).replace(' ', '.');
		} catch (Throwable e) {
			return "";
		}
	}

	public static final int CoreMainVersion = 1;
	public static final int CoreSubVersion = 1;

	public static boolean CheckVersionInferiorEgual(String aCoreVersion,
			int aMainVersion, int aSubVersion) {
		int pos1 = aCoreVersion.indexOf(".");
		if (pos1 == -1)
			return false;
		String main_version = aCoreVersion.substring(0, pos1);
		int pos2 = aCoreVersion.indexOf(".", pos1 + 1);
		if (pos2 == -1)
			return false;
		String sub_version = aCoreVersion.substring(pos1 + 1, pos2 - pos1 + 1);
		if (Integer.parseInt(main_version) > aMainVersion)
			return false;
		else if ((Integer.parseInt(main_version) == aMainVersion)
				&& (Integer.parseInt(sub_version) > aSubVersion))
			return false;
		return true;
	}

	public static boolean CheckCoreVersion(String aCoreVersion) {
		return CheckVersionInferiorEgual(aCoreVersion, CoreMainVersion,
				CoreSubVersion);
	}
}
