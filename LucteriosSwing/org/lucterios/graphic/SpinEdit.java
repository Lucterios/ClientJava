/*
 // *    This file is part of Lucterios.
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

package org.lucterios.graphic;

import javax.swing.*;
import javax.swing.plaf.basic.*;

import java.awt.event.*;

public class SpinEdit extends JComponent implements ActionListener,
		FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private class ExtraField extends JTextField {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		SpinEdit mOwner;

		public ExtraField(SpinEdit owner) {
			super();
			mOwner = owner;
			PopupListener popupListener = new PopupListener();
			popupListener.setActions(getActions());
			popupListener.addEditionMenu(true);
			addMouseListener(popupListener);
		}

		protected void processKeyEvent(KeyEvent e) {
			if ((e.getKeyCode() == KeyEvent.VK_LEFT)
					|| (e.getKeyCode() == KeyEvent.VK_RIGHT)
					|| (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
					|| (e.getKeyCode() == KeyEvent.VK_DELETE)
					|| ((e.getKeyChar() >= '0') && (e.getKeyChar() <= '9')))
				super.processKeyEvent(e);
			mOwner.processKey(e);
			e.consume();
		}
	}

	private JButton upButton;

	private JButton downButton;

	private JTextField numberField;

	private long number;

	private long upperLimit;

	private long bottomLimit;

	private boolean reverse;

	private String actionCommand = "SpinEdit";

	// ///////////////////
	// Consturctors
	// ///////////////////

	/**
	 * Creates SpinEdit control with upper limit of max integer value and bottom
	 * limit of 0 and start value 0.
	 */

	public SpinEdit() {
		init(0, 0, Integer.MAX_VALUE);
	}

	/**
	 * Creates SpinEdit control with upper limit of max integer value and bottom
	 * limit of 0 and given start value .
	 * 
	 * @param num
	 *            start value
	 */

	public SpinEdit(long num) {
		init(num, 0, Integer.MAX_VALUE);
	}

	/**
	 * Creates SpinEdit control with given upper limit, bottom limit and start
	 * value.
	 * 
	 * @param num
	 *            start value
	 * @param bottomL
	 *            bottom limit
	 * @param upperL
	 *            upper limit
	 */

	public SpinEdit(long num, long bottomL, long upperL) {
		init(num, bottomL, upperL);
	}

	public void init(long num, long bottomL, long upperL) {
		upperLimit = upperL;
		bottomLimit = bottomL;
		if (num <= upperLimit && num >= bottomLimit)
			number = num;
		else
			number = bottomLimit;

		setLayout(null);

		if (numberField != null)
			remove(numberField);
		numberField = new ExtraField(this);
		numberField.setName(getName());
		numberField.setText(Long.toString(number));
		numberField.setEnabled(true);
		add(numberField);

		upButton = new BasicArrowButton(BasicArrowButton.NORTH);
		add(upButton);

		downButton = new BasicArrowButton(BasicArrowButton.SOUTH);
		add(downButton);

		numberField.setActionCommand("numF");
		numberField.addActionListener(this);
		numberField.addFocusListener(this);
		upButton.setActionCommand("Up");
		upButton.addActionListener(this);
		downButton.setActionCommand("Down");
		downButton.addActionListener(this);

		reverse = false;
		setFocusable(false);
	}

	// /////////////////
	// Public methods
	// /////////////////

	public void setEnabled(boolean aValue) {
		upButton.setEnabled(aValue);
		downButton.setEnabled(aValue);
		numberField.setEnabled(aValue);
	}

	public void requestFocus() {
		numberField.requestFocus();
	}

	public synchronized void addFocusListener(FocusListener aFocus) {
		numberField.addFocusListener(aFocus);
		upButton.addFocusListener(aFocus);
		downButton.addFocusListener(aFocus);
	}

	public synchronized void removeFocusListener(FocusListener aFocus) {
		numberField.removeFocusListener(aFocus);
		upButton.removeFocusListener(aFocus);
		downButton.removeFocusListener(aFocus);
	}

	/**
	 * Sets new number on SpinEdit control.
	 * 
	 * @param num
	 *            new number
	 */

	public void setNumber(long num) {
		long old = number;
		if (number <= upperLimit && number >= bottomLimit)
			number = num;
		numberField.setText(Long.toString(number));
		firePropertyChange("number", old, number);
	}

	/**
	 * Returns the number on SpinEdit control.
	 */

	public long getNumber() {
		return number;
	}

	/**
	 * Sets the spinning direction of buttons.
	 * 
	 * @param r
	 *            is dir reverse
	 */

	public void setReverse(boolean r) {
		boolean old = reverse;
		reverse = r;
		firePropertyChange("reverse", old, reverse);
	}

	/**
	 * Returns the spinning direction of buttons (reverse or not).
	 */

	public boolean isReverse() {
		return reverse;
	}

	/**
	 * Sets the upper limit of number.
	 * 
	 * @param limit
	 *            upper limit
	 */

	public void setUpperLimit(int limit) {
		long old = upperLimit;
		upperLimit = limit;
		if (number > upperLimit)
			setNumber(upperLimit);
		firePropertyChange("upperLimit", old, upperLimit);
	}

	/**
	 * Returns the upper limit.
	 */

	public long getUpperLimit() {
		return upperLimit;
	}

	/**
	 * Sets the bottom limit of number.
	 * 
	 * @param limit
	 *            bottom limit
	 */

	public void setBottomLimit(long limit) {
		long old = bottomLimit;
		bottomLimit = limit;
		if (number < bottomLimit)
			setNumber(bottomLimit);
		firePropertyChange("bottomLimit", old, bottomLimit);
	}

	/**
	 * Returns the bottom limit.
	 */

	public long getBottomLimit() {
		return bottomLimit;
	}

	/**
	 * Sets the action command for this SpinEdit.
	 * 
	 * @param actionCommand
	 *            action command
	 */

	public void setActionCommand(String actionCommand) {
		this.actionCommand = actionCommand;
	}

	/**
	 * Returns the action command of this SpinEdit.
	 */

	public String getActionCommand() {
		return actionCommand;
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		numberField.postActionEvent();
	}

	public void processKey(KeyEvent e) {
		processKeyEvent(e);
	}

	protected void checkNumberValue() {
		long old = number;
		try {
			long num = Long.parseLong(numberField.getText());
			if (num <= upperLimit && num >= bottomLimit)
				number = num;
		} catch (NumberFormatException exept) {
			number = old;
		}
	}

	private boolean mKeyEventFlag = true;

	protected void processKeyEvent(KeyEvent e) {
		if (mKeyEventFlag) {
			if (e.getKeyCode() == KeyEvent.VK_UP)
				upButton.doClick();
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				downButton.doClick();
		}
		mKeyEventFlag = !mKeyEventFlag;
		checkNumberValue();
	}

	public void actionPerformed(ActionEvent e) {
		long old = number;

		if (e.getActionCommand().equals("numF")) {
			checkNumberValue();
		} else if (e.getActionCommand().equals("Up")) {
			if (reverse) {
				if (number > bottomLimit)
					number -= 1;
			} else if (number < upperLimit)
				number += 1;
		} else if (e.getActionCommand().equals("Down")) {
			if (reverse) {
				if (number < upperLimit)
					number += 1;
			} else if (number > bottomLimit)
				number -= 1;
		}
		numberField.setText(Long.toString(number));
		fireActionEvent();
		firePropertyChange("number", old, number);
	}

	/**
	 * Adds an ActionListener to the SpinEdit
	 */

	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	/**
	 * Removes an ActionListener to the SpinEdit
	 */

	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	public void fireActionEvent() {
		ActionEvent e = null;
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i].equals(ActionListener.class)) {
				if (e == null)
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
							actionCommand);
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int fieldWidth = (3 * width) / 4;
		numberField.setBounds(0, 0, fieldWidth, height);
		upButton.setBounds(fieldWidth, 0, width - fieldWidth, height / 2);
		downButton.setBounds(fieldWidth, height / 2, width - fieldWidth,
				height / 2);
	}

}
