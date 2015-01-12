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

public class PrintTable extends PrintContainer
{
    public PrintVector columns;
    public PrintVector rows;

    public PrintTable()
    {
        super();
        columns=new PrintVector("Colonnes");
        rows=new PrintVector("Lignes");
    }

    protected void init()
    {
        super.init();
        columns.init();
        columns.setOwner(this);
        rows.init();
        rows.setOwner(this);
        ReadProperty.clear();
        ReadProperty.put("columns","org.lucterios.Print.Data.PrintColumn");
        ReadProperty.put("rows","org.lucterios.Print.Data.PrintRow");
    }

    public void removeRow(PrintRow row)
    {
        rows.remove(row);
    }

    public PrintRow newRow()
    {
        PrintRow new_row=new PrintRow();
        new_row.init();
        new_row.setOwner(this);
        while (new_row.cell.size()<columns.size())
        {
            PrintCell new_cell=new PrintCell();
            new_cell.init();
            new_cell.setOwner(new_row);
            new_row.cell.add(new_cell);
        }
        rows.add(new_row);
        return new_row;
    }

    public void removeCol(PrintColumn col)
    {
        int idx=columns.indexOf(col);
        for(int row_idx=0;row_idx<rows.size();row_idx++)
            ((PrintRow)rows.get(row_idx)).cell.remove(idx);
        columns.remove(col);
    }

    public PrintColumn newCol()
    {
        PrintColumn new_col=new PrintColumn();
        new_col.init();
        new_col.setOwner(this);
        columns.add(new_col);
        for(int ir=0;ir<rows.size();ir++)
        {
            PrintRow row=(PrintRow)rows.get(ir);
            while (row.cell.size()<columns.size())
            {
                PrintCell new_cell=new PrintCell();
                new_cell.init();
                new_cell.setOwner(row);
                row.cell.add(new_cell);
            }
        }
        return new_col;
    }
    public String toString()
    {
        int idx=-1;
        if ((Owner!=null) && PrintArea.class.isInstance(Owner))
             idx=((PrintArea)Owner).indexOf(this)+1;
        return "Table "+idx;
    }
}
