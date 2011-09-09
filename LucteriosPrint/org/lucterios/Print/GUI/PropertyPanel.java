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

import java.lang.reflect.Field;

import org.lucterios.Print.Data.*;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.gui.GUIComponent.GUIFocusListener;
import org.lucterios.gui.GUIParam.ReSizeMode;
        
/**
 *
 * @author  lg
 */
public abstract class PropertyPanel implements GUIFocusListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected GUIHyperText lblProperty=null;
    protected GUILabel lblAligne=null;
    protected GUIHyperText lblTitle=null;
    protected GUIContainer mContainer;

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
    public PropertyPanel(GUIContainer guiContainer) {
		super();
		mContainer=guiContainer;
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
            for(int idx=0;idx<mContainer.count();idx++)
            {
                GUIComponent cmp=mContainer.get(idx);
                if ((cmp!=null) && (cmp.getName()!=null))
                {
                    if ("__".equals( cmp.getName().substring(0,2) ))
                        componentReturn(cmp);
                }
            }
        }
    }

    private void componentReturn(GUIComponent cmp)
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

	private void setFieldValue(GUIComponent cmp, Field field, Object objectValue) throws IllegalAccessException 
	{
		if (field.getType().isAssignableFrom(Integer.TYPE))
		{
		    int value=(int)((GUISpinEdit)cmp).getNumber();
		    field.setInt(objectValue,value);
		}
		else if (field.getType().isAssignableFrom(Double.TYPE))
		{
		    double value=((GUIEdit)cmp).getValue();
		    field.setDouble(objectValue,value);
		}
		else if (field.getType().isAssignableFrom(String.class))
		{
		    String value=null;
		    if (GUICombo.class.isInstance(cmp))
		    {
		    	GUICombo combo=(GUICombo)cmp;
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
		        value=((GUIEdit)cmp).getTextString();
		    if (value!=null)
		        field.set(objectValue,value);
		}
	}

    protected GUIParam addComponent(int posY,String cmpTitle,int width)
    {
        GUIParam param;
        
        param=new GUIParam(0,posY);
        param.setReSize(ReSizeMode.RSM_NONE);
        param.setPad(2);
        GUILabel lbl_cmp_title = mContainer.createLabel(param);
        lbl_cmp_title.setTextString(cmpTitle);
        
        param=new GUIParam(1,posY,width,1);
        param.setReSize(ReSizeMode.RSM_HORIZONTAL);
        param.setPad(2);
        return param;
    }
	
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
        
        GUIComponent edit_cmp=null;
        if (field!=null)
        {
            GUIParam param=addComponent(posY,cmpTitle,2);
            if (field.getType().isAssignableFrom(Integer.TYPE))
            {
                edit_cmp = mContainer.createSpinEdit(param);
                ((GUISpinEdit)edit_cmp).init(((Integer)temp_value).intValue(), 1,50);
            }
            else if (field.getType().isAssignableFrom(Double.TYPE))
            {
                edit_cmp = mContainer.createEdit(param);
                ((GUIEdit)edit_cmp).setRange(0,5000,2);
                ((GUIEdit)edit_cmp).setValue(((Double)temp_value).doubleValue());
            }
            else 
            {
                if (select==null)
                {
                    edit_cmp = mContainer.createEdit(param);
                    ((GUIEdit)edit_cmp).setTextString(temp_value.toString());
                }
                else 
                    edit_cmp = createCombo(param,select, temp_value);
            }
            edit_cmp.setName("__"+cmpName);
            edit_cmp.addFocusListener(this);
        }
    }

	private GUIComponent createCombo(GUIParam param, Object[] select, Object temp_value) {
		GUIComponent edit_cmp;
		edit_cmp = mContainer.createCombo(param);
		((GUICombo)edit_cmp).addList(select);
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
		((GUICombo)edit_cmp).setSelectedIndex(select_idx);
		return edit_cmp;
	}

    public void clear()
    {
        lblTitle.setTextString("");
        int idx=0;
        while (idx<mContainer.count())
        {
            if ((!mContainer.get(idx).equals( lblTitle )) && (!mContainer.get(idx).equals( lblAligne )) && (!mContainer.get(idx).equals( lblProperty )))
            	mContainer.remove(idx);
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
