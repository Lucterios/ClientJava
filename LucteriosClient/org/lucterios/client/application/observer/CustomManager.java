package org.lucterios.client.application.observer;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lucterios.client.application.comp.CmpButton;
import org.lucterios.client.application.comp.CmpCheck;
import org.lucterios.client.application.comp.CmpChecklist;
import org.lucterios.client.application.comp.CmpDate;
import org.lucterios.client.application.comp.CmpDateTime;
import org.lucterios.client.application.comp.CmpDownLoad;
import org.lucterios.client.application.comp.CmpEdit;
import org.lucterios.client.application.comp.CmpFastGrid;
import org.lucterios.client.application.comp.CmpFloat;
import org.lucterios.client.application.comp.CmpHyperLink;
import org.lucterios.client.application.comp.CmpImage;
import org.lucterios.client.application.comp.CmpLabelform;
import org.lucterios.client.application.comp.CmpMemo;
import org.lucterios.client.application.comp.CmpPasswd;
import org.lucterios.client.application.comp.CmpSelect;
import org.lucterios.client.application.comp.CmpText;
import org.lucterios.client.application.comp.CmpTextform;
import org.lucterios.client.application.comp.CmpTime;
import org.lucterios.client.application.comp.CmpUpload;
import org.lucterios.client.application.comp.Cmponent;
import org.lucterios.client.application.comp.CompDefault;
import org.lucterios.client.presentation.Observer;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.graphic.ExceptionDlg;
import org.lucterios.utils.graphic.JAdvancePanel;

public class CustomManager extends JAdvancePanel {

	private static final long serialVersionUID = 1L;

	public static int CustomManagerCount = 0;

	static public Map<String, Class> ListComponents = new TreeMap<String, Class>();

	static public boolean initalize() throws IOException {
		ListComponents.put("LABELFORM", CmpLabelform.class);
		ListComponents.put("LABEL", CmpLabelform.class);
		ListComponents.put("IMAGE", CmpImage.class);
		ListComponents.put("GRID", CmpFastGrid.class);
		ListComponents.put("BUTTON", CmpButton.class);
		ListComponents.put("CHECK", CmpCheck.class);
		ListComponents.put("CHECKLIST", CmpChecklist.class);
		ListComponents.put("DATE", CmpDate.class);
		ListComponents.put("DATETIME", CmpDateTime.class);
		ListComponents.put("EDIT", CmpEdit.class);
		ListComponents.put("FLOAT", CmpFloat.class);
		ListComponents.put("MEMO", CmpMemo.class);
		ListComponents.put("MEMOFORM", CmpTextform.class);
		ListComponents.put("PASSWD", CmpPasswd.class);
		ListComponents.put("SELECT", CmpSelect.class);
		ListComponents.put("TEXT", CmpText.class);
		ListComponents.put("TIME", CmpTime.class);
		ListComponents.put("LINK", CmpHyperLink.class);
		ListComponents.put("UPLOAD", CmpUpload.class);
		ListComponents.put("DOWNLOAD", CmpDownLoad.class);
		return true;
	}

	private WeakReference<Observer> mObserver;

	private Observer getObserver() {
		return mObserver.get();
	}

	public ArrayList<SimpleParsing> mComposants = new ArrayList<SimpleParsing>();

	public ArrayList<Cmponent> mCmponents = new ArrayList<Cmponent>();

	public String[] mCompNames = new String[0];

	private static final String CST_TABPANE = "TAB";

	private JTabbedPane PnlTab = null;

	public CustomManager(Observer aObserver) {
		super();
		CustomManagerCount++;
		setName("pnl_Cst");
		setFocusable(false);
		setLayout(new GridBagLayout());
		setFontImage(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("ObserverFont.jpg")), TEXTURE);
		mObserver = new WeakReference<Observer>(aObserver);
		mCmponents.clear();
		mComposants.clear();
	}

	protected void finalize() throws Throwable {
		CustomManagerCount--;
		super.finalize();
	}

	protected void clear() {
		mCmponents.clear();
		mComposants.clear();
		if (PnlTab != null)
			PnlTab.removeChangeListener(ChangeTab);
		PnlTab = null;
	}

	public void close() {
		clear();
		mObserver = null;
	}

	public void init(SimpleParsing aXmlComponent) {
		clear();
		mCompNames = new String[aXmlComponent.getTagCount()];
		for (int index = 0; index < aXmlComponent.getTagCount(); index++) {
			SimpleParsing component = aXmlComponent.getSubTag(index);
			mComposants.add(component);
			mCompNames[index] = component.getAttribut("name");
		}
	}

	private int tabActif = 0;

	private ChangeListener ChangeTab = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			tabActif = PnlTab.getSelectedIndex();
		}
	};

	static public Component getComponentByName(Container aPnl, String aName) {
		Component res = null;
		if (aPnl != null)
			for (int index = 0; (res == null)
					&& (index < aPnl.getComponentCount()); index++) {
				Component cmp = aPnl.getComponent(index);
				if ((cmp != null) && (cmp.getName() != null)
						&& (cmp.getName().equals(aName)))
					res = cmp;
			}
		return res;
	}

	public int getCmponentCount() {
		return mCmponents.size();
	}

	public Cmponent getCmponents(int aIndex) {
		return mCmponents.get(aIndex);
	}

	public Cmponent getCmponentName(String aName) {
		Cmponent res = null;
		for (int cmp_idx = 0; (res == null) && (cmp_idx < getCmponentCount()); cmp_idx++)
			if (aName.equals(getCmponents(cmp_idx).getName()))
				res = getCmponents(cmp_idx);
		return res;
	}

	protected Cmponent getComponentWithClassCheck(Container aPnl, String aName,
			String aCompName) {
		Cmponent comp = (Cmponent) getComponentByName(aPnl, aName);
		if (comp != null) {
			Class comp_class = getComponentClass(aCompName);
			if (!comp.getClass().equals(comp_class)) {
				aPnl.remove(comp);
				comp = null;
			}
		}
		return comp;
	}

	private Class getComponentClass(String aCompName) {
		Class cmp_class = null;
		if (ListComponents.containsKey(aCompName))
			cmp_class = (Class) ListComponents.get(aCompName);
		return cmp_class;
	}

	private Cmponent createComponent(String aCompName, String aCompId)
			throws LucteriosException {
		Class cmp_class = getComponentClass(aCompName);
		Cmponent new_comp = null;

		if (cmp_class != null) {
			try {
				new_comp = (Cmponent) cmp_class.newInstance();
			} catch (InstantiationException e) {
				throw new LucteriosException(
						"Erreur de creation de composants", e);
			} catch (IllegalAccessException e) {
				throw new LucteriosException(
						"Erreur de creation de composants", e);
			}
		}

		if (new_comp == null)
			new_comp = new CompDefault(aCompName);
		new_comp.setName(aCompId);
		return new_comp;
	}

	private JPanel getTab(int tag) {
		JPanel pnl = null;
		for (int tb_cmp = 0; (pnl == null) && (tb_cmp < PnlTab.getTabCount()); tb_cmp++)
			if (JAdvancePanel.class.isInstance(PnlTab.getComponentAt(tb_cmp))) {
				JAdvancePanel ad_pnl = (JAdvancePanel) PnlTab
						.getComponentAt(tb_cmp);
				if (ad_pnl.Tag == tag)
					pnl = ad_pnl;
			}
		if (pnl == null)
			pnl = newTab(new Integer(tag).toString(), tag);
		return pnl;
	}

	private JPanel newTab(String aName, int tag) {
		PnlTab.removeChangeListener(ChangeTab);
		JAdvancePanel new_pnl = new JAdvancePanel();
		new_pnl.Tag = tag;
		new_pnl.setFontImage(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("ObserverFont.jpg")), TEXTURE);
		new_pnl.setFocusable(false);
		// new_pnl.setOpaque(PnlTab.isOpaque());
		new_pnl.setLayout(new GridBagLayout());
		PnlTab.addTab(aName, new_pnl);
		PnlTab.addChangeListener(ChangeTab);
		return new_pnl;
	}

	public void fillComponents() throws LucteriosException {
		mCmponents.clear();
		for (int index = 0; index < mComposants.size(); index++)
			addNewComponent(index);

		removeComponentNotUsed();

		reorganiseMainPanel();

		repaint();
		if (PnlTab != null)
			PnlTab.setSelectedIndex(tabActif);
		mComposants.clear();
	}

	private void removeComponentNotUsed() {
		removeComponentNotUsed(this);
		if (PnlTab != null) {
			for (int tb_cmp = 0; tb_cmp < PnlTab.getTabCount(); tb_cmp++)
				removeComponentNotUsed((JPanel) PnlTab.getComponentAt(tb_cmp));
			int tb_cmp = 0;
			while (tb_cmp < PnlTab.getTabCount())
				if (((JPanel) PnlTab.getComponentAt(tb_cmp))
						.getComponentCount() == 0)
					PnlTab.remove(tb_cmp);
				else
					tb_cmp++;
		}
	}

	private void removeComponentNotUsed(JPanel aPnl) {
		int index = 0;
		while (index < aPnl.getComponentCount()) {
			boolean found = false;
			Component current = aPnl.getComponent(index);
			if (Cmponent.class.isInstance(current)) {
				for (int idx_name = 0; !found && (idx_name < mCompNames.length); idx_name++)
					found = mCompNames[idx_name].equals(current.getName());
			}
			if (found)
				index++;
			else
				aPnl.remove(current);
		}
	}

	private void reorganiseMainPanel() {
		if (PnlTab == null) {
			JLabel lbl = new JLabel();
			lbl.setFocusable(false);
			reorganiseComponents(this, lbl, false);
		} else {
			reorganiseComponents(this, PnlTab, true);
			for (int tb_cmp = 0; tb_cmp < PnlTab.getTabCount(); tb_cmp++) {
				JLabel lbl = new JLabel();
				lbl.setFocusable(false);
				reorganiseComponents((JPanel) PnlTab.getComponentAt(tb_cmp),
						lbl, false);
			}
		}
	}

	private void reorganiseComponents(JPanel aPanel, Component aNewComponent,
			boolean aSupCondition) {
		int h_max = 0;
		int v_max = 0;
		boolean has_weightx_no_null = false;
		boolean has_weighty_no_null = false;
		Cmponent comp = null;
		for (int pnl_cmp = 0; pnl_cmp < aPanel.getComponentCount(); pnl_cmp++)
			if (Cmponent.class.isInstance(aPanel.getComponent(pnl_cmp))) {
				comp = (Cmponent) aPanel.getComponent(pnl_cmp);
				h_max = java.lang.Math.max(h_max, comp.X + comp.W);
				v_max = java.lang.Math.max(v_max, comp.Y);
				has_weightx_no_null = (has_weightx_no_null || (comp.mWeightx > 0));
				has_weighty_no_null = (has_weightx_no_null || (comp.mWeighty > 0));
			} else
				aPanel.getComponent(pnl_cmp).setFocusable(false);
		if ((comp != null) && !has_weightx_no_null)
			comp.mGdbConp.weightx = 1;
		if ((!has_weighty_no_null) || aSupCondition) {
			GridBagConstraints gdbComp = new GridBagConstraints();
			gdbComp.gridx = 0;
			gdbComp.gridy = v_max + 1;
			gdbComp.gridwidth = h_max;
			gdbComp.gridheight = 1;
			gdbComp.anchor = GridBagConstraints.NORTH;
			gdbComp.fill = GridBagConstraints.BOTH;
			gdbComp.weightx = 1;
			gdbComp.weighty = 1;
			gdbComp.insets = new Insets(1, 1, 1, 1);
			aPanel.add(aNewComponent, gdbComp);
		}
	}

	private void addNewComponent(int aComponentIdx) throws LucteriosException {
		Cmponent comp;
		SimpleParsing value_obj = mComposants.get(aComponentIdx);

		int tag_num = 1;
		if (PnlTab != null)
			tag_num = (PnlTab.getTabCount() + 1);
		tag_num = value_obj.getAttributInt("tab", tag_num);
		if (CST_TABPANE.equals(value_obj.getTagName())) {
			if (PnlTab == null) {
				PnlTab = new javax.swing.JTabbedPane();
				PnlTab.setOpaque(false);
			}
			newTab(value_obj.getText(), tag_num);
		} else {
			JPanel current_panel;
			if (tag_num == 0) {
				current_panel = this;
			} else {
				if (PnlTab == null)
					PnlTab = new JTabbedPane();
				current_panel = getTab(tag_num);
			}

			comp = getComponentWithClassCheck(current_panel, value_obj
					.getAttribut("name"), value_obj.getTagName());
			if (comp == null) {
				comp = createComponent(value_obj.getTagName(), value_obj
						.getAttribut("name"));
				comp.init(current_panel, getObserver(), value_obj);
			}
			comp.setValue(value_obj);
			mCmponents.add(comp);
		}
	}

	public void initialComponents() {
		try {
			for (int index = 0; index < getCmponentCount(); index++)
				getCmponents(index).initialize();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	public void runJavaScripts() {
		try {
			for (int index = 0; index < getCmponentCount(); index++)
				getCmponents(index).runJavaScript();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	public Cmponent getFirstCmponentFocusabled() {
		for (int index = 0; index < getCmponentCount(); index++)
			if (getCmponents(index).isFocusable())
				return getCmponents(index);
		return null;
	}

}
