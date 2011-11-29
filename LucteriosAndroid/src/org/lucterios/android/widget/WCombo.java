package org.lucterios.android.widget;

import java.util.ArrayList;

import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIComponent;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.database.Cursor;
import android.text.InputType;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;

public class WCombo extends LinearLayout implements GUICombo,OnFocusChangeListener {

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>(); 
	
	public void clearFocusListener(){
		mFocusListener.clear();
	}

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
		
	
	private AutoCompleteTextView _text;
	private ImageButton _button;

	public WCombo(Context context, WContainer owner) {
		super(context);
		this.createChildControls(context);
		setOnFocusChangeListener(this);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus && GUIComponent.class.isInstance(v))
			for(GUIFocusListener l:mFocusListener)
				l.focusLost(null,(GUIComponent)v);		
	}

	private void createChildControls(Context context) {
		this.setOrientation(HORIZONTAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		_text = new AutoCompleteTextView(context);
		_text.setSingleLine();
		_text.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_NORMAL
				| InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
				| InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
				| InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
		_text.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		this.addView(_text, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1));

		_button = new ImageButton(context);
		_button.setImageResource(android.R.drawable.arrow_down_float);
		_button.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				_text.showDropDown();
			}
		});
		this.addView(_button, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
	}

	/**
	 * Sets the source for DDLB suggestions. Cursor MUST be managed by
	 * supplier!!
	 * 
	 * @param source
	 *            Source of suggestions.
	 * @param column
	 *            Which column from source to show.
	 */
	public void setSuggestionSource(Cursor source, String column) {
		String[] from = new String[] { column };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this
				.getContext(), android.R.layout.simple_dropdown_item_1line,
				source, from, to);
		// this is to ensure that when suggestion is selected
		// it provides the value to the textbox
		cursorAdapter.setStringConversionColumn(source.getColumnIndex(column));
		_text.setAdapter(cursorAdapter);
	}

	public String getText() {
		return _text.getText().toString();
	}

	public void setText(String text) {
		_text.setText(text);
	}

	public Object getSelectedItem() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addElement(Object obj) {
		// TODO Auto-generated method stub
		
	}

	public void removeAllElements() {
		// TODO Auto-generated method stub
		
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	public void addList(Object[] Items) {
		// TODO Auto-generated method stub
		
	}

	public int getSelectedIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setSelectedIndex(int index) {
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
