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

package org.lucterios.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lag
 *
 */
public class BufferInputStream extends InputStream 
{
	//private java.io.StringBufferInputStream mBuffer;
	private String mTextBuffer;
	private int mIndex=0;
	/**
	 * 
	 */
	public BufferInputStream(String textBuffer) {
		super();
		//mBuffer=new java.io.StringBufferInputStream(textBuffer);
		mTextBuffer=textBuffer;
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	public int read() throws IOException 
	{
		if (mIndex<mTextBuffer.length())
		{
			char c=mTextBuffer.charAt(mIndex++);
			return (int)c;
		}
		else
			return -1;
	}

}
