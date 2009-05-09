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

public class PrintColumn extends PrintAbstract
{
    public double width=10;
    public PrintCell cell=new PrintCell();
    public String data="";

    public String toString()
    {
        int idx=((PrintVector)getOwner()).indexOf(this)+1;
        return "Colonne "+idx;
    }

    public Object[] getSelection()
    {
        ArrayList data_list=Owner.getDataList();
        if (data_list.size()>0)
            return data_list.toArray();
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

    public ArrayList getDataList()
    {
        ArrayList data_list=Owner.getDataList();
        if (data.length()>0)
            data_list.addAll(extractDataList(data_list,getDataPath()));
        return data_list;
    }

    protected void init()
    {
        width=10;
        cell.init();
        cell.setOwner(this);
        data="";
        ReadProperty.clear();
    }
}
