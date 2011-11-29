package org.lucterios.swing;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.HtmlLabel;
import org.lucterios.graphic.PopupListener;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.LucteriosException;

public class SHyperText extends HtmlLabel implements ClipboardOwner,GUIHyperText {
	
	private static final long serialVersionUID = 1L;

	private CursorMouseListener mCursorMouseListener;
	protected String mUrl=null;
	protected String mText=null;
	protected GUIActionListener mActionLink=null;
	
	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SHyperText(GUIComponent aOwner){
        super();
        mOwner=aOwner;
        mCursorMouseListener=new CursorMouseListener(this,this);
		setBorder(BorderFactory.createEmptyBorder());
		setEditable(false);
		setFocusable(false);
		setContentType("text/html");
		setBackground(this.getBackground());
		addMouseListener(mCursorMouseListener);
		
		PopupListener popupListener = new PopupListener();
		popupListener.setActions(getActions());
		JMenuItem mi = new JMenuItem("Copier");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				copyToClipboard();
			}
		});
		popupListener.getPopup().add(mi);
		addMouseListener(popupListener);
		setNbClick(1);
	}

	private void copyToClipboard() {
		String value_to_copy = getToolTipText();
		if (value_to_copy.startsWith("mailto:"))
			value_to_copy = value_to_copy.substring(7);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(value_to_copy), this);
		System.out.print("COPY:" + value_to_copy);
	}
	
	public void addActionListener(GUIActionListener l){
		mCursorMouseListener.add(l);
	}

	public void removeActionListener(GUIActionListener l){
		mCursorMouseListener.remove(l);
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
				mCursorMouseListener.clear();
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
			mCursorMouseListener.clear();
			mActionLink=null;					
		}
	}
	
	public void clearFocusListener() {}
	
	public void addFocusListener(GUIFocusListener l) { }

	public void removeFocusListener(GUIFocusListener l) { }

	public int getBackgroundColor() {
		return getBackground().getRGB();
	}

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));
	}

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) {	}

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
