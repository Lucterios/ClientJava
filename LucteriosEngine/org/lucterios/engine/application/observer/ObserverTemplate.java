/*
 *    This file is part of Lucterios.
 *
 *    Lucterios is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    Lucterios is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Lucterios; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
 */

package org.lucterios.engine.application.observer;

import java.lang.ref.WeakReference;

import org.lucterios.Print.MainPrintPanel;
import org.lucterios.engine.gui.GraphicTool;
import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.utils.GCTools;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class ObserverTemplate extends ObserverAbstract implements DialogVisitor {
	SimpleParsing data_elements = null;
	SimpleParsing style_elements = null;

	protected int mModelId = 0;
	protected String mTitle = "";

	private GUIContainer pnl_Cst;
	private GUIContainer pnl_Btn;
	private GUIEdit txt_Title;
	private GUILabel lbl_Title;

	private GUIContainer scrl_StyleModel;
	private MainPrintPanel PrintPanel;

	public boolean isValidate;

	public String getObserverName() {
		return "Core.Template";
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		data_elements = null;
		style_elements = null;
		mTitle = "";
		SimpleParsing template = mContent.getFirstSubTag("TEMPLATE");
		if (template != null) {
			mModelId = template.getAttributeInt("model", 0);
			mTitle = template.getAttribute("title");
			data_elements = template.getFirstSubTag("XMLOBJECT");
			style_elements = template.getFirstSubTag("XSLTEXT");
		}
	}

	public MapContext getParameters(String aActionId, int aSelect,
			boolean aCheckNull) {
		MapContext attrib = mContext;
		attrib.put("model_id", new Integer(mModelId));
		attrib.put("title", getModelTitle());
		attrib.put("model", getStyle());
		return attrib;
	}

	public byte getType() {
		return ObserverConstant.TYPE_DIALOG;
	}

	public void show(String aTitle) {
		super.show(aTitle);
		String data_str = data_elements.getText();
		String style_str = style_elements.getText();
		setValue(data_str, style_str, mTitle);
	}

	public void show(String aTitle, GUIForm new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {
		mTitle=aTitle;
		mGUIDialog = new WeakReference<GUIDialog>(aGUI);
		getGUIDialog().setDialogVisitor(this);
		getGUIDialog().setVisible(true);
	}

	public void execute(GUIDialog dialog) {
		dialog.setTextTitle(mTitle);
		GUIParam param;

		param=new GUIParam(0,0);
		param.setFill(FillMode.FM_HORIZONTAL);
		param.setReSize(ReSizeMode.RSM_HORIZONTAL);
		pnl_Cst = dialog.getContainer().createContainer(ContainerType.CT_NORMAL,param);

		lbl_Title = pnl_Cst.createLabel(new GUIParam(0,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
		lbl_Title.setTextString("Titre du model");

		param=new GUIParam(1,0);
		param.setPrefSizeY(19);
		param.setPrefSizeX(250);
		txt_Title = pnl_Cst.createEdit(param);

		param=new GUIParam(0,1);
		scrl_StyleModel = dialog.getContainer().createContainer(ContainerType.CT_SCROLL, param);
		PrintPanel = new MainPrintPanel(scrl_StyleModel);

		param=new GUIParam(0,2);
		param.setFill(FillMode.FM_HORIZONTAL);
		param.setReSize(ReSizeMode.RSM_HORIZONTAL);
		pnl_Btn = dialog.getContainer().createContainer(ContainerType.CT_NORMAL, param);
		SimpleParsing act = new SimpleParsing();
		act.parse("<ACTIONS>"
						+ "<ACTION extension='"
						+ getSourceExtension()
						+ "' action='"
						+ getSourceAction()
						+ "' close='1' modal='0' icon='images/ok.png'><![CDATA[_Ok]]></ACTION>"
						+ "<ACTION close='1' modal='0' icon='images/cancel.png'><![CDATA[_Annuler]]></ACTION>"
						+ "</ACTIONS>");
		GUIButton btn=GraphicTool.fillPanelByButton(pnl_Btn, this, Singletons.Factory(),act, true);
		getGUIDialog().setDefaultButton(btn);
		show(mTitle);
	}

	private String mDataXML = "";

	public void setValue(String aDataXML, String aStyleModel, String aTitle){
		mDataXML = aDataXML;
		try {
			PrintPanel.getManager().getPage().Load(aStyleModel);
			PrintPanel.getManager().getPage().setXML(mDataXML);
		} catch (Exception se) {
			se.printStackTrace();
		}
		PrintPanel.getManager().refresh();
		txt_Title.setTextString(aTitle);
		isValidate = false;
	}

	public String getStyle() {
		return PrintPanel.getManager().getPage().Save();
	}

	public String getModelTitle() {
		return txt_Title.getTextString();
	}

	boolean closed = false;

	public void close(boolean aMustRefreshParent) {
		if (!closed) {
			closed = true;
			super.close(aMustRefreshParent);
			if (getGUIDialog() != null)
				getGUIDialog().dispose();
			GCTools.postOrderGC();
		}
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}

	public void closing() { }
}
