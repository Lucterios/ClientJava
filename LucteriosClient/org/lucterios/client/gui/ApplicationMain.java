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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import org.lucterios.client.application.ActionImpl;
import org.lucterios.client.application.ActionLocal;
import org.lucterios.client.application.ApplicationDescription;
import org.lucterios.client.application.Connection;
import org.lucterios.client.application.Menu;
import org.lucterios.client.application.Menu.ToolBar;
import org.lucterios.client.application.observer.LogonBox;
import org.lucterios.client.application.observer.ObserverAcknowledge;
import org.lucterios.client.application.observer.ObserverAuthentification;
import org.lucterios.client.application.observer.ObserverMenu;
import org.lucterios.client.gui.ThemeMenu.LookAndFeelCallBack;
import org.lucterios.client.setting.AboutBox;
import org.lucterios.client.setting.Constants;
import org.lucterios.client.setting.Update;
import org.lucterios.client.utils.TimeLabel;
import org.lucterios.engine.application.Action;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.WatchDog;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.engine.resources.Resources;
import org.lucterios.engine.setting.SetupDialog;
import org.lucterios.engine.transport.ImageCache;
import org.lucterios.engine.utils.LucteriosConfiguration.Server;
import org.lucterios.graphic.DesktopTools;
import org.lucterios.swing.SDialog;
import org.lucterios.swing.SForm;
import org.lucterios.utils.LucteriosException;
import org.lucterios.swing.SFormList;
import org.lucterios.graphic.FrameControle;
import org.lucterios.graphic.Tools;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.HtmlLabel;
import org.lucterios.graphic.MemoryJauge;
import org.lucterios.graphic.ProgressPanel;
import org.lucterios.graphic.WaitingWindow;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.NotifyFrameChange;

public class ApplicationMain extends JFrame implements RefreshButtonPanel,
		Connection, NotifyFrameChange, ToolBar, FrameControle,
		LookAndFeelCallBack {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int OFFSET = 75;

	private static final int PROGRESS_SIZE = 7;

	private JMenuItem refreshWinItem;
	private JMenuItem closeAllWinItem;
	private JMenuItem closeWinItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem contentMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem setupMenuItem;
	private JMenuItem disconnectMenuItem;
	private JMenu fileMenu;
	private JMenu windowsMenu;
	private JMenu helpMenu;
	private JMenuBar menuBar;
	private JMenuItem refreshMenuItem;

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

	public ApplicationMain() {
		super();
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
		final JFrame frame = this;
		return new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SetupDialog.run(new SDialog(frame));
			}
		};
	}

	private void initAction() {
		ObserverFactory fact = Singletons.Factory();

		mMenuAction = new ActionImpl();
		mMenuAction.initialize(null, fact, "Menu", "CORE", "menu");

		mExitAction = new ActionImpl();
		mExitAction.initialize(null, fact, "Exit", "CORE", "exitConnection");

		mConnectionInfoOwnerObserber = new ObserverAcknowledge(){
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
		mFormList = new SFormList();

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

		mProgressPanelTop = new ProgressPanel(true);
		mProgressPanelTop.setMinimumSize(new Dimension(PROGRESS_SIZE,
				PROGRESS_SIZE));
		mProgressPanelTop.setPreferredSize(new Dimension(PROGRESS_SIZE,
				PROGRESS_SIZE));
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 1;
		cnt.weightx = 1;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		getContentPane().add(mProgressPanelTop, cnt);

		mToolNavigator = new MainPanel(this,mConnectionInfoOwnerObserber);
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 2;
		cnt.weightx = 1;
		cnt.weighty = 1;
		cnt.fill = GridBagConstraints.BOTH;
		getContentPane().add(mToolNavigator, cnt);

		mProgressPanelBottom = new ProgressPanel(false);
		mProgressPanelBottom.setMinimumSize(new Dimension(PROGRESS_SIZE,
				PROGRESS_SIZE));
		mProgressPanelBottom.setPreferredSize(new Dimension(PROGRESS_SIZE,
				PROGRESS_SIZE));
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 3;
		cnt.weightx = 1;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		getContentPane().add(mProgressPanelBottom, cnt);

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
		mTimeValue = new TimeLabel();
		cnt = new GridBagConstraints();
		cnt.gridx = 3;
		cnt.gridy = 0;
		cnt.weightx = 0;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		mStatBarPnl.add(mTimeValue, cnt);

		mMemoryJauge = new MemoryJauge();
		mMemoryJauge.setPreferredSize(new Dimension(PROGRESS_SIZE * 12,
				PROGRESS_SIZE * 2));
		cnt = new GridBagConstraints();
		cnt.gridx = 4;
		cnt.gridy = 0;
		cnt.weightx = 0;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.NONE;
		cnt.insets = new Insets(0, 5, 0, 5);
		mStatBarPnl.add(mMemoryJauge, cnt);

		mTimeValue.addActionListener(mMemoryJauge);
		mTimeValue.addActionListener(Tools.createOrderGCAction());
		mTimeValue.start();
	}

	public void initMenu() {
		menuBar = new JMenuBar();
		menuBar.setName("MainMenu");

		fileMenu = new JMenu();
		fileMenu.setText("Fichier");
		fileMenu.setMnemonic('f');

		disconnectMenuItem = new org.lucterios.client.application.MenuItem(
				mDisconnectAction, "");
		fileMenu.add(disconnectMenuItem);

		setupMenuItem = new org.lucterios.client.application.MenuItem(
				mSetupAction, "");
		fileMenu.add(setupMenuItem);

		fileMenu.addSeparator();

		exitMenuItem = new org.lucterios.client.application.MenuItem(
				mQuitAction, "");
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		windowsMenu = new JMenu();
		windowsMenu.setText("Fenêtre");
		windowsMenu.setMnemonic('n');
		windowsMenu.setName("windowsMenu");

		closeWinItem = new org.lucterios.client.application.MenuItem(
				mCloseWinAction, "");
		windowsMenu.add(closeWinItem);

		closeAllWinItem = new org.lucterios.client.application.MenuItem(
				mCloseAllWinAction, "");
		windowsMenu.add(closeAllWinItem);

		refreshWinItem = new org.lucterios.client.application.MenuItem(
				mRefreshWinAction, "");
		windowsMenu.add(refreshWinItem);

		refreshMenuItem = new org.lucterios.client.application.MenuItem(
				mRefreshMenuAction, "");
		windowsMenu.add(refreshMenuItem);

		menuBar.add(windowsMenu);

		helpMenu = new JMenu();
		helpMenu.setText("Aide");
		helpMenu.setMnemonic('a');
		helpMenu.setName("helpMenu");

		contentMenuItem = new org.lucterios.client.application.MenuItem(
				mContentMenuAction, "");
		helpMenu.add(contentMenuItem);
		helpMenu.add(ThemeMenu.getThemeMenu(this));

		aboutMenuItem = new org.lucterios.client.application.MenuItem(
				mAboutAction, "");
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);
		mToolBar.addAction(mDisconnectAction);
		mToolBar.addAction(mQuitAction);
		mToolBar.addAction(mContentMenuAction);
		mToolBar.refresh(menuBar);
	}

	public void newShortCut(String aActionName, String aShortCut,
			Action aActionListener) {
		mFormList.newShortCut(aActionName, KeyStroke.getKeyStroke(aShortCut),
				(javax.swing.Action) aActionListener);
	}

	public void initialToolBar() {
		setActive(false);
		mToolNavigator.clearTools();
	}

	public void terminatToolBar() {
		mToolNavigator.setVisible(true);
		mToolNavigator.setMainMenuBar(menuBar);
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

	public void refreshIntFrame() {
		while (windowsMenu.getItemCount() > NB_WIND_MENU)
			windowsMenu.remove(NB_WIND_MENU);
		if (mFormList.count() > 0) {
			for (int menu_idx = 0; menu_idx < windowsMenu.getItemCount(); menu_idx++)
				windowsMenu.getItem(menu_idx).setEnabled(true);
			windowsMenu.add(new JSeparator());
			JMenuItem new_menu;
			for (int frame_idx = 0; frame_idx < mFormList.count(); frame_idx++) {
				String num_str = "" + (frame_idx + 1);
				new_menu = new JMenuItem(num_str + " - "
						+ mFormList.get(frame_idx).getTitle());
				contentMenuItem.setMnemonic(num_str.charAt(0));
				new_menu.setActionCommand(mFormList.get(frame_idx).getName());
				new_menu.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						GUIForm frame_to_select = null;
						for (int frame_idx = 0; frame_idx < mFormList.count(); frame_idx++)
							if (mFormList.get(frame_idx).getName().equals(
									evt.getActionCommand()))
								frame_to_select = mFormList.get(frame_idx);
						if (frame_to_select != null)
							selectIntFrame(frame_to_select);
						else
							refreshIntFrame();
					}
				});
				windowsMenu.add(new_menu);
			}
		} else
			for (int menu_idx = 0; menu_idx < windowsMenu.getItemCount(); menu_idx++) {
				JMenuItem mn_item = windowsMenu.getItem(menu_idx);
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
		Insets insets = Tools.convertcoordToInsets(DesktopTools.instance().getCoord());
		Dimension screen = kit.getScreenSize();
		rect.width = (int) (screen.getWidth() - insets.left - insets.right);
		rect.height = (int) (screen.getHeight() - insets.top - insets.bottom);
		rect.x = (int) (insets.left);
		rect.y = (int) (insets.top);
		return rect;
	}

	public void reorganize() {
		mToolBar.refresh(menuBar);
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
		setIconImage(mDescription.getLogoImage());
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
				WaitingWindow ww = new WaitingWindow(
						"Téléchargement de la nouvelle version.<br>Veuillez patienter.");
				try {
					up.runDownloadAndExtract(mDescription.getTitle());
				} finally {
					ww.dispose();
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

	public GUIDialog newDialog(GUIDialog aOwnerDialog, GUIForm aOwnerFrame) {
		SDialog dlg;
		if (aOwnerFrame != null)
			dlg = new SDialog((SForm) aOwnerFrame);
		else if (aOwnerDialog != null)
			dlg = new SDialog((SDialog) aOwnerDialog);
		else
			dlg = new SDialog(this);
		javax.swing.SwingUtilities.updateComponentTreeUI(dlg);
		dlg.mFrameControle = this;
		return dlg;
	}

	public Component[] getComponentsForLookAndFeel() {
		Component[] cmp = new Component[mFormList.count() + 2];
		for (int frame_idx = 0; frame_idx < mFormList.count(); frame_idx++)
			cmp[frame_idx] = (Component) mFormList.get(frame_idx);
		cmp[mFormList.count()] = this;
		cmp[mFormList.count() + 1] = mToolNavigator;
		return cmp;
	}

	private void ExitConnection() {
		if (!Constants.CheckVersionInferiorEgual(mDescription
				.getServerVersion(), 0, 12))
			mExitAction.runAction(new MapContext());
	}

}
