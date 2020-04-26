package anr.ui;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import anr.panels.AdvancedPanel;
import anr.panels.GeneralPanel;
import anr.panels.TerminalPanel;

public class GUIManager {

    private static GUIManager instance = null;

    // UI
    public static final int WINDOW_SIZE_WIDTH = 1024;
    public static final int WINDOW_SIZE_HEIGHT = 512;
    public static final String CUSTOM_FONT_NAME = "Dialog";
    public static final int CUSTOM_FONT_SIZE = 14;
    private JFrame frame;
    

    private GUIManager() 
    { 

    } 
  
    public static GUIManager getInstance() 
    { 
        if (instance == null) 
        instance = new GUIManager(); 
  
        return instance; 
    }

    /**
     * Sets up the user interface.
     */
    public void setupUI() {
        // java swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // main window
                frame = new JFrame("Unity-Java AutoWSD Dashboard");
                frame.setLayout(new BorderLayout());
                frame.setSize(WINDOW_SIZE_WIDTH, WINDOW_SIZE_HEIGHT);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // tabs
                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                JPanel generalPanel = GeneralPanel.getInstance().createGeneralPanel();
                JPanel advancedPanel = AdvancedPanel.getInstance().createAdvancedPanel();
                tabbedPane.add("General", generalPanel);
                tabbedPane.add("Advanced", advancedPanel);
                frame.add(tabbedPane, BorderLayout.CENTER);

                // terminal
                JPanel terminalPanel = TerminalPanel.getInstance().createTerminalPanel();
                frame.add(terminalPanel, BorderLayout.SOUTH);
            }
        });
    }

    /**
     * Displays a message.
     * 
     * @param _title    title
     * @param _message  message
     * @param _type     type
     */
    public void showWarningMessage(String _title, String _message, int _type) {
        JOptionPane.showMessageDialog(frame, _message, _title, _type);
    }
}