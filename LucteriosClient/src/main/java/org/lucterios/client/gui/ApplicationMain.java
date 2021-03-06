/*
 *    This file is part of Lucterios.
 *
 *    Lucterios is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    Lucterios is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Lucterios; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
 */

package org.lucterios.client.gui;

import org.lucterios.engine.application.observer.LogonBox;
import org.lucterios.engine.application.observer.ObserverAcknowledge;
import org.lucterios.engine.application.observer.ObserverAuthentification;
import org.lucterios.engine.application.observer.ObserverMenu;
import org.lucterios.engine.application.observer.ObserverMenu.ToolBarInterface;
import org.lucterios.client.setting.Constants;
import org.lucterios.client.setting.Update;
import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.application.ActionLocal;
import org.lucterios.engine.application.ApplicationDescription;
import org.lucterios.engine.application.Connection;
import org.lucterios.engine.gui.RefreshButtonPanel;
import org.lucterios.engine.gui.TabCategoriesPanel;
import org.lucterios.engine.gui.ToogleManagerPanel;
import org.lucterios.engine.gui.ToolBar;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.WatchDog;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.engine.setting.AboutBox;
import org.lucterios.engine.setting.SetupDialog;
import org.lucterios.engine.transport.ImageCache;
import org.lucterios.engine.utils.LucteriosConfiguration.Server;
import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;
import org.lucterios.graphic.TimeLabel;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.MemoryJauge;
import org.lucterios.graphic.ProgressPanel;
import org.lucterios.graphic.WaitingWindow;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIImage;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIWindows;
import org.lucterios.gui.GuiFormList;
import org.lucterios.gui.NotifyFrameChange;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class ApplicationMain implements RefreshButtonPanel,
		Connection, NotifyFrameChange, ToolBarInterface {
	private static final double INITIAL_SPLIT_RATIO = 0.33;
	private static final int OFFSET = 75;

	private static final int PROGRESS_SIZE = 5;

	private GUIMenu refreshWinItem;
	private GUIMenu closeAllWinItem;
	private GUIMenu closeWinItem;
	private GUIMenu contentMenuItem;
	private GUIMenu exitMenuItem;
	private GUIMenu setupMenuItem;
	private GUIMenu disconnectMenuItem;
	private GUIMenu fileMenu;
	private GUIMenu windowsMenu;
	private GUIMenu helpMenu;
	private GUIMenu aboutMenuItem;
	private GUIMenu refreshMenuItem;

	private GUIImage mConnectionLogo;
	private GUILabel mLogName;
	private GUILabel mServer;
	private TimeLabel mTimeValue;
	private MemoryJauge mMemoryJauge;
	private GUIContainer mStatBarPnl;
	private ToolBar mToolBar;

	private TabCategoriesPanel mTabCategoriesPanel;
	private ToogleManagerPanel mToogleManagerPanel;
	
	static private int NB_WIND_MENU = 4;

	private ApplicationDescription mDescription;

	private Action mMenuAction;
	private Action mConnectionInfoAction;
	private Observer mConnectionInfoOwnerObserber;
	private Action mExitAction;

	private Action mDisconnectAction;
	private Action mSetupAction;
	private Action mQuitAction;
	private Action mCloseWinAction;
	private Action mCloseAllWinAction;
	private Action mRefreshWinAction;
	private Action mRefreshMenuAction;
	private Action mContentMenuAction;
	private Action mAboutAction;

	private ProgressPanel mProgressPanelTop;
	private ProgressPanel mProgressPanelBottom;
	
	private GUIFrame mFrame;
	private GUIContainer mMainSplitPanel;

	public ApplicationMain(GUIGenerator generator) throws LucteriosException {
		super();	
		mFrame=generator.getFrame();
		mFrame.setImage(generator.CreateImage(this.getClass().getResource("/connect.png")));
		mFrame.setVisible(false);
		getFormList().setNotifyFrameChange(this);
		ObserverMenu.mToolBar = this;
		
		initAction();
		initialize();
		initMenu();
		ObserverMenu.mDefaultParent=mConnectionInfoOwnerObserber;
		mFrame.pack();
		reorganize();
	}

	private GUIContainer getContainer() {
		return mFrame.getGUIContainer();
	}

	private GUIGenerator getGenerator() {
		return mFrame.getGenerator();
	}

	private GuiFormList getFormList() {
		return mFrame.getFormList();
	}
	
	public GUIActionListener getRunSetupDialog() {
		return new GUIActionListener() {
			public void actionPerformed() {
				SetupDialog.run(Singletons.getWindowGenerator()
						.newDialog(mFrame));
			}
		};
	}

	private void initAction() {
		ObserverFactory fact = Singletons.Factory();

		mMenuAction = new ActionImpl();
		mMenuAction.initialize(null, fact, "Menu", "CORE", "menu");

		mExitAction = new ActionImpl();
		mExitAction.initialize(null, fact, "Exit", "CORE", "exitConnection");

		mConnectionInfoOwnerObserber = new ObserverAcknowledge() {
			public void setActive(boolean aIsActive) {
				ApplicationMain.this.setActive(aIsActive);
			}
		};
		mConnectionInfoAction = new ActionImpl();
		mConnectionInfoAction.initialize(mConnectionInfoOwnerObserber, fact,
				"CnxInfo", "common", "authentification");

		mDisconnectAction = new ActionLocal("Déconnecter", 'd',
					getGenerator().CreateImage(this.getClass().getResource("/disconnected.png")),
				new GUIActionListener(){
					public void actionPerformed() {
						disconnectSession();
					}
				}, "alt ctrl F4");

		mSetupAction = new ActionLocal("Configurer...", 'c',
				getGenerator().CreateImage(this.getClass().getResource("/configure.png")), 
				getRunSetupDialog(),
				null);

		mQuitAction = new ActionLocal("Quitter", 'q',
				getGenerator().CreateImage(this.getClass().getResource("/exit.png")), 
				new GUIActionListener(){
					public void actionPerformed() {
						ExitConnection();
						Singletons.exit();
					}
				}, "ctrl q");

		mCloseWinAction = new ActionLocal("Fermer", 'f', null,
				new GUIActionListener(){
					public void actionPerformed() {
						GUIForm frame_current = getFormList().getFrameSelected();
						if (frame_current != null)
							frame_current.dispose();
					}
				}, "ctrl w");

		mCloseAllWinAction = new ActionLocal("Fermer toutes", 't', null,
				new GUIActionListener(){
					public void actionPerformed() {
						while (getFormList().count() > 0) {
							getFormList().get(0).Close();
						}
					}
				}, "shift ctrl w");

		mRefreshWinAction = new ActionLocal("Rafraichir", 'r', null,
				new GUIActionListener(){
					public void actionPerformed() {
						if (getFormList().count() > 0)
							getFormList().getFrameSelected().refresh();
					}
				}, "F5");

		mRefreshMenuAction = new ActionLocal("Rafraichir tout", 'f', null,
				new GUIActionListener(){
					public void actionPerformed() {
						refreshMainFrame();
					}
				}, "alt ctrl F5");

		mContentMenuAction = new ActionLocal("Aide", 'a',
				getGenerator().CreateImage(this.getClass().getResource("/help.png")), 
				new GUIActionListener(){
					public void actionPerformed() {
						contentMenuItemActionPerformed();
					}
				}, "F1");

		mAboutAction = new ActionLocal("A propos", 'p', null,
				new GUIActionListener(){
					public void actionPerformed() {
						aboutMenuItemActionPerformed();
					}
				}, null);
	}

	private void initialize() {
		mFrame.setTextTitle("Application");
		mFrame.addWindowClose(new GUIActionListener() {			
			public void actionPerformed() {
				ExitConnection();
			}
		});
	
		GUIContainer pnl;
		GUIParam param;
		
		pnl=getContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0,0,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH));
		mToolBar = new ToolBar();
		mToolBar.initialize(pnl);

		param=new GUIParam(0,1,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH);
		param.setPrefSizeX(PROGRESS_SIZE);
		param.setPrefSizeY(PROGRESS_SIZE);
		pnl=getContainer().createContainer(ContainerType.CT_NORMAL, param);
		pnl.setMinimumSize(PROGRESS_SIZE, PROGRESS_SIZE);
		mProgressPanelTop = new ProgressPanel(true,pnl);

		mMainSplitPanel=getContainer().createContainer(ContainerType.CT_SPLITER, new GUIParam(0,2,1,1,ReSizeMode.RSM_BOTH,FillMode.FM_BOTH));
		
		mTabCategoriesPanel=new TabCategoriesPanel(this, mConnectionInfoOwnerObserber);
		mTabCategoriesPanel.initialize(mMainSplitPanel.getSplite(ContainerType.CT_TAB, true));
		mTabCategoriesPanel.getContainer().setMouseClickAction(new GUIActionListener() {
			public void actionPerformed() {
				doubleClickSpliter();
			}
		});
		
		mToogleManagerPanel=new ToogleManagerPanel(this, mConnectionInfoOwnerObserber);
		mToogleManagerPanel.initialize(mMainSplitPanel.getSplite(ContainerType.CT_NORMAL, false));
		
		pnl=getContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0,3,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH));
		mProgressPanelBottom = new ProgressPanel(false,pnl);
		pnl.setSize(PROGRESS_SIZE,PROGRESS_SIZE);

		mStatBarPnl =getContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0,4,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH));

		mConnectionLogo = mStatBarPnl.createImage(new GUIParam(0,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_BOTH));

		param=new GUIParam(1,0,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH);
		mServer = mStatBarPnl.createLabel(param);
		mServer.setStyle(0);

		param=new GUIParam(2,0,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH);
		mLogName = mStatBarPnl.createLabel(param);
		
		param=new GUIParam(3,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_BOTH);
		param.setPad(4);
		mTimeValue = new TimeLabel(mStatBarPnl,param);

		param=new GUIParam(4,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE);
		param.setPad(4);
		pnl=mStatBarPnl.createContainer(ContainerType.CT_NORMAL,param);
		mMemoryJauge = new MemoryJauge(pnl);
		pnl.setSize(PROGRESS_SIZE * 20,PROGRESS_SIZE*2);
		pnl.setVisible(Logging.getInstance().isLogActivate());

		mTimeValue.addActionListener(mMemoryJauge);
		mTimeValue.start();
		getFormList().setObjects(new Object[]{this,mTabCategoriesPanel});
		getFormList().setObjects(new Object[]{this,mToogleManagerPanel});
	}

	private void initMenu() {
		fileMenu = mFrame.addMenu(true);
		fileMenu.setText("Fichier");
		fileMenu.setMnemonic('f');
		fileMenu.setTag(-1);

		disconnectMenuItem = fileMenu.addMenu(false);
		disconnectMenuItem.setAction(mDisconnectAction);
		disconnectMenuItem.setTag(-1);

		setupMenuItem = fileMenu.addMenu(false);
		setupMenuItem.setAction(mSetupAction);
		setupMenuItem.setTag(-1);

		fileMenu.addSeparator();

		exitMenuItem = fileMenu.addMenu(false);
		exitMenuItem.setAction(mQuitAction);
		exitMenuItem.setTag(-1);

		windowsMenu = mFrame.addMenu(true);
		windowsMenu.setText("Fenêtre");
		windowsMenu.setMnemonic('n');
		windowsMenu.setName("windowsMenu");
		windowsMenu.setTag(1);

		closeWinItem = windowsMenu.addMenu(false);
		closeWinItem.setAction(mCloseWinAction);
		closeWinItem.setTag(1);

		closeAllWinItem = windowsMenu.addMenu(false);
		closeAllWinItem.setAction(mCloseAllWinAction);
		closeAllWinItem.setTag(1);

		refreshWinItem  = windowsMenu.addMenu(false);
		refreshWinItem.setAction(mRefreshWinAction);
		refreshWinItem.setTag(1);

		refreshMenuItem = windowsMenu.addMenu(false);
		refreshMenuItem.setAction(mRefreshMenuAction);
		refreshMenuItem.setTag(1);

		helpMenu = mFrame.addMenu(true);
		helpMenu.setText("Aide");
		helpMenu.setMnemonic('a');
		helpMenu.setName("helpMenu");
		helpMenu.setTag(2);

		contentMenuItem = helpMenu.addMenu(false);
		contentMenuItem.setAction(mContentMenuAction);
		contentMenuItem.setTag(2);
		
		getFormList().addThemeMenuSelector(helpMenu.addMenu(true));

		aboutMenuItem = helpMenu.addMenu(false);
		aboutMenuItem.setAction(mAboutAction);
		aboutMenuItem.setTag(2);

		mToolBar.addAction(mDisconnectAction);
		mToolBar.addAction(mQuitAction);
		mToolBar.addAction(mContentMenuAction);
		mToolBar.refresh(mFrame);
	}

	public void newShortCut(String aActionName, String aShortCut,
			GUIAction aActionListener) {
		getFormList().newShortCut(aActionName, aShortCut, aActionListener);
	}

	private double mDividerLocation = -1;
	public void initialToolBar() {
		setActive(false);
		if (mDividerLocation != -1)
			mDividerLocation = (1.0 * mMainSplitPanel.getDividerLocation()) / mMainSplitPanel.getSizeX();
		else
			mDividerLocation = INITIAL_SPLIT_RATIO;
		mMainSplitPanel.setDividerLocation(0);		
		mTabCategoriesPanel.clearTools();
		mToogleManagerPanel.clearTools();
	}

	public void terminatToolBar() {
		mTabCategoriesPanel.getContainer().setVisible(true);
		mTabCategoriesPanel.setMainMenuBar(mFrame);
		getFormList().assignShortCut(mTabCategoriesPanel.getContainer());
		mToogleManagerPanel.getContainer().setVisible(true);
		mToogleManagerPanel.setMainMenuBar(mFrame);			
		getFormList().assignShortCut(mTabCategoriesPanel.getContainer());
		
		mConnectionInfoAction.actionPerformed();
		newShortCut("disconnect", mDisconnectAction.getKeyStroke(),
				mDisconnectAction);
		newShortCut("exit", mQuitAction.getKeyStroke(), mQuitAction);
		newShortCut("close", mCloseWinAction.getKeyStroke(), mCloseWinAction);
		newShortCut("closeAll", mCloseAllWinAction.getKeyStroke(),
				mCloseAllWinAction);
		newShortCut("refresh", mRefreshWinAction.getKeyStroke(),
				mRefreshWinAction);
		newShortCut("refresh_all", mRefreshMenuAction.getKeyStroke(),
				mRefreshMenuAction);
		newShortCut("help", mContentMenuAction.getKeyStroke(),
				mContentMenuAction);
		setActive(true);
	}

	public void doubleClickSpliter() {
		if (mToogleManagerPanel.getContainer().isVisible())
			mDividerLocation = (1.0 * mMainSplitPanel.getDividerLocation()) / mMainSplitPanel.getSizeX();
		mToogleManagerPanel.getContainer().setVisible(!mToogleManagerPanel.getContainer().isVisible());
		if (mToogleManagerPanel.getContainer().isVisible())
			mMainSplitPanel.setDividerLocation(mDividerLocation);
	}
	
	private void aboutMenuItemActionPerformed() {
		AboutBox about_dlg = new AboutBox(mFrame);
		about_dlg.show(mDescription);
	}

	private void contentMenuItemActionPerformed() {
		try {
			Singletons.Transport().openPageInWebBrowser("Help.php");
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	private void disconnectSession() {
		WatchDog.runWatchDog(null);
		WatchDog.setWatchDogRefresher(null);
		ExitConnection();
		LogonBox logon_box = new LogonBox();
		logon_box.mActionSetUp = getRunSetupDialog();
		ObserverAuthentification.refreshMenu = true;
		mCloseAllWinAction.actionPerformed();
		Singletons.Transport().setSession("");
		if (!logon_box.logon(""))
			disconnectSession();
	}

	private void refreshMainFrame() {
		ImageCache.clearMiniImages();
		setActive(false);
		getFormList().clearShortCut();
		mMenuAction.actionPerformed();
		for (int frame_idx = 0; frame_idx < getFormList().count(); frame_idx++)
			getFormList().get(frame_idx).refresh();
		mFrame.toFront();
	}

	public void refreshAll() {
		refreshMainFrame();
	}
	
	private class WinActionListener implements GUIActionListener{
		private String cmd;
		public WinActionListener(String cmd){
			this.cmd=cmd;
		}
		public void actionPerformed() {
			GUIForm frame_to_select = null;
			for (int frame_idx = 0; frame_idx < mFrame.getFormList().count(); frame_idx++)
				if (mFrame.getFormList().get(frame_idx).getName().equals(cmd))
					frame_to_select = mFrame.getFormList().get(frame_idx);
			if (frame_to_select != null){
				if (getFormList().getFrameSelected() != null)
					getFormList().getFrameSelected().setSelected(false);
				frame_to_select.setSelected(true);
				getFormList().selectFrame(frame_to_select);
			}			
			else
				refreshIntFrame();
		}
	}

	private void refreshIntFrame() {
		while (windowsMenu.getMenuCount() > NB_WIND_MENU)
			windowsMenu.removeMenu(NB_WIND_MENU);
		if (getFormList().count() > 0) {
			for (int menu_idx = 0; menu_idx < windowsMenu.getMenuCount(); menu_idx++)
				windowsMenu.getMenu(menu_idx).setEnabled(true);
			windowsMenu.addSeparator();
			GUIMenu new_menu;
			for (int frame_idx = 0; frame_idx < getFormList().count(); frame_idx++) {
				String num_str = "" + (frame_idx + 1);
				new_menu = windowsMenu.addMenu(false);
				new_menu.setText(num_str + " - " + getFormList().get(frame_idx).getTextTitle());
				new_menu.setMnemonic(num_str.charAt(0));
				new_menu.setActionListener(new WinActionListener(getFormList().get(frame_idx).getName()));
			}
		} else
			for (int menu_idx = 0; menu_idx < windowsMenu.getMenuCount(); menu_idx++) {
				GUIMenu mn_item = windowsMenu.getMenu(menu_idx);
				if (mn_item != null) {
					String ref_name = refreshMenuItem.getText();
					String item_name = mn_item.getText();
					mn_item.setEnabled((refreshMenuItem != null)
							&& ref_name.equals(item_name));
				}
			}
	}

	private int[] getArea() {
		int[] rect = new int[]{0,0,0,0};
		int[] coord = Singletons.getDesktop().getCoord(Singletons.getWindowGenerator());
		int[] size = Singletons.getWindowGenerator().getScreenSize();
		rect[0] = (int) (coord[1]); // x
		rect[1] = (int) (coord[0]); // y
		rect[2] = (int) (size[0] - coord[1] - coord[3]); // w
		rect[3] = (int) (size[1] - coord[0] - coord[2]); // h
		return rect;
	}

	public void reorganize() {
		mToolBar.refresh(mFrame);
		mFrame.Maximise();

		int[] globale_area = getArea();
		mStatBarPnl.setSize(mStatBarPnl.getSizeX(),mStatBarPnl.getSizeY());
		int nb = getFormList().count();
		for (int frame_idx = 0; frame_idx < nb; frame_idx++) {
			getFormList().get(frame_idx).setLocation(
					globale_area[0] + (frame_idx + 1) * OFFSET,
					globale_area[1] + (frame_idx + 1) * OFFSET);
			getFormList().get(frame_idx).setSize(9 * (globale_area[2]) / 10,
					9 * (globale_area[3]) / 10);
			getFormList().get(frame_idx).setSelected(false);
		}
		if (nb > 0)
			getFormList().getFrameSelected().setSelected(true);
		if (mToogleManagerPanel.hasToggle()) {
			mMainSplitPanel.setDividerLocation((int) (mDividerLocation * mMainSplitPanel.getSizeX()));
		} else {
			mMainSplitPanel.setDividerLocation(0);
		}
	}

	public void Change() {
		refreshIntFrame();
	}

	public void setActive(boolean aIsActive) {
		mFrame.setActive(aIsActive);
		if (mProgressPanelTop != null)
			mProgressPanelTop.setActive(aIsActive);
		if (mProgressPanelBottom != null)
			mProgressPanelBottom.setActive(aIsActive);
	}

	public void setValue(ApplicationDescription aDescription, String aSubTitle,
			String aLogin, String aRealName, boolean refreshMenu) {

		MapContext context = new MapContext();
		context.put("ses", Singletons.Transport().getSession());
		context.put("info", "true");
		mConnectionInfoOwnerObserber.setContext(context);

		mDescription = aDescription;
		mFrame.setImage(mDescription.getLogoImage());
		String sub_title = aSubTitle;
		if (!sub_title.equals(""))
			mFrame.setTextTitle(mDescription.getTitle() + " - " + sub_title);
		else
			mFrame.setTextTitle(mDescription.getTitle());
		Server server = Singletons.getLastServer();
		mConnectionLogo.setImage(this.getClass().getResource("/connection" + server.ConnectionMode + ".png"));
		mLogName.setTextString(aRealName);
		mServer.setTextString(aLogin + "@" + server.ServerName);
		mDescription.setlogin(aLogin + "@" + server.HostName + "/"
				+ server.Directory + ":" + server.HostPort);
		checkUpgrade();
		if (refreshMenu)
			refreshMainFrame();
	}

	private void checkUpgrade() {
		Update up = new Update(Singletons.Transport());
		if (up.isArchiveMostRecent(Constants.Version())) {
			String text = mDescription.getTitle() + " - version "
					+ Constants.Version();
			text += "\n\nLa version " + up.getVersion() + " est disponible.";
			text += "\nVoulez-vous la télécharger?";
			if (Singletons.getWindowGenerator().showConfirmDialog(text, "Mise à jours")) {
				WaitingWindow ww = new WaitingWindow("Téléchargement de la nouvelle version.<br>Veuillez patienter.","");
				GUIWindows wind=Singletons.getWindowGenerator().newWindows();
				wind.setWindowVisitor(ww);
				wind.setVisible(true);
				try {
					up.runDownloadAndExtract(mDescription.getTitle());
				} finally {
					wind.dispose();
				}
			}
		}

		if (!Constants.CheckCoreVersion(mDescription.getServerVersion()))
			Singletons.getWindowGenerator().showErrorDialog("Ce client est plus ancien que le serveur.\nVeuillez le mettre à jour : Des fonctionnalités peuvent ne pas fonctionner correctement.", "Mise à jour");
	}

	private void ExitConnection() {
		if (!Constants.CheckVersionInferiorEgual(mDescription
				.getServerVersion(), 0, 12))
			mExitAction.runAction(new MapContext());
	}

	public void show() throws LucteriosException {
		mFrame.setVisible(true);
	}

}
