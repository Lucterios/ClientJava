package org.lucterios.gui;

import org.lucterios.ui.GUIActionListener;

public interface GUIContainer extends GUIComponent {
	
	public enum ContainerType {CT_NORMAL,CT_SCROLL,CT_TAB,CT_SPLITER}
	
	public interface Redrawing  {
		public void paint(GUIGraphic g);
	}

	public void invokeLater(Runnable runnable);
	public void setRedraw(Redrawing redrawing);
	public void setMouseClickAction(GUIActionListener l);
	public void setResizeAction(GUIActionListener l);
	public void removeChangeListener(GUIActionListener l);
	public void addChangeListener(GUIActionListener l);
	public Object getObject();
	public void setObject(Object obj);
	
	public GUIContainer createContainer(ContainerType type,GUIParam param);	
	public GUIButton createButton(GUIParam param);
	public GUICheckList createCheckList(GUIParam param);
	public GUIEdit createEdit(GUIParam param);
	public GUIGrid createGrid(GUIParam param);
	public GUIMemo createMemo(GUIParam param);
	public GUICheckBox createCheckBox(GUIParam param);
	public GUICombo createCombo(GUIParam param);
	public GUILabel createLabel(GUIParam param);
	public GUISpinEdit createSpinEdit(GUIParam param);
	public GUIHyperText createHyperText(GUIParam param);

	public GUIContainer addTab(ContainerType type,String name,AbstractImage icon);
	public GUIContainer getSplite(ContainerType type,boolean right);
	public ContainerType getType();
	public int count();
	public void removeAll();
	public void remove(GUIComponent cont);
	public void remove(int tbCmp);
	public GUIComponent get(int index);
	public void calculBtnSize(GUIButton[] btns);
	public void setSize(int width, int height);
	public int getSizeX();
	public int getSizeY();
	public void setBorder(int top, int left, int bottom, int rigth, int color);

	public void setDividerLocation(int location);
	public int getDividerLocation();
	
	public void setToolTipText(String string);
	public void repaint();
	public void setMinimumScroll();
	public void setMaximumScroll();
	public void removeSplite(boolean right);
	public int getSelectedIndex();
	public void setSelectedIndex(int tabActif);
	public int getTag();
	public void setTag(int tag);
	public void requestFocus();
	public void setActive(boolean aActive);	
}
