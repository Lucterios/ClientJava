package org.lucterios.gui;

public interface GUIContainer {
	
	public enum FillMode {FM_NONE,FM_BOTH,FM_HORIZONTAL,FM_VERTICAL}
	public enum ReSizeMode {RSM_NONE,RSM_BOTH,RSM_HORIZONTAL,RSM_VERTICAL}
	public enum ContainerType {CT_NORMAL,CT_SCROLL,CT_TAB}

	public GUIContainer createContainer(ContainerType type,int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);	
	public GUIButton createButton(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);
	public GUICheckList createCheckList(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);
	public GUIEdit createEdit(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);
	public GUIGrid createGrid(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);
	public GUIMemo createMemo(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);
	public GUICheckBox createCheckBox(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);
	public GUICombo createCombo(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);
	public GUILabel createLabel(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);
	public GUISpinEdit createSpinEdit(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill);

	public GUIContainer addTab(ContainerType type,String name);
	public ContainerType getType();
	public void calculBtnSize(GUIButton[] btns);
}
