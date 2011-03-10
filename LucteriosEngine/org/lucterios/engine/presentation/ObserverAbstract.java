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

package org.lucterios.engine.presentation;

import java.lang.ref.WeakReference;
import java.util.Set;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionConstantes;
import org.lucterios.gui.IDialog;
import org.lucterios.gui.IForm;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public abstract class ObserverAbstract implements Observer {
	protected Action mCloseAction = null;
	
	public static int ObserverCount=0;
	
	public ObserverAbstract() {
		super();
		ObserverCount++;
	}
	
	protected void finalize() throws Throwable{
		ObserverCount--;
		super.finalize();
	}

	protected WeakReference<Observer> mParent = null;

	public void setParent(Observer aParent) {
		mParent = new WeakReference<Observer>(aParent);
	}

	public Observer getParent() {
		if (mParent!=null)
			return mParent.get();
		else
			return null;
	}

	protected String mExtension = "";
	protected String mAction = "";

	public void setSource(String aExtension, String aAction) {
		mExtension = aExtension;
		mAction = aAction;
	}

	public String toString() {
		String resValue=getClass().getName()+"["+mExtension+"->"+mAction+"]";
		String resParam="";
		if (mContext!=null) {
			Set<String> keys=mContext.keySet();
			for(String key:keys){
				resParam+="'"+key+"'='"+mContext.get(key)+"',";
			}
		}
		return resValue+"("+resParam+")";
	}
	
	public String getSourceExtension() {
		return mExtension;
	}

	public String getSourceAction() {
		return mAction;
	}

	protected SimpleParsing mContent;

	public void setContent(SimpleParsing aContent) {
		mContent = aContent;
        if (mContent!=null) {
            SimpleParsing xml_item_close = mContent.getFirstSubTag("CLOSE_ACTION");
            if (xml_item_close != null) {
                SimpleParsing[] xml_items = xml_item_close.getSubTag("ACTION");
                if (xml_items != null) {
                    for (int action_idx = 0; (mCloseAction == null)
                            && (action_idx < xml_items.length); action_idx++) {
                        SimpleParsing xml_item = xml_items[action_idx];
                        mCloseAction = Singletons.CreateAction();
                        mCloseAction.initialize(this, Singletons.Factory(),
                                xml_item);
                        mCloseAction.setCheckNull(false);
                        mCloseAction.setClose(true);
                        mCloseAction.setUsedContext(true);
                    }
                }
            }
        }
	}

	public SimpleParsing getContent() {
		return mContent;
	}

	public String getContentText(){
		String content = "";
        if (mContent!=null) {
            content = mContent.getSubContent();
            int pos = content.indexOf("<CONTEXT/>");
            if (pos == -1)
                pos = content.indexOf("</CONTEXT>");
            if (pos != -1)
                content = content.substring(pos + 10);
        }
		return content;
	}
	
	protected String mCaption = "";

	public void setCaption(String aCaption) {
		mCaption = aCaption;
		mTitle = aCaption;
	}

	protected MapContext mContext = null;

	public void setContext(MapContext aContext) {
		mContext = aContext;
	}

	public MapContext getContext() {
		return mContext;
	}

	protected String mTitle = "";

	public String getTitle() {
		return mTitle;
	}

	public void show(String aTitle) throws LucteriosException {
		if (!"".equals( aTitle ) && "".equals( mCaption ))
			mTitle = aTitle;
	}

	protected WeakReference<IForm> mGUIFrame = null;
	protected WeakReference<IDialog> mGUIDialog = null;

	public IForm getGUIFrame() {
        if (mGUIFrame!=null)
            return mGUIFrame.get();
        else
            return null;
	}

	public IDialog getGUIDialog() {
        if (mGUIDialog!=null)
            return mGUIDialog.get();
        else
            return null;
	}

	public void setActive(boolean aIsActive) {
		if (getParent() != null) {
			getParent().setActive(aIsActive);
		}
	}

	public void eventForEnabled(boolean aBefore) {

	}

	boolean closed = false;

	public void close(boolean aMustRefreshParent) {
		if (!closed) {
			closed = true;
			if (mCloseAction != null)
				mCloseAction.actionPerformed();
			mCloseAction = null;
			try {
				Observer parent=getParent();
				if (aMustRefreshParent && (parent != null))
					parent.refresh();
				parent=null;
			} catch (LucteriosException e) {
				e.printStackTrace();
				//ExceptionDlg.throwException(e);
			}
		}
	}

	public MapContext getParameters(String aActionId, int aSelect, boolean aCheckNull) throws LucteriosException {
		return new MapContext();
	}

	public void refresh() throws LucteriosException {
		Action refresh = Singletons.CreateAction();
		refresh.initialize(this, Singletons.Factory(), getTitle(),
				getSourceExtension(), getSourceAction());
		refresh.setFormType(ActionConstantes.FORM_REFRESH);
		refresh.setClose(false);
		refresh.setUsedContext(true);
		refresh.actionPerformed();
		refresh=null;
	}
}
