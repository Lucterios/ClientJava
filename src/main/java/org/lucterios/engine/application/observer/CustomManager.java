package org.lucterios.engine.application.observer;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import org.lucterios.engine.application.comp.CmpButton;
import org.lucterios.engine.application.comp.CmpCheck;
import org.lucterios.engine.application.comp.CmpChecklist;
import org.lucterios.engine.application.comp.CmpDate;
import org.lucterios.engine.application.comp.CmpDateTime;
import org.lucterios.engine.application.comp.CmpDownLoad;
import org.lucterios.engine.application.comp.CmpEdit;
import org.lucterios.engine.application.comp.CmpFastGrid;
import org.lucterios.engine.application.comp.CmpFloat;
import org.lucterios.engine.application.comp.CmpHyperLink;
import org.lucterios.engine.application.comp.CmpImage;
import org.lucterios.engine.application.comp.CmpLabelform;
import org.lucterios.engine.application.comp.CmpMemo;
import org.lucterios.engine.application.comp.CmpPasswd;
import org.lucterios.engine.application.comp.CmpSelect;
import org.lucterios.engine.application.comp.CmpTextform;
import org.lucterios.engine.application.comp.CmpTime;
import org.lucterios.engine.application.comp.CmpUpload;
import org.lucterios.engine.application.comp.Cmponent;
import org.lucterios.engine.application.comp.CompDefault;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public class CustomManager  {

	public static int CustomManagerCount = 0;

	static public Map<String, Class<? extends Cmponent>> ListComponents = new TreeMap<String, Class<? extends Cmponent>>();

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
		ListComponents.put("TIME", CmpTime.class);
		ListComponents.put("LINK", CmpHyperLink.class);
		ListComponents.put("UPLOAD", CmpUpload.class);
		ListComponents.put("DOWNLOAD", CmpDownLoad.class);
		return true;
	}

	private Observer mObserver;

	private Observer getObserver() {
		return mObserver;
	}

	public ArrayList<SimpleParsing> mComposants = new ArrayList<SimpleParsing>();

	public ArrayList<Cmponent> mCmponents = new ArrayList<Cmponent>();

	public String[] mCompNames = new String[0];

	private static final String CST_TABPANE = "TAB";

	private GUIContainer PnlTab = null;

	private GUIContainer mContainer=null;

	public CustomManager(Observer aObserver) {
		super();
		CustomManagerCount++;
		mObserver = aObserver;
		mCmponents.clear();
		mComposants.clear();
	}

	public void setContainer(GUIContainer container) {
		mContainer=container;
	}
	
	protected void finalize() throws Throwable {
		CustomManagerCount--;
		super.finalize();
	}

	protected void clear() {
		mCompNames = new String[0];
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
		if (aXmlComponent!=null) {
			mCompNames = new String[aXmlComponent.getTagCount()];
			for (int index = 0; index < aXmlComponent.getTagCount(); index++) {
				SimpleParsing component = aXmlComponent.getSubTag(index);
				mComposants.add(component);
				mCompNames[index] = component.getAttribute("name");
			}
		}
	}

	private int tabActif = 0;

	private GUIActionListener ChangeTab = new GUIActionListener() {
		public void actionPerformed() {
			tabActif = PnlTab.getSelectedIndex();
		}
	};

	static public GUIContainer getComponentByName(GUIContainer aPnl, String aName) {
		GUIContainer res = null;
		if (aPnl != null)
			for (int index = 0; (res == null) && (index < aPnl.count()); index++) {
				GUIComponent cmp = aPnl.get(index);
				if ((cmp!=null) && GUIContainer.class.isInstance(cmp)) {
					GUIContainer cont=(GUIContainer)cmp;
					if ((cont.getObject()!=null) && (Cmponent.class.isInstance(cont.getObject())) && (((Cmponent)cont.getObject()).getName() != null)
							&& (((Cmponent)cont.getObject()).getName().equals(aName)))
						res = cont;
				}
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

	protected Cmponent getComponentWithClassCheck(GUIContainer aPnl, String aName,
			String aCompName) {
		Cmponent comp = null;
		GUIContainer cont=getComponentByName(aPnl, aName);
		if (cont != null) {
			comp = (Cmponent)cont.getObject();
			if (comp != null) {
				Class<? extends Cmponent> comp_class = getComponentClass(aCompName);
				if (!comp.getClass().equals(comp_class)) {
					aPnl.remove(cont);
					comp = null;
					cont = null;
				}
			}
		}
		return comp;
	}

	private Class<? extends Cmponent> getComponentClass(String aCompName) {
		Class<? extends Cmponent> cmp_class = null;
		if (ListComponents.containsKey(aCompName))
			cmp_class = ListComponents.get(aCompName);
		return cmp_class;
	}

	private Cmponent createComponent(String aCompName, String aCompId) {
		Class<? extends Cmponent> cmp_class = getComponentClass(aCompName);
		Cmponent new_comp = null;

		if (cmp_class != null) {
			try {
				new_comp = cmp_class.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				new_comp = null;
			}
		}

		if (new_comp == null)
			new_comp = new CompDefault(aCompName);
		new_comp.setName(aCompId);
		return new_comp;
	}

	private GUIContainer getTab(Object tag) {
		GUIContainer pnl = null;
		for (int tb_cmp = 0; (pnl == null) && (tb_cmp < PnlTab.count()); tb_cmp++){
			GUIContainer ad_pnl = (GUIContainer)PnlTab.get(tb_cmp);
			if (tag.equals(ad_pnl.getTag()))
				pnl = ad_pnl;
		}
		if (pnl == null)
			pnl = newTab(tag.toString(), tag);
		return pnl;
	}

	private GUIContainer newTab(String aName, Object tag) {
		PnlTab.removeChangeListener(ChangeTab);
		GUIContainer new_pnl = PnlTab.addTab(ContainerType.CT_SCROLL, aName,AbstractImage.Null);
		new_pnl.setTag(tag);
		PnlTab.addChangeListener(ChangeTab);
		return new_pnl;
	}

	public void fillComponents(){
		mCmponents.clear();
		for (int index = 0; index < mComposants.size(); index++)
			addNewComponent(index);

		removeComponentNotUsed();

		reorganiseMainPanel();

		mContainer.repaint();
		if (PnlTab != null)
			PnlTab.setSelectedIndex(tabActif);
		mComposants.clear();
	}

	private void removeComponentNotUsed() {
		removeComponentNotUsed(mContainer);
		if (PnlTab != null) {
			for (int tb_cmp = 0; tb_cmp < PnlTab.count(); tb_cmp++)
				removeComponentNotUsed((GUIContainer)PnlTab.get(tb_cmp));
			int tb_cmp = 0;
			while (tb_cmp < PnlTab.count())
				if (((GUIContainer) PnlTab.get(tb_cmp)).count() == 0)
					PnlTab.remove(tb_cmp);
				else
					tb_cmp++;
		}
	}

	private void removeComponentNotUsed(GUIContainer aPnl) {
		int index = 0;
		while (index < aPnl.count()) {
			boolean found = false;
			GUIComponent current = aPnl.get(index);
			if (GUIContainer.class.isInstance(current) && Cmponent.class.isInstance(((GUIContainer)current).getObject())) {
				Cmponent cmp=(Cmponent)((GUIContainer)current).getObject();
				for (int idx_name = 0; !found && (idx_name < mCompNames.length); idx_name++)
					found = mCompNames[idx_name].equals(cmp.getName());
			}
			if (found || (current==null) || (current==PnlTab))
				index++;
			else
				aPnl.remove(current);
		}
	}

	private void reorganiseMainPanel() {
		if (PnlTab == null) {
			GUIParam param=reorganiseComponents(mContainer, false);
			if (param!=null)
				mContainer.createLabel(param);
		} else {
			for (int tb_cmp = 0; tb_cmp < PnlTab.count(); tb_cmp++) {
				GUIParam param=reorganiseComponents((GUIContainer)PnlTab.get(tb_cmp),false);
				if (param!=null)
					((GUIContainer)PnlTab.get(tb_cmp)).createLabel(param);
			}
		}
	}

	private GUIParam reorganiseComponents(GUIContainer aPanel, boolean aSupCondition) {
		GUIParam param=null;
		int h_max = 0;
		int v_max = 0;
		boolean has_weightx_no_null = false;
		boolean has_weighty_no_null = false;
		Cmponent comp = null;
		for (int pnl_cmp = 0; pnl_cmp < aPanel.count(); pnl_cmp++) {
			GUIComponent cmp=aPanel.get(pnl_cmp);
			if (GUIContainer.class.isInstance(cmp) && Cmponent.class.isInstance(((GUIContainer)cmp).getObject())) {
				comp = (Cmponent)((GUIContainer)cmp).getObject();
				h_max = java.lang.Math.max(h_max, comp.X + comp.W);
				v_max = java.lang.Math.max(v_max, comp.Y);
				has_weightx_no_null = (has_weightx_no_null || (comp.getWeightx() > 0));
				has_weighty_no_null = (has_weighty_no_null || (comp.getWeighty() > 0));
			};
		}
		if ((comp != null) && !has_weightx_no_null)
			comp.setWeightx(1);
		if ((!has_weighty_no_null) || aSupCondition) {
			param=new GUIParam(0,v_max + 1);
			param.setW(h_max);
			param.setH(1);
			param.setFill(FillMode.FM_BOTH);
			param.setReSize(ReSizeMode.RSM_BOTH);
			param.setPad(0);
		}
		return param;
	}

	private void addNewComponent(int aComponentIdx) {
		Cmponent comp;
		SimpleParsing value_obj = mComposants.get(aComponentIdx);

		int tag_num = 1;
		if (PnlTab != null)
			tag_num = (PnlTab.count() + 1);
		tag_num = value_obj.getAttributeInt("tab", tag_num);
		if (CST_TABPANE.equals(value_obj.getTagName())) {
			if (PnlTab == null) {
				GUIParam param=reorganiseComponents(mContainer, true);
				PnlTab = mContainer.createContainer(ContainerType.CT_TAB, param);
			}
			newTab(value_obj.getText(), tag_num);
		} else {
			GUIContainer current_panel;
			if (tag_num == 0) {
				current_panel = mContainer;
			} else {
				current_panel = getTab(tag_num);
			}

			comp = getComponentWithClassCheck(current_panel, value_obj
					.getAttribute("name"), value_obj.getTagName());
			if (comp == null) {
				comp = createComponent(value_obj.getTagName(), value_obj
						.getAttribute("name"));
				comp.init(current_panel, getObserver(), value_obj);
			}
			comp.setValue(value_obj);
			mCmponents.add(comp);
		}
	}

	public void initialComponents() {
		for (int index = 0; index < getCmponentCount(); index++)
			getCmponents(index).initialize();
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
