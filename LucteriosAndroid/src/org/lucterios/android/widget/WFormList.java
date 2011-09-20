package org.lucterios.android.widget;

import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GuiFormList;
import org.lucterios.ui.GUIAction;

public class WFormList extends GuiFormList {

	private WGenerator mGenerator;

	public WFormList(WGenerator generator ) {
		super();
		mGenerator=generator; 
	}
	
	@Override
	public GUIForm create(String aId) {
		GUIForm form=(WForm)super.create(aId);
		form.setNotifyFrameChange(this);
		form.setImage(mGenerator.getFrame().getImage());
		return form;
	}	
	
	@Override
	protected GUIForm newForm(String aId) {
		return new WForm(aId, mGenerator);
	}

	@Override
	public void addThemeMenuSelector(GUIMenu menu) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void clearShortCut() { }

	@Override
	public void newShortCut(String aActionName, String aShortCut,
			GUIAction aActionListener) { }

	@Override
	public void setObjects(Object[] objects) { }

	public void assignShortCut(Object aComp) { }

}
