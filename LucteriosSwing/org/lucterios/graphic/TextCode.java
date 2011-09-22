package org.lucterios.graphic;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import org.lucterios.utils.Tools;

public class TextCode extends JTextPane implements KeyListener, CaretListener {

	private static final long serialVersionUID = 1L;
	public static short NUMBERS_WIDTH = 35;
	public static Color BAR_COLOR = Color.LIGHT_GRAY;
	public static Color NUM_COLOR = Color.BLACK;
	protected PopupListener popupListener;
	private int mFirstLine = 1;
	private int m_StringSize = 0;

	public TextCode() {
		super(new DefaultStyledDocument(new StyleContext()));
    	setBackground(Color.WHITE);
    	setOpaque(false);
  		setDragEnabled(true);
		addKeyListener(this);
		setTabs(4);
		
		popupListener = new PopupListener();
		popupListener.setActions(getActions());
		popupListener.addEditionMenu(true);
		getDocument().addUndoableEditListener(popupListener.getUndo());
		addMouseListener(popupListener);
		addCaretListener(this);
	}
	
	public PopupListener getPopupListener() {
		return popupListener;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
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
		int length = getDocument().getLength();
		getStyledDocument().setParagraphAttributes(0, length, attributes, true);
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

	int mDot = 0;

	public void setStringSize(int aStringSize) {
		m_StringSize=aStringSize;
	}

	public void paintComponent(Graphics g) {
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
			super.paintComponent(g);
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
		TextInserter.applyTabBinding(this);
	}

	public void keyPressed(KeyEvent e) {
		if (mFirstLine==0) {
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
			String current_text=getValue();
			if ((current_text.length()<=m_StringSize)){
				Caret car=getCaret();
				int dot=car.getDot();
				int mark=car.getMark();
				if (mark==dot) mark-=1;
				if (mark>dot) {
					int tmp=dot;
					dot=mark;
					mark=tmp;
				}
				current_text=current_text.substring(0, mark)+current_text.substring(dot);
				setText(current_text);
				setCaretPosition(mark);
				setSelectionStart(mark);
				setSelectionEnd(mark);
			}
		}
	}

	int mMark = 0;

	public void insertText(String specialToAdd) {
		StyledDocument style = (StyledDocument)getDocument();
		try {
			if (mDot != mMark)
				style.remove(Math.min(mDot, mMark), Math.abs(mMark - mDot));
			style.insertString(Math.min(mDot, mMark), specialToAdd, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

    public String getValue(){
		return Tools.replace(getText(), "\n", "{[newline]}");
	}

	public void caretUpdate(CaretEvent e) {
		mDot = e.getDot();
		mMark = e.getMark();
	}

}