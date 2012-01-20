package org.lucterios.swing;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.PopupListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIEdit;
import org.lucterios.ui.GUIActionListener;

public class SEdit extends JPasswordField implements GUIEdit,FocusListener,KeyListener {

	private static final long serialVersionUID = 1L;

	private FocusListenerList mFocusListener=new FocusListenerList(); 
	private CursorMouseListener mCursorMouseListener;
	
	public void clearFocusListener() {
		mFocusListener.clear();
	}
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

    public void focusLost(java.awt.event.FocusEvent e) 
    {
    	if (isFloatEditor())
    		setText(convertValue(getValue()));
    	mFocusListener.focusLost(e);
    }
    
	public void focusGained(FocusEvent e) { }
	
	double mMinVal=Double.MIN_VALUE;
    double mMaxVal=Double.MAX_VALUE;
    int mPrecVal=2;
    boolean mIsFloatEditor=false;   
    
	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SEdit(GUIComponent aOwner){
        super();
        putClientProperty("JPasswordField.cutCopyAllowed",Boolean.TRUE);
        mOwner=aOwner;
        mCursorMouseListener=new CursorMouseListener(this,this);
        addFocusListener(this);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));        
		PopupListener popupListener = new PopupListener();
		popupListener.setActions(getActions());
		popupListener.addEditionMenu(true);
		addMouseListener(popupListener);
		addMouseListener(mCursorMouseListener);
		setEchoChar((char)0);
    }

	public void setPassword(char c) {
		setEchoChar(c);
	}	    
	
	private String convertValue(Double init_value)
    {
        double Val_r = (double)Math.min(mMaxVal, Math.max(mMinVal, init_value));
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
                    
    public void setValue(Double aVal)
    {
        setText(convertValue(aVal));
    }

    protected Double getValue(String aValue)
    {
        try
        {
                return new Double(aValue.replace(',', '.'));
        }
        catch(NumberFormatException e)
        {
                return mMinVal;
        }
    }
    
    public Double getValue()
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

	// ACTION
	public void addActionListener(GUIActionListener l){
		mCursorMouseListener.add(l);
	}

	public void removeActionListener(GUIActionListener l){
		mCursorMouseListener.remove(l);
	}

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		setFocusable(aFlag);
	}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {
		mCursorMouseListener.actionPerformed(null);
	}

	public void keyTyped(KeyEvent e) {}

	public int[] getCaretPositions() {
		int[] result=new int[]{0,0};
		Caret caret=this.getCaret();
		result[0]=caret.getDot();
		result[1]=caret.getMark();
		return result;
	}
	
	public boolean isActive() {
		return getOwner().isActive();
	}
	
	public void requestFocusGUI() {
		requestFocus();
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(mNbClick);
	}
}
