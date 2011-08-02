package org.lucterios.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.lucterios.graphic.PopupListener;
import org.lucterios.gui.GUIEdit;
import org.lucterios.ui.GUIActionListener;

public class SEdit extends JPasswordField implements GUIEdit,FocusListener,MouseListener {

	private static final long serialVersionUID = 1L;

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

    public void focusLost(java.awt.event.FocusEvent evt) 
    {
    	if (isFloatEditor())
    		setText(convertValue(getValue()));
		for(GUIFocusListener l:mFocusListener)
			l.focusLost();
    }
    
	public void focusGained(FocusEvent e) { }
	
	double mMinVal=Double.MIN_VALUE;
    double mMaxVal=Double.MAX_VALUE;
    int mPrecVal=2;
    boolean mIsFloatEditor=false;   
    
    public SEdit() 
    {
        super();
        addFocusListener(this);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));        
		PopupListener popupListener = new PopupListener();
		popupListener.setActions(getActions());
		popupListener.addEditionMenu(true);
		addMouseListener(popupListener);
		addMouseListener(this);
		setEchoChar((char)0);
    }

	public void setPassword(char c) {
		setEchoChar(c);
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

	public boolean isFloatEditor() {
		return mIsFloatEditor;
	}

	public void setFloatEditor(boolean isFloatEditor) {
		this.mIsFloatEditor = isFloatEditor;
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
        return getValue(getTextString());
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
		private SEdit mFltFld;
        public FloatCaseDocument(SEdit aFltFld)
        {
                mFltFld=aFltFld;
        }

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException 
        {		
        	if (isFloatEditor()) {
                if (str == null) 
                {
                        return;
                }
                boolean is_numeric=true;
                boolean has_point=(mFltFld.getTextString().indexOf(".")!=-1);
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
        	else
        		super.insertString(offs, str, a);
        }
    }

	public String getTextString() {
		return new String(getPassword());
	}

	public void setTextString(String text) {
		setText(text);		
	}	

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
	}

	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	public void addActionListener(GUIActionListener l) { }

	public void removeActionListener(GUIActionListener l) { }

	private boolean mIsActiveMouse=false;
	public void setActiveMouseAction(boolean isActive) {
		mIsActiveMouse=isActive;		
	}

	public void mouseEntered(MouseEvent e) {
		if (mIsActiveMouse) {
			if (!Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals(getCursor())) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
	}

	public void mouseExited(MouseEvent e) {
		if (mIsActiveMouse) {
			if (Cursor.getPredefinedCursor(Cursor.HAND_CURSOR).equals(getCursor())) {
				setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public void mouseClicked(MouseEvent e) { }
	
	public void mousePressed(MouseEvent e) { }

	public void mouseReleased(MouseEvent e) { }
	
}
