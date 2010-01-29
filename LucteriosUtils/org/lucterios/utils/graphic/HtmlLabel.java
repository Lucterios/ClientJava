package org.lucterios.utils.graphic;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;

public class HtmlLabel extends JEditorPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static float SizeFactor=1.0f;
	
	private int mFontSize;

	public HtmlLabel(){
		super();
		setContentType("text/html");
		Font df=new Font(null);
		mFontSize=(int) (SizeFactor*df.getSize()+0.5f);
		setText("");
		setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void setText(String aText){
		super.setText(String.format("<font style='font-size:%d;'>%s</font>",mFontSize,aText));
	}

	public int getFontSize(){
		return mFontSize;
	}

	public void setFontSize(int fontSize){
		mFontSize=fontSize;
	}
}
