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

package org.lucterios.Print.GUI;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Field;

import javax.swing.*;

import org.lucterios.Print.Data.*;
        
/**
 *
 * @author  lg
 */
public abstract class PropertyPanel extends javax.swing.JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected javax.swing.JLabel lblProperty=null;
    protected javax.swing.JLabel lblAligne=null;
    protected javax.swing.JLabel lblTitle=null;

    protected class SelectData
    {
        public String Title="";
        public String Data="";
        public SelectData(String text)
        {
            String[] cmp_names=splitName(text);
            if (cmp_names.length==1)
            {
                Title=cmp_names[0];
                Data=cmp_names[0];
            }
            else if (cmp_names.length>1)
            {
                Title=cmp_names[0];
                Data=cmp_names[1];
            }
        }
        public String toString(){return Title;}
    }
    static public String[] splitName(String cmpName)
    {
        String[] cmp_names;
        try{
            cmp_names=cmpName.split("%");
            if (cmp_names.length==0)
            {
                cmp_names=new String[]{cmpName};
            }
        }catch(java.util.regex.PatternSyntaxException e){cmp_names=new String[]{};}
        return cmp_names;
    }

    protected void readData()
    {
        if (mCurrent!=null)
        {
            for(int idx=0;idx<getComponentCount();idx++)
            {
                java.awt.Component cmp=getComponent(idx);
                if ((cmp!=null) && (cmp.getName()!=null))
                {
                    if ("__".equals( cmp.getName().substring(0,2) ))
                        componentReturn(cmp);
                }
            }
        }
    }

    private void componentReturn(java.awt.Component cmp)
    {
        java.lang.reflect.Field field=null;
        String cmpName=cmp.getName().substring(2);
        String[] cmp_names=splitName(cmpName);
        Object temp_value=mCurrent;
        try{
        	for(int i=0;i<(cmp_names.length-1);i++) {
        		field=temp_value.getClass().getField(cmp_names[i]);
                temp_value=field.get(temp_value);
            }
            field=temp_value.getClass().getField(cmp_names[(cmp_names.length-1)]);
        }catch(java.lang.NoSuchFieldException e){field=null;}
        catch(java.lang.IllegalAccessException ae){field=null;}

        if (field!=null)
        {            
            try
            {
                setFieldValue(cmp, field, temp_value);
            }
            catch (IllegalAccessException e)
            {
                System.out.println("Valeur non consultable "+e);
            }
        }
    }

	private void setFieldValue(java.awt.Component cmp, Field field, Object objectValue) throws IllegalAccessException 
	{
		if (field.getType().isAssignableFrom(Integer.TYPE))
		{
		    int value=(int)((org.lucterios.utils.graphic.FloatField)cmp).getValue();
		    field.setInt(objectValue,value);
		}
		else if (field.getType().isAssignableFrom(Double.TYPE))
		{
		    double value=((org.lucterios.utils.graphic.FloatField)cmp).getValue();
		    field.setDouble(objectValue,value);
		}
		else if (field.getType().isAssignableFrom(String.class))
		{
		    String value=null;
		    if (JComboBox.class.isInstance(cmp))
		    {
		        JComboBox combo=(JComboBox)cmp;
		        Object select_obj=combo.getSelectedItem();
		        if (select_obj!=null)
		        {
		            if (SelectData.class.isInstance(select_obj))
		                value=((SelectData)select_obj).Data;
		            else
		                value=select_obj.toString();
		        }
		    }
		    else
		        value=((JTextField)cmp).getText();
		    if (value!=null)
		        field.set(objectValue,value);
		}
	}

    protected void addComponent(int posY,String cmpTitle,JComponent cmp,int width)
    {
        java.awt.GridBagConstraints gridBagConstraints;
        JLabel lbl_cmp_title = new JLabel();
        lbl_cmp_title.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        lbl_cmp_title.setText(cmpTitle);
        lbl_cmp_title.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 11));
        lbl_cmp_title.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = posY;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1,3,3,1);
        add(lbl_cmp_title, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = width;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridy = posY;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1,3,3,1);
        cmp.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {}
			public void focusLost(FocusEvent e) {
				propertyFocusLost();
			}
        });
        add(cmp, gridBagConstraints);
    }
    
    protected abstract void propertyFocusLost();
	
    protected void addEdit(int posY,String cmpTitle,String cmpName,Object[] select)
    {
        String[] cmp_names=splitName(cmpName);
        java.lang.reflect.Field field=null;
        Object temp_value=mCurrent;
        try{
            for(String cmp_name:cmp_names) {
                field=temp_value.getClass().getField(cmp_name);
                temp_value=field.get(temp_value);
            }
        }catch(java.lang.NoSuchFieldException fe){field=null;System.out.println("Error "+fe);}
        catch(java.lang.IllegalAccessException ae){field=null;System.out.println("Error "+ae);}
        JComponent edit_cmp=null;
        if (field!=null)
        {
            if (field.getType().isAssignableFrom(Integer.TYPE))
            {
                edit_cmp = new org.lucterios.utils.graphic.FloatField();
                ((org.lucterios.utils.graphic.FloatField)edit_cmp).setRange(1,50,0);
                ((org.lucterios.utils.graphic.FloatField)edit_cmp).setValue(((Integer)temp_value).intValue());
            }
            else if (field.getType().isAssignableFrom(Double.TYPE))
            {
                edit_cmp = new org.lucterios.utils.graphic.FloatField();
                ((org.lucterios.utils.graphic.FloatField)edit_cmp).setRange(0,5000,2);
                ((org.lucterios.utils.graphic.FloatField)edit_cmp).setValue(((Double)temp_value).doubleValue());
            }
            else 
            {
                if (select==null)
                {
                    edit_cmp = new JTextField();
                    ((JTextField)edit_cmp).setText(temp_value.toString());
                }
                else
                    edit_cmp = createCombo(select, temp_value);
            }
            edit_cmp.setName("__"+cmpName);
            addComponent(posY,cmpTitle,edit_cmp,2);
        }
    }

	private JComponent createCombo(Object[] select, Object temp_value) {
		JComponent edit_cmp;
		edit_cmp = new JComboBox(select);
		int select_idx=-1;
		if (SelectData[].class.isInstance(select))
		{
		    for(int idx=0;idx<select.length;idx++)
		        if (((SelectData)select[idx]).Data.equals(temp_value.toString()))
		            select_idx=idx;
		}
		else
		{
		    for(int idx=0;idx<select.length;idx++)
		    {
		        if (select[idx].toString().equalsIgnoreCase(temp_value.toString()))
		            select_idx=idx;
		    }
		}
		((JComboBox)edit_cmp).setSelectedIndex(select_idx);
		return edit_cmp;
	}

    public void clear()
    {
        lblTitle.setText("");
        int idx=0;
        while (idx<getComponentCount())
        {
            if ((!getComponent(idx).equals( lblTitle )) && (!getComponent(idx).equals( lblAligne )) && (!getComponent(idx).equals( lblProperty )))
                remove(idx);
            else
                idx++;
        }
    }
    
    protected PrintAbstract mCurrent=null;
    public void setCurrent(PrintAbstract current)
    {
        mCurrent=current;
    }
}
