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

package org.lucterios.graphic;

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.*;

public class TextCode extends JPanel implements KeyListener, CaretListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static short NUMBERS_WIDTH = 35;
	public static Color BAR_COLOR = Color.LIGHT_GRAY;
	public static Color NUM_COLOR = Color.BLACK;
    
    private JTextPane editor;
    private PopupListener popupListener;

	private int mFirstLine = 1;
	private int m_StringSize = 0;
    
	public TextCode()
	{
		super(true);
		setLayout(new BorderLayout());
		
		// create the embedded JTextComponent
		editor = createEditor();
		editor.setFocusable(true);

        popupListener = new PopupListener();
        popupListener.setActions(editor.getActions());
        popupListener.addEditionMenu(true);
		editor.getDocument().addUndoableEditListener(popupListener.getUndo());
		editor.setBorder(BorderFactory.createLineBorder(Color.BLACK));        
	
		JScrollPane scroller = new JScrollPane();
		scroller.setFocusable(false);
		
		JViewport port = scroller.getViewport();
		port.add(editor);
		port.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
		port.setFocusable(false);

		add("Center", scroller);
		setFocusable(false);

		editor.addKeyListener(this);
        editor.addCaretListener(this);
        editor.addMouseListener(popupListener);
	}

	public PopupListener getPopupListener() {
		return popupListener;
	}

	public void requestFocusGUI() {
		editor.requestFocus();
	}
	
	public void setValue(String aText)
	{
		if(getEditor().getDocument() != null)
			getEditor().getDocument().removeUndoableEditListener(popupListener.getUndo());
		getEditor().setText(aText);
		getEditor().getDocument().addUndoableEditListener(popupListener.getUndo());
		popupListener.resetUndoManager();
		validate();
	}
	
	public void setEnabled(boolean aEnabled)
	{
		super.setEnabled(aEnabled);
		getEditor().setEnabled(aEnabled);
		getEditor().setEditable(aEnabled);
		if (popupListener.getPopup()!=null)
		{
			popupListener.getPopup().setEnabled(aEnabled);
			for(int idx=0;idx<popupListener.getPopup().getComponentCount();idx++)
				popupListener.getPopup().getComponent(idx).setEnabled(aEnabled);
		}
	}

	public String getValue()
	{
		return getEditor().getText();
	}

	public void setTabs(int charactersPerTab) {
		FontMetrics fm = getFontMetrics(getFont() );
		int charWidth = fm.charWidth('w');
		int tabWidth = charWidth * charactersPerTab;
	
		TabStop[] tabs = new TabStop[10];
	
		for (int j = 0; j < tabs.length; j++)
		{
			int tab = j + 1;
			tabs[j] = new TabStop( tab * tabWidth );
		}
	
		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = getEditor().getDocument().getLength();
		getEditor().getStyledDocument().setParagraphAttributes(0, length, attributes, true);
	}
	
    protected JTextPane createEditor()
    {
		StyleContext sc = new StyleContext();
		DefaultStyledDocument doc = new DefaultStyledDocument(sc);
		JTextPane p = new JTextPane(doc);
		p.setDragEnabled(true);
        return p;
   }

    protected JTextPane getEditor()
    {
		return editor;
    }

    
	protected int lineNumberWidth() {
		if (mFirstLine>0)
			return NUMBERS_WIDTH;
		else
			return 0;
	}

	public static class TextInserter extends AbstractAction
	{
		private static final long serialVersionUID = 1L;
		private JTextPane textArea;
		private String insertable;
	
		private TextInserter(JTextPane textArea, String insertable)
		{
			this.textArea   = textArea;
			this.insertable = insertable;
		}
	
		public static void applyTabBinding(JTextPane textArea)
		{
			textArea.getInputMap(JComponent.WHEN_FOCUSED)
			        .put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.CTRL_MASK+KeyEvent.CTRL_DOWN_MASK),"tab");
			textArea.getActionMap()
			        .put("tab",new TextInserter(textArea, "\t"));
		}
	
		public void actionPerformed(ActionEvent evt)
		{
			int pos=textArea.getCaretPosition();
			StyledDocument style=(StyledDocument)textArea.getDocument();
		    try 
		    {
				style.insertString(pos,insertable,null);
			} 
		    catch (BadLocationException e) 
		    {
				e.printStackTrace();
			}			
		}
	}

	public void setFirstLine(int aFirstLine) {
		mFirstLine=aFirstLine;
		if (mFirstLine<=0)
			invertFocusTraversalBehaviour();
	}

	public int getFirstLine() {
		return mFirstLine;
	}

	public void setStringSize(int aStringSize) {
		m_StringSize=aStringSize;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (mFirstLine>0) {
	      int h=getFontMetrics(getFont()).getHeight();
	      Rectangle clip = g.getClipBounds();
	      Color old_color=g.getColor();
	 
	      g.setColor(BAR_COLOR);     
	      g.fillRect(0,0,lineNumberWidth(),clip.y+clip.height);
	      g.setColor(NUM_COLOR);
	      for(int num=0;num<=(1+(clip.y+clip.height)/h);num++)
	    	  g.drawString(Integer.toString(num + mFirstLine),0, (num+1)*h);
	      
	      g.setColor(old_color);
		}
   }

	private void invertFocusTraversalBehaviour() {
		Set<AWTKeyStroke> forwardKeys  = this.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		Set<AWTKeyStroke> backwardKeys = this.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
	
		if (forwardKeys.size() != 1 || backwardKeys.size() != 1) return;
		final AWTKeyStroke fks = forwardKeys.iterator().next();
		final AWTKeyStroke bks = backwardKeys.iterator().next();
		final int fkm = fks.getModifiers();
		final int bkm = bks.getModifiers();
		final int ctrlMask      = KeyEvent.CTRL_MASK+KeyEvent.CTRL_DOWN_MASK;
		final int ctrlShiftMask = KeyEvent.SHIFT_MASK+KeyEvent.SHIFT_DOWN_MASK+ctrlMask;
		if (fks.getKeyCode() != KeyEvent.VK_TAB || (fkm & ctrlMask) == 0 || (fkm & ctrlMask) != fkm)
		{	
			return;
		}
		if (bks.getKeyCode() != KeyEvent.VK_TAB || (bkm & ctrlShiftMask) == 0 || (bkm & ctrlShiftMask) != bkm)
		{	
			return;
		}
		Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(1);
		newForwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB,0));
		this.setFocusTraversalKeys(
			KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
			Collections.unmodifiableSet(newForwardKeys)
		);
		Set<AWTKeyStroke> newBackwardKeys = new HashSet<AWTKeyStroke>(1);
		newBackwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK+KeyEvent.SHIFT_DOWN_MASK));
		this.setFocusTraversalKeys(
			KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
			Collections.unmodifiableSet(newBackwardKeys)
		);
		TextInserter.applyTabBinding(getEditor());
	}

	public void keyPressed(KeyEvent e) {
		if (mFirstLine<=0) {
			if ((e.getKeyCode()==KeyEvent.VK_TAB) && e.isShiftDown())
			{
				e.consume();
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
			}
			else if (e.getKeyCode()==KeyEvent.VK_TAB)
			{
				e.consume();
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
			}
		}
	}

	public void keyTyped(KeyEvent e) {}
	
    public Insets getInsets(Insets insets) 
    {
    	insets = super.getInsets(insets);
    	insets.left += lineNumberWidth();
    	return insets;
    }
	
	public Insets getInsets() 
    {
    	return getInsets(new Insets(0,0,0,0));
    }
 
	public void keyReleased(KeyEvent e) {
		if (m_StringSize>0) {
			String current_text=getEditor().getText();
			if ((current_text.length()<=m_StringSize)){
				Caret car=getEditor().getCaret();
				int dot=car.getDot();
				int mark=car.getMark();
				if (mark==dot) mark-=1;
				if (mark>dot) {
					int tmp=dot;
					dot=mark;
					mark=tmp;
				}
				current_text=current_text.substring(0, mark)+current_text.substring(dot);
				getEditor().setText(current_text);
				getEditor().setCaretPosition(mark);
				getEditor().setSelectionStart(mark);
				getEditor().setSelectionEnd(mark);
			}
		}
	}
    
	public void insertText(String specialToAdd) {
		StyledDocument style = (StyledDocument)getEditor().getDocument();
		try {
			if (mDot != mMark)
				style.remove(Math.min(mDot, mMark), Math.abs(mMark - mDot));
			style.insertString(Math.min(mDot, mMark), specialToAdd, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
    
    int mDot=0;
    int mMark=0;
	public void caretUpdate(CaretEvent e) 
	{
		mDot = e.getDot();
		mMark = e.getMark();
	} 
}