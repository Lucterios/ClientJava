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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICheckBox;
import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.ui.GUIActionListener;

public class SContainer extends Container implements GUIContainer,FocusListener,MouseListener,ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ContainerType mType;
	private JPanel mPanel = null;
	private JTabbedPane mTab = null;
	private Redrawing mRedrawing=null;
	private JSplitPane mSpliter=null;

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

    public void focusLost(java.awt.event.FocusEvent evt) 
    {
		for(GUIFocusListener l:mFocusListener)
			l.focusLost();
    }
    
	public void focusGained(FocusEvent e) { }
	
	private GUIActionListener mMouseClickAction=null;
	private GUIActionListener mResizeAction=null;

	public void setMouseClickAction(GUIActionListener mMouseClickAction) {
		this.mMouseClickAction = mMouseClickAction;
	}

	public void setResizeAction(GUIActionListener mResizeAction) {
		this.mResizeAction = mResizeAction;
	}
	
	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>();

	public void addActionListener(GUIActionListener l) {
		mActionListener.add(l);
	}

	public void removeActionListener(GUIActionListener l) {
		mActionListener.remove(l);
	}

	private Object mObject;

	public Object getObject() {
		return mObject;
	}

	public void setObject(Object obj) {
		mObject=obj;
	}

	public JPanel getPanel(){
		return mPanel;
	}
	
	public void invokeLater(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}
	
	public SContainer(ContainerType type) {
		super();
		setName("");
		mType = type;
		creator();
	}

	private void creator() {
		addFocusListener(this);
		addMouseListener(this);
		addComponentListener(this);
		switch (mType) {
		case CT_NORMAL:
			mPanel = new JPanel();
			mPanel.setLayout(new GridBagLayout());
			mPanel.setOpaque(false);
			this.setLayout(new GridBagLayout());
			add(mPanel,getCnt(new GUIParam(0, 0)));
			break;
		case CT_SCROLL:
			mPanel = new JPanel();
			mPanel.setLayout(new GridBagLayout());
			mPanel.setOpaque(false);
			JScrollPane scoll = new JScrollPane(mPanel);
			scoll.setFocusable(true);
			scoll.setAutoscrolls(true);
			this.setLayout(new GridBagLayout());
			add(scoll,getCnt(new GUIParam(0, 0)));
			break;
		case CT_TAB:
			this.setLayout(new GridBagLayout());
			mTab = new JTabbedPane();
			mTab.addMouseListener(this);
			mTab.addComponentListener(this);
			mTab.setOpaque(false);
			add(mTab, getCnt(new GUIParam(0, 0)));
			break;
		case CT_SPLITER:
			this.setLayout(new GridBagLayout());
			mSpliter=new JSplitPane();
			mSpliter.setOpaque(false);
			mSpliter.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			mSpliter.setOneTouchExpandable(true);
			mSpliter.setLeftComponent(null);
			mSpliter.setRightComponent(null);
			mSpliter.addMouseListener(this);
			mSpliter.addComponentListener(this);
			add(mSpliter, getCnt(new GUIParam(0, 0)));
		}
	}
	
	public void removeAll(){
		for(int cnt_idx=0;cnt_idx<count();cnt_idx++) {
			GUIContainer cnt=get(cnt_idx);
			if (cnt!=null)
				cnt.setVisible(false);
		}
		switch (mType) {
		case CT_TAB:
			mTab.removeMouseListener(this);
			mTab.removeComponentListener(this);
			mTab=null;
			break;
		case CT_SPLITER:
			mSpliter.removeMouseListener(this);
			mSpliter.removeComponentListener(this);
			mSpliter=null;
		}
		removeFocusListener(this);
		removeMouseListener(this);
		removeComponentListener(this);
		super.removeAll();
		creator();
	}

	public void setRedraw(Redrawing redrawing) {
		mRedrawing=redrawing;
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

	protected GridBagConstraints getCnt(GUIParam param) {
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
		SContainer result = new SContainer(type);
		if (SwingImage.class.isInstance(icon))
			mTab.addTab(name,(ImageIcon)icon.getData(),result);
		else
			mTab.addTab(name,result);
		result.setName(this.getName()+"|"+name+"|");
		return result;
	}
	
	public GUIContainer get(int index){
		SContainer result = null;
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
				if (SContainer.class.isInstance(cmp)) {
					if (index==num_cmp)
						result = (SContainer)cmp;
					num_cmp++;
				}
			}
		}
		return result;
	}
	
	public GUIContainer getSplite(ContainerType type,boolean right) {
		if (mSpliter == null)
			return null;
		SContainer result = null;
		Component cmp;
		if (right)
			cmp=mSpliter.getRightComponent();
		else
			cmp=mSpliter.getLeftComponent();
		if (cmp==null) {
			result = new SContainer(type);
			if (right)
				mSpliter.setRightComponent(result);
			else
				mSpliter.setLeftComponent(result);
		}
		else if (SContainer.class.isInstance(cmp))
			result=(SContainer)cmp;
		return result;
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
		SButton result = new SButton();
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUICheckBox createCheckBox(GUIParam param) {
		if (mPanel == null)
			return null;
		SCheckBox result = new SCheckBox();
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUICheckList createCheckList(GUIParam param) {
		if (mPanel == null)
			return null;
		SCheckList result = new SCheckList();
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUICombo createCombo(GUIParam param) {
		if (mPanel == null)
			return null;
		SCombo result = new SCombo();
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUIContainer createContainer(ContainerType type, GUIParam param) {
		if (mPanel == null)
			return null;
		SContainer result = new SContainer(type);
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		result.setName(this.getName()+"["+param.getX()+","+param.getY()+"]");
		return result;
	}

	public GUIEdit createEdit(GUIParam param) {
		if (mPanel == null)
			return null;
		SEdit result = new SEdit();
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUIGrid createGrid(GUIParam param) {
		if (mPanel == null)
			return null;
		SGrid result = new SGrid();
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUILabel createLabel(GUIParam param) {
		if (mPanel == null)
			return null;
		SLabel result = new SLabel();
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUIMemo createMemo(GUIParam param) {
		if (mPanel == null)
			return null;
		SMemo result = new SMemo();
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUISpinEdit createSpinEdit(GUIParam param) {
		if (mPanel == null)
			return null;
		SSpinEdit result = new SSpinEdit();
		mPanel.add(result, getCnt(param));
		changePreferenceSize(param, result);
		return result;
	}

	public GUIHyperText createHyperText(GUIParam param) {
		if (mPanel == null)
			return null;
		SHyperText result = new SHyperText();
		mPanel.add(result, getCnt(param));
		result.setBackground(mPanel.getBackground());
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
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		setPreferredSize(new Dimension(width, height));
	}

	public void mousePressed(MouseEvent e) {
		if (e.getClickCount()==1) {
			for(GUIActionListener l:mActionListener)
				l.actionPerformed();
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			if (mMouseClickAction!=null)
				mMouseClickAction.actionPerformed();
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (mIsActiveMouse) {
			if (!Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals(getCursor())) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
	}

	public void mouseExited(MouseEvent e) {
		if (mIsActiveMouse) {
			if (Cursor.getPredefinedCursor(Cursor.HAND_CURSOR).equals(getCursor())) {
				setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public void mouseReleased(MouseEvent e) {	}

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

	private boolean mIsActiveMouse=false;
	public void setActiveMouseAction(boolean isActive) {
		mIsActiveMouse=isActive;		
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

}
