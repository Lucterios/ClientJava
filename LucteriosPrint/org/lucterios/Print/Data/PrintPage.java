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

import java.io.*;

import org.lucterios.utils.StringList;
import org.w3c.dom.*;
import org.apache.xerces.parsers.DOMParser;

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

    public org.w3c.dom.Document XMLDataDom;

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

    public Document parse(String xmlText) throws org.xml.sax.SAXException,java.io.IOException
    {
        DOMParser parser = new DOMParser();
        InputStream xml_src= new org.lucterios.utils.BufferInputStream(xmlText);
        parser.parse(new org.xml.sax.InputSource(xml_src));
        return parser.getDocument();
    }

    public void Load(String printString) throws org.xml.sax.SAXException,java.io.IOException
    {
        org.w3c.dom.Element RootDomNode=null;
        org.w3c.dom.Document DomDocument = parse("<?xml version='1.0' encoding='iso-8859-1'?>\n"+printString);
        if (DomDocument!=null)
        {
            RootDomNode = DomDocument.getDocumentElement();
            if (RootDomNode.getNodeName().equalsIgnoreCase("model"))
                read(RootDomNode);
        }
    }

    public String Save()
    {
        return write("model");
    }

    private void addDataElement(org.w3c.dom.Element aItem,String aLastName)
    {
        String node_name=aLastName+"/"+aItem.getNodeName();
        if (!mDataList.contains(node_name))
            mDataList.add(node_name);
        org.w3c.dom.NodeList nodes=aItem.getChildNodes();
        for(int node_idx=0;node_idx<nodes.getLength();node_idx++)
            if (nodes.item(node_idx).getNodeType()==org.w3c.dom.Node.ELEMENT_NODE)
                addDataElement((org.w3c.dom.Element)nodes.item(node_idx),node_name);
    }

    public void setXML(String XMLString) throws org.xml.sax.SAXException,java.io.IOException
    {
        mDataList.clear();
        if ((XMLString!=null) && (XMLString.trim().length()>0))
        {
            XMLDataDom=parse("<?xml version='1.0' encoding='iso-8859-1'?>"+XMLString);
            mDataList.add("");
            addDataElement(XMLDataDom.getDocumentElement(),"");
        }
        else
            XMLDataDom=null;
    }

    public String toString()
    {
        return "Model";
    }
}
