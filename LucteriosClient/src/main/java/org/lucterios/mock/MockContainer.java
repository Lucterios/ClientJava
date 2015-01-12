package org.lucterios.mock;

import java.util.ArrayList;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICheckBox;
import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GUIHyperMemo;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUIImage;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.gui.GUITree;
import org.lucterios.ui.GUIActionListener;

public class MockContainer extends MockComponent implements GUIContainer {

	private ContainerType mType;
	private ArrayList<GUIComponent> mComponentList = new ArrayList<GUIComponent>();

	public void addChangeListener(GUIActionListener l) {
	}

	public void removeChangeListener(GUIActionListener l) {
	}

	public void setMouseClickAction(GUIActionListener mMouseClickAction) {
	}

	public void setResizeAction(GUIActionListener mResizeAction) { }

	private Object mObject = null;
	private Object mTag = 0;

	public Object getObject() {
		return mObject;
	}

	public void setObject(Object obj) {
		mObject = obj;
	}

	public Object getTag() {
		return mTag;
	}

	public void setTag(Object tag) {
		mTag = tag;
	}

	public void invokeLater(Runnable runnable) {
		runnable.run();
	}

	public MockContainer(ContainerType type, GUIComponent aOwner) {
		super(aOwner);
		mType = type;
		if (mType == ContainerType.CT_SPLITER) {
			mComponentList.add(null);
			mComponentList.add(null);
		}
	}

	private boolean mIsGrid = true;
	public void setLayoutIsGrid(boolean isGrid) {
		if ((mType == ContainerType.CT_NORMAL)
				|| (mType == ContainerType.CT_SCROLL)) {
			mIsGrid = isGrid;
		}
	}
	public boolean IsGrid() {
		return mIsGrid;
	}

	public void removeAll() {
		mComponentList.clear();
	}

	public void remove(GUIComponent comp) {
		mComponentList.remove(comp);
	}

	public void remove(int index) {
		mComponentList.remove(index);
	}

	public void setRedraw(Redrawing redrawing) { }

	public ContainerType getType() {
		return mType;
	}

	public int count() {
		return mComponentList.size();
	}

	public void setMaximumScroll() {
		if (mType == ContainerType.CT_SCROLL) {
		}
	}

	public void setMinimumScroll() {
		if (mType == ContainerType.CT_SCROLL) {
		}
	}

	public GUIContainer addTab(ContainerType type, String name,
			AbstractImage icon) {
		if (mType != ContainerType.CT_TAB)
			return null;
		MockContainer result = new MockContainer(type, this);
		result.setParam(icon);
		result.setName(this.getName() + "|" + name + "|");
		mComponentList.add(result);
		return result;
	}

	public int getSelectedIndex() {
		if (mType != ContainerType.CT_TAB)
			return -1;
		return 0;
	}

	public void setSelectedIndex(int tabIdx) {
	}

	public GUIComponent get(int index) {
		GUIComponent result = mComponentList.get(index);
		return result;
	}

	public GUIContainer getSplite(ContainerType type, boolean rightOrBottom) {
		if (mType != ContainerType.CT_TAB)
			return null;
		MockContainer result = null;
		int cmpId;
		if (rightOrBottom)
			cmpId = 0;
		else
			cmpId = 1;
		if (mComponentList.get(cmpId) == null) {
			result = new MockContainer(type, this);
			mComponentList.remove(cmpId);
			mComponentList.add(cmpId, result);
		} else if (MockContainer.class.isInstance(mComponentList.get(cmpId)))
			result = (MockContainer) mComponentList.get(cmpId);
		return result;
	}

	private boolean mHorizontal = true;
	public void setSpliteOrientation(boolean horizontal) {
		if (mType != ContainerType.CT_SPLITER)
			mHorizontal = horizontal;
	}
	public boolean getHorizontal(){
		return mHorizontal;
	}

	public void removeSplite(boolean rightOrBottom) {
		int cmpId;
		if (rightOrBottom)
			cmpId = 0;
		else
			cmpId = 1;
		mComponentList.remove(cmpId);
		mComponentList.add(cmpId, null);
	}

	public GUIButton createButton(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockButton result = new MockButton(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUICheckBox createCheckBox(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockCheckBox result = new MockCheckBox(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUICheckList createCheckList(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockCheckList result = new MockCheckList(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUICombo createCombo(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockCombo result = new MockCombo(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUIContainer createContainer(ContainerType type, GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockContainer result = new MockContainer(type, this);
		result.setParam(param);
		mComponentList.add(result);
		result.setName(this.getName() + "[" + param.getX() + "," + param.getY()
				+ "]");
		return result;
	}

	public GUIEdit createEdit(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockEdit result = new MockEdit(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUIGrid createGrid(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockGrid result = new MockGrid(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUILabel createLabel(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockLabel result = new MockLabel(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUIImage createImage(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockImage result = new MockImage(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUIMemo createMemo(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockMemo result = new MockMemo(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUISpinEdit createSpinEdit(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockSpinEdit result = new MockSpinEdit(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUIHyperText createHyperText(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockHyperText result = new MockHyperText(this);
		result.setParam(param);
		mComponentList.add(result);
		result.setBackgroundColor(getBackgroundColor());
		return result;
	}

	public GUIHyperMemo createHyperMemo(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockHyperMemo result = new MockHyperMemo(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public GUITree createTree(GUIParam param) {
		if ((mType != ContainerType.CT_NORMAL)
				&& (mType != ContainerType.CT_SCROLL))
			return null;
		MockTree result = new MockTree(this);
		result.setParam(param);
		mComponentList.add(result);
		return result;
	}

	public void calculBtnSize(GUIButton[] btns) {
	}

	private int mWidth=0;
	public int getSizeX() {
		return mWidth;
	}

	private int mHeight=0;
	public int getSizeY() {
		return mHeight;
	}

	public void setSize(int width, int height) {
		mWidth=width;
		mHeight=height;
	}

	
	public void setDividerLocation(double ratio) {
		if (mType != ContainerType.CT_SPLITER)
			return;
	}

	public void setDividerLocation(int location) {
		if (mType != ContainerType.CT_SPLITER)
			return;
	}

	public int getDividerLocation() {
		if (mType != ContainerType.CT_SPLITER)
			return -1;
		return 100;
	}

	public void setMinimumSize(int width, int height) {
	}

	public void setMaximumSize(int width, int height) {
	}

	public void setBorder(int top, int left, int bottom, int right, int color) {
	}

	public void setBounds(int top, int left, int bottom, int rigth) {
	}
	
	public void setToolTipText(String text) {
	}

	public void repaint() {
	}

	public void requestFocusGUI() {
	}

	public int getSelectColor() {
		return 0x0000FF;
	}

	public int getHighlightColor() {
		return 0xFFFFFF;
	}
	
	public MockButton getButton(int index) {		
		return (MockButton)mComponentList.get(index);
	}

	public MockCheckBox getCheckBox(int index) {
		return (MockCheckBox)mComponentList.get(index);
	}

	public MockCheckList getCheckList(int index) {
		return (MockCheckList)mComponentList.get(index);
	}

	public MockCombo getCombo(int index) {
		return (MockCombo)mComponentList.get(index);
	}

	public MockContainer getContainer(int index) {
		return (MockContainer)mComponentList.get(index);
	}

	public MockEdit getEdit(int index) {
		return (MockEdit)mComponentList.get(index);
	}

	public MockGrid getGrid(int index) {
		return (MockGrid)mComponentList.get(index);
	}

	public MockLabel getLabel(int index) {
		return (MockLabel)mComponentList.get(index);
	}

	public MockImage getImage(int index) {
		return (MockImage)mComponentList.get(index);
	}

	public MockMemo getMemo(int index) {
		return (MockMemo)mComponentList.get(index);
	}

	public MockSpinEdit getSpinEdit(int index) {
		return (MockSpinEdit)mComponentList.get(index);
	}

	public MockHyperText getHyperText(int index) {
		return (MockHyperText)mComponentList.get(index);
	}

	public MockHyperMemo getHyperMemo(int index) {
		return (MockHyperMemo)mComponentList.get(index);
	}

	public MockTree getTree(int index) {
		return (MockTree)mComponentList.get(index);
	}
	
}
