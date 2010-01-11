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

package org.lucterios.Print;

import java.awt.*;
import java.awt.print.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.*;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.fop.render.awt.viewer.Command;
import org.apache.fop.render.awt.viewer.PreviewPanel;
import org.apache.fop.render.awt.viewer.Renderable;
import org.apache.fop.render.awt.viewer.Translator;
import org.lucterios.Print.FopGenerator.ClosePreview;
import org.lucterios.Print.resources.Resources;
import org.lucterios.utils.graphic.JAdvancePanel;

public class PrintPreviewPanel extends JAdvancePanel 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** The Translator for localization */
    protected Translator translator;
    /** The AWT renderer */
    protected AWTRenderer renderer;
    /** The FOUserAgent associated with this window */
    protected FOUserAgent foUserAgent;
    /**
     * Renderable instance that can be used to reload and re-render a document after 
     * modifications.
     */
    protected Renderable renderable;

    /** The JCombobox to rescale the rendered page view */
    private JComboBox scale;

    /** The JLabel for the process status bar */
    private JLabel processStatus;

    /** The JLabel information status bar */
    private JLabel infoStatus;

    /** The main display area */
    private PreviewPanel previewPanel;

    /** Formats the text in the scale combobox. */
    private DecimalFormat percentFormat = new DecimalFormat("###0.0#",
                                            new DecimalFormatSymbols(Locale.ENGLISH));

    protected ClosePreview mClosePreview=null;
    
    /**
     * Creates a new PreviewDialog that uses the given renderer.
     * @param foUserAgent the user agent
     * @param renderable the Renderable instance that is used to reload/re-render a document
     *                   after modifications.
     */
    public PrintPreviewPanel(FOUserAgent foUserAgent, Renderable renderable,ClosePreview aClosePreview) {
        renderer = (AWTRenderer) foUserAgent.getRendererOverride();
        this.foUserAgent = foUserAgent;
        this.renderable = renderable;
        translator = new Translator();
        mClosePreview=aClosePreview;

        setLayout(new GridBagLayout());
        GridBagConstraints gdbComp;
        
        //Commands aka Actions
        Command printAction = new Command(translator.getString("Menu.Print"), "Print") {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void doit() {
                startPrinterJob(true);
            }
        };
        Command firstPageAction = new Command(translator.getString("Menu.First.page"),
                                      "firstpg") {
            /**
										 * 
										 */
										private static final long serialVersionUID = 1L;

			public void doit() {
                goToFirstPage();
            }
        };
        Command previousPageAction = new Command(translator.getString("Menu.Prev.page"),
                                         "prevpg") {
            /**
											 * 
											 */
											private static final long serialVersionUID = 1L;

			public void doit() {
                goToPreviousPage();
            }
        };
        Command nextPageAction = new Command(translator.getString("Menu.Next.page"), "nextpg") {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void doit() {
                goToNextPage();
            }

        };
        Command lastPageAction = new Command(translator.getString("Menu.Last.page"), "lastpg") {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void doit() {
                goToLastPage();
            }
        };
        Command reloadAction = new Command(translator.getString("Menu.Reload"), "reload") {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void doit() {
                previewPanel.reload();
            }
        };

        
        //Sets size to be 61%x90% of the screen size
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        // Needed due to bug in Sun's JVM 1.5 (6429775)
        //Rather frivolous size - fits A4 page width in 1024x768 screen on my desktop
        setSize(screen.width * 61 / 100, screen.height * 9 / 10);

        //Page view stuff
        previewPanel = new PreviewPanel(foUserAgent, renderable, renderer);
        gdbComp = new GridBagConstraints();
        gdbComp.gridx = 0;
        gdbComp.gridy = 1;
        gdbComp.fill = GridBagConstraints.BOTH;
        gdbComp.weightx = 1;
        gdbComp.weighty = 1;
        gdbComp.insets=new Insets(1,20,1,20);
        JScrollPane scrol=new JScrollPane(previewPanel);
        previewPanel.setOpaque(false);
        scrol.setOpaque(false);
        add(scrol, gdbComp);

        // Keyboard shortcuts - pgup/pgdn
        InputMap im = previewPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "nextPage");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "prevPage");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "firstPage");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "lastPage");
        previewPanel.getActionMap().put("nextPage", nextPageAction);
        previewPanel.getActionMap().put("prevPage", previousPageAction);
        previewPanel.getActionMap().put("firstPage", firstPageAction);
        previewPanel.getActionMap().put("lastPage", lastPageAction);

        //Scaling combobox
        scale = new JComboBox();
        scale.addItem(translator.getString("Menu.Fit.Window"));
        scale.addItem(translator.getString("Menu.Fit.Width"));
        scale.addItem("25%");
        scale.addItem("50%");
        scale.addItem("75%");
        scale.addItem("100%");
        scale.addItem("150%");
        scale.addItem("200%");
        scale.setMaximumSize(new Dimension(80, 24));
        scale.setPreferredSize(new Dimension(80, 24));
        scale.setSelectedItem("100%");
        scale.setEditable(true);
        scale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaleActionPerformed(e);
            }
        });

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.add(printAction);
        toolBar.add(reloadAction);
        toolBar.addSeparator();
        toolBar.add(firstPageAction);
        toolBar.add(previousPageAction);
        toolBar.add(nextPageAction);
        toolBar.add(lastPageAction);
        toolBar.addSeparator(new Dimension(20, 0));
        toolBar.add(new JLabel(translator.getString("Menu.Zoom") + " "));
        toolBar.add(scale);
		if (mClosePreview!=null){
	        toolBar.addSeparator();
	        Action act=new AbstractAction("Quitter",new ImageIcon(Resources.class.getResource("exit.png")))
			{
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
	            	mClosePreview.close();
	            }
			};
			toolBar.add(act);
		}
        gdbComp = new GridBagConstraints();
        gdbComp.gridx = 0;
        gdbComp.gridy = 0;
        gdbComp.fill = GridBagConstraints.BOTH;
        gdbComp.weightx = 1;
        gdbComp.weighty = 0;
        toolBar.setOpaque(false);
        add(toolBar, gdbComp);

        // Status bar
        JPanel statusBar = new JPanel();
        processStatus = new JLabel();
        processStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(0, 3, 0, 0)));
        infoStatus = new JLabel();
        infoStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(0, 3, 0, 0)));

        statusBar.setLayout(new GridBagLayout());

        processStatus.setPreferredSize(new Dimension(200, 21));
        processStatus.setMinimumSize(new Dimension(200, 21));

        infoStatus.setPreferredSize(new Dimension(100, 21));
        infoStatus.setMinimumSize(new Dimension(100, 21));
        statusBar.add(processStatus,
                      new GridBagConstraints(0, 0, 1, 0, 2.0, 0.0,
                                             GridBagConstraints.CENTER,
                                             GridBagConstraints.HORIZONTAL,
                                             new Insets(0, 0, 0, 3), 0, 0));
        statusBar.add(infoStatus,
                      new GridBagConstraints(1, 0, 1, 0, 1.0, 0.0,
                                             GridBagConstraints.CENTER,
                                             GridBagConstraints.HORIZONTAL,
                                             new Insets(0, 0, 0, 0), 0, 0));
        gdbComp = new GridBagConstraints();
        gdbComp.gridx = 0;
        gdbComp.gridy = 2;
        gdbComp.fill = GridBagConstraints.BOTH;
        gdbComp.weightx = 1;
        gdbComp.weighty = 0;    
        statusBar.setOpaque(false);
        add(statusBar, gdbComp);
    }
    
    /**
     * Creates a new PreviewDialog that uses the given renderer.
     * @param foUserAgent the user agent
     */
    public PrintPreviewPanel(FOUserAgent foUserAgent,ClosePreview aClosePreview) {
        this(foUserAgent, null,aClosePreview);
    }  

    /** {@inheritDoc} */
    public void notifyRendererStopped() {
        reload();
    }

    public void reload() {
        setStatus(translator.getString("Status.Show"));
        previewPanel.reload();
        notifyPageRendered();
    }
    
    /**
     * Changes the current visible page
     * @param number the page number to go to
     */
    public void goToPage(int number) {
        if (number != previewPanel.getPage()) {
            previewPanel.setPage(number);
            notifyPageRendered();
        }
    }

    /**
     * Shows the previous page.
     */
    public void goToPreviousPage() {
        int page = previewPanel.getPage();
        if (page > 0) {
            goToPage(page - 1);
        }
    }

    /**
     * Shows the next page.
     */
    public void goToNextPage() {
        int page = previewPanel.getPage();
        if (page < renderer.getNumberOfPages() - 1) {
            goToPage(page + 1);
        }
    }

    /** Shows the first page. */
    public void goToFirstPage() {
        goToPage(0);
    }

    /**
     * Shows the last page.
     */
    public void goToLastPage() {
        goToPage(renderer.getNumberOfPages() - 1);
    }

    /** Scales page image */
    public void setScale(double scaleFactor) {
        scale.setSelectedItem(percentFormat.format(scaleFactor) + "%");
        previewPanel.setScaleFactor(scaleFactor / 100d);
    }

    public void setScaleToFitWindow() {
        try {
            setScale(previewPanel.getScaleToFitWindow() * 100);
        } catch (FOPException fopEx) {
            fopEx.printStackTrace();
        }
    }
    
    public void setScaleToFitWidth() {
        try {
            setScale(previewPanel.getScaleToFitWidth() * 100);
        } catch (FOPException fopEx) {
            fopEx.printStackTrace();
        }
    }

    private void scaleActionPerformed(ActionEvent e) {
        try {
            int index = scale.getSelectedIndex();
            if (index == 0) {
                setScale(previewPanel.getScaleToFitWindow() * 100);
            } else if (index == 1) {
                setScale(previewPanel.getScaleToFitWidth() * 100);
            } else {
                String item = (String)scale.getSelectedItem();
                setScale(Double.parseDouble(item.substring(0, item.indexOf('%'))));
            }
        } catch (FOPException fopEx) {
            fopEx.printStackTrace();
        }
    }

    /** Prints the document */
    public void startPrinterJob(boolean showDialog) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPageable(renderer);
        if (!showDialog || pj.printDialog()) {
            try {
                pj.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets message to be shown in the status bar in a thread safe way.
     * @param message the message
     */
    public void setStatus(String message) {
        SwingUtilities.invokeLater(new ShowStatus(message));
    }

    /** This class is used to show status in a thread safe way. */
    private class ShowStatus implements Runnable {

        /** The message to display */
        private String message;

        /**
         * Constructs ShowStatus thread
         * @param message message to display
         */
        public ShowStatus(String message) {
            this.message = message;
        }

        public void run() {
            processStatus.setText(message.toString());
        }
    }

    /**
     * Updates the message to be shown in the info bar in a thread safe way.
     */
    public void notifyPageRendered() {
        SwingUtilities.invokeLater(new ShowInfo());
    }

    /** This class is used to show info in a thread safe way. */
    private class ShowInfo implements Runnable {

        public void run() {
            String message = translator.getString("Status.Page") + " "
                    + (previewPanel.getPage() + 1) + " "
                    + translator.getString("Status.of") + " "
                    + (renderer.getNumberOfPages());
            infoStatus.setText(message);
        }
    }

    /**
     * Opens standard Swing error dialog box and reports given exception details.
     * @param e the Exception
     */
    public void reportException(Exception e) {
        String msg = translator.getString("Exception.Occured");
        setStatus(msg);
        JOptionPane.showMessageDialog(this,
            "<html><b>" + msg + ":</b><br>"
                + e.getClass().getName() + "<br>"
                + e.getMessage() + "</html>",
            translator.getString("Exception.Error"),
            JOptionPane.ERROR_MESSAGE
        );
    }
}
