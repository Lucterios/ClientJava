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

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.*;


public class Logging 
{
    private class HTMLFormatter extends Formatter
    {
        private String replace(String aValue,String aTarget,String aRemplacement)
        {
            String result=aValue;
            int len=aTarget.length();
            int pos;
            while((pos=result.indexOf(aTarget))!=-1)
            {
                result=result.substring(0, pos)+aRemplacement+result.substring(pos+len);
            }
            return result;
        }
        
        public String format(LogRecord record)
        {
            StringBuffer text= new StringBuffer();
            boolean xfer_record=record.getSourceClassName().equals(CLASS_XFER);
            String dt=DateFormat.getDateTimeInstance().format(new Date(record.getMillis()));
            String title="";
            String tdclass="";
            if (xfer_record)
            {
                tdclass=record.getSourceMethodName();
                if (record.getSourceMethodName().equals(METHOD_XFER_OUT))
                    title="Envoyé";
                else
                    title="Reçu";                
            }
            else
            {
                tdclass=record.getSourceClassName();
                title="Message";
            }
            text.append("<table class='TITLE'>\n");
            text.append("<tr onclick='ChangeItem(\""+record.getSequenceNumber()+"\")'><td width='70%'>"+title+"</td><td>"+dt+"</td></tr>\n<tr><td colspan='2'>\n");
            text.append("<table id='"+record.getSequenceNumber()+"' class='"+tdclass+"'>\n");
            if (!xfer_record)
                text.append("<tr><td>"+record.getSourceMethodName()+"</td></tr>\n");
            String msg=record.getMessage();
            msg=replace(msg, "><", "&gt;\n&lt;");
            msg=replace(msg, "<", "&lt;");
            msg=replace(msg, ">", "&gt;");
            text.append("<tr><td><pre>"+msg+"</pre></td></tr>\n");
            text.append("</table>\n");
            text.append("</td></tr>\n");
            text.append("</table>\n");
            return text.toString();
        }
        public String getHead(Handler h)
        {
            StringBuffer text= new StringBuffer();
            text.append("<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.0 Transitional//EN'>\n");
            text.append("<HTML>\n<HEAD>\n");
            text.append("<TITLE>CLIENT JAVA APAS - DEBUG MONITORING</TITLE>\n");
            text.append("<STYLE>\n<!--\n");
            text.append("H1{");
            text.append("color:#0000AA;");
            text.append("text-align:center;");
            text.append("}");
            text.append("H2{");
            text.append("color:#0000FF;");
            text.append("text-align:center;");
            text.append("}");
            text.append(".TITLE{");
            text.append("color:#0000DD;");
            text.append("background:#CFCFCF;");
            text.append("width:100%;");
            text.append("border:1px solid black;");
            text.append("}");
            text.append(".LOG{");
            text.append("color:black;");
            text.append("background:#AFAFAF;");
            text.append("width:100%;");
            text.append("border:1px solid black;");
            text.append("display:none;");
            text.append("}");
            text.append(".OUT");
            text.append("{");
            text.append("color:black;");
            text.append("background:#AFAFAF;");
            text.append("width:100%;");
            text.append("border:1px solid black;");
            text.append("display:none;");
            text.append("}");
            text.append(".IN");
            text.append("{");
            text.append("color:black;");
            text.append("background:#AFAFAF;");
            text.append("width:100%;");
            text.append("border:1px solid black;");
            text.append("display:none;");
            text.append("}\n-->\n");
            text.append("</STYLE>\n");
            text.append("<SCRIPT type='text/javascript'>\n");
            text.append("function ChangeItem(ItemId)\n");
            text.append("{\n");
            text.append("   var field_obj=document.getElementById(ItemId);\n");
            text.append("   if (field_obj.style.display=='none')\n");
            text.append("	field_obj.style.display='block';\n");
            text.append("   else\n");
            text.append("	field_obj.style.display='none';\n");
            text.append("}\n");
            text.append("</SCRIPT>\n");
            text.append("</HEAD>\n<BODY>\n");
            text.append("<H1>CLIENT JAVA APAS</H1>\n");
            text.append("<H2>DEBUG MONITORING</H2>\n");
            text.append("<HR>\n");    
            return text.toString();
        }
        public String getTail(Handler h)
        {
            return "</body>\n</html>\n";
        }
    }
    static private Logging mInstance=null;
    
    static public Logging getInstance()
    {
        if (mInstance==null)
            mInstance=new Logging();
        return mInstance;
    }
    
    private Logger mLogger=null;
    
    /** Creates a new instance of Logging */
    private Logging() 
    {
        
    }

    public void setDebugLevel(String aLevelParam)
    {
        int level=-1;
        if ((aLevelParam.length()>=5) && (aLevelParam.substring(0, 5).equalsIgnoreCase("DEBUG")))
        {
            if (aLevelParam.length()>5)
            try
            {
                String lvl=aLevelParam.substring(6);
                level=Integer.parseInt(lvl);
            }
            catch(Exception e){level=0;}
            else
                level=0;
        }
        setDebugLevel(level);
    }

    private Level convertLevel(int aLevel)
    {
        switch(aLevel)
        {
            case 0:
                return Level.SEVERE;
            case 1:
                return Level.WARNING;
            case 2:
                return Level.INFO;
            case 3:
                return Level.FINE;
            default:
                return Level.ALL;
        }
    }
    
    static public String FILE_NAME="LucteriosClientDebug";
    
    public void setDebugLevel(int aLevel)
    {
        if (aLevel>=0)
        try
        {
            Handler fh=new FileHandler(FILE_NAME+".html",50000,1);
            fh.setFormatter(new HTMLFormatter());
            mLogger=Logger.getLogger("ApasClient");
            mLogger.addHandler(fh);
            mLogger.setLevel(Level.ALL); // convertLevel(aLevel));
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        else
            mLogger=null;
    }
    
    static public String CLASS_XFER="XFER";
    static public String CLASS_LOG="LOG";
    static public String METHOD_XFER_IN="IN";
    static public String METHOD_XFER_OUT="OUT";

    public void setInText(String aText)
    {
        if (mLogger!=null)
            mLogger.logp(convertLevel(0),CLASS_XFER,METHOD_XFER_IN,aText);
    }
    public void setOutText(String aText)
    {
        if (mLogger!=null)
            mLogger.logp(convertLevel(0),CLASS_XFER,METHOD_XFER_OUT,aText);    
    }

    public void writeLog(String aTitle, String aText,int aLevel)
    {
        if (mLogger!=null)
            mLogger.logp(convertLevel(aLevel),CLASS_LOG,aTitle,aText);        
    }
}
