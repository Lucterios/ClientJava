package org.lucterios.gui;

import org.lucterios.ui.GUIActionListener;

public interface GUIContainer extends GUIComponent {
	
	public enum ContainerType {CT_NORMAL,CT_SCROLL,CT_TAB}
	
	public interface Redrawing  {
		public void paint(GUIGraphic g);
	}

	public void invokeLater(Runnable runnable);
	public void setRedraw(Redrawing redrawing);
	public void addActionListener(GUIActionListener l);
	public void removeActionListener(GUIActionListener l);
	
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

	public GUIContainer addTab(ContainerType type,String name);
	public ContainerType getType();
	public int count();
	public void removeAll();
	public void calculBtnSize(GUIButton[] btns);
	public void setSize(int width, int height);
	public int getSizeX();
	public int getSizeY();

	public void setToolTipText(String string);
	public void repaint();

}
