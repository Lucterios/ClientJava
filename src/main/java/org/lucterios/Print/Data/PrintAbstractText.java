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

import org.lucterios.utils.SimpleParsing;

public class PrintAbstractText extends PrintAbstract
{
    public String text_align="start";
    public int line_height=12;
    public String font_family="sans-serif";
    public String font_weight="";
    public int font_size=12;

    public String content="";

    public String toString()
    {
        return "!! Abstract Text !!";
    }
    
    protected void init()
    {
            text_align="start";
            line_height=12;
            font_family="sans-serif";
            font_weight="";
            font_size=12;
            content="";
            NoAttributProperty.add("content");
    }

    protected String write_text()
    {
            return content.trim()+"\n";
    }

    public String NewText(org.w3c.dom.Element item)
    {
        String result="";
        if (item!=null)
        {
            String tmp_txt="";
            org.w3c.dom.NodeList items=item.getChildNodes();
            for(int item_idx=0;item_idx<items.getLength();item_idx++)
            {
                if ((items.item(item_idx).getNodeType()==org.w3c.dom.Node.CDATA_SECTION_NODE) || (items.item(item_idx).getNodeType()==org.w3c.dom.Node.TEXT_NODE))
                {
                    if (items.item(item_idx).getNodeValue()!=null)
                        tmp_txt+=items.item(item_idx).getNodeValue();
                }
                else if (items.item(item_idx).getNodeType()==org.w3c.dom.Node.ELEMENT_NODE)
                {
                    tmp_txt+=NewText((org.w3c.dom.Element)items.item(item_idx));
                }
            }
            String attributes="";
            org.w3c.dom.NamedNodeMap attrs=item.getAttributes();
            for(int at_i=0;at_i<attrs.getLength();at_i++)
            {
                org.w3c.dom.Attr att=(org.w3c.dom.Attr)attrs.item(at_i);
                attributes+=" "+att.getName()+"='"+att.getValue()+"'";
            }
            attributes=attributes.trim();
            result="<"+item.getNodeName();
            if (attributes.length()>0)
                result+=" "+attributes.trim();
            if (tmp_txt.trim().length()>0)
                result+=">"+tmp_txt+"</"+item.getNodeName()+">";
            else
                result+="/>";
        }
        return result;
    }

    public void read(SimpleParsing aXmlItem)
    {
        super.read(aXmlItem);
        content=aXmlItem.getText();
    }
}
