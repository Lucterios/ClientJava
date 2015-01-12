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

package org.lucterios.engine.application;

public class ActionConstantes {

	public final static int FORM_NOMODAL = 0;
	public final static int FORM_MODAL = 1;
	public final static int FORM_REFRESH = 2;

	public final static int SELECT_NONE = 1;
	public final static int SELECT_SINGLE = 0;
	public final static int SELECT_MULTI = 2;

    public static final int CTRL_MASK		= 1 << 1;

    public static final int META_MASK		= 1 << 2;
	
	public static int getMaxSelect(int aSelect1, int aSelect2) {
		int res = SELECT_NONE;
		if ((aSelect1 == SELECT_MULTI) || (aSelect2 == SELECT_MULTI)) {
			res = SELECT_MULTI;
		} else if ((aSelect1 == SELECT_SINGLE) || (aSelect2 == SELECT_SINGLE)) {
			res = SELECT_SINGLE;
		}
		return res;
	}

	public static int getControlKey() {
		String os_arch = System.getProperty("os.arch");
		if (os_arch.equalsIgnoreCase("PowerPC")
				|| os_arch.equalsIgnoreCase("ppc")) {
			return META_MASK;
		} else {
			return CTRL_MASK;
		}
	}

}
