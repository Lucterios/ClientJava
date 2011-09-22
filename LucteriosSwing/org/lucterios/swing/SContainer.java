package org.lucterios.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.graphic.SwingImage;
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
import org.lucterios.style.ThemeMenu;
import org.lucterios.ui.GUIActionListener;

public class SContainer extends Container implements GUIContainer,ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final boolean OPAQUE=true;

	private ContainerType mType;
	private JPanel mPanel = null;
	private JTabbedPane mTab = null;
	private Redrawing mRedrawing=null;
	private JSplitPane mSpliter=null;

	private CursorMouseListener mCursorMouseListener;
	private FocusListenerList mFocusListener=new FocusListenerList(); 
	private ArrayList<GUIActionListener> mChangeListener=new ArrayList<GUIActionListener>(); 
	
	public void addFocusListener(GUIFocusListener l){
		if (l!=null)
			mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void addChangeListener(GUIActionListener l) {
		if (l!=null)
			mChangeListener.add(l);
	}

	public void removeChangeListener(GUIActionListener l) {
		mChangeListener.remove(l);	
	}
	
	private GUIActionListener mResizeAction=null;

	public void setMouseClickAction(GUIActionListener mMouseClickAction) {
		if (mMouseClickAction!=null)
			mCursorMouseListener.add(mMouseClickAction);
		else
			mCursorMouseListener.clear();
	}

	public void setResizeAction(GUIActionListener mResizeAction) {
		this.mResizeAction = mResizeAction;
	}
	
	public void addActionListener(GUIActionListener l) { }

	public void removeActionListener(GUIActionListener l) {	}

	private Object mObject=null;
	private Object mTag=0;

	public Object getObject() {
		return mObject;
	}

	public void setObject(Object obj) {
		mObject=obj;
	}

	public Object getTag() {
		return mTag;
	}

	public void setTag(Object tag) {
		mTag=tag;		
	}
	
	public JPanel getPanel(){
		return mPanel;
	}
	
	public void invokeLater(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}
	
	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SContainer(ContainerType type,GUIComponent aOwner){
        super();
        mCursorMouseListener=new CursorMouseListener(this,this);
        mOwner=aOwner;
		setName("");
		mType = type;
		creator();
	}

	private void creator() {
		addFocusListener(mFocusListener);
		addMouseListener(mCursorMouseListener);
		addComponentListener(this);
		setFocusable(false);
		switch (mType) {
		case CT_NORMAL:
			mPanel = new JPanel();
			mPanel.setFocusable(false);
			mPanel.setLayout(new GridBagLayout());
			mPanel.setOpaque(OPAQUE);
			this.setLayout(new GridBagLayout());
			add(mPanel,getCntGrid(new GUIParam(0, 0)));
			break;
		case CT_SCROLL:
			mPanel = new JPanel();
			mPanel.setLayout(new GridBagLayout());
			mPanel.setFocusable(false);
			mPanel.setOpaque(OPAQUE);
			JScrollPane scoll = new JScrollPane(mPanel);
			scoll.setFocusable(true);
			scoll.setAutoscrolls(true);
			this.setLayout(new GridBagLayout());
			add(scoll,getCntGrid(new GUIParam(0, 0)));
			break;
		case CT_TAB:
			this.setLayout(new GridBagLayout());
			mTab = new JTabbedPane();
			mTab.setFocusable(false);
			mTab.addMouseListener(mCursorMouseListener);
			mTab.addComponentListener(this);
			mTab.setOpaque(OPAQUE);
			mTab.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					for(GUIActionListener l:mChangeListener)
						l.actionPerformed();
				}
			});
			add(mTab, getCntGrid(new GUIParam(0, 0)));
			break;
		case CT_SPLITER:
			this.setLayout(new GridBagLayout());
			mSpliter=new JSplitPane();
			mSpliter.setFocusable(false);
			mSpliter.setOpaque(OPAQUE);
			mSpliter.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			mSpliter.setOneTouchExpandable(true);
			mSpliter.setLeftComponent(null);
			mSpliter.setRightComponent(null);
			mSpliter.addMouseListener(mCursorMouseListener);
			mSpliter.addComponentListener(this);
			add(mSpliter, getCntGrid(new GUIParam(0, 0)));
		}
	}
	
	public void setLayoutIsGrid(boolean isGrid){
		if (mPanel != null) {
			if (isGrid)
				mPanel.setLayout(new GridBagLayout());
			else
				mPanel.setLayout(null);
		}
	}
	
	public void removeAll(){
		for(int cnt_idx=0;cnt_idx<count();cnt_idx++) {
			GUIComponent cnt=get(cnt_idx);
			if (cnt!=null)
				cnt.setVisible(false);
		}
		switch (mType) {
		case CT_TAB:
			mTab.removeMouseListener(mCursorMouseListener);
			mTab.removeComponentListener(this);
			mTab=null;
			break;
		case CT_SPLITER:
			mSpliter.removeMouseListener(mCursorMouseListener);
			mSpliter.removeComponentListener(this);
			mSpliter=null;
		}
		removeFocusListener(mFocusListener);
		removeMouseListener(mCursorMouseListener);
		removeComponentListener(this);
		super.removeAll();
		creator();
	}

	public void remove(GUIComponent comp) {
		if (Component.class.isInstance(comp)) {
			if (mTab != null)
				mTab.remove((Component)comp);
			if (mPanel != null)
				mPanel.remove((Component)comp);
		}
	}
	public void remove(int index) {
		remove(get(index));
	}	
	
	public void setRedraw(Redrawing redrawing) {
		mRedrawing=redrawing;
		if (mRedrawing!=null) {
			if (mTab != null)
				mTab.setOpaque(!OPAQUE);
			if (mSpliter!=null)
				mSpliter.setOpaque(!OPAQUE);
			if (mPanel != null)
				mPanel.setOpaque(!OPAQUE);			
		}
	}
	
	public ContainerType getType() {
		return mType;
	}
	
	public int count(){
		if (mTab != null)
			return mTab.getTabCount();
		if (mSpliter!=null)
			return 2;
		if (mPanel != null)
			return mPanel.getComponentCount();
		return 0;
	}
	
	protected Object getCnt(GUIParam param) {
		if ((mPanel!=null) && (mPanel.getLayout()==null))
			return null;
		else
			return getCntGrid(param);
	}
	
	protected GridBagConstraints getCntGrid(GUIParam param) {	
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = param.getX();
		gridBagConstraints.gridy = param.getY();
		gridBagConstraints.gridheight = param.getH();
		gridBagConstraints.gridwidth = param.getW();
		switch (param.getReSize()) {
		case RSM_NONE:
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			break;
		case RSM_HORIZONTAL:
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 0;
			break;
		case RSM_VERTICAL:
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 1;
			break;
		case RSM_BOTH:
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			break;
		}
		switch (param.getFill()) {
		case FM_NONE:
			gridBagConstraints.fill = GridBagConstraints.NONE;
			break;
		case FM_HORIZONTAL:
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			break;
		case FM_VERTICAL:
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			break;
		case FM_BOTH:
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			break;
		}
		if (param.getPad()>0)
			gridBagConstraints.insets = new Insets(param.getPad(), param.getPad(), param.getPad(), param.getPad());
		return gridBagConstraints;
	}

	public void setMaximumScroll(){
		if (mType==ContainerType.CT_SCROLL){
			for(int idx=0;idx<getComponentCount();idx++)
				if (JScrollPane.class.isInstance(getComponent(idx))) {
					JScrollPane scroll = (JScrollPane)getComponent(idx);
					JScrollBar vert = scroll.getVerticalScrollBar();
					vert.setValue(vert.getMinimum());
					JScrollBar hori = scroll.getHorizontalScrollBar();
					hori.setValue(hori.getMinimum());
				}
		}
	}
	
	public void setMinimumScroll(){
		if (mType==ContainerType.CT_SCROLL){
			for(int idx=0;idx<getComponentCount();idx++)
				if (JScrollPane.class.isInstance(getComponent(idx))) {
					JScrollPane scroll = (JScrollPane)getComponent(idx);
					scroll.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
					JScrollBar vert = scroll.getVerticalScrollBar();
					vert.setValue(vert.getMinimum());
					JScrollBar hori = scroll.getHorizontalScrollBar();
					hori.setValue(hori.getMinimum());
					scroll.repaint();
				}
		}
	}

	public GUIContainer addTab(ContainerType type, String name,AbstractImage icon) {
		if (mTab == null) 
			return null;
		SContainer result = new SContainer(type,this);
		if (SwingImage.class.isInstance(icon))
			mTab.addTab(name,(ImageIcon)icon.getData(),result);
		else
			mTab.addTab(name,result);
		result.setName(this.getName()+"|"+name+"|");
		return result;
	}

	public int getSelectedIndex() {
		if (mTab == null) 
			return -1;
		return mTab.getSelectedIndex();
	}
	

	public void setSelectedIndex(int tabIdx) {
		if (mTab == null) 
			return;
		 mTab.setSelectedIndex(tabIdx);
	}
	
	public GUIComponent get(int index){
		GUIComponent result = null;
		if (mSpliter!=null) {
			if (index==0)
				result = (SContainer) mSpliter.getLeftComponent();
			else if (index==1)
				result = (SContainer) mSpliter.getRightComponent();
		}
		else if (mTab != null) {
			int num_cmp=0;
			for(int idx=0;idx<mTab.getComponentCount() && (result==null);idx++) {
				Component cmp=mTab.getComponent(idx);
				if (SContainer.class.isInstance(cmp)) {
					if (index==num_cmp)
						result = (SContainer)cmp;
					num_cmp++;
				}
			}
		}
		else {
			int num_cmp=0;
			for(int idx=0;idx<mPanel.getComponentCount() && (result==null);idx++) {
				Component cmp=mPanel.getComponent(idx);
				if (GUIComponent.class.isInstance(cmp)) {
					if (index==num_cmp)
						result = (GUIComponent)cmp;
					num_cmp++;
				}
			}
		}
		return result;
	}
	
	public GUIContainer getSplite(ContainerType type,boolean rightOrBottom) {
		if (mSpliter == null)
			return null;
		SContainer result = null;
		Component cmp;
		if (rightOrBottom)
			cmp=mSpliter.getRightComponent();
		else
			cmp=mSpliter.getLeftComponent();
		if (cmp==null) {
			result = new SContainer(type,this);
			if (rightOrBottom)
				mSpliter.setRightComponent(result);
			else
				mSpliter.setLeftComponent(result);
		}
		else if (SContainer.class.isInstance(cmp))
			result=(SContainer)cmp;
		return result;
	}
	
	public void setSpliteOrientation(boolean horizontal) {
		if (mSpliter != null) {
			if (horizontal)
				mSpliter.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			else
				mSpliter.setOrientation(JSplitPane.VERTICAL_SPLIT);
		}
	}
	
	public void removeSplite(boolean right){
		if (right)
			mSpliter.setRightComponent(null);
		else
			mSpliter.setLeftComponent(null);
	}

	private void changePreferenceSize(GUIParam param, Component result) {
		if ((param.getPrefSizeX()>0) && (param.getPrefSizeY()>0)) {
			result.setPreferredSize(new Dimension(param.getPrefSizeX(),param.getPrefSizeY()));
		}
	}

	public GUIButton createButton(GUIParam param) {
		if (mPanel == null)
			return null;
		SButton result = new SButton(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUICheckBox createCheckBox(GUIParam param) {
		if (mPanel == null)
			return null;
		SCheckBox result = new SCheckBox(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUICheckList createCheckList(GUIParam param) {
		if (mPanel == null)
			return null;
		SCheckList result = new SCheckList(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUICombo createCombo(GUIParam param) {
		if (mPanel == null)
			return null;
		SCombo result = new SCombo(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUIContainer createContainer(ContainerType type, GUIParam param) {
		if (mPanel == null)
			return null;
		SContainer result = new SContainer(type,this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		result.setName(this.getName()+"["+param.getX()+","+param.getY()+"]");
		return result;
	}

	public GUIEdit createEdit(GUIParam param) {
		if (mPanel == null)
			return null;
		SEdit result = new SEdit(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUIGrid createGrid(GUIParam param) {
		if (mPanel == null)
			return null;
		SGrid result = new SGrid(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUILabel createLabel(GUIParam param) {
		if (mPanel == null)
			return null;
		SLabel result = new SLabel(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUIImage createImage(GUIParam param) {
		if (mPanel == null)
			return null;
		SImage result = new SImage(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUIMemo createMemo(GUIParam param) {
		if (mPanel == null)
			return null;
		SMemo result = new SMemo(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUISpinEdit createSpinEdit(GUIParam param) {
		if (mPanel == null)
			return null;
		SSpinEdit result = new SSpinEdit(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUIHyperText createHyperText(GUIParam param) {
		if (mPanel == null)
			return null;
		SHyperText result = new SHyperText(this);
		mPanel.add(result, getCnt(param));
		result.setBackground(mPanel.getBackground());
		changePreferenceSize(param, result);
		return result;
	}

	public GUIHyperMemo createHyperMemo(GUIParam param) {
		if (mPanel == null)
			return null;
		SHyperMemo result = new SHyperMemo(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUITree createTree(GUIParam param) {
		if (mPanel == null)
			return null;
		STree result = new STree(this);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}
	
	public void calculBtnSize(GUIButton[] btns) {
		int wbtn = 0;
		int hbtn = 0;
		for (GUIButton btn : btns) {
			wbtn = Math.max(wbtn, ((SButton) btn).getPreferredSize().width);
			hbtn = Math.max(hbtn, ((SButton) btn).getPreferredSize().height);
		}
		for (GUIButton btn : btns) {
			((SButton) btn)
					.setPreferredSize(new java.awt.Dimension(wbtn, hbtn));
			((SButton) btn).setMaximumSize(new java.awt.Dimension(wbtn, hbtn));
			((SButton) btn).setMinimumSize(new java.awt.Dimension(wbtn, hbtn));
		}
	}

	public int getSizeX() {
		return getSize().width;
	}

	public int getSizeY() {
		return getSize().height;
	}

	@Override
	public void paint(Graphics g) {
		if (mRedrawing!=null) {
			mRedrawing.paint(new SGraphic(g));
		}
		super.paint(g);
	}
	
	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
		if (mPanel!= null)
			mPanel.setBackground(new Color(color));
		if (mTab!= null)
			mTab.setBackground(new Color(color));
		if (mSpliter!=null)
			mSpliter.setBackground(new Color(color));
	}

	public int getBackgroundColor(){
		return getBackground().getRGB();
	}
	
	public void setDividerLocation(double ratio){
		if (mSpliter == null)
			return;
		mSpliter.setDividerLocation(ratio);
	}
	
	public void setDividerLocation(int location){
		if (mSpliter == null)
			return;
		mSpliter.setDividerLocation(location);
	}
	public int getDividerLocation(){
		if (mSpliter == null)
			return -1;
		return mSpliter.getDividerLocation();
	}
	
	public void setMinimumSize(int width, int height){
		setMinimumSize(new Dimension(width, height));
	}

	public void setMaximumSize(int width, int height){
		setMaximumSize(new Dimension(width, height));
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		setPreferredSize(new Dimension(width, height));
	}

	public void componentHidden(ComponentEvent e) {}

	public void componentMoved(ComponentEvent e) {}

	public void componentResized(ComponentEvent e) {
		if (mResizeAction!=null)
			mResizeAction.actionPerformed();
	}

	public void componentShown(ComponentEvent e) {}

	public void setBorder(int top, int left, int bottom, int right, int color) {
		if (mTab != null)
			mTab.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, new Color(color)));
		if (mSpliter!=null)
			mSpliter.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, new Color(color)));
		if (mPanel != null)
			mPanel.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, new Color(color)));				
	}

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}

	public void setToolTipText(String text) {
		if (mTab != null)
			mTab.setToolTipText(text);
		if (mSpliter!=null)
			mSpliter.setToolTipText(text);
		if (mPanel != null)
			mPanel.setToolTipText(text);
	}

	@Override
	public void setVisible(boolean isVisible) {
		if (mTab != null)
			mTab.setVisible(isVisible);
		if (mSpliter!=null)
			mSpliter.setVisible(isVisible);
		if (mPanel != null)
			mPanel.setVisible(isVisible);
		super.setVisible(isVisible);
	}

	private boolean mActive=true;
	public void setActive(boolean aActive) {
		mActive=aActive;
	}
	
	public boolean isActive() {
		if (getOwner()!=null)
			return getOwner().isActive();
		else
			return mActive;
	}
	
	@Override
	public void setCursor(Cursor cursor) {
		super.setCursor(cursor);
		if (mTab != null)
			mTab.setCursor(cursor);
		if (mSpliter!=null)
			mSpliter.setCursor(cursor);
		if (mPanel != null)
			mPanel.setCursor(cursor);
		for(int index=0;index<count();index++) {
			((Component)get(index)).setCursor(cursor);
		}
	}

	public void requestFocusGUI() {
		requestFocus();
	}


	public int getSelectColor(){
		int color=0x0000FF;
		if (ThemeMenu.getDefaultTheme()!=null)
			color=ThemeMenu.getDefaultTheme().getPrimaryControl().getRGB();
		return color;
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(mNbClick);
	}
}
