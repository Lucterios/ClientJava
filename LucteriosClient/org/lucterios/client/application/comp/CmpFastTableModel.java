package org.lucterios.client.application.comp;

import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import org.lucterios.client.presentation.Singletons;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.StringDico;

public class CmpFastTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	public final static Icon NullImage=new ImageIcon();
	
	class GridColomn {
		public final static int TypeString = 0;
		public final static int TypeInt = 1;
		public final static int TypeFloat = 2;
		public final static int TypeBool = 3;
		public final static int TypeIcon = 4;

		public final static String TAGNAME = "HEADER";

		public GridColomn(SimpleParsing aXmlItem) {
			if (aXmlItem.getTagName().equalsIgnoreCase(TAGNAME)) {
				mHeaderId = aXmlItem.getAttribut("name");
				mName = aXmlItem.getText();
				String type = aXmlItem.getAttribut("type");
				if (type == null)
					mHeaderType = TypeString;
				else if ("int".equals( type ))
					mHeaderType = TypeInt;
				else if ("float".equals( type ))
					mHeaderType = TypeFloat;
				else if ("bool".equals( type ))
					mHeaderType = TypeBool;
				else if ("icon".equals( type ))
					mHeaderType = TypeIcon;
				else
					mHeaderType = TypeString;
			} else
				mHeaderId = "";
		}

		private String mHeaderId = "";

		public String getHeaderId() {
			return mHeaderId;
		}

		private String mName = "";

		public String getName() {
			return mName;
		}

		public int mHeaderType = 0;

		public Class getColumnClass() {
			switch (mHeaderType) {
			case TypeInt:
				return Integer.class;
			case TypeFloat:
				return Double.class;
			case TypeBool:
				return Boolean.class;
			case TypeIcon:
				return Icon.class;
			default:
				return String.class;
			}
		}

		private TreeMap<String,Icon> mIconCache=new TreeMap<String, Icon>();
		public Object getValue(GridRow row) {
			String valuetxt = row.GetCell(mHeaderId);
			try {
				switch (mHeaderType) {
				case TypeInt:
					return new Integer(valuetxt);
				case TypeFloat:
					return new Double(valuetxt);
				case TypeBool:
					return new Boolean(valuetxt.toLowerCase().equalsIgnoreCase(
							"oui"));
				case TypeIcon:
					Icon new_icon;
					if (valuetxt.trim().equals(""))
						new_icon = NullImage;
					else if (mIconCache.containsKey(valuetxt))
						new_icon = (Icon)mIconCache.get(valuetxt);
					else {
						new_icon = (Icon) Singletons.Transport().getIcon(valuetxt,0);
						if (new_icon==null)
							new_icon = NullImage;
						mIconCache.put(valuetxt, new_icon);
					}
					return new_icon;
				default:
					return valuetxt;
				}
			} catch (Exception e) {
				return getDefaultValue();
			}
		}

		private Object getDefaultValue() {
			switch (mHeaderType) {
			case TypeInt:
				return new String("?");
			case TypeFloat:
				return new String("??");
			case TypeBool:
				return new String("!");
			case TypeIcon:
				return new String("***");
			default:
				return new String("!!");
			}
		}
	}

	public class GridRow {
		public final static String TAGNAME = "RECORD";

		public GridRow(SimpleParsing aXmlItem) {
			mCells.clear();
			if (aXmlItem.getTagName().equalsIgnoreCase(TAGNAME)) {
				mId = aXmlItem.getAttribut("id");

				SimpleParsing[] xml_items = aXmlItem.getSubTag("VALUE");
				for (int val_idx = 0; val_idx < xml_items.length; val_idx++) {
					String field_name = xml_items[val_idx].getAttribut("name");
					String value = xml_items[val_idx].getText().trim();
					mCells.put(field_name, value);
				}
			} else
				mId = "-1";
		}

		private String mId;

		public String GetId() {
			return mId;
		}

		private StringDico mCells = new StringDico();

		public String GetCell(String colName) {
			if (mCells.containsKey(colName))
				return (String) mCells.get(colName);
			else
				return null;
		}
	}
	
	private SimpleParsing[] mHeaderXml;
	private SimpleParsing[] mContentXml;
	
	private GridColomn[] mGridColomn=null;
	private GridRow[] mGridRow=null;

	public void close() {
		mHeaderXml=null;
		mContentXml=null;
		mGridColomn=null;
	}
	public void setText(SimpleParsing aXmlItem) {
		mHeaderXml = aXmlItem.getSubTag("HEADER");
		mContentXml = aXmlItem.getSubTag("RECORD");
		mGridColomn = new GridColomn[mHeaderXml.length];
		mGridRow = new GridRow[mContentXml.length];
	}

	public GridColomn getColumnObject(int columnIndex) {
		GridColomn resValue=mGridColomn[columnIndex];
		if (resValue==null) {
			resValue= new GridColomn(mHeaderXml[columnIndex]);
			mGridColomn[columnIndex]=resValue;
		}
		return resValue;
	}

	public int getColumnCount() {
		return mHeaderXml.length;
	}

	public Class<?> getColumnClass(int columnIndex) {
		 return getColumnObject(columnIndex).getColumnClass();
	}
	
	public String getColumnName(int columnIndex) {
		 return getColumnObject(columnIndex).getName();		 
	}

	public GridRow getRowObject(int rowIndex) {
		GridRow resValue=mGridRow[rowIndex];
		if (resValue==null) {
			resValue= new GridRow(mContentXml[rowIndex]);
			mGridRow[rowIndex]=resValue;
		}
		return resValue;
	}
	
	public int getRowCount() {
		return mContentXml.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		GridRow current=getRowObject(rowIndex);
		GridColomn col=getColumnObject(columnIndex);
		return col.getValue(current);
	}
}
