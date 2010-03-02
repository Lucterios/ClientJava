package org.lucterios.utils.graphic;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class HtmlLabel extends JEditorPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String StyleText;
	
	public HtmlLabel(){
		super(new HTMLEditorKit().getContentType(),"");
		setBorder(BorderFactory.createEmptyBorder());
        ((HTMLDocument)getDocument()).getStyleSheet().addRule(StyleText);	
	}

	public static void changeFontSize(float scale) {
		UIDefaults defaults = UIManager.getDefaults();
		Enumeration keys = defaults.keys();
		while(keys.hasMoreElements()) {
		  Object key = keys.nextElement();
		  Object value = defaults.get(key);
		  if(value != null && value instanceof Font) {
		     UIManager.put(key, null);
		     Font font = UIManager.getFont(key);
		     if(font != null) {
		    	 float font_size = font.getSize2D();
		    	 float size=font_size * scale;
		         UIManager.put(key, new FontUIResource(font.deriveFont(size)));
		     }
		  }
		}
		Font font = UIManager.getFont("Label.font");        
		StringBuffer bodyRule = new StringBuffer(); 
		addFont(bodyRule,"body",font,false,false,false);
		addFont(bodyRule,"h1",font.deriveFont(font.getSize2D()*1.40f),true,false,true);
		addFont(bodyRule,"h2",font.deriveFont(font.getSize2D()*1.25f),true,true,false);
		addFont(bodyRule,"h3",font.deriveFont(font.getSize2D()*1.20f),false,true,true);
		addFont(bodyRule,"h4",font.deriveFont(font.getSize2D()*1.15f),false,true,false);
		addFont(bodyRule,"h5",font.deriveFont(font.getSize2D()*1.10f),false,false,false);
		StyleText=bodyRule.toString();
		
	}
	
	private static void addFont(StringBuffer bodyRule,String name,Font font,boolean bold,boolean italic,boolean underline) {
		bodyRule.append(name);
		bodyRule.append("{");
		bodyRule.append("font-family: ");
		bodyRule.append(font.getFamily());
		bodyRule.append(";");
		bodyRule.append("font-size: ");
		bodyRule.append(font.getSize());		
		bodyRule.append("pt;");
		if (bold)
			bodyRule.append("font-weight:bold");
		else
			bodyRule.append("font-weight:normal");
		bodyRule.append(";");

		if (italic)
			bodyRule.append("font-style:italic");
		else
			bodyRule.append("font-style:normal");
		bodyRule.append(";");

		if (underline)
			bodyRule.append("text-decoration: underline");
		else
			bodyRule.append("text-decoration: normal");
		bodyRule.append(" }");
	}

	
}
