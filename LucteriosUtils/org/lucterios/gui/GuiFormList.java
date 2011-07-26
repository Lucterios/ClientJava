package org.lucterios.gui;

import java.util.ArrayList;

public abstract class GuiFormList implements NotifyFrameList  {

	protected ArrayList<GUIForm> mList;
	private GUIForm mFormSelected = null;

	public GuiFormList() {
		mList = new ArrayList<GUIForm>();
	}

	public void clear() {
		mList.clear();
	}

	public GUIForm create(String aId) {
		GUIForm form = null;
		for (int idx = 0; (form == null) && idx < count(); idx++)
			if (get(idx).getName().equals(aId))
				form = get(idx);
		if (form == null) {
			form = newForm(aId);
			add(form);
		}
		return form;
	}

	protected abstract GUIForm newForm(String aId);

	private void add(GUIForm aNewForm) {
		aNewForm.setNotifyFrameList(this);
		mList.add(aNewForm);
		if (mFormSelected == null)
			mFormSelected = aNewForm;
	}

	public void removeFrame(GUIForm aForm) {
		mList.remove(aForm);
		System.gc();
	}

	public int count() {
		return mList.size();
	}

	public GUIForm get(int index) {
		return mList.get(index);
	}

	public void selectFrame(GUIForm aForm) {
		mFormSelected = aForm;
	}

	public GUIForm getFrameSelected() {
		return mFormSelected;
	}

	public abstract void addThemeMenuSelector(GUIMenu menu);
	
}