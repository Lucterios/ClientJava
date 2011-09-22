package org.lucterios.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicArrowButton;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.PopupListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.ui.GUIActionListener;

public class SSpinEdit extends JComponent implements ActionListener,GUISpinEdit,FocusListener {

	private static final long serialVersionUID = 1L;

	private class ExtraField extends JTextField {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		SSpinEdit mOwner;

		public ExtraField(SSpinEdit owner) {
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

	private CursorMouseListener mCursorMouseListener;
	
	private JButton upButton;

	private JButton downButton;

	private JTextField numberField;

	private long number;

	private long upperLimit;

	private long bottomLimit;

	private boolean reverse;

	private String actionCommand = "SpinEdit";

	// ///////////////////
	// Constructors
	// ///////////////////

	private FocusListenerList mFocusListener=new FocusListenerList(); 
	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>(); 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void addActionListener(GUIActionListener l){
		mActionListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void removeActionListener(GUIActionListener l){
		mActionListener.remove(l);
	}
	
	/**
	 * Creates SpinEdit control with upper limit of max integer value and bottom
	 * limit of 0 and start value 0.
	 */

	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SSpinEdit(GUIComponent aOwner){
    	super();
        mOwner=aOwner;
        mCursorMouseListener=new CursorMouseListener(this,this);
		init(0, 0, Integer.MAX_VALUE);
	}

	/**
	 * Creates SpinEdit control with upper limit of max integer value and bottom
	 * limit of 0 and given start value .
	 * 
	 * @param num
	 *            start value
	 */

	public SSpinEdit(long num) {
		super();
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

	public SSpinEdit(long num, long bottomL, long upperL) {
		super();
		init(num, bottomL, upperL);
	}

	public void init(long num, long bottomL, long upperL) {
		addMouseListener(mCursorMouseListener);
		upperLimit = upperL;
		bottomLimit = bottomL;
		if (num <= upperLimit && num >= bottomLimit)
			number = num;
		else
			number = bottomLimit;

		setLayout(null);

		if (numberField != null) {
			numberField.removeMouseListener(mCursorMouseListener);
			remove(numberField);
		}
		numberField = new ExtraField(this);
		numberField.setName(getName());
		numberField.setText(Long.toString(number));
		numberField.setEnabled(true);
		numberField.addMouseListener(mCursorMouseListener);
		add(numberField);

		upButton = new BasicArrowButton(BasicArrowButton.NORTH);
		upButton.setFocusable(false);
		add(upButton);

		downButton = new BasicArrowButton(BasicArrowButton.SOUTH);
		downButton.setFocusable(false);
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
		numberField.setMinimumSize(new Dimension(50, 20));
		numberField.setPreferredSize(new Dimension(50, 20));
		upButton.setMinimumSize(new Dimension(10, 10));
		upButton.setPreferredSize(new Dimension(10, 10));
		downButton.setMinimumSize(new Dimension(10, 10));
		downButton.setPreferredSize(new Dimension(10, 10));
		setMinimumSize(new Dimension(60, 20));
		setPreferredSize(new Dimension(60, 20));
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
		mFocusListener.focusLost(e);
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
		for(GUIActionListener l:mActionListener)
			l.actionPerformed();
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
	
	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
	}

	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}
	
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		numberField.setFocusable(aFlag);
	}

	public boolean isActive() {
		return getOwner().isActive();
	}
	
	public void requestFocusGUI() {
		requestFocus();
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(mNbClick);
	}
}
