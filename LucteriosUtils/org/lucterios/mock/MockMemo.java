package org.lucterios.mock;

import java.util.ArrayList;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIMenu;

public class MockMemo extends MockComponent implements GUIMemo {

	private static final long serialVersionUID = 1L;

    public static short NUMBERS_WIDTH=35;

	public MockMemo(GUIComponent aOwner){
    	super(aOwner);
		setTabs(4);
    }             
    
	public void setTabs(int charactersPerTab)
	{
	}
	
	private int mFirstLine=1;

	public void setFirstLine(int aFirstLine)
	{
		mFirstLine=aFirstLine;
	};

	public int getFirstLine(){
		return mFirstLine;
	}

	public void setStringSize(int aStringSize) {	};

	private String mText="";
	public String getValue(){
		return mText;
	}

	private ArrayList<MockMenu> mMenulist=new ArrayList<MockMenu>();
	
	public ArrayList<MockMenu> getMenulist(){
		return mMenulist;
	}
 	public GUIMenu getPopupMenu() {
		MockMenu newmenu=new MockMenu(true);
		mMenulist.add(newmenu);
		return newmenu;
	}

	public void insertText(String specialToAdd) {
		mText+=specialToAdd;
	}

	public void setText(String text) {
		mText=text;
	}
		
}
