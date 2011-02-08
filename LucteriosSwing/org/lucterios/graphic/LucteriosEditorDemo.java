package org.lucterios.graphic;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class LucteriosEditorDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
        try {
			boolean with_file_action=true;
			JFrame frame = new JFrame();
			frame.setTitle("Lucterios Editor");
			frame.setBackground(Color.lightGray);
			frame.getContentPane().setLayout(new BorderLayout());
			LucteriosEditor editor = new LucteriosEditor(with_file_action);
			editor.addStandardPopupMenu(with_file_action);
        	editor.addSpecialMenus("MENU SPECIAL",new String[]{"AAA","BBB","CCC"},"{[","]}");
			
			frame.getContentPane().add("Center", editor);
			frame.setJMenuBar(editor.createMenubar(with_file_action));
			frame.pack();
			frame.setSize(500, 600);
			frame.setVisible(true);
        } catch (Throwable t) {
            System.out.println("uncaught exception: " + t);
            t.printStackTrace();
        }
	}

}
