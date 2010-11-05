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

import org.lucterios.utils.StringList;

public class PrintCell extends PrintBorder
{
    public PrintAbstractText print_text=new PrintAbstractText();
    public String data="";
    public String display_align="";

    public String toString()
    {
        int idx=((PrintVector)getOwner()).indexOf(this)+1;
        return "Cellule "+idx;
    }

    public Object[] getSelection()
    {
    	StringList data_list=Owner.getDataList();
        if (data_list.size()>0)
        {
            if (PrintRow.class.isInstance(getOwner().getOwner()) && !"".equals( ((PrintRow)(getOwner().getOwner())).data ))
            {
                int idx=1;
                while (idx<data_list.size())
                    if (((String)data_list.get(idx)).charAt(0)=='/')
                        data_list.remove(idx);
                    else
                        idx++;
            }
            return data_list.toArray();
        }
        else
            return new Object[0];
    }

    public String getDataPath()
    {
        String data_path=Owner.getDataPath();
        if (data.length()>0)
        {
            if (data.charAt(0)=='/')
                data_path=data;
            else
                data_path+="/"+data;
        }
        return data_path;
    }

    public StringList getDataList()
    {
    	StringList data_list=Owner.getDataList();
        if (data.length()>0)
            data_list.addAll(extractDataList(data_list,getDataPath()));
        return data_list;
    }
 
    protected void init()
    {
        super.init();
        print_text.init();
        print_text.setOwner(this);
        data="";
        display_align="center";
        NoAttributProperty.add("print_text");
    }

    public void setText(String aText)
    {
        print_text.content=aText;
    }

    public String getText()
    {
        return print_text.content;
    }

    protected String write_text()
    {
            return getText()+"\n";
    }

    protected String write_attrib()
    {
        String xml_attributes=super.write_attrib();
        xml_attributes=xml_attributes+print_text.write_attrib();
        return xml_attributes;
    }

    public void read(org.w3c.dom.Element aXmlItem)
    {
            super.read(aXmlItem);
            print_text.read(aXmlItem);
    }
}
