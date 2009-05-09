package org.lucterios.client.application.comp;

import java.util.Comparator;
import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.lucterios.client.presentation.Singletons;
import org.lucterios.utils.SimpleParsing;

import sbrunner.gui.tableView.AbstractTableViewModel;
import sbrunner.gui.tableView.TableViewColumn;

public class CmpTableModel extends AbstractTableViewModel {
	private static final long serialVersionUID = 1L;

	public final static Icon NullImage=new ImageIcon();
	
	class GridColomn implements TableViewColumn {
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

		public Comparator getComparator() {
			return new Comparator() {
				public int compare(Object o1, Object o2) {
					GridRow row1 = (GridRow) o1;
					GridRow row2 = (GridRow) o2;
					String val1 = row1.GetCell(mHeaderId);
					String val2 = row2.GetCell(mHeaderId);
					return val1.compareTo(val2);
				}
			};
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

		public boolean isCellEditable(Object arg0) {
			return false;
		}

		private TreeMap mIconCache=new TreeMap();
		public Object getValue(Object row) {
			String valuetxt = ((GridRow) row).GetCell(mHeaderId);
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
						new_icon = (Icon) Singletons.Transport().getIcon(valuetxt);
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

		public void setValue(Object arg0, Object arg1) {
		}

		public boolean isSortable() {
			return true;
		}

		public boolean isSearchable() {
			return true;
		}

		public boolean isDefaultVisible() {
			return true;
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

		private TreeMap mCells = new TreeMap();

		public String GetCell(String colName) {
			if (mCells.containsKey(colName))
				return (String) mCells.get(colName);
			else
				return null;
		}
	}
	
	private SimpleParsing[] mHeaderXml;
	private SimpleParsing[] mContentXml;

	public void close() {
		mHeaderXml=null;
		mContentXml=null;
	}
	public void setText(SimpleParsing aXmlItem) {
		mHeaderXml = aXmlItem.getSubTag("HEADER");
		mContentXml = aXmlItem.getSubTag("RECORD");
	}

	public int getRowCount() {
		return mContentXml.length;
	}

	public TableViewColumn[] getColumns() {
		TableViewColumn[] result = new TableViewColumn[mHeaderXml.length];
		for (int col_idx = 0; col_idx < mHeaderXml.length; col_idx++)
			result[col_idx] = new GridColomn(mHeaderXml[col_idx]);
		return result;
	}

	public Object getRowObject(int pRowIndex) {
		return new GridRow(mContentXml[pRowIndex]);
	}
}
