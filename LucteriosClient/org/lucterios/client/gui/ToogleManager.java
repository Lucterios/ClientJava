package org.lucterios.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.WatchDog;
import org.lucterios.engine.presentation.WatchDog.WatchDogRefresher;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.LucteriosException;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class ToogleManager implements Runnable,
		WatchDogRefresher {

	class ToggleAction implements GUIActionListener {
		private static final long serialVersionUID = 1L;

		private int num;

		public ToggleAction(int num) {
			this.num = num;
		}

		public void actionPerformed() {
			Singletons.getWindowGenerator().invokeLater(new Runnable() {
				public void run() {
					changeButton(num);
				}
			});
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<TogglePanel> mToggles = new ArrayList<TogglePanel>();
	private List<GUIButton> mButtons = new ArrayList<GUIButton>();

	private GUIContainer mainToogle;

	private Observer mParent;
	
	private GUIContainer mContainer=null;

	private GUILabel mSeparatorLabel=null;
	
	private int mButtonSelected=-1;

	public void changeButton(int newSelected) {
		if (mButtonSelected!=-1) {
			mToggles.get(mButtonSelected).clear();
		}
		mButtonSelected=newSelected;
		mainToogle.removeAll();
		for(int btnIdx=0;btnIdx<mButtons.size();btnIdx++) {
			mToggles.get(btnIdx).clear();
			mButtons.get(btnIdx).setSelected(btnIdx == mButtonSelected);
		}
		mToggles.get(mButtonSelected).initialize(mainToogle);
		Singletons.getWindowGenerator().invokeLater(this);
	}
	
	public GUIContainer getContainer() {
		return mContainer;
	}

	public ToogleManager(Observer aParent) {
		mParent=aParent;
	}
	
	public void initialize(GUIContainer container) {
		mContainer=container;
	}	

	public void addMenu(GUIMenu currentMenu) {
		for (int index = 0; index < currentMenu.getMenuCount(); index++) {
			GUIMenu current_menu = currentMenu.getMenu(index);
			if (current_menu.isNode()) {
				addMenu(currentMenu.getMenu(index));
			}
			if (current_menu.isPoint() && Action.class.isInstance(current_menu.getAction())) {
				mToggles.add(new TogglePanel(current_menu.getText(), current_menu.getMenuImage().resizeIcon(32, false),
					((Action)current_menu.getAction()).getExtension(),((Action)current_menu.getAction()).getAction(),mParent));
			}
		}
	}

	public void clear() {
		WatchDog.setWatchDogRefresher(null);
		mContainer.removeAll();
		mToggles.clear();
		mButtons.clear();
	}

	public int getToggleCount() {
		return mToggles.size();
	}

	public void showToggles() {
		if (mToggles.size() > 0) {
			mSeparatorLabel=mContainer.createLabel(new GUIParam(0,mToggles.size()+2));
			for (int index = 0; index < mToggles.size(); index++) {
				TogglePanel current = mToggles.get(index);
				GUIButton button = mContainer.createButton(new GUIParam(0,index,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH));
				button.setToggle(true);
				button.setTextString(current.getTitle());
				button.setImage(current.getIcon());
				button.addActionListener(new ToggleAction(index));
				button.setSelected(false);
				mButtons.add(button);
			}

			mainToogle = mContainer.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,mToggles.size()));
			
			changeButton(0);
			WatchDog.setWatchDogRefresher(this);
		}
	}

	public void run() {
		try {
			if (mSeparatorLabel!=null) {
				mContainer.remove(mSeparatorLabel);
				mSeparatorLabel=null;
			}
			refreshClient();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	public void refreshClient() throws LucteriosException {
		if (mContainer.getSizeX()>10){
			for (TogglePanel current : mToggles) {
				current.refreshPanel();
			}
		}
	}

}
