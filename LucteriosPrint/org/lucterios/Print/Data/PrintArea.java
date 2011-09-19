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

package org.lucterios.Print.Data;

import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.StringList;

public class PrintArea extends PrintVector {
	public double extent = 0;
	public String data = "";

	public PrintArea() {
		super("body");
	}

	public PrintArea(String aName) {
		super(aName);
	}

	public Object[] getSelection() {
		StringList data_list = Owner.getDataList();
		if (data_list.size() > 0)
			return data_list.toArray();
		else
			return new Object[0];
	}

	public String getDataPath() {
		String data_path = Owner.getDataPath();
		if (data.length() > 0) {
			if (data.charAt(0) == '/')
				data_path = data;
			else
				data_path += "/" + data;
		}
		return data_path;
	}

	public StringList getDataList() {
		StringList data_list = Owner.getDataList();
		if (data.length() > 0)
			data_list.addAll(extractDataList(data_list, getDataPath()));
		return data_list;
	}

	public String toString() {
		if (name.equalsIgnoreCase("before"))
			return "En-tête";
		else if (name.equalsIgnoreCase("after"))
			return "Pied de page";
		if (name.equalsIgnoreCase("start"))
			return "Bord gauche";
		else if (name.equalsIgnoreCase("end"))
			return "Bord droit";
		else if (name.equalsIgnoreCase("body")) {
			int idx = -1;
			if (PrintVector.class.isInstance(getOwner()))
				idx = ((PrintVector) getOwner()).indexOf(this) + 1;
			return "Page " + idx;
		} else
			return "!! " + name + " !!";
	}

	protected void init() {
		super.init();
		extent = 0;
		data = "";
		NoAttributProperty.clear();
		if (!"body".equals(name))
			NoAttributProperty.add("data");
	}

	protected String write_text() {
		String xml_childs = "";
		for (PrintAbstract obj : containers) {
			if (PrintText.class.isInstance(obj))
				xml_childs = xml_childs + obj.write("text");
			if (PrintImage.class.isInstance(obj))
				xml_childs = xml_childs + obj.write("image");
			if (PrintTable.class.isInstance(obj))
				xml_childs = xml_childs + obj.write("table");
		}
		return xml_childs;
	}

	public void read(SimpleParsing aXmlItem) {
		super.read(aXmlItem);
		clear();
		for (int node_idx = 0; node_idx < aXmlItem.getTagCount(); node_idx++) {
			SimpleParsing node_item = aXmlItem.getSubTag(node_idx);
			PrintAbstract new_obj = null;
			if (node_item.getTagName().equalsIgnoreCase("text"))
				new_obj = new PrintText();
			if (node_item.getTagName().equalsIgnoreCase("table"))
				new_obj = new PrintTable();
			if (node_item.getTagName().equalsIgnoreCase("image"))
				new_obj = new PrintImage();
			if (new_obj != null) {
				add(new_obj);
				new_obj.read(node_item);
			}
		}
	}
}
