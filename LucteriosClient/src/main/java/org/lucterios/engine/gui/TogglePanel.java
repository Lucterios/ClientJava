package org.lucterios.engine.gui;

import org.lucterios.engine.application.observer.ObserverCustom;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIContainer;

public class TogglePanel {

	private ObserverCustom mObserverCustom = null;
	private String title;
	private AbstractImage mIcon;
	private GUIContainer mContainer=null;

	private String mAction;
	private String mExtension;
	private Observer mParent;

	public TogglePanel(String title, AbstractImage abstractImage, String extension,
			String action,Observer aParent) {
		this.title = title;
		this.mIcon = abstractImage;
		mAction=action;
		mExtension=extension;
		mParent=aParent;
		clear();
	}

	public void initialize(GUIContainer container) {
		mContainer=container;
		mObserverCustom = new ObserverCustom();
		mObserverCustom.setGUIContainer(mContainer);
		mObserverCustom.setSource(mExtension, mAction);
		mObserverCustom.setContext(new MapContext());
		mObserverCustom.setParent(mParent);
		mContainer.setVisible(false);
	}

	public void clear() {
		if (mContainer!=null)
			mContainer.setVisible(false);
		mObserverCustom=null;
		mContainer=null;
	}
	
	public String getTitle() {
		return title;
	}

	public void refreshPanel() throws LucteriosException {
		if (mContainer!=null)
			mContainer.setVisible(true);
		if (mObserverCustom!=null)
			mObserverCustom.refresh();
	}

	public AbstractImage getIcon() {
		return mIcon;
	}

}
