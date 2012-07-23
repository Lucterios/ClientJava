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
    
	public static final int FAILURE=0;
	public static final int CRITIC=1;
	public static final int GRAVE=2;
	public static final int IMPORTANT=3;
	public static final int MINOR=4;
	
	
	public String mRequest="";
	public String mReponse="";
	private Exception mLastException=null;
	public boolean mWithTrace=true;
	private int mType=FAILURE;

	public LucteriosException(String aMsg)
	{
		super(aMsg);
		mWithTrace=true;
	}

	
	public LucteriosException(String aMsg,boolean aWithTrace)
	{
		this(aMsg);
		mWithTrace=aWithTrace;
	}

	public LucteriosException(String aMsg,Exception aLastException)
	{
		this(aMsg);
		mLastException=aLastException;
	}

	public LucteriosException(String aMsg,String aRequest,String aReponse)
	{
		this(aMsg);
        mRequest=aRequest;
        mReponse=aReponse;
	}
	
	public LucteriosException(String S,String aRequest,String aReponse,Exception aLastException)
	{
		this(S,aRequest,aReponse);
		mLastException=aLastException;
	}

	public LucteriosException(String S,String aRequest,String aReponse,Exception aLastException,int aType)
	{
		this(S,aRequest,aReponse,aLastException);
		mType=aType;
		mWithTrace=false;
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


	public int getType() {
		return mType;
	}	
}
