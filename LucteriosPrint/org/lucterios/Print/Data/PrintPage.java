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

package org.lucterios.Print.Data;

import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.StringList;

public class PrintPage extends PrintAbstract
{
    public double margin_right=10;
    public double margin_left=10;
    public double margin_bottom=10;
    public double margin_top=10;
    public double page_width=210;
    public double page_height=297;

    public PrintArea header;
    public PrintArea bottom;
    public PrintArea left;
    public PrintArea rigth;
    public PrintVector body;

    public SimpleParsing XMLDataDom;

    public PrintPage()
    {
        super();
        init();
    }

    protected void init()
    {
        margin_right=10;
        margin_left=10;
        margin_bottom=10;
        margin_top=10;
        page_width=210;
        page_height=297;

        header=new PrintArea("before");
        header.init();
        bottom=new PrintArea("after");
        bottom.init();
        left=new PrintArea("start");
        left.init();
        rigth=new PrintArea("end");
        rigth.init();
        header.setOwner(this);
        bottom.setOwner(this);
        left.setOwner(this);
        rigth.setOwner(this);
        
        body=new PrintVector("Pages");
        body.init();
        body.setOwner(this);
        body.add(new PrintArea());
        ReadProperty.clear();
        ReadProperty.put("body","org.lucterios.Print.Data.PrintArea");

        mDataList.clear();
    }

    StringList mDataList=new StringList();
    public StringList getDataList()
    {
        return (StringList) mDataList.clone();
    }

    public String getDataPath()
    {
        return "";
    }

    public SimpleParsing parse(String xmlText) throws LucteriosException
    {
    	SimpleParsing parser = new SimpleParsing();
        parser.parse(xmlText,true);
        return parser;
    }

    public void Load(String printString) throws LucteriosException
    {
        SimpleParsing DomDocument = parse("<?xml version='1.0' encoding='iso-8859-1'?>\n"+printString);
        if (DomDocument!=null)
        {
            if (DomDocument.getTagName().equalsIgnoreCase("model"))
                read(DomDocument);
        }
    }

    public String Save()
    {
        return write("model");
    }

    private void addDataElement(SimpleParsing aItem,String aLastName)
    {
        String node_name=aLastName+"/"+aItem.getTagName();
        if (!mDataList.contains(node_name))
            mDataList.add(node_name);
        for(int node_idx=0;node_idx<aItem.getTagCount();node_idx++)
               addDataElement(aItem.getSubTag(node_idx),node_name);
    }

    public void setXML(String XMLString) throws LucteriosException
    {
        mDataList.clear();
        if ((XMLString!=null) && (XMLString.trim().length()>0))
        {
            XMLDataDom=parse("<?xml version='1.0' encoding='iso-8859-1'?>"+XMLString);
            mDataList.add("");
            addDataElement(XMLDataDom,"");
        }
        else
            XMLDataDom=null;
    }

    public String toString()
    {
        return "Model";
    }
}
