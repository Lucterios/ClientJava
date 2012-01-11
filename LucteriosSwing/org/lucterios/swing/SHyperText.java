package org.lucterios.swing;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.FormSubmitEvent;
import javax.swing.text.html.HTMLEditorKit;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.HtmlLabel;
import org.lucterios.graphic.PopupListener;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.LucteriosException;

public class SHyperText extends HtmlLabel implements ClipboardOwner,GUIHyperText, HyperlinkListener {
	
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
        HTMLEditorKit kit = (HTMLEditorKit)getEditorKit();
        kit.setAutoFormSubmission(false);
		addHyperlinkListener(this); 
		
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

	public void hyperlinkUpdate(HyperlinkEvent linkEvent)
	{
		try {
            if (linkEvent instanceof FormSubmitEvent)
            {
            	FormSubmitEvent form_event=((FormSubmitEvent)linkEvent);
            	String[] datas=form_event.getData().split("&");
            	File html_file=new File(DesktopInterface.getInstance().getTempPath(),"tmp.html");
            	html_file.delete();
        		FileWriter file_html = new FileWriter(html_file);
            	file_html.write("<html>\n");
            	file_html.write("<style type='text/css'>\n");
                file_html.write("  body {\n");
                file_html.write("    color: black;\n");
                file_html.write("    text-align:center;\n"); 
                file_html.write(String.format("    background-color: #%02X%02X%02X\n",getBackground().getRed(),getBackground().getGreen(),getBackground().getBlue()));
                file_html.write("}\n");
                file_html.write("</style>\n");
            	file_html.write("<body>\n");
            	file_html.write("<form name='autoform'");
            	file_html.write(" action='"+linkEvent.getURL().toString()+"'");
            	file_html.write(" method='"+form_event.getMethod().toString()+"'");
            	file_html.write(">\n");
            	file_html.write("<p>Chargement de votre page en cours.</p>\n");
            	for (String data_item : datas) {
            		int pos=data_item.indexOf('=');	                		
            		String data_name="";
            		String data_value="";
            		if (pos>0) {
            			data_name=data_item.substring(0,pos);
            			data_value = URLDecoder.decode(data_item.substring(pos+1),"ISO-8859-1");
            		}
                	file_html.write("<input type='hidden' name='"+data_name+"' value='"+data_value+"'/>\n");
				}
            	file_html.write("<input type='submit' name='OK' value='Charger'/>\n");
                file_html.write("</form>\n");
            	file_html.write("<script type='text/javascript'>\n");
            	file_html.write("document.forms[\"autoform\"].submit();\n");
            	file_html.write("</script>\n");
            	file_html.write("</body>\n");
            	file_html.write("</html>\n");
            	file_html.flush();
            	file_html.close();
            	DesktopInterface.getInstance().launch(html_file.getAbsolutePath());
            }
            else
            	DesktopInterface.getInstance().launch(linkEvent.getURL().toString());
		} catch (Exception excep) {
			excep.printStackTrace();
		}
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
