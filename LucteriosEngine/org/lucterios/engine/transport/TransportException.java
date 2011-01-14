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

package org.lucterios.client.transport;

import org.lucterios.utils.LucteriosException;

public class TransportException extends LucteriosException {
	private static final long serialVersionUID = 1L;
	public final static int TYPE_STANDARD = 0;
	public final static int TYPE_HTTP = 1;
	public int mType = TYPE_STANDARD;
	public int mCode = 0;

	public TransportException(String aError, int aType, int aCode) {
		super(aError);
		mCode = aCode;
		mType = aType;
	}

	public TransportException(String aError, int aType, int aCode,
			Exception aLastException) {
		super(aError, aLastException);
		mCode = aCode;
		mType = aType;
	}

	public TransportException(String aError, int aType, int aCode,
			String aRequest, String aReponse) {
		super(aError, aRequest, aReponse);
		mCode = aCode;
		mType = aType;
	}

	public TransportException(String aError, int aType, int aCode,
			String aRequest, String aReponse, Exception aLastException) {
		super(aError, aRequest, aReponse, aLastException);
		mCode = aCode;
		mType = aType;
	}

	public String getExtraInfo() {
		String msg = super.getExtraInfo();
		msg += "<b>Code</b> " + mCode + "<br>";
		msg += "<b>Type</b> " + mType + "<br>";
		return msg;
	}

}
