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

import java.util.*;

public class PrintVector extends PrintAbstract
{
    public String name="";
    protected ArrayList<PrintAbstract> containers;

    public PrintVector(String aName)
    {
        super();
        containers=new ArrayList<PrintAbstract>();
        name=aName;
    }

    protected void init()
    {
        clear();
        NoAttributProperty.add("name");
    }

    public String toString()
    {
        return name;
    }

    public void clear()
    {
        containers.clear();
    }

    public int size()
    {
        return containers.size();
    }

    public void add(PrintAbstract item)
    {
        item.init();
        containers.add(item);
        item.setOwner(this);
    }

    public void remove(PrintAbstract item)
    {
        containers.remove(item);
    }

    public void remove(int index)
    {
        if ((index>=0) && (index<containers.size()))
            containers.remove(index);
    }

    public PrintAbstract get(int index)
    {
        if ((index>=0) && (index<containers.size()))
            return containers.get(index);
        else
            return null;
    }

    public int indexOf(PrintAbstract item)
    {
        return containers.indexOf(item);
    }

    public String writeArrayList(String aName)
    {
        String xml_childs="";
        for(PrintAbstract obj:containers) {
            xml_childs=xml_childs+obj.write(aName);
        }
        return xml_childs;
    }

    protected String write_text()
    {
        String xml_childs="";
        for(PrintAbstract obj:containers) {
            xml_childs=xml_childs+obj.write(name);
        }
        return xml_childs;
    }

    public void readArrayList(org.w3c.dom.NodeList aNodes, Class aClass)
    {
        init();
        for(int node_idx=0;node_idx<aNodes.getLength();node_idx++)
        try
        {
            PrintAbstract obj=(PrintAbstract)aClass.newInstance();
            add(obj);
            obj.read((org.w3c.dom.Element)aNodes.item(node_idx));
        } catch(IllegalAccessException e) {
                System.out.println("Access illegal "+e);
        } catch(InstantiationException e){
            System.out.println("Error instance "+e);
        }
    }
}