package org.lucterios.client.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.lucterios.client.application.Menu;
import org.lucterios.client.application.MenuItem;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.WatchDog;
import org.lucterios.engine.presentation.WatchDog.WatchDogRefresher;
import org.lucterios.engine.resources.Resources;
import org.lucterios.utils.LucteriosException;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.JAdvancePanel;
import org.lucterios.graphic.Tools;

public class ToogleManager extends JAdvancePanel implements ActionListener,
		WatchDogRefresher {

	class ToggleAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private int num;

		public ToggleAction(String text, ImageIcon icon, int num) {
			super(text, icon);
			this.num = num;
		}

		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					changeButton();
				}
			});
		}

		public void changeButton() {
			setVisible(false);
			try {
				mainToogle.removeAll();
				int btn_num = 0;
				int index = 0;
				while (index < getComponentCount()) {
					if (JToggleButton.class.isInstance(getComponent(index))) {
						JToggleButton btn = (JToggleButton) getComponent(index);
						btn.setSelected(btn_num == num);
						btn_num++;
					}
					index++;
				}
				GridBagConstraints cnt = new GridBagConstraints();
				cnt.gridx = 0;
				cnt.gridy = 0;
				cnt.weightx = 1;
				cnt.weighty = 1;
				cnt.fill = GridBagConstraints.BOTH;
				mainToogle.add(mToggles.get(num), cnt);
				mToggles.get(num).repaint();
				mainToogle.repaint();
				repaint();
			} finally {
				setVisible(true);
			}
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<TogglePanel> mToggles = new ArrayList<TogglePanel>();

	private JPanel mainToogle;

	private Observer mParent;

	public ToogleManager(Image fontImage, Observer aParent) {
		setFontImage(fontImage, TEXTURE);
		setLayout(new GridBagLayout());
		mParent=aParent;
	}

	public void addMenu(Menu aMenu) {
		for (int index = 0; index < aMenu.getMenuComponentCount(); index++) {
			if (Menu.class.isInstance(aMenu.getMenuComponent(index))) {
				Menu current_menu = (Menu) aMenu.getMenuComponent(index);
				addMenu(current_menu);
			}
			if (MenuItem.class.isInstance(aMenu.getMenuComponent(index))) {
				MenuItem current_menu = (MenuItem) aMenu
						.getMenuComponent(index);
				mToggles.add(new TogglePanel(current_menu.getText(), Tools
						.resizeIcon((ImageIcon)current_menu.mAction.getIcon().getData(), 32, false),
						current_menu.mAction.getExtension(),
						current_menu.mAction.getAction(),mParent));
			}
		}
	}

	public void clear() {
		WatchDog.setWatchDogRefresher(null);
		this.removeAll();
		mToggles.clear();
	}

	public int getToggleCount() {
		return mToggles.size();
	}

	public void showToggles() {
		if (mToggles.size() > 0) {
			JToggleButton first_button = null;
			GridBagConstraints cnt = new GridBagConstraints();
			cnt.gridx = 0;
			cnt.weightx = 1;
			cnt.weighty = 0;
			cnt.fill = GridBagConstraints.BOTH;
			for (int index = 0; index < mToggles.size(); index++) {
				TogglePanel current = mToggles.get(index);
				JToggleButton button = new JToggleButton(new ToggleAction(
						current.getTitle(), current.getIcon(), index));
				if (first_button == null)
					first_button = button;
				button.setSelected(false);
				cnt.gridy = index;
				add(button, cnt);
			}

			mainToogle = new JPanel();
			mainToogle.setLayout(new GridBagLayout());
			mainToogle.setOpaque(false);
			cnt.gridx = 0;
			cnt.gridy = mToggles.size();
			cnt.weightx = 1;
			cnt.weighty = 1;
			cnt.fill = GridBagConstraints.BOTH;
			add(mainToogle, cnt);

			Toolkit tkt = Toolkit.getDefaultToolkit();
			ImageIcon icon = new ImageIcon(tkt.getImage(Resources.class
					.getResource("refresh.png")));
			JButton refresh = new JButton("Rafraichir", Tools.resizeIcon(icon,
					24, false));
			refresh.addActionListener(this);
			cnt.weighty = 0;
			cnt.gridy = mToggles.size() + 1;
			add(refresh, cnt);
			actionPerformed(null);
			first_button.doClick();
			WatchDog.setWatchDogRefresher(this);
		}
	}

	public void actionPerformed(ActionEvent aEvent) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					refreshClient();
				} catch (LucteriosException e) {
					ExceptionDlg.throwException(e);
				}
			}

		});
	}

	public void refreshClient() throws LucteriosException {
		for (TogglePanel current : mToggles) {
			current.refreshPanel();
		}
	}

}
