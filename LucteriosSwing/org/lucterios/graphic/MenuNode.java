package org.lucterios.graphic;

import javax.swing.JMenu;

import org.lucterios.gui.AbstractImage;

public class MenuNode extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mDescription="";
	private int mTag=0;

	public MenuNode() {
		super();
	}
	
	public int getTag() {
		return mTag;
	}

	public void setTag(int tag) {
		mTag=tag;
	}	

	public AbstractImage getMenuIcon(){
		return new SwingImage(this.getIcon());
	}

	public void setDescription(String description) {
		if (description==null)
			this.mDescription = "";
		else
			this.mDescription = description;
	}

	public String getDescription() {
		return mDescription;
	}

	private AbstractImage mImage=AbstractImage.Null;
	public void setImage(AbstractImage image) {
		mImage=image;
	}

	public AbstractImage getImage() {
		return mImage;
	}

}