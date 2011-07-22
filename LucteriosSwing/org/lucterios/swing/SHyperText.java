package org.lucterios.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.HtmlLabel;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUIButton.GUIActionListener;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.LucteriosException;

public class SHyperText extends HtmlLabel implements GUIHyperText,MouseListener {
	
	private static final long serialVersionUID = 1L;

	public ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>(); 
	protected String mUrl=null;
	protected String mText=null;
	protected GUIActionListener mActionLink=null;
	
	public SHyperText(){
		super();
		setBorder(BorderFactory.createEmptyBorder());
		setEditable(false);
		setFocusable(false);
		setContentType("text/html");
		setBackground(this.getBackground());
		addMouseListener(this);
	}
	
	public void addActionListener(GUIActionListener l){
		mActionListener.add(l);
	}

	public void removeActionListener(GUIActionListener l){
		mActionListener.remove(l);
	}

	public String getTextString() {
		return getText();
	}

	public void setTextString(String text) {
		mText=text;
		initValues();
	}

	public void setHyperLink(String aUrl) {
		mUrl=aUrl;
		initValues();
	}

	private void initValues() {
		if (mUrl!=null) {
			setText("<font size='-1' color='blue'><u><center>"+mText+"</center></u></font>");
			if (mUrl.length()>0) {
				setToolTipText(mUrl);
				mActionLink=new GUIActionListener() {		
					public void actionPerformed() {
						try {
							DesktopInterface.getInstance().launch(mUrl);
						} catch (LucteriosException e1) 
						{
							ExceptionDlg.throwException(e1);
						}
					}
				};
				addActionListener(mActionLink);
			}
		}
		else {
			setText(mText);
			setToolTipText("");
			removeActionListener(mActionLink);
			mActionLink=null;					
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		for(GUIActionListener l:mActionListener)
			l.actionPerformed();
	}

	public void mouseEntered(MouseEvent event)
	{
		if (mActionLink!=null) {
			Cursor cur=Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			setCursor(cur);
		}
	}
	public void mouseExited(MouseEvent event)
	{
		Cursor cur=Cursor.getDefaultCursor();
		setCursor(cur);
	}
	
	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void addFocusListener(GUIFocusListener l) {
		// TODO Auto-generated method stub
		
	}

	public void removeFocusListener(GUIFocusListener l) {
		// TODO Auto-generated method stub
		
	}

	public int getBackgroundColor() {
		return getBackground().getRGB();
	}

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));
	}
}
