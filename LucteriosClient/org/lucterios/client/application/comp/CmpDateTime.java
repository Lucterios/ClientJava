package org.lucterios.client.application.comp;

import java.awt.GridBagConstraints;
import java.awt.event.FocusEvent;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;


public class CmpDateTime extends CmpAbstractEvent {

	private static final long serialVersionUID = 1L;
	private CmpTime time;
	private CmpDate date;
	
	public CmpDateTime(){
		super();
		mFill = GridBagConstraints.HORIZONTAL;
		mWeightx = 1.0;
	}

	public void init(JPanel aOwnerPanel, Observer aObsCustom, SimpleParsing aXmlItem) {
		super.init(aOwnerPanel, aObsCustom,aXmlItem);
		time.init(null,aObsCustom,aXmlItem);
		date.init(null,aObsCustom,aXmlItem);
	}
	
	public void requestFocus() {
		date.requestFocus();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		time.setEnabled(aEnabled);
		date.setEnabled(aEnabled);
	}

	protected boolean hasChanged() {
		return time.hasChanged() || date.hasChanged();
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		Map<String,Object> map_date=date.getRequete(aActionIdent);
		String text=(String)map_date.get(date.getName());
		text+=" ";		
		Map<String,Object> map_time=time.getRequete(aActionIdent);
		text+=(String)map_time.get(time.getName());
		tree_map.put(getName(), text);
		return tree_map;
	}

	protected void initComponent() {
		setLayout(new java.awt.GridBagLayout());
		GridBagConstraints gdbConstr;
		date = new CmpDate();
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 0;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		gdbConstr.weightx = 0.0;
		gdbConstr.weighty = 1.0;
		add(date, gdbConstr);
	
		time = new CmpTime();
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 1;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		gdbConstr.weightx = 0.0;
		gdbConstr.weighty = 1.0;
		add(time, gdbConstr);

		JLabel pnl_date = new JLabel();
		pnl_date.setFocusable(false);
		pnl_date.setOpaque(false);
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 3;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		gdbConstr.weightx = 1.0;
		gdbConstr.weighty = 1.0;
		add(pnl_date, gdbConstr);
	}
	
	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		String date_time = getXmlItem().getText().trim();
		String[] date_times = date_time.split(" ");
		if (date_times.length > 1)	
			date.setValue("<DATE>"+date_times[0]+"</DATE>");
		if (date_times.length > 2)	
			time.setValue("<TIME>"+date_times[1]+"</TIME>");
		date.addFocusListener(this);
		time.addFocusListener(this);
	}

	public void focusLost(FocusEvent aEvent) {
		if ((mEventAction != null) && hasChanged()) {
			Cmponent new_Cmponent_focused = getParentOfControle(aEvent.getOppositeComponent());
			if ((new_Cmponent_focused != null) && !new_Cmponent_focused.getName().equals(getName())) {
				date.removeFocusListener(this);
				time.removeFocusListener(this);
				getObsCustom().setNameComponentFocused(new_Cmponent_focused.getName());
				mEventAction.actionPerformed();
			}
		}
	}

}
