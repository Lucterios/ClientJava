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

import java.io.PrintStream;
import java.io.PrintWriter;

public class LucteriosException extends Exception
{
	private static final long serialVersionUID = 1L;
	public String mRequest="";
	public String mReponse="";
	public Exception mLastException=null;
	
	public LucteriosException(String aMsg)
	{
		super(aMsg);
	}

	public LucteriosException(String aMsg,Exception aLastException)
	{
		super(aMsg);
		mLastException=aLastException;
	}

	public LucteriosException(String aMsg,String aRequest,String aReponse)
	{
		super(aMsg);
        mRequest=aRequest;
        mReponse=aReponse;
	}
	
	public LucteriosException(String S,String aRequest,String aReponse,Exception aLastException)
	{
		super(S);
        mRequest=aRequest;
        mReponse=aReponse;
		mLastException=aLastException;
	}
	
	public String getExtraInfo()
	{
		return "";
	}
	
    public void printStackTrace() 
    {
        super.printStackTrace();
        if (mLastException != null) 
        {        	
            System.out.println("----------");
            System.out.println("Caused by "+mLastException.getClass().getName()+":"+mLastException.getMessage());
            mLastException.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream s) 
    {
        super.printStackTrace(s);
        if (mLastException != null) 
        {
            s.println("----------");
            s.println("Caused by "+mLastException.getClass().getName()+":"+mLastException.getMessage());
            mLastException.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) 
    {
        super.printStackTrace(s);
        if (mLastException != null) 
        {
            s.println("----------");
            s.println("Caused by:");
            mLastException.printStackTrace(s);
        }
    }	
}
