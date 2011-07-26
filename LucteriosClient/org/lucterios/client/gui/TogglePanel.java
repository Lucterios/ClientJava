package org.lucterios.client.gui;

import org.lucterios.client.application.observer.ObserverCustom;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
import org.lucterios.form.JAdvancePanel;
import org.lucterios.gui.AbstractImage;

public class TogglePanel extends JAdvancePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ObserverCustom mObserverCustom = null;
	private String title;
	private AbstractImage mIcon;

	public TogglePanel(String title, AbstractImage abstractImage, String extension,
			String action,Observer aParent) {
		mObserverCustom = new ObserverCustom();
		mObserverCustom.setGUIContainer(this);
		mObserverCustom.setSource(extension, action);
		mObserverCustom.setContext(new MapContext());
		mObserverCustom.setParent(aParent);
		this.title = title;
		this.mIcon = abstractImage;
	}

	public String getTitle() {
		return title;
	}

	public void refreshPanel() throws LucteriosException {
		setVisible(false);
		mObserverCustom.refresh();
	}

	public AbstractImage getIcon() {
		return mIcon;
	}

}
