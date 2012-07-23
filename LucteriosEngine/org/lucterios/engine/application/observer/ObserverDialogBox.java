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

import org.lucterios.engine.gui.GraphicTool;
import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.utils.GCTools;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.Tools;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUIImage;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class ObserverDialogBox extends ObserverAbstract implements Runnable, DialogVisitor {
	public String mText;
	public int mType;
	public SimpleParsing mActions;

	public String getObserverName() {
		return "Core.DialogBox";
	}

	public byte getType() {
		return ObserverConstant.TYPE_DIALOG;
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		SimpleParsing xml_text = mContent.getFirstSubTag("TEXT");
		mText = xml_text.getText();
		mType = xml_text.getAttributeInt("type", 0);
		mActions = mContent.getFirstSubTag("ACTIONS");
	}

	GUIContainer mPnlBtn;
	GUIContainer mGUIContainer = null;

	public void show(String aTitle) {
		super.show(aTitle);
		if ((getGUIDialog() != null) && (mGUIContainer != null)) {
			if (aTitle != null)
				getGUIDialog().setTextTitle(getTitle());
			mGUIContainer.removeAll();

			GUIImage lbl_img=mGUIContainer.createImage(new GUIParam(0,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
			
			GUIParam param=new GUIParam(1,0);
			param.setPad(20);
			GUIHyperText lbl_message=mGUIContainer.createHyperText(param);
			
			mPnlBtn = mGUIContainer.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,1,2,1));

			GUIButton btn=GraphicTool.fillPanelByButton(mPnlBtn, this, Singletons.Factory(),mActions, true);
			getGUIDialog().setDefaultButton(btn);

			switch (mType) {
			case 2: {
				lbl_img.setImage(Singletons.Transport().getIcon("images/confirm.png", 0));
				break;
			}
			case 3: {
				lbl_img.setImage(Singletons.Transport().getIcon("images/warning.png", 0));
				break;
			}
			case 4: {
				lbl_img.setImage(Singletons.Transport().getIcon("images/error.png", 0));
				break;
			}
			default: {
				lbl_img.setImage(Singletons.Transport().getIcon("images/info.png", 0));
				break;
			}
			}
			mText = Tools.convertLuctoriosFormatToHtml(mText);
			lbl_message.setTextString(mText);
		}
		Singletons.getWindowGenerator().invokeLater(this);
	}

	public void show(String aTitle, GUIForm new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {
		mTitle=aTitle;
		mGUIDialog = aGUI;
		getGUIDialog().setDialogVisitor(this);
		getGUIDialog().setVisible(true);
	}

	public void execute(GUIDialog dialog) {
		mGUIContainer = dialog.getGUIContainer();
		getGUIDialog().setNotifyFrameClose(this);
		getGUIDialog().setResizable(false);
		show(mTitle);
	}
	
	public void run() {
		if (getGUIDialog() != null)
			getGUIDialog().toFront();
		setActive(true);
		mGUIContainer.setVisible(true);
	}

	public MapContext getParameters(String aActionId, int aSelect,
			boolean aCheckNull) {
		return mContext;
	}

	public void setActive(boolean aIsActive) {
		getGUIDialog().setActive(aIsActive);
		mPnlBtn.setEnabled(aIsActive);
		if (getParent() != null) {
			getParent().setActive(aIsActive);
		}
	}

	boolean closed = false;

	public void close(boolean aMustRefreshParent) {
		if (!closed) {
			closed = true;
			if (mGUIContainer != null) {
				mGUIContainer.removeAll();
				mGUIContainer = null;
			}
			if (getGUIDialog() != null)
				getGUIDialog().dispose();
			super.close(aMustRefreshParent);
			GCTools.postOrderGC();
		}
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}

	public void closing() {	}

}
