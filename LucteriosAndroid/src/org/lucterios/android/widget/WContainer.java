package org.lucterios.android.widget;

import java.util.ArrayList;

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
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TabHost.TabSpec;

public class WContainer extends TableLayout implements GUIContainer {

	private GUIComponent mOwner;
	
	private TableLayout mPanel=null;
	private TabHost mTab=null;
	
	private ContainerType mType;
	private String mName;
	public WContainer(Context context,ContainerType type,GUIComponent aOwner){
		super(context);
		mType=type;
		mOwner=aOwner;
		creator();
	}

	private void creator() {
		switch(mType) {
			case CT_NORMAL:
				mPanel=this;
				mPanel.setBaselineAligned(false);
				break;
			case CT_SCROLL:
				ScrollView scroll=new ScrollView(getContext());
				this.setBaselineAligned(false);
				addViewEx(this,scroll,new GUIParam(0,0));
				mPanel=new TableLayout(getContext());
				mPanel.setBaselineAligned(false);
				scroll.addView(mPanel,new ScrollView.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
				break;
			case CT_TAB:
				mTab = new TabHost(getContext());
		 
		        // the tabhost needs a tabwidget, that is a container for the visible tabs
		        TabWidget tabWidget = new TabWidget(getContext());
		        tabWidget.setId(android.R.id.tabs);
		        TabHost.LayoutParams params1=new TabHost.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		        mTab.addView(tabWidget, params1); 
		 
		        // the tabhost needs a frame layout for the views associated with each visible tab
		        FrameLayout frameLayout = new FrameLayout(getContext());
		        frameLayout.setId(android.R.id.tabcontent);
		        frameLayout.setPadding(0, 65, 0, 0);
		        TabHost.LayoutParams params2=new TabHost.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		        mTab.addView(frameLayout,params2); 
		 
		        // setup must be called if you are not initialising the tabhost from XML
		        mTab.setup(); 							
				addViewEx(this,mTab,new GUIParam(0,0));
				break;
			case CT_SPLITER:
				break;
		}
	}
	
	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
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
	
	public void setMouseClickAction(GUIActionListener mMouseClickAction) { }

	public void setResizeAction(GUIActionListener mResizeAction) { }
	
	public void addActionListener(GUIActionListener l) { }

	public void removeActionListener(GUIActionListener l) {	}

	private Object mObject=null;
	private Object mTag=0;

	private boolean mActive;

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
	
	
	public ContainerType getType(){
		return mType;
	}

	private void addView(View view,GUIParam param){
		addViewEx(mPanel,view,param);
	}
	
	private void addViewEx(TableLayout target,View view,GUIParam param){
		while (target.getChildCount()<(param.getY()+1)) {
			TableRow new_row=new TableRow(getContext());
			new_row.setBaselineAligned(false);
			LayoutParams param_row=new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
			target.addView(new_row,param_row);
		}
		TableRow row=(TableRow)target.getChildAt(param.getY());
		while (row.getChildCount()<param.getX()) {
			row.addView(new View(getContext()),new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		}
		if (row.getChildCount()>param.getX())
			row.removeView(row.getChildAt(param.getX()));
		row.addView(view,param.getX(),getParam(param));
	}
	
	private TableRow.LayoutParams getParam(GUIParam param) {
		TableRow.LayoutParams params=new TableRow.LayoutParams();
		params.span=param.getH();
		switch (param.getFill()) {
			case FM_NONE:
				params.height=LayoutParams.WRAP_CONTENT;
				params.weight=LayoutParams.WRAP_CONTENT;
				break;
			case FM_HORIZONTAL:
				params.height=LayoutParams.WRAP_CONTENT;
				params.weight=LayoutParams.FILL_PARENT;
				break;
			case FM_VERTICAL:
				params.height=LayoutParams.FILL_PARENT;
				params.weight=LayoutParams.WRAP_CONTENT;
				break;
			case FM_BOTH:
				params.height=LayoutParams.FILL_PARENT;
				params.weight=LayoutParams.FILL_PARENT;
				break;
		}
		return params;
	}

	private class WTabContentFactory implements TabHost.TabContentFactory{
		private WContainer mContainer;
		public WTabContentFactory(WContainer container){
			super();
			mContainer=container;
		}
        public View createTabContent(String tag)
        {
        	return mContainer;
        }
	}

	public GUIContainer addTab(ContainerType type, String name,AbstractImage icon) {
		WContainer result=new WContainer(getContext(),type,this);
		TabSpec ts1 = mTab.newTabSpec("TAB_"+name);
        ts1.setIndicator(name);
        ts1.setContent(new WTabContentFactory(result));
        mTab.addTab(ts1);
		return result;
	}

	public GUIContainer createContainer(ContainerType type, GUIParam param) {
		if (mPanel==null) return null;
		WContainer result=new WContainer(getContext(),type,this);
		addView(result,param);
		return result;
	}
	
	public GUIButton createButton(GUIParam param) {
		if (mPanel==null) return null;
		WButton result=new WButton(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUICheckBox createCheckBox(GUIParam param) {
		if (mPanel==null) return null;
		WCheckBox result=new WCheckBox(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUICheckList createCheckList(GUIParam param) {
		if (mPanel==null) return null;
		WCheckList result=new WCheckList(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUICombo createCombo(GUIParam param) {
		if (mPanel==null) return null;
		WCombo result=new WCombo(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUIEdit createEdit(GUIParam param) {
		if (mPanel==null) return null;
		WEdit result=new WEdit(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUIGrid createGrid(GUIParam param) {
		if (mPanel==null) return null;
		WGrid result=new WGrid(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUILabel createLabel(GUIParam param) {
		if (mPanel==null) return null;
		WLabel result=new WLabel(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUIMemo createMemo(GUIParam param) {
		if (mPanel==null) return null;
		WMemo result=new WMemo(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUISpinEdit createSpinEdit(GUIParam param) {
		if (mPanel==null) return null;
		WSpinEdit result=new WSpinEdit(getContext(),this);
		addView(result,param);
		return result;
	}
	
	public GUITree createTree(GUIParam param) {
		if (mPanel==null) return null;
		WTree result=new WTree(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUIHyperMemo createHyperMemo(GUIParam param) {
		if (mPanel==null) return null;
		WHyperMemo result=new WHyperMemo(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUIHyperText createHyperText(GUIParam param) {
		if (mPanel==null) return null;
		WHyperText result=new WHyperText(getContext(),this);
		addView(result,param);
		return result;
	}

	public GUIImage createImage(GUIParam param) {
		if (mPanel==null) return null;
		WImage result=new WImage(getContext(),this);
		addView(result,param);
		return result;
	}
	
	public void calculBtnSize(GUIButton[] btns) {
		// TODO Auto-generated method stub
	}

	public String toString(){
		String tag="["+super.toString()+"]->";
		String result=tag+mType.name()+"\n";
		if (mPanel!=null) {
			result+=tag+" Rows="+mPanel.getChildCount()+"\n";
			for(int Ridx=0;Ridx<mPanel.getChildCount();Ridx++) {
				result+=tag+"#"+Ridx+"\n";
				TableRow row=(TableRow)mPanel.getChildAt(Ridx);
				for(int Cidx=0;Cidx<row.getChildCount();Cidx++)
					result+=row.getChildAt(Cidx)+"\n";
			}			
		}
		result+=tag+"<-"+mType.name();
		return result;
	}

	public int count() {
		if (mTab != null)
			return mTab.getChildCount();
		/*if (mSpliter!=null)
			return 2;*/
		if (mPanel != null)
			return mPanel.getChildCount();
		return 0;
	}

	public GUIComponent get(int index) {
		GUIComponent result = null;
		/*if (mSpliter!=null) {
			if (index==0)
				result = (SContainer) mSpliter.getLeftComponent();
			else if (index==1)
				result = (SContainer) mSpliter.getRightComponent();
		}
		else*/ if (mTab != null) {
			int num_cmp=0;
			for(int idx=0;idx<mTab.getChildCount() && (result==null);idx++) {
				View cmp=mTab.getChildAt(idx);
				if (WContainer.class.isInstance(cmp)) {
					if (index==num_cmp)
						result = (WContainer)cmp;
					num_cmp++;
				}
			}
		}
		else {
			int num_cmp=0;
			for(int idx=0;idx<mPanel.getChildCount() && (result==null);idx++) {
				View cmp=mPanel.getChildAt(idx);
				if (GUIComponent.class.isInstance(cmp)) {
					if (index==num_cmp)
						result = (GUIComponent)cmp;
					num_cmp++;
				}
			}
		}
		return result;
	}

	
	public int getDividerLocation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSelectedIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSizeX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSizeY() {
		// TODO Auto-generated method stub
		return 0;
	}

	public GUIContainer getSplite(ContainerType type, boolean rightOrBottom) {
		// TODO Auto-generated method stub
		return null;
	}

	public void invokeLater(Runnable runnable) {
		// TODO Auto-generated method stub
		
	}

	public void remove(GUIComponent comp) {
		if (View.class.isInstance(comp)) {
			if (mTab != null)
				mTab.removeView((View)comp);
			if (mPanel != null)
				mPanel.removeView((View)comp);
		}
	}

	public void remove(int tbCmp) {
		if (mTab != null)
			mTab.removeViewAt(tbCmp);
		if (mPanel != null)
			mPanel.removeViewAt(tbCmp);
	}

	public void removeAll() {
		super.removeAllViews();
		creator();
	}

	public void removeSplite(boolean right) {
		// TODO Auto-generated method stub
		
	}

	public void repaint() {
		// TODO Auto-generated method stub
		
	}

	public void setActive(boolean aActive) {
		mActive = aActive;
	}

	public void setBorder(int top, int left, int bottom, int rigth, int color) {
		// TODO Auto-generated method stub
		
	}

	public void setBounds(int top, int left, int bottom, int rigth) {
		// TODO Auto-generated method stub
		
	}

	public void setDividerLocation(double ratio) {
		// TODO Auto-generated method stub
		
	}

	public void setDividerLocation(int location) {
		// TODO Auto-generated method stub
		
	}

	public void setLayoutIsGrid(boolean isGrid) {
		// TODO Auto-generated method stub
		
	}

	public void setMaximumScroll() {
		// TODO Auto-generated method stub
		
	}

	public void setMaximumSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void setMinimumScroll() {
		// TODO Auto-generated method stub
		
	}

	public void setMinimumSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void setRedraw(Redrawing redrawing) {
		// TODO Auto-generated method stub
		
	}

	public void setSelectedIndex(int tabActif) {
		// TODO Auto-generated method stub
		
	}

	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void setSpliteOrientation(boolean horizontal) {
		// TODO Auto-generated method stub
		
	}

	public void setToolTipText(String string) {
		// TODO Auto-generated method stub	
	}

	public int getBackgroundColor() {
		return 0;
	}

	public String getName() {
		return mName;
	}

	public GUIComponent getOwner() {
		return mOwner;
	}

	public boolean isActive() {
		return mActive;
	}

	public boolean isVisible() {
		return super.getVisibility()==VISIBLE;
	}

	public void setActiveMouseAction(boolean isActive) {
	}

	public void setName(String name) {
		mName=name;
	}

	public void setVisible(boolean visible) {
		super.setVisibility(visible?VISIBLE:INVISIBLE);
	}
	
	public void requestFocusGUI() {
		requestFocus();
	}

	public int getSelectColor() {
		return 0x0F0F0F;
	}

}
