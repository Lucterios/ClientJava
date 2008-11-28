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

package org.lucterios.client.application.observer;

import java.net.URL;
import java.util.*;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.lucterios.Print.FopGenerator;
import org.lucterios.Print.MainPrintPanel;
import org.lucterios.Print.ModelConverter;
import org.lucterios.Print.SelectPrintDlg;
import org.lucterios.client.application.Button;
import org.lucterios.client.presentation.ObserverAbstract;
import org.lucterios.client.presentation.ObserverConstant;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.utils.Dialog;
import org.lucterios.client.utils.Form;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.graphic.ExceptionDlg;
import org.lucterios.utils.graphic.Tools;

public class ObserverTemplate extends ObserverAbstract {
	SimpleParsing data_elements = null;
	SimpleParsing style_elements = null;

	protected int mModelId = 0;
	protected String mTitle = "";

	private JPanel pnl_Cst;
	private JPanel pnl_Btn;
	private JTextField txt_Title;
	private JLabel lbl_Title;

	private JScrollPane scrl_StyleModel;
	private MainPrintPanel PrintPanel;

	public boolean isValidate;

	public String getObserverName() {
		return "Core.Template";
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		data_elements = null;
		style_elements = null;
		mTitle = "";
		SimpleParsing template = mContent.getFirstSubTag("TEMPLATE");
		if (template != null) {
			mModelId = template.getAttributInt("model", 0);
			mTitle = template.getAttribut("title");
			data_elements = template.getFirstSubTag("XMLOBJECT");
			style_elements = template.getFirstSubTag("XSLTEXT");
		}
	}

	public Map getParameters(String aActionId, int aSelect, boolean aCheckNull) {
		Map attrib = mContext;
		attrib.put("model_id", new Integer(mModelId));
		attrib.put("title", getModelTitle());
		attrib.put("model", getStyle());
		return attrib;
	}

	public byte getType() {
		return ObserverConstant.TYPE_DIALOG;
	}

	public void show(String aTitle) throws LucteriosException {
		super.show(aTitle);
		String data_str = data_elements.getText();
		String style_str = style_elements.getText();
		setValue(data_str, style_str, mTitle);
	}

	public void show(String aTitle, Form new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, Dialog aGUI) throws LucteriosException {
		mGUIDialog = aGUI;
		aGUI.setTitle(aTitle);
		aGUI.getContentPane().setLayout(new java.awt.BorderLayout());
		GridBagConstraints gridBagConstraints;

		pnl_Cst = new JPanel();
		pnl_Cst.setName("pnl_Cst");
		pnl_Cst.setLayout(new GridBagLayout());
		aGUI.getContentPane().add(pnl_Cst, java.awt.BorderLayout.CENTER);

		lbl_Title = new javax.swing.JLabel();
		lbl_Title.setText("Titre du model");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 0.0;
		pnl_Cst.add(lbl_Title, gridBagConstraints);

		txt_Title = new javax.swing.JTextField();
		txt_Title.setPreferredSize(new java.awt.Dimension(250, 19));
		txt_Title.setMinimumSize(new java.awt.Dimension(250, 19));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		pnl_Cst.add(txt_Title, gridBagConstraints);

		PrintPanel = new MainPrintPanel();
		scrl_StyleModel = new JScrollPane();
		scrl_StyleModel.setViewportView(PrintPanel);
		scrl_StyleModel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrl_StyleModel
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.3;
		pnl_Cst.add(scrl_StyleModel, gridBagConstraints);

		pnl_Btn = new JPanel();
		pnl_Btn.setName("pnl_Btn");
		pnl_Btn.setLayout(new java.awt.GridBagLayout());
		aGUI.getContentPane().add(pnl_Btn, java.awt.BorderLayout.SOUTH);
		SimpleParsing act = new SimpleParsing();
		act.parse("<ACTIONS>"
						+ "<ACTION extension='"
						+ getSourceExtension()
						+ "' action='"
						+ getSourceAction()
						+ "' close='1' modal='0' icon='images/ok.png'><![CDATA[_Ok]]></ACTION>"
						+ "<ACTION close='1' modal='0' icon='images/cancel.png'><![CDATA[_Annuler]]></ACTION>"
						+ "</ACTIONS>");
		Button.fillPanelByButton(pnl_Btn, this, Singletons.Factory(), act,true);

		JButton btn_preview = new JButton();
		btn_preview.setText("Previsualisation");
		btn_preview.setMnemonic('P');
		btn_preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				preview();
			}
		});
		GridBagConstraints gdbConstr_btn = new GridBagConstraints();
		gdbConstr_btn.gridx = 2;
		gdbConstr_btn.gridy = 0;
		gdbConstr_btn.gridwidth = 1;
		gdbConstr_btn.gridheight = 1;
		gdbConstr_btn.fill = GridBagConstraints.NONE;
		gdbConstr_btn.anchor = GridBagConstraints.CENTER;
		gdbConstr_btn.weightx = 1.0;
		gdbConstr_btn.weighty = 0.0;
		gdbConstr_btn.insets = new Insets(10, 0, 10, 0);
		pnl_Btn.add(btn_preview, gdbConstr_btn);
		JButton[] btns = new JButton[3];
		int index = 0;
		for (int idx = 0; idx < pnl_Btn.getComponentCount(); idx++)
			if (JButton.class.isInstance(pnl_Btn.getComponent(idx)))
				btns[index++] = (JButton) pnl_Btn.getComponent(idx);
		btns[2] = btn_preview;
		Tools.calculBtnSize(btns);

		show(aTitle);

		aGUI.pack();
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screen = kit.getScreenSize();
		Insets insets = kit.getScreenInsets(aGUI.getGraphicsConfiguration());
		int w = (int) (screen.getWidth() - insets.left - insets.right);
		int h = (int) (screen.getHeight() - insets.top - insets.bottom);
		Dimension dimension = new Dimension(w, h);
		aGUI.setSize(dimension);
		aGUI.setLocation((screen.width - aGUI.getSize().width) / 2,
				(screen.height - aGUI.getSize().height) / 2);
		aGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aGUI.setVisible(true);
	}

	public void preview() {
		try {
			mGUIDialog
					.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try {
				URL url = Singletons.Transport().getUrl();
				ModelConverter model = new ModelConverter(getStyle(), url
						.toString());
				model.Run();
				String print_pre_fop = model.toXap(mDataXML, "");
				FopGenerator fop_generator = new FopGenerator(print_pre_fop,
						"Previsualisation", false);
				fop_generator.SelectPrintMedia(null, mGUIDialog,
						SelectPrintDlg.MODE_PREVIEW, null);
			} finally {
				mGUIDialog.setCursor(Cursor.getDefaultCursor());
			}
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	private String mDataXML = "";

	public void setValue(String aDataXML, String aStyleModel, String aTitle)
			throws LucteriosException {
		mDataXML = aDataXML;
		try {
			PrintPanel.getManager().getPage().Load(aStyleModel);
			PrintPanel.getManager().getPage().setXML(mDataXML);
			PrintPanel.getManager().refresh();
			txt_Title.setText(aTitle);
			isValidate = false;
		} catch (org.xml.sax.SAXException se) {
			throw new LucteriosException("Model d'impression invalide", se);
		} catch (java.io.IOException ioe) {
			throw new LucteriosException("Model d'impression invalide", ioe);
		}
	}

	public String getStyle() {
		return PrintPanel.getManager().getPage().Save();
	}

	public String getModelTitle() {
		return txt_Title.getText();
	}

	boolean closed = false;

	public void close(boolean aMustRefreshParent) {
		if (!closed) {
			closed = true;
			super.close(aMustRefreshParent);
			if (mGUIDialog != null)
				mGUIDialog.dispose();
			System.gc();
		}
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}
}
