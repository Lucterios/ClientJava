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

package org.lucterios.engine.gui;

import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.Tools;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUIImage;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class ActionActivator implements GUIActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GUIActionListener mActionListener = null;
	private String mText;
	private String mDescription;
	private AbstractImage mIcon=null;
	
	private GUIContainer mContainer=null;
	
	private String mToolTip="";

	public GUIContainer getContainer() {
		return mContainer;
	}

	public ActionActivator(GUIActionListener aActionListener, String aText,
			String aDescription, String aToolTip, AbstractImage image) {
		super();
		mActionListener = aActionListener;
		mText = aText;
		mDescription = Tools.convertLuctoriosFormatToHtml(aDescription);
		mIcon = image.resizeIcon(32, true);
		mToolTip=aToolTip;
	}

	public ActionActivator(String aText, String aDescription,
			AbstractImage image) {
		super();
		mActionListener = null;
		mText = aText;
		mDescription = Tools.convertLuctoriosFormatToHtml(aDescription);
		mIcon = image.resizeIcon(64, true);
		mToolTip="";
	}

	public String toString() {
		return mText;
	}

	public GUIActionListener getActionListener() {
		return mActionListener;
	}

	GUIHyperText mtext = null;
	GUIHyperText mdescription = null;
	GUIImage mImage = null;

	public void initialize(GUIContainer container) {
		mContainer=container;
		addMouseListenerByComponent(mContainer);
		setToolTipNotEmpty(mToolTip, mContainer);

		if (mActionListener == null)
			mContainer.setBorder(1, 0, 1, 0, 0x808080);

		GUIParam param=new GUIParam(0,0,1,2);
		param.setPad(5);
		param.setFill(FillMode.FM_BOTH);
		param.setReSize(ReSizeMode.RSM_NONE);
		mImage = mContainer.createImage(param);
		mImage.setImage(mIcon);
		setToolTipNotEmpty(mToolTip, mImage);
		addMouseListenerByComponent(mImage);

		param=new GUIParam(1,0);
		if (mDescription.length() == 0)
			param.setH(2);
		param.setFill(FillMode.FM_HORIZONTAL);
		param.setReSize(ReSizeMode.RSM_HORIZONTAL);
		mtext = mContainer.createHyperText(param);
		setToolTipNotEmpty(mToolTip, mtext);
		if (mActionListener != null)
			mtext.setTextString("<h3><b>" + mText + "<b></h3>");
		else
			mtext.setTextString("<center><h2><b>" + mText + "<b></h2></center>");
		addMouseListenerByComponent(mtext);

		if (mDescription.length() != 0) {
			param=new GUIParam(1,1,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH);
			mdescription=mContainer.createHyperText(param);
			if (mActionListener != null)
				mdescription.setTextString("<h5>" + mDescription + "</h5>");
			else
				mdescription.setTextString("<center><h3>" + mDescription
						+ "</h3></center>");
			setToolTipNotEmpty(mToolTip, mdescription);
			addMouseListenerByComponent(mdescription);
		}
	}

	private void addMouseListenerByComponent(GUIComponent comp) {
		if (mActionListener != null) {
			comp.setActiveMouseAction(true);
			comp.addActionListener(this);
			comp.setNbClick(1);
		}
	}

	private void setToolTipNotEmpty(String aToolTip, GUIComponent comp) {
		if (aToolTip.length() > 0) {
			aToolTip = Tools.replace(aToolTip, "{[newline}]", "\n");
			comp.setToolTipText(aToolTip);
		}
	}

	long mLastTimeActionRunning = 0;

	public void actionPerformed() {
		long new_time = System.currentTimeMillis();
		if (((mLastTimeActionRunning == 0) || ((new_time - mLastTimeActionRunning) > 2000))
				&& (mActionListener != null)) {
			mLastTimeActionRunning = new_time;
			mActionListener.actionPerformed();
		}
	}

}
