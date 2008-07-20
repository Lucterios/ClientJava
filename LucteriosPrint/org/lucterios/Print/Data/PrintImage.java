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

public class PrintImage extends PrintContainer
{
    public String picture="";

    protected void init()
    {
            super.init();
            picture="";
            NoAttributProperty.add("picture");
    }

    protected String write_text()
    {
            return picture+"\n";
    }

    public String toString()
    {
       int idx=((PrintArea)getOwner()).indexOf(this)+1;
       return "Image "+idx;
    }

    public void read(org.w3c.dom.Element aXmlItem)
    {
        super.read(aXmlItem);
        picture="";
        org.w3c.dom.NodeList nodes=aXmlItem.getChildNodes();
        for(int n_idx=0;n_idx<nodes.getLength();n_idx++)
            if ((nodes.item(n_idx).getNodeType()==org.w3c.dom.Node.CDATA_SECTION_NODE) || (nodes.item(n_idx).getNodeType()==org.w3c.dom.Node.TEXT_NODE))
            {
                picture=nodes.item(n_idx).getNodeValue().trim();
            }
    }

}
