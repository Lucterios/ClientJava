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
import org.lucterios.utils.StringDico;
import org.lucterios.utils.StringList;

public abstract class PrintAbstract
{
    protected StringDico ReadProperty=new StringDico();
    protected StringList NoAttributProperty=new StringList();
    protected PrintAbstract Owner=null;

    public void setOwner(PrintAbstract aOwner)
    {
        Owner=aOwner;
    }

    public PrintAbstract getOwner()
    {
        return Owner;
    }
    
    public PrintAbstract()
    {
        ReadProperty.clear();
    }

    public Object[] getSelection()
    {
        return new Object[0];
    }

    public String getDataPath()
    {
        return Owner.getDataPath();
    }

    public StringList getNoAttributProperty()
    {
        return NoAttributProperty;
    }

    protected abstract void init();

    public String toString()
    {
        return "!! Abstract !!";
    }

    public boolean isRow() {
		return (PrintVector.class.isInstance(this) && "Lignes".equals( ((PrintVector)this).name ));
	}

    public boolean isColumn() {
		return (PrintVector.class.isInstance(this) && "Colonnes".equals( ((PrintVector)this).name ));
	}

    public boolean isPage() {
		return (PrintVector.class.isInstance(this) && "Pages".equals( ((PrintVector)this).name ));
	}
    
    
    protected StringList extractDataList(StringList aDataList,String DataPath)
    {
    	StringList data_list=new StringList();
        for(String current_path:aDataList)
        {
            if (current_path.length()>DataPath.length())
            {
                String sub_current_path=current_path.substring(0,DataPath.length());
                if (sub_current_path.equals(DataPath))
                    data_list.add(current_path.substring(DataPath.length()+1));
            }
        }
        return data_list;
    }
    
    public StringList getDataList()
    {
        if (Owner!=null)
            return Owner.getDataList();
        else
        {
        	StringList data_list=new StringList();
            data_list.add("");
            return data_list;
        }
    }

    protected String write_text(){return "";}

    protected String write_attrib()
    {
        String xml_attributes="";
        java.lang.reflect.Field[] fields=this.getClass().getFields();
        for(java.lang.reflect.Field field:fields)
        {
            String field_name=field.getName().toLowerCase();
            if (!NoAttributProperty.contains(field_name))
            try
            {
                if (field.getType().isAssignableFrom(Integer.TYPE) || field.getType().isAssignableFrom(Double.TYPE))
                    xml_attributes=xml_attributes+" "+field_name+"='"+field.get(this).toString()+"'";
                else if (field.getType().isAssignableFrom(String.class))
                {
                    String val=field.get(this).toString().trim();
                    xml_attributes=xml_attributes+" "+field_name+"='"+val+"'";
                }
            }
            catch (IllegalAccessException e)
            {
                System.out.println("Valeur non consultable "+e);
            }
        }
        return xml_attributes;
    }

    public String write(String aName)
    {
        String xml_childs=write_text();
        String xml_attributes=write_attrib();
        java.lang.reflect.Field[] fields=this.getClass().getFields();
        for(java.lang.reflect.Field field:fields)
        {
            String field_name=field.getName().toLowerCase();
            if (!NoAttributProperty.contains(field_name))
            try
            {
                Object value=field.get(this);
                if (ReadProperty.containsKey(field_name) && (PrintVector.class.isInstance(value)) && (!PrintArea.class.isInstance(value)))
                {
                    PrintVector val_list=(PrintVector)value;
                    xml_childs=xml_childs+val_list.writeArrayList(field_name);
                }
                else if (PrintAbstract.class.isInstance(value))
                {
                    PrintAbstract obj=(PrintAbstract)value;
                    xml_childs=xml_childs+obj.write(field_name);
                }
            }
            catch (IllegalAccessException e)
            {
                System.out.println("Valeur non consultable "+e);
            }
        }
        String xml_result="<"+aName;
        if (!xml_attributes.equals( "" ))
            xml_result=xml_result+xml_attributes;
        if (!xml_childs.equals( "" ))
            xml_result=xml_result+">\n"+xml_childs+"</"+aName+">\n";
        else
            xml_result=xml_result+"/>\n";
        return xml_result;
    }

    public String ToString()
    {
        return write(this.getClass().getName());
    }

    public void read(SimpleParsing aXmlItem)
    {
        this.init();
        java.lang.reflect.Field[] fields=this.getClass().getFields();
        for(java.lang.reflect.Field field:fields)
        {
            String field_name=field.getName().toLowerCase();
            if (aXmlItem.hasAttribute(field_name))
            {
                String value=aXmlItem.getAttribute(field_name);
                try
                {
                    affectField(field, value);
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Valeur invalid "+e);
                }
                catch (IllegalAccessException e)
                {
                    System.out.println("Valeur non consultable "+e);
                }
            }
            else
            {
                 SimpleParsing[] nodes=aXmlItem.getSubTag(field_name);
                 if (nodes.length>0)
                 {
                    try
                    {
                        Object value=field.get(this);
                        readPrintObject(field_name, nodes, value);
                    } catch(ClassNotFoundException e) {
                            System.out.println("Class non trouv�e "+e);
                    } catch(IllegalAccessException e) {
                            System.out.println("Access illegal "+e);
                    }
                 }
            }
        }
    }

	private void readPrintObject(String fieldName, SimpleParsing[] nodes, Object value) throws ClassNotFoundException 
	{
		if (ReadProperty.containsKey(fieldName) && (PrintVector.class.isInstance(value)))
		{
		    PrintVector val_list=(PrintVector)value;
		    val_list.setOwner(this);
		    String class_name=(String)ReadProperty.get(fieldName);
		    Class<?> class_inst=Class.forName(class_name);
		    val_list.readArrayList(nodes,class_inst);
		}
		else if (PrintAbstract.class.isInstance(value))
		{
		    PrintAbstract obj=(PrintAbstract)value;
		    obj.setOwner(this);
		    obj.read(nodes[0]);
		}
	}

	private void affectField(java.lang.reflect.Field field, String value) throws IllegalAccessException 
	{
		if (field.getType().isAssignableFrom(Integer.TYPE))
		    field.setInt(this,Integer.parseInt(value,10));
		else if (field.getType().isAssignableFrom(Double.TYPE))
		{
		    java.text.DecimalFormatSymbols dfs=new java.text.DecimalFormatSymbols();
		    char dec_sep=dfs.getDecimalSeparator();
		    field.setDouble(this,Double.valueOf(value.replace(dec_sep,'.')).doubleValue());
		}
		else if (field.getType().isAssignableFrom(String.class))
		    field.set(this,value);
	}
}