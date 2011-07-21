package org.lucterios.gui;

public interface GUIContainer {
	
	public enum ContainerType {CT_NORMAL,CT_SCROLL,CT_TAB}

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
	public void calculBtnSize(GUIButton[] btns);
	public void setVisible(boolean isVisible);
	public void setSize(int width, int height);
	public int getSizeX();
	public int getSizeY();
}
