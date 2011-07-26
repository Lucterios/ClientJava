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

import java.awt.*;

import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.lucterios.client.application.ActionImpl;
import org.lucterios.client.application.ActionLocal;
import org.lucterios.client.application.Menu;
import org.lucterios.client.application.Menu.ToolBar;
import org.lucterios.client.application.observer.LogonBox;
import org.lucterios.client.application.observer.ObserverAcknowledge;
import org.lucterios.client.application.observer.ObserverAuthentification;
import org.lucterios.client.application.observer.ObserverMenu;
import org.lucterios.client.setting.Constants;
import org.lucterios.client.setting.Update;
import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ApplicationDescription;
import org.lucterios.engine.application.Connection;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.WatchDog;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.engine.resources.Resources;
import org.lucterios.engine.setting.AboutBox;
import org.lucterios.engine.setting.SetupDialog;
import org.lucterios.engine.transport.ImageCache;
import org.lucterios.engine.utils.LucteriosConfiguration.Server;
import org.lucterios.swing.SContainer;
import org.lucterios.swing.SDialog;
import org.lucterios.swing.SForm;
import org.lucterios.swing.SFrame;
import org.lucterios.swing.SWindows;
import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.LucteriosException;
import org.lucterios.swing.SFormList;
import org.lucterios.graphic.FrameControle;
import org.lucterios.graphic.TimeLabel;
import org.lucterios.graphic.Tools;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.HtmlLabel;
import org.lucterios.graphic.MemoryJauge;
import org.lucterios.graphic.ProgressPanel;
import org.lucterios.graphic.WaitingWindow;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.NotifyFrameChange;
import org.lucterios.gui.GUIContainer.ContainerType;

public class ApplicationMain extends SFrame implements RefreshButtonPanel,
		Connection, NotifyFrameChange, ToolBar, FrameControle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int OFFSET = 75;

	private static final int PROGRESS_SIZE = 7;

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

	private JLabel mConnectionLogo;
	private JLabel mLogName;
	private JLabel mServer;
	private TimeLabel mTimeValue;
	private MemoryJauge mMemoryJauge;
	private JPanel mStatBarPnl;
	private MainPanel mToolNavigator;
	private org.lucterios.client.gui.ToolBar mToolBar;

	private SFormList mFormList;

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

	public ApplicationMain(GUIGenerator generator) {
		super(generator);
		HtmlLabel.changeFontSize(0.9f);

		setVisible(false);
		initAction();
		initialize();
		initMenu();
		ActionLocal.mFrameControle = this;
		Menu.mToolBar = this;
		pack();
		reorganize();
	}

	public ActionListener getRunSetupDialog() {
		final SFrame frame = this;
		return new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SetupDialog.run(Singletons.getWindowGenerator()
						.newDialog(frame));
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
				new javax.swing.ImageIcon(Resources.class
						.getResource("disconnected.png")),
				new AbstractAction() {
					private static final long serialVersionUID = 1L;

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						disconnectSession();
					}
				}, KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4,
						java.awt.event.InputEvent.ALT_MASK
								+ java.awt.event.InputEvent.CTRL_MASK));

		mSetupAction = new ActionLocal("Configurer...", 'c',
				new javax.swing.ImageIcon(Resources.class
						.getResource("configure.png")), getRunSetupDialog(),
				null);

		mQuitAction = new ActionLocal("Quitter", 'q',
				new javax.swing.ImageIcon(Resources.class
						.getResource("exit.png")), new AbstractAction() {
					private static final long serialVersionUID = 1L;

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						ExitConnection();
						Singletons.exit();
					}
				}, javax.swing.KeyStroke.getKeyStroke(
						java.awt.event.KeyEvent.VK_Q,
						java.awt.event.InputEvent.CTRL_MASK));

		mCloseWinAction = new ActionLocal("Fermer", 'f', null,
				new AbstractAction() {
					private static final long serialVersionUID = 1L;

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						GUIForm frame_current = mFormList.getFrameSelected();
						if (frame_current != null)
							frame_current.dispose();
					}
				}, javax.swing.KeyStroke.getKeyStroke(
						java.awt.event.KeyEvent.VK_W,
						java.awt.event.InputEvent.CTRL_MASK));

		mCloseAllWinAction = new ActionLocal("Fermer toutes", 't', null,
				new AbstractAction() {
					private static final long serialVersionUID = 1L;

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						while (mFormList.count() > 0) {
							mFormList.get(0).Close();
						}
					}
				}, javax.swing.KeyStroke.getKeyStroke(
						java.awt.event.KeyEvent.VK_W,
						java.awt.event.InputEvent.CTRL_MASK
								+ java.awt.event.InputEvent.SHIFT_MASK));

		mRefreshWinAction = new ActionLocal("Rafraichir", 'r', null,
				new AbstractAction() {
					private static final long serialVersionUID = 1L;

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						if (mFormList.count() > 0)
							mFormList.getFrameSelected().refresh();
					}
				}, javax.swing.KeyStroke.getKeyStroke(
						java.awt.event.KeyEvent.VK_F5, 0));

		mRefreshMenuAction = new ActionLocal("Rafraichir tout", 'f', null,
				new AbstractAction() {
					private static final long serialVersionUID = 1L;

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						refreshMainFrame();
					}
				}, javax.swing.KeyStroke.getKeyStroke(
						java.awt.event.KeyEvent.VK_F5,
						java.awt.event.InputEvent.CTRL_MASK
								+ java.awt.event.InputEvent.ALT_MASK));

		mContentMenuAction = new ActionLocal("Aide", 'a',
				new javax.swing.ImageIcon(Resources.class
						.getResource("help.png")), new AbstractAction() {
					private static final long serialVersionUID = 1L;

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						contentMenuItemActionPerformed(evt);
					}
				}, javax.swing.KeyStroke.getKeyStroke(
						java.awt.event.KeyEvent.VK_F1, 0));

		mAboutAction = new ActionLocal("A propos", 'p', null,
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						aboutMenuItemActionPerformed(evt);
					}
				}, null);
	}

	public void initialize() {
		mFormList = new SFormList(this.getGenerator());
		mFormList.setObjects(new Object[]{this,mToolNavigator});

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Application");
		setName("MainFrame");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				ExitConnection();
			}
		});
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cnt;

		mToolBar = new org.lucterios.client.gui.ToolBar();
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 0;
		cnt.weightx = 1;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		Dimension size = mToolBar.getPreferredSize();
		mToolBar.setMinimumSize(new Dimension(0,
				(int) (org.lucterios.client.gui.ToolBar.ICON_SIZE * 1.8)));
		mToolBar.setPreferredSize(new Dimension(size.width,
				(int) (org.lucterios.client.gui.ToolBar.ICON_SIZE * 1.8)));
		mToolBar.setMaximumSize(new Dimension(Integer.MAX_VALUE,
				(int) (org.lucterios.client.gui.ToolBar.ICON_SIZE * 1.8)));
		getContentPane().add(mToolBar, cnt);

		SContainer pnl;
		pnl=new SContainer(ContainerType.CT_NORMAL);
		mProgressPanelTop = new ProgressPanel(true,pnl);
		pnl.setMinimumSize(new Dimension(PROGRESS_SIZE,PROGRESS_SIZE));
		pnl.setPreferredSize(new Dimension(PROGRESS_SIZE,PROGRESS_SIZE));
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 1;
		cnt.weightx = 1;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		getContentPane().add(pnl, cnt);

		mToolNavigator = new MainPanel(this, mConnectionInfoOwnerObserber);
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 2;
		cnt.weightx = 1;
		cnt.weighty = 1;
		cnt.fill = GridBagConstraints.BOTH;
		getContentPane().add(mToolNavigator, cnt);

		pnl=new SContainer(ContainerType.CT_NORMAL);
		mProgressPanelBottom = new ProgressPanel(false,pnl);
		pnl.setMinimumSize(new Dimension(PROGRESS_SIZE,PROGRESS_SIZE));
		pnl.setPreferredSize(new Dimension(PROGRESS_SIZE,PROGRESS_SIZE));
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 3;
		cnt.weightx = 1;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		getContentPane().add(pnl, cnt);

		mStatBarPnl = new JPanel();
		mStatBarPnl.setLayout(new GridBagLayout());
		mStatBarPnl.setFont(new Font("TimesRoman", Font.PLAIN, 9));
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 4;
		cnt.weightx = 1;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		getContentPane().add(mStatBarPnl, cnt);

		mConnectionLogo = new JLabel();
		mConnectionLogo.setHorizontalAlignment(JLabel.CENTER);
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 0;
		cnt.weightx = 0;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		mStatBarPnl.add(mConnectionLogo, cnt);
		mLogName = new JLabel();
		mLogName.setFont(new Font("TimesRoman", Font.BOLD, 9));
		mLogName.setHorizontalAlignment(JLabel.CENTER);
		cnt = new GridBagConstraints();
		cnt.gridx = 1;
		cnt.gridy = 0;
		cnt.weightx = 0;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		mStatBarPnl.add(mLogName, cnt);
		mServer = new JLabel();
		mServer.setFont(new Font("TimesRoman", Font.PLAIN, 9));
		mServer.setHorizontalAlignment(JLabel.CENTER);
		cnt = new GridBagConstraints();
		cnt.gridx = 2;
		cnt.gridy = 0;
		cnt.weightx = 1;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		mStatBarPnl.add(mServer, cnt);
		
		mTimeValue = new TimeLabel(new SContainer(ContainerType.CT_NORMAL),new GUIParam(0,0));
		cnt = new GridBagConstraints();
		cnt.gridx = 3;
		cnt.gridy = 0;
		cnt.weightx = 0;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		mStatBarPnl.add((Component)mTimeValue.getLabel(), cnt);

		pnl=new SContainer(ContainerType.CT_NORMAL);
		mMemoryJauge = new MemoryJauge(pnl);
		pnl.setPreferredSize(new Dimension(PROGRESS_SIZE * 12,
				PROGRESS_SIZE * 2));
		cnt = new GridBagConstraints();
		cnt.gridx = 4;
		cnt.gridy = 0;
		cnt.weightx = 0;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.NONE;
		cnt.insets = new Insets(0, 5, 0, 5);
		mStatBarPnl.add(pnl, cnt);

		mTimeValue.addActionListener(mMemoryJauge);
		mTimeValue.addActionListener(new GUIActionListener() {			
			public void actionPerformed() {
				Tools.createOrderGCAction();
			}
		});		
		mTimeValue.start();
	}

	public void initMenu() {
		fileMenu = addMenu(true);
		fileMenu.setText("Fichier");
		fileMenu.setMnemonic('f');

		disconnectMenuItem = fileMenu.addMenu(false);
		disconnectMenuItem.setAction(mDisconnectAction);

		setupMenuItem = fileMenu.addMenu(false);
		setupMenuItem.setAction(mSetupAction);

		fileMenu.addSeparator();

		exitMenuItem = fileMenu.addMenu(false);
		exitMenuItem.setAction(mQuitAction);

		windowsMenu = addMenu(true);
		windowsMenu.setText("Fenêtre");
		windowsMenu.setMnemonic('n');
		windowsMenu.setName("windowsMenu");

		closeWinItem = windowsMenu.addMenu(false);
		closeWinItem.setAction(mCloseWinAction);

		closeAllWinItem = windowsMenu.addMenu(false);
		closeAllWinItem.setAction(mCloseAllWinAction);

		refreshWinItem  = windowsMenu.addMenu(false);
		refreshWinItem.setAction(mRefreshWinAction);

		refreshMenuItem = windowsMenu.addMenu(false);
		refreshMenuItem.setAction(mRefreshMenuAction);

		helpMenu = addMenu(true);
		helpMenu.setText("Aide");
		helpMenu.setMnemonic('a');
		helpMenu.setName("helpMenu");

		contentMenuItem = helpMenu.addMenu(false);
		contentMenuItem.setAction(mContentMenuAction);
		
		mFormList.addThemeMenuSelector(helpMenu.addMenu(true));

		aboutMenuItem = helpMenu.addMenu(false);
		aboutMenuItem.setAction(mAboutAction);

		mToolBar.addAction(mDisconnectAction);
		mToolBar.addAction(mQuitAction);
		mToolBar.addAction(mContentMenuAction);
		mToolBar.refresh(this,1,2);
	}

	public void newShortCut(String aActionName, String aShortCut,
			GUIAction aActionListener) {
		mFormList.newShortCut(aActionName, KeyStroke.getKeyStroke(aShortCut),
				(javax.swing.Action) aActionListener);
	}

	public void initialToolBar() {
		setActive(false);
		mToolNavigator.clearTools();
	}

	public void terminatToolBar() {
		mToolNavigator.setVisible(true);
		mToolNavigator.setMainMenuBar(this,1,2);
		mFormList.assignShortCut(mToolNavigator);
		mConnectionInfoAction.actionPerformed();
		terminateShortCut();
		setActive(true);
	}

	private void terminateShortCut() {
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
	}

	private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		AboutBox about_dlg = new AboutBox(this);
		about_dlg.show(mDescription);
	}

	private void contentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			Singletons.Transport().openPageInWebBrowser("Help.php");
		} catch (LucteriosException e) {
			throwException(e);
		}
	}

	private void disconnectSession() {
		WatchDog.runWatchDog(null);
		WatchDog.setWatchDogRefresher(null);
		ExitConnection();
		LogonBox logon_box = new LogonBox();
		logon_box.mActionSetUp = getRunSetupDialog();
		try {
			ObserverAuthentification.refreshMenu = true;
			mCloseAllWinAction.actionPerformed();
			Singletons.Transport().setSession("");
			logon_box.logon("");
		} catch (LucteriosException e) {
			throwException(e);
			disconnectSession();
		}
		logon_box.dispose();
	}

	public void selectIntFrame(GUIForm aInternalFrame) {
		if (mFormList.getFrameSelected() != null)
			mFormList.getFrameSelected().setSelected(false);
		aInternalFrame.setSelected(true);
		mFormList.selectFrame(aInternalFrame);
	}

	private void throwException(Exception e) {
		ExceptionDlg.throwException(e);
	}

	public void refreshMainFrame() {
		ImageCache.clearMiniImages();
		setActive(false);
		ObserverMenu.Main = this;
		mFormList.clearShortCut();
		mMenuAction.actionPerformed();
		for (int frame_idx = 0; frame_idx < mFormList.count(); frame_idx++)
			mFormList.get(frame_idx).refresh();
		reorganize();
		toFront();
	}
	
	private class WinActionListener implements GUIActionListener{
		private String cmd;
		public WinActionListener(String cmd){
			this.cmd=cmd;
		}
		public void actionPerformed() {
			GUIForm frame_to_select = null;
			for (int frame_idx = 0; frame_idx < mFormList.count(); frame_idx++)
				if (mFormList.get(frame_idx).getName().equals(cmd))
					frame_to_select = mFormList.get(frame_idx);
			if (frame_to_select != null)
				selectIntFrame(frame_to_select);
			else
				refreshIntFrame();
		}
	}

	public void refreshIntFrame() {
		while (windowsMenu.getMenuCount() > NB_WIND_MENU)
			windowsMenu.remove(NB_WIND_MENU);
		if (mFormList.count() > 0) {
			for (int menu_idx = 0; menu_idx < windowsMenu.getMenuCount(); menu_idx++)
				windowsMenu.getMenu(menu_idx).setEnabled(true);
			windowsMenu.addSeparator();
			GUIMenu new_menu;
			for (int frame_idx = 0; frame_idx < mFormList.count(); frame_idx++) {
				String num_str = "" + (frame_idx + 1);
				new_menu = windowsMenu.addMenu(false);
				new_menu.setText(num_str + " - " + mFormList.get(frame_idx).getTitle());
				new_menu.setMnemonic(num_str.charAt(0));
				new_menu.setActionListener(new WinActionListener(mFormList.get(frame_idx).getName()));
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

	private Rectangle getArea() {
		Rectangle rect = new Rectangle();
		Toolkit kit = Toolkit.getDefaultToolkit();
		Insets insets = Tools.convertcoordToInsets(Singletons.getDesktop()
				.getCoord(Singletons.getWindowGenerator()));
		Dimension screen = kit.getScreenSize();
		rect.width = (int) (screen.getWidth() - insets.left - insets.right);
		rect.height = (int) (screen.getHeight() - insets.top - insets.bottom);
		rect.x = (int) (insets.left);
		rect.y = (int) (insets.top);
		return rect;
	}

	public void reorganize() {
		mToolBar.refresh(this,1,2);
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		Rectangle globale_area = getArea();
		mStatBarPnl.setMaximumSize(mStatBarPnl.getSize());
		mStatBarPnl.setMinimumSize(mStatBarPnl.getSize());
		int nb = mFormList.count();
		for (int frame_idx = 0; frame_idx < nb; frame_idx++) {
			mFormList.get(frame_idx).setLocation(
					globale_area.x + (frame_idx + 1) * OFFSET,
					globale_area.y + (frame_idx + 1) * OFFSET);
			mFormList.get(frame_idx).setSize(9 * (globale_area.width) / 10,
					9 * (globale_area.height) / 10);
			mFormList.get(frame_idx).setSelected(false);
		}
		if (nb > 0)
			mFormList.getFrameSelected().setSelected(true);
	}

	public void Change() {
		refreshIntFrame();
	}

	public void setActive(boolean aIsActive) {
		Cursor current_cursor;
		if (aIsActive)
			current_cursor = Cursor.getDefaultCursor();
		else
			current_cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		setCursor(current_cursor);
		mToolNavigator.setCursor(current_cursor);
		if (mProgressPanelTop != null)
			mProgressPanelTop.setActive(aIsActive);
		if (mProgressPanelBottom != null)
			mProgressPanelBottom.setActive(aIsActive);
	}

	public void refreshSize() {
		Dimension dim = getSize();
		pack();
		setSize(dim);
	}

	private void refreshConnectionInfoOwnerObs() {
		MapContext context = new MapContext();
		context.put("ses", Singletons.Transport().getSession());
		context.put("info", "true");
		mConnectionInfoOwnerObserber.setContext(context);
	}

	public void setValue(ApplicationDescription aDescription, String aSubTitle,
			String aLogin, String aRealName, boolean refreshMenu) {
		refreshConnectionInfoOwnerObs();
		mDescription = aDescription;
		Image logoIcon = null;
		if (mDescription.getLogo() != null)
			logoIcon = ((ImageIcon) mDescription.getLogo()).getImage();
		setIconImage(logoIcon);
		String sub_title = aSubTitle;
		if (!sub_title.equals(""))
			setTitle(mDescription.getTitle() + " - " + sub_title);
		else
			setTitle(mDescription.getTitle());
		Server server = LogonBox.getLastServer();
		mConnectionLogo.setIcon(new javax.swing.ImageIcon(Resources.class
				.getResource("connection" + server.ConnectionMode + ".png")));
		mLogName.setText(aRealName);
		mServer.setText(aLogin + "@" + server.ServerName);
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
			if (JOptionPane.showConfirmDialog(this, text, "Mise à jours",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				WaitingWindow ww = new WaitingWindow("Téléchargement de la nouvelle version.<br>Veuillez patienter.","");
				SWindows wind=(SWindows)Singletons.getWindowGenerator().newWindows();
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
			JOptionPane
					.showMessageDialog(
							this,
							"Ce client est plus ancien que le serveur.\nVeuillez le mettre à jour : Des fonctionnalités peuvent ne pas fonctionner correctement.",
							"Mise à jour", JOptionPane.WARNING_MESSAGE);
	}

	public SForm newFrame(String aActionId) {
		SForm frame = (SForm) mFormList.create(aActionId);
		javax.swing.SwingUtilities.updateComponentTreeUI(frame);
		frame.mFrameControle = this;
		frame.setNotifyFrameChange(this);
		Rectangle globale_area = getArea();
		int nb = mFormList.count() - 1;
		frame.setLocation(globale_area.x + (nb + 1) * OFFSET, globale_area.y
				+ (nb + 1) * OFFSET);
		frame.setSize(9 * (globale_area.width) / 10,
				9 * (globale_area.height) / 10);
		frame.setIconImage(this.getIconImage());
		return frame;
	}

	public GUIDialog newDialog(GUIDialog aOwnerDialog, GUIForm aOwnerForm) {
		GUIDialog dlg;
		if ((aOwnerForm != null) || (aOwnerDialog != null))
			dlg = getGenerator().newDialog(aOwnerDialog, aOwnerForm);
		else
			dlg = getGenerator().newDialog(this);
		((SDialog)dlg).mFrameControle = this;
		return dlg;
	}

	private void ExitConnection() {
		if (!Constants.CheckVersionInferiorEgual(mDescription
				.getServerVersion(), 0, 12))
			mExitAction.runAction(new MapContext());
	}

}
