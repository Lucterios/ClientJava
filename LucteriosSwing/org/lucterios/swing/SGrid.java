package org.lucterios.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import org.lucterios.graphic.CheckFormRenderer;
import org.lucterios.graphic.ImageFormRenderer;
import org.lucterios.graphic.LabelFormRenderer;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GridInterface;
import org.lucterios.ui.GUIActionListener;

public class SGrid extends JScrollPane implements FocusListener,MouseListener,
		ListSelectionListener, GUIGrid {

	private static final long serialVersionUID = 1L;

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	private ArrayList<GUISelectListener> mSelectListener=new ArrayList<GUISelectListener>(); 
	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>(); 
	
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
		
    public void focusLost(java.awt.event.FocusEvent evt) 
    {
		for(GUIFocusListener l:mFocusListener)
			l.focusLost();
    }
    
	public void focusGained(FocusEvent e) { }
	
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		for(GUISelectListener l:mSelectListener)
			l.selectionChanged();
	}

	public void mouseClicked(MouseEvent e) {
		if ((e.getClickCount() == 2) && (getSelectedRowCount() > 0)) {
			for(GUIActionListener l:mActionListener)
				l.actionPerformed();
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		if (mIsActiveMouse) {
			if (!Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals(getCursor())) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
	}

	public void mouseExited(MouseEvent e) {
		if (mIsActiveMouse) {
			if (Cursor.getPredefinedCursor(Cursor.HAND_CURSOR).equals(getCursor())) {
				setCursor(Cursor.getDefaultCursor());
			}
		}
	}
		
	private class SGridModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private GridInterface mGridInterface=null;
		
		public SGridModel(){
			super();
		}
		
		public void setGridInterface(GridInterface gridInterface) {
			this.mGridInterface = gridInterface;
		}

		public GridInterface getGridInterface() {
			return this.mGridInterface;
		}
		
		public int getColumnCount() {
			if (mGridInterface!=null)
				return mGridInterface.getColumnCount();
			return 0;
		}
		
		public Class<?> getColumnClass(int columnIndex) {
			if (mGridInterface!=null)
				return mGridInterface.getColumnClass(columnIndex);
			return null;
		}

		public String getColumnName(int columnIndex) {
			if (mGridInterface!=null)
				return mGridInterface.getColumnName(columnIndex);
			return "";
		}

		public int getRowCount() {
			if (mGridInterface!=null)
				return mGridInterface.getRowCount();
			return 0;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (mGridInterface!=null)
				return mGridInterface.getValueAt(rowIndex, columnIndex);
			return null;
		}
		
	}
	
	private SGridModel mModel=null;
	private JTable cmp_tbl;

	private LabelFormRenderer mLabelForm = new LabelFormRenderer();
	private ImageFormRenderer mImageForm = new ImageFormRenderer();
	
	public SGrid(){
		super();
		cmp_tbl= new JTable();
		addFocusListener(this);
		addMouseListener(this);
		setFocusable(false);
		setColumnHeader(null);
		JTableHeader th = cmp_tbl.getTableHeader();
		Font old = th.getFont();
		th.setFont(new Font(old.getName(), old.getStyle() + Font.BOLD, old.getSize()));
		setColumnHeaderView(th);
		setFocusable(false);
		setViewportView(cmp_tbl);
		cmp_tbl.getSelectionModel().addListSelectionListener(this);
		cmp_tbl.setColumnSelectionAllowed(false);
		cmp_tbl.setRowSelectionAllowed(true);
		cmp_tbl.setFocusable(true);
		cmp_tbl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);		
		cmp_tbl.setDefaultRenderer(Boolean.class, new CheckFormRenderer());
		cmp_tbl.setDefaultRenderer(String.class, mLabelForm);
		cmp_tbl.setDefaultRenderer(Integer.class, mLabelForm);
		cmp_tbl.setDefaultRenderer(Double.class, mLabelForm);
		cmp_tbl.setDefaultRenderer(Icon.class, mImageForm);
	}

	public void setGridInterface(GridInterface gridInterface) {
		mModel=new SGridModel();
		mModel.setGridInterface(gridInterface);
		cmp_tbl.setModel(mModel);
		doLayout();
	}
	
	public void refreshData(){
		if (mModel!=null)
			setGridInterface(mModel.getGridInterface());
	}

	public int getSelectedRowCount() {
		return cmp_tbl.getSelectedRowCount();
	}

	public int[] getSelectedRows() {
		return cmp_tbl.getSelectedRows();
	}

	public void setMultiSelection(boolean isMultiSelection) {
		cmp_tbl.setSelectionMode(isMultiSelection?javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:javax.swing.ListSelectionModel.SINGLE_SELECTION);
	}

	public void setRowSelection(int row) {
		if (row>=cmp_tbl.getModel().getRowCount())
			row=cmp_tbl.getModel().getRowCount()-1;
		if (row>=0)
			cmp_tbl.setRowSelectionInterval(row, row);
	}

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
	}
	
	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	private boolean mIsActiveMouse=false;
	public void setActiveMouseAction(boolean isActive) {
		mIsActiveMouse=isActive;		
	}
	
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		cmp_tbl.setFocusable(aFlag);
	}
}
