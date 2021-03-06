package org.lucterios.engine.application.comp;

import java.util.TreeMap;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GridInterface;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.StringDico;

public class CmpFastTableModel implements GridInterface {
	class GridColomn {
		public final static int TypeString = 0;

		public final static int TypeInt = 1;

		public final static int TypeFloat = 2;

		public final static int TypeBool = 3;

		public final static int TypeIcon = 4;

		public final static String TAGNAME = "HEADER";

		public GridColomn(SimpleParsing aXmlItem) {
			if (aXmlItem.getTagName().equalsIgnoreCase(TAGNAME)) {
				mHeaderId = aXmlItem.getAttribute("name");
				mName = aXmlItem.getText();
				String type = aXmlItem.getAttribute("type");
				if (type == null)
					mHeaderType = TypeString;
				else if ("int".equals(type))
					mHeaderType = TypeInt;
				else if ("float".equals(type))
					mHeaderType = TypeFloat;
				else if ("bool".equals(type))
					mHeaderType = TypeBool;
				else if ("icon".equals(type))
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

		public Class<?> getColumnClass() {
			switch (mHeaderType) {
			case TypeInt:
				return Integer.class;
			case TypeFloat:
				return Double.class;
			case TypeBool:
				return Boolean.class;
			case TypeIcon:
				return AbstractImage.class;
			default:
				return String.class;
			}
		}

		private TreeMap<String, AbstractImage> mIconCache = new TreeMap<String, AbstractImage>();

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
					AbstractImage new_icon;
					if (valuetxt.trim().equals(""))
						new_icon = AbstractImage.Null;
					else if (mIconCache.containsKey(valuetxt))
						new_icon =  mIconCache.get(valuetxt);
					else {
						new_icon = Singletons.Transport().getIcon(valuetxt, 0);
						if (new_icon == null)
							new_icon = AbstractImage.Null;
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
				mId = aXmlItem.getAttribute("id");

				SimpleParsing[] xml_items = aXmlItem.getSubTag("VALUE");
				for (int val_idx = 0; val_idx < xml_items.length; val_idx++) {
					String field_name = xml_items[val_idx].getAttribute("name");
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

	private GridColomn[] mGridColomn = null;

	private GridRow[] mGridRow = null;

	public void close() {
		mHeaderXml = null;
		mContentXml = null;
		mGridColomn = null;
	}

	public void setText(SimpleParsing aXmlItem) {
		mHeaderXml = aXmlItem.getSubTag("HEADER");
		mContentXml = aXmlItem.getSubTag("RECORD");
		mGridColomn = new GridColomn[getColumnCount()];
		mGridRow = new GridRow[getRowCount()];
	}

	public GridColomn getColumnObject(int columnIndex) {
		GridColomn resValue = mGridColomn[columnIndex];
		if (resValue == null) {
			resValue = new GridColomn(mHeaderXml[columnIndex]);
			mGridColomn[columnIndex] = resValue;
		}
		return resValue;
	}

	public int getColumnCount() {
		if (mHeaderXml != null)
			return mHeaderXml.length;
		else
			return 0;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return getColumnObject(columnIndex).getColumnClass();
	}

	public String getColumnName(int columnIndex) {
		return getColumnObject(columnIndex).getName();
	}

	public GridRow getRowObject(int rowIndex) {
		GridRow resValue = mGridRow[rowIndex];
		if (resValue == null) {
			resValue = new GridRow(mContentXml[rowIndex]);
			mGridRow[rowIndex] = resValue;
		}
		return resValue;
	}

	public int getRowCount() {
		if (mContentXml != null)
			return mContentXml.length;
		else
			return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		GridRow current = getRowObject(rowIndex);
		GridColomn col = getColumnObject(columnIndex);
		return col.getValue(current);
	}
}
