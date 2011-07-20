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

package org.lucterios.form;

import javax.swing.*;

import javax.swing.text.*;

import org.lucterios.graphic.PopupListener;

public class FloatField extends JTextField 
{	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double mMinVal=Double.MIN_VALUE;
    double mMaxVal=Double.MAX_VALUE;
    int mPrecVal=2;
    public FloatField() 
    {
        super();
        addFocusListener(new java.awt.event.FocusAdapter() 
        {
            public void focusLost(java.awt.event.FocusEvent evt) 
            {
                setText(convertValue(getValue()));
            }
        });	
		PopupListener popupListener = new PopupListener();
		popupListener.setActions(getActions());
		popupListener.addEditionMenu(true);
		addMouseListener(popupListener);	        
    }

    private String convertValue(double init_value)
    {
        double Val_r = (double) Math
				.min(mMaxVal, Math.max(mMinVal, init_value));
		long val_i = Math.round(Val_r * Math.pow(10, mPrecVal));
		String val = new Double(val_i / Math.pow(10, mPrecVal)).toString();
		int pos = val.indexOf(".");
		if (pos != -1) {
			if (val.length() <= (pos + mPrecVal + 1))
				while (val.length() < (pos + mPrecVal + 1))
					val = val + "0";
			else if (mPrecVal > 0)
				val = val.substring(0, pos + mPrecVal + 1);
			else
				val = val.substring(0, pos);
		}
		return val;
    }
    
    public void setRange(double aMinVal,double aMaxVal,int aPrecVal)
    {
        mMinVal=aMinVal;
        mMaxVal=aMaxVal;
        mPrecVal=Math.max(0,aPrecVal);
    }
                    
    public void setValue(double aVal)
    {
        setText(convertValue(aVal));
    }

    protected double getValue(String aValue)
    {
        try
        {
                return new Double(aValue).doubleValue();
        }
        catch(NumberFormatException e)
        {
                return mMinVal;
        }
    }
    public double getValue()
    {
        return getValue(getText());
    }
                    
    protected Document createDefaultModel() 
    {
        return new FloatCaseDocument(this);
    }
    
    class FloatCaseDocument extends PlainDocument 
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private FloatField mFltFld;
        public FloatCaseDocument(FloatField aFltFld)
        {
                mFltFld=aFltFld;
        }

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException 
        {		
                if (str == null) 
                {
                        return;
                }
                boolean is_numeric=true;
                boolean has_point=(mFltFld.getText().indexOf(".")!=-1);
                char[] chars = str.toCharArray();
                for (int i = 0; (i < chars.length) && is_numeric; i++) 
                {
                        if ((chars[i]>='0') && (chars[i]<='9'))
                                is_numeric=true;
                        else
                        {
                                if (chars[i]=='.')
                                        is_numeric=(!has_point);
                                else
                                        is_numeric=false;
                        }
                }
                if (is_numeric)
                        super.insertString(offs, new String(str), a);
        }
    }
}
