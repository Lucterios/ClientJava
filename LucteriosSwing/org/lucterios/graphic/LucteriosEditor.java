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
import java.beans.*;
import java.io.*;
import java.net.URL;

import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.*;

import org.lucterios.graphic.resources.Resources;

public class LucteriosEditor extends JPanel implements ActionListener, CaretListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public static final String openAction = "open";
    public static final String newAction  = "new";
    public static final String saveAction = "save";
    public static final String exitAction = "exit";
	
    private JTextComponent editor;
    private JMenuBar menubar;
    private JToolBar toolbar;
    private JComponent status;
    private PopupListener popupListener;

	public LucteriosEditor(boolean withFileAction)
	{
		super(true);
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BorderLayout());
		
		// create the embedded JTextComponent
		editor = createEditor();

        popupListener = new PopupListener();
        popupListener.setActions(editor.getActions());
        popupListener.addEditionMenu(true);
		editor.getDocument().addUndoableEditListener(popupListener.getUndo());
	
		JScrollPane scroller = new JScrollPane();
		JViewport port = scroller.getViewport();
		port.add(editor);
		port.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout());
		panel.add("North",createToolbar(withFileAction));
		panel.add("Center", scroller);
		add("Center", panel);
		add("South", createStatusbar());

        editor.addCaretListener(this);
        editor.addMouseListener(popupListener);
	}

    public void setOpaque(boolean isOpaque)
    {
    	super.setOpaque(isOpaque);
    	if (toolbar!=null)
    		toolbar.setOpaque(isOpaque);
    	if (status!=null)
            status.setOpaque(isOpaque);
    }
	
    public static void main(String[] args)
    {
        try {
			JFrame frame = new JFrame();
			frame.setTitle("Lucterios Editor");
			frame.setBackground(Color.lightGray);
			frame.getContentPane().setLayout(new BorderLayout());
						
			final LucteriosEditor editor = new LucteriosEditor(false);
			editor.addStandardPopupMenu(false);
        	editor.addSpecialMenus("MENU SPECIAL",new String[]{"AAA","BBB","CCC"},"{[","]}");        	
			frame.getContentPane().add(editor,BorderLayout.CENTER);
			frame.setJMenuBar(editor.createMenubar(false));
			frame.addWindowListener(new AppCloser());
			
			JPanel header=new JPanel();
			header.setLayout(new GridBagLayout());
			frame.getContentPane().add(header,BorderLayout.NORTH);
			final JTextArea code=new JTextArea();
			JScrollPane scrCode=new JScrollPane();
	        scrCode.setViewportView(code);
	        scrCode.setMinimumSize(new java.awt.Dimension(100,200));
	        scrCode.setPreferredSize(new java.awt.Dimension(200,200));
			GridBagConstraints cst;
			cst=new GridBagConstraints();
			cst.gridx=0;
			cst.gridy=0;
			cst.gridheight=2;
			cst.gridwidth=1;
	        cst.fill = java.awt.GridBagConstraints.BOTH;
			header.add(scrCode,cst);
			JButton btn_load=new JButton();
			btn_load.setText("Load");
			btn_load.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					editor.load(code.getText());
				}
				
			});
			cst=new GridBagConstraints();
			cst.gridx=1;
			cst.gridy=0;
			cst.gridheight=1;
			cst.gridwidth=1;
			header.add(btn_load,cst);
			JButton btn_save=new JButton();
			btn_save.setText("Save");
			btn_save.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					code.setText(editor.save());
				}				
			});
			cst=new GridBagConstraints();
			cst.gridx=1;
			cst.gridy=1;
			cst.gridheight=1;
			cst.gridwidth=1;
			header.add(btn_save,cst);
			
			frame.pack();
			frame.setSize(500, 600);
			frame.setVisible(true);
        } catch (Throwable t) {
            System.out.println("uncaught exception: " + t);
            t.printStackTrace();
        }
    }

	public void load(String aText)
	{
		if(getEditor().getDocument() != null)
			getEditor().getDocument().removeUndoableEditListener(popupListener.getUndo());
		StyleContext sc = new StyleContext();
		DefaultStyledDocument doc=new DefaultStyledDocument(sc);
		LucteriosDocumentParser parse=new LucteriosDocumentParser(doc,sc);
		parse.loadDocument(aText);
		getEditor().setDocument(doc);
		getEditor().getDocument().addUndoableEditListener(popupListener.getUndo());
		popupListener.resetUndoManager();
		validate();
	}
	
	public void setEnabled(boolean aEnabled)
	{
		super.setEnabled(aEnabled);
		getEditor().setEnabled(aEnabled);
		getEditor().setEditable(aEnabled);
		toolbar.setEnabled(aEnabled);
		for(int idx=0;idx<toolbar.getComponentCount();idx++)
			toolbar.getComponent(idx).setEnabled(aEnabled);
		if (menubar!=null)
		{
			menubar.setEnabled(aEnabled);
			for(int idx=0;idx<menubar.getComponentCount();idx++)
				menubar.getComponent(idx).setEnabled(aEnabled);
		}
		if (popupListener.getPopup()!=null)
		{
			popupListener.getPopup().setEnabled(aEnabled);
			for(int idx=0;idx<popupListener.getPopup().getComponentCount();idx++)
				popupListener.getPopup().getComponent(idx).setEnabled(aEnabled);
		}
	}

	public String save()
	{
		LucteriosDocumentParser parse=new LucteriosDocumentParser((DefaultStyledDocument)getEditor().getDocument(),new StyleContext());
		return parse.saveDocument();
	}

    public Action[] getActions()
    {
		return TextAction.augmentList(editor.getActions(), defaultActions);
    }

    protected JTextComponent createEditor()
    {
		StyleContext sc = new StyleContext();
		DefaultStyledDocument doc = new DefaultStyledDocument(sc);
		JTextPane p = new JTextPane(doc);
		p.setDragEnabled(true);
        return p;
   }

    protected JTextComponent getEditor()
    {
		return editor;
    }

    static public final class AppCloser extends WindowAdapter
    {
        public void windowClosing(WindowEvent e) {
		    System.exit(0);
		}
    }

    protected Frame getFrame()
    {
		for (Container p = getParent(); p != null; p = p.getParent())
		{
			if (p instanceof Frame)
				return (Frame) p;
		}
		return null;
    }

    protected Container getToolbar() {
		return toolbar;
    }

    protected JMenuBar getMenubar() {
		return menubar;
    }

    protected Component createStatusbar() {
		status = new StatusBar();
		return status;
    }

    private Component createToolbar(boolean withFileAction) {
		toolbar = new JToolBar();
		if (withFileAction)
		{
			toolbar.add(createToolbarButton("new","new.gif"));
			toolbar.add(createToolbarButton("open","open.gif"));
			toolbar.add(createToolbarButton("save","save.gif"));
			toolbar.add(Box.createHorizontalStrut(5));
		}
		toolbar.add(createToolbarButton("cut-to-clipboard","cut.gif"));
		toolbar.add(createToolbarButton("copy-to-clipboard","copy.gif"));
		toolbar.add(createToolbarButton("paste-from-clipboard","paste.gif"));
		toolbar.add(Box.createHorizontalStrut(5));
		toolbar.add(createToolbarButton("font-bold","bold.gif"));
		toolbar.add(createToolbarButton("font-italic","italic.gif"));
		toolbar.add(createToolbarButton("font-underline","underline.gif"));
		toolbar.add(Box.createHorizontalGlue());
		return toolbar;
    }

    static final String ADD_SPECIAL="@ADD@";
    
    public void addSpecialMenus(String menuName,Object[] variables,String tagBegin,String tagEnd)
    {
		if (popupListener.getPopup()!=null)
		{
			int idx=0;
			while(idx<popupListener.getPopup().getComponentCount())
			{
				if (JMenuItem.class.isInstance(popupListener.getPopup().getComponent(idx)) && ((JMenuItem)popupListener.getPopup().getComponent(idx)).getText().equalsIgnoreCase(menuName))
					popupListener.getPopup().remove(idx);
				else
					idx++;
			}
			idx=popupListener.getPopup().getComponentCount()-1;
			while((idx>=0) && JSeparator.class.isInstance(popupListener.getPopup().getComponent(popupListener.getPopup().getComponentCount()-1)))
				popupListener.getPopup().remove(idx);
			
		}
    	if (variables!=null)
    	{
	        popupListener.getPopup().addSeparator();
	        JMenuItem menu_variables = new JMenu();
	        menu_variables.setText(menuName);
	        for(int index=0;index<variables.length;index++)
	        {
	        	String var_name=variables[index].toString();
	        	if (var_name.length()>0)
	        	{
		            JMenuItem variable_item = new JMenuItem();
		            variable_item.setText(var_name);
		            variable_item.setActionCommand(ADD_SPECIAL+tagBegin+var_name+tagEnd);
		            variable_item.addActionListener(this);
		            menu_variables.add(variable_item);
	        	}
	        }
	        popupListener.getPopup().add(menu_variables);
    	}
    }
    
    public void actionPerformed(ActionEvent event)
    {
    	String action_name=event.getActionCommand();
    	if (action_name.indexOf(ADD_SPECIAL)==0)
    	{
    		String special_to_add=action_name.substring(ADD_SPECIAL.length());
    		StyledDocument style=(StyledDocument)editor.getDocument();
    	    try 
    	    {
    	    	if (mDot!=mMark)
    	    		style.remove(Math.min(mDot,mMark),Math.abs(mMark-mDot));
				style.insertString(Math.min(mDot,mMark),special_to_add,null);
			} 
    	    catch (BadLocationException e) 
    	    {
				e.printStackTrace();
			}
    	}
    	
    }
    
    int mDot=0;
    int mMark=0;
	public void caretUpdate(CaretEvent e) 
	{
		mDot = e.getDot();
		mMark = e.getMark();
	} 
           
     protected JButton createToolbarButton(String cmd,String icon) {
    	URL url=Resources.class.getResource(icon);
    	JButton b;
    	if (url!=null)
    		b = new JButton(new ImageIcon(url));
		else
			b = new JButton();
        b.setRequestFocusEnabled(false);
        b.setMargin(new Insets(1,1,1,1));
		Action a = popupListener.getAction(cmd);
		if (a != null) {
			b.setActionCommand(cmd);
			b.addActionListener(a);
		} else {
			b.setEnabled(false);
		}
        return b;
    }
    
    public void addStandardPopupMenu(boolean withFileAction)
    {        
		if (withFileAction)
		{
			popupListener.getPopup().add(createFichierMenu(true));
			popupListener.getPopup().addSeparator();
		}
		popupListener.getPopup().add(createColorMenu());
		popupListener.getPopup().addSeparator();
			
		popupListener.getPopup().add(createFontMenu(true));
    }  


    public JMenuBar createMenubar(boolean withFileAction) {
		JMenuBar mb = new JMenuBar();
		if (withFileAction)
			mb.add(createFichierMenu(false));
    	mb.add(popupListener.createMenuItem("Undo","Annuler",false));
    	mb.add(popupListener.createMenuItem("Redo","Recommencer",false));		
    	mb.add(popupListener.createMenuItem("cut-to-clipboard","Couper",false));
    	mb.add(popupListener.createMenuItem("copy-to-clipboard","Copier",false));
    	mb.add(popupListener.createMenuItem("paste-from-clipboard","Coller",false));
		mb.add(createFontMenu(false));
		this.menubar = mb;
		return mb;
    }

    protected PropertyChangeListener createActionChangeListener(JMenuItem b) {
		return new ActionChangedListener(b);
    }

    JMenu createFichierMenu(boolean recreate) {
		JMenu menu = new JMenu("Fichier");
		menu.add(popupListener.createMenuItem("new","Nouveau",recreate));
		menu.add(popupListener.createMenuItem("open","Ouvrir",recreate));
		menu.add(popupListener.createMenuItem("save","Sauver",recreate));
		menu.addSeparator();
		menu.add(popupListener.createMenuItem("exit","Quitter",recreate));
		return menu;
    }

    JMenu createFontMenu(boolean recreate) {
    	JMenu menu = new JMenu("Font");
		menu.add(popupListener.createMenuItem("font-bold","Gras",recreate));
		menu.add(popupListener.createMenuItem("font-italic","Italique",recreate));
		menu.add(popupListener.createMenuItem("font-underline","Soulign�",recreate));
		return menu;
    }
    
    JMenu createColorMenu() {
		ActionListener a;
		JMenuItem mi;
		JMenu menu = new JMenu("Couleurs");
		mi = new JMenuItem("Rouge");
		mi.setHorizontalTextPosition(JButton.RIGHT);
		mi.setIcon(new ColoredSquare(Color.red));
		a = new StyledEditorKit.ForegroundAction("set-foreground-red", Color.red);
		mi.addActionListener(a);
		menu.add(mi);
		mi = new JMenuItem("Vert");
		mi.setHorizontalTextPosition(JButton.RIGHT);
		mi.setIcon(new ColoredSquare(Color.green));
		a = new StyledEditorKit.ForegroundAction("set-foreground-green", Color.green);
		mi.addActionListener(a);
		menu.add(mi);
		mi = new JMenuItem("Bleu");
		mi.setHorizontalTextPosition(JButton.RIGHT);
		mi.setIcon(new ColoredSquare(Color.blue));
		a = new StyledEditorKit.ForegroundAction("set-foreground-blue", Color.blue);
		mi.addActionListener(a);
		menu.add(mi);
		mi = new JMenuItem("Noir");
		mi.setHorizontalTextPosition(JButton.RIGHT);
		mi.setIcon(new ColoredSquare(Color.black));
		a = new StyledEditorKit.ForegroundAction("set-foreground-black", Color.black);
		mi.addActionListener(a);
		menu.add(mi);
		return menu;
    }

    private Action[] defaultActions = {
		new NewAction(),
		new OpenAction(),
        new SaveAction(),
		new ExitAction()
    };


    private class ActionChangedListener implements PropertyChangeListener {
        JMenuItem menuItem;

        ActionChangedListener(JMenuItem mi) {
            super();
            this.menuItem = mi;
        }
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (Action.NAME.equals( e.getPropertyName() )) {
                String text = (String) e.getNewValue();
                menuItem.setText(text);
            } else if ("enabled".equals( propertyName )) {
                Boolean enabledState = (Boolean) e.getNewValue();
                menuItem.setEnabled(enabledState.booleanValue());
            }
        }
    }

    class StatusBar extends JComponent {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public StatusBar() {
			super();
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		}

        public void paint(Graphics g) {
			super.paint(g);
		}

    }
    
    class OpenAction extends NewAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		OpenAction() {
			super(openAction);
		}

        public void actionPerformed(ActionEvent e) {
			Frame frame = getFrame();
            JFileChooser chooser = new JFileChooser();
            int ret = chooser.showOpenDialog(frame);

            if (ret != JFileChooser.APPROVE_OPTION) {
				return;
		    }

            File f = chooser.getSelectedFile();
			if (f.isFile() && f.canRead()) {
				frame.setTitle(f.getName());
				Thread loader = new FileLoader(f, editor.getDocument());
				loader.start();
			} else {
				JOptionPane.showMessageDialog(getFrame(),
						"Could not open file: " + f,
						"Error opening file",
						JOptionPane.ERROR_MESSAGE);
			}
		}
    }

    class SaveAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		SaveAction() {
			super(saveAction);
		}

        public void actionPerformed(ActionEvent e) {
            Frame frame = getFrame();
            JFileChooser chooser = new JFileChooser();
            int ret = chooser.showSaveDialog(frame);

            if (ret != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File f = chooser.getSelectedFile();
            frame.setTitle(f.getName());
            Thread saver = new FileSaver(f, editor.getDocument());
            saver.start();
		}
    }

    class NewAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		NewAction() {
			super(newAction);
		}

		NewAction(String nm) {
			super(nm);
		}

        public void actionPerformed(ActionEvent e) {

		    if(getEditor().getDocument() != null)
				getEditor().getDocument().removeUndoableEditListener(popupListener.getUndo());
		    getEditor().setDocument(new DefaultStyledDocument());
		    getEditor().getDocument().addUndoableEditListener(popupListener.getUndo());
		    popupListener.resetUndoManager();
	        validate();
		}
    }

    class ExitAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		ExitAction() {
			super(exitAction);
		}

        public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
    }

    class FileLoader extends Thread {

		FileLoader(File f, Document doc) {
			setPriority(4);
			this.f = f;
			this.doc = doc;
		}

        public void run() {
			try {
				// initialize the statusbar
				status.removeAll();
				JProgressBar progress = new JProgressBar();
				progress.setMinimum(0);
				progress.setMaximum((int) f.length());
				status.add(progress);
				status.revalidate();

				// try to start reading
				String text="";
				Reader in = new FileReader(f);
				char[] buff = new char[4096];
				int nch;
				while ((nch = in.read(buff, 0, buff.length)) != -1){
					text+=new String(buff, 0, nch);
					progress.setValue(progress.getValue() + nch);
				}
				in.close();
				load(text);
			}
			catch (IOException e) {
					final String msg = e.getMessage();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(getFrame(),
									"Could not open file: " + msg,
									"Error opening file",
									JOptionPane.ERROR_MESSAGE);
							}
					});
			}
			doc.addUndoableEditListener(popupListener.getUndo());
			// we are done... get rid of progressbar
			status.removeAll();
			status.revalidate();

			popupListener.resetUndoManager();
		}

		Document doc;
		File f;
    }

    class FileSaver extends Thread {
        Document doc;
        File f;

		FileSaver(File f, Document doc) {
			setPriority(4);
			this.f = f;
			this.doc = doc;
		}

        public void run() {
		    try {
				// initialize the statusbar
				status.removeAll();
				JProgressBar progress = new JProgressBar();
				progress.setMinimum(0);
				progress.setMaximum((int) doc.getLength());
				status.add(progress);
				status.revalidate();

				// start writing
				Writer out = new FileWriter(f);
				String text = save();
				out.write(text, 0, text.length());
				out.flush();
				out.close();
			}
			catch (IOException e) {
					final String msg = e.getMessage();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(getFrame(),
								"Could not save file: " + msg,
								"Error saving file",
								JOptionPane.ERROR_MESSAGE);
							}
					});
			}
			status.removeAll();
			status.revalidate();
		}
    }

    class ColoredSquare implements Icon {
		Color color;
		public ColoredSquare(Color c) {
			this.color = c;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color oldColor = g.getColor();
			g.setColor(color);
			g.fill3DRect(x,y,getIconWidth(), getIconHeight(), true);
			g.setColor(oldColor);
		}
		public int getIconWidth() { return 12; }
		public int getIconHeight() { return 12; }
    }

	public void close() {
	    editor=null;
	    menubar=null;
	    toolbar=null;
	    status=null;
	    popupListener=null;
	}

}