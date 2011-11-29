package org.lucterios.android.widget;

import java.util.ArrayList;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GridInterface;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class WGrid extends ScrollView implements GUIGrid,OnFocusChangeListener,OnLongClickListener {

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	private ArrayList<GUISelectListener> mSelectListener=new ArrayList<GUISelectListener>(); 
	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>(); 
	
	public void clearFocusListener(){
		mFocusListener.clear();
	}

	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void addSelectListener(GUISelectListener l){
		mSelectListener.add(l);
	}

	public void addActionListener(GUIActionListener l){
		mActionListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void removeSelectListener(GUISelectListener l) {
		mSelectListener.remove(l);
	}	
	
	public void removeActionListener(GUIActionListener l){
		mActionListener.remove(l);
	}		
	
	public class CellView extends TextView implements OnClickListener {
		
		private int m_row;
		private WGrid m_grid;

		public CellView(WGrid grid,int row) {
			super(grid.getContext());
			m_row=row;
			m_grid=grid;
			setSingleLine();
			setTextColor(Color.BLACK);
			setGravity(Gravity.VERTICAL_GRAVITY_MASK);
			setOnClickListener(this);
			MarginLayoutParams marg=new MarginLayoutParams(MarginLayoutParams.FILL_PARENT,MarginLayoutParams.FILL_PARENT);
			marg.setMargins(1, 1, 1,1);
			setLayoutParams(marg);
		}

		public void onClick(View v) {
			if (m_row>=0)
				m_grid.setSelectRows(new int[]{m_row});
			else
				m_grid.setSelectRows(new int[0]);
		}
		
	}

	private TableLayout m_mainLayout; 
	
	private GridInterface m_GridData=null;
	
	private int[] m_selectRows=new int[0];
	
	public WGrid(Context context, WContainer owner) {
		super(context);
	    setBackgroundColor(Color.WHITE);
		setPadding(0, 0, 0, 0);
		m_mainLayout=new TableLayout(context);
		m_mainLayout.setStretchAllColumns(true);
		m_mainLayout.setBackgroundColor(Color.BLACK);
		m_mainLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		addView(m_mainLayout);
		m_mainLayout.setOnFocusChangeListener(this);
		m_mainLayout.setOnLongClickListener(this);
	}

	public boolean onLongClick(View v) {
		for(GUIActionListener l:mActionListener)
			l.actionPerformed();
		return (mActionListener.size()>0);
	}
		
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus && GUIComponent.class.isInstance(v))
			for(GUIFocusListener l:mFocusListener)
				l.focusLost(null,(GUIComponent)v);		
	}

	public void setGridInterface(GridInterface gridData) {
		m_GridData = gridData;
	}

	public int[] getSelectRows() {
		return m_selectRows;
	}

	public void setSelectRows(int[] selectRows) {
		if (selectRows!=null) {
			m_selectRows = selectRows;
			refreshColor();
			for(GUISelectListener l:mSelectListener) {
		    	l.selectionChanged();
			}
		}
	}
	
	public void refreshData(){
		m_mainLayout.removeAllViews();
		if (m_GridData!=null) {
			TableRow header=new TableRow(getContext());
			for(int col=0;col<m_GridData.getColumnCount();col++){
				CellView text_view = new CellView(this,-1);
				text_view.setText(m_GridData.getColumnName(col));
				text_view.setTypeface(Typeface.DEFAULT_BOLD);
				text_view.setGravity(Gravity.CENTER);
				text_view.setBackgroundColor(Color.GRAY);
				header.addView(text_view);
			}
			m_mainLayout.addView(header);			
			for(int row=0;row<m_GridData.getRowCount();row++){
				TableRow rowdata=new TableRow(getContext());
				for(int col=0;col<m_GridData.getColumnCount();col++){
					CellView text_view = new CellView(this,row);
					text_view.setText(m_GridData.getValueAt(row, col).toString());
					rowdata.addView(text_view);
				}
				m_mainLayout.addView(rowdata);			
			}
		}
		setSelectRows(new int[0]);
	}

	protected void refreshColor(){
		for(int row=1;row<m_mainLayout.getChildCount();row++) { // row=0 : header
			boolean is_selected=false;
			for(int sel_id=0;!is_selected && (sel_id<m_selectRows.length);sel_id++) {
				is_selected=(m_selectRows[sel_id]==(row-1));
			}
			int color;
			if (is_selected) {
				color=Color.rgb(255,165,0);
			}
			else {
				if (((row-1) % 2) != 0)
					color=Color.WHITE;
				else
					color=Color.rgb(220, 220, 220);		
			}
			TableRow rowdata=(TableRow)m_mainLayout.getChildAt(row);
			for(int col=0;col<rowdata.getChildCount();col++){
				CellView text_view=(CellView)rowdata.getChildAt(col);
				text_view.setBackgroundColor(color);
			}
		}
	}

	public int getSelectedRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int[] getSelectedRows() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMultiSelection(boolean isMultiSelection) {
		// TODO Auto-generated method stub
		
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	public void setRowSelection(int row) {
		// TODO Auto-generated method stub
		
	}

	public void clearTable() {
		// TODO Auto-generated method stub
		
	}

	public int getBackgroundColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public GUIComponent getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	public void repaint() {
		// TODO Auto-generated method stub
		
	}

	public void requestFocusGUI() {
		// TODO Auto-generated method stub
		
	}

	public void setActiveMouseAction(boolean isActive) {
		// TODO Auto-generated method stub
		
	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	public void setToolTipText(String toolTip) {
		// TODO Auto-generated method stub
		
	}

	public void setNbClick(int mNbClick) { }
}
