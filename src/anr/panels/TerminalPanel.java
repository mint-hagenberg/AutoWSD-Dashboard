package anr.panels;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import anr.ui.GUIManager;

public class TerminalPanel {

    private static TerminalPanel instance = null;

    private JTextArea terminal = new JTextArea();
    private TitledBorder titledBorder = BorderFactory.createTitledBorder("Console");
    private JLabel connectionLabel = new JLabel();

    private TerminalPanel() 
    { 

    } 
  
    public static TerminalPanel getInstance() 
    { 
        if (instance == null) 
        instance = new TerminalPanel(); 
  
        return instance; 
    }

    /** 
     * Creates the Terminal panel.
     */
    public JPanel createTerminalPanel() {
        JPanel terminalPanel = new JPanel();
        terminalPanel.setPreferredSize(new Dimension(GUIManager.WINDOW_SIZE_WIDTH - 30, 180));
        terminalPanel.setMinimumSize(new Dimension(GUIManager.WINDOW_SIZE_WIDTH - 30, 180));
        terminalPanel.setBounds(0, 0, GUIManager.WINDOW_SIZE_WIDTH - 30, 180);

        // text area
        terminal = new JTextArea("");
        terminal.setEditable(false);
        terminal.setCursor(null);
        terminal.setWrapStyleWord(true);
        terminal.setLineWrap(true);

        // scroll panel for text area
        JScrollPane scrollPane = new JScrollPane(terminal);
        scrollPane.setViewportView(terminal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(GUIManager.WINDOW_SIZE_WIDTH - 30, 128));
        scrollPane.setMinimumSize(new Dimension(GUIManager.WINDOW_SIZE_WIDTH - 30, 128));
        scrollPane.setBounds(0, 0, GUIManager.WINDOW_SIZE_WIDTH - 30, 128);

        // title and border
        // titledBorder = BorderFactory.createTitledBorder("Console");
        titledBorder.setTitleJustification(TitledBorder.CENTER);

        terminalPanel.add(connectionLabel);
        terminalPanel.add(scrollPane);
        terminalPanel.setBorder(titledBorder);

        return terminalPanel;
    }

    /**
     * Updates the terminal text.
     * 
     * @param _text  text
     */
    public void setTerminalText(String _text) {
        if (terminal != null) {
            terminal.setText(_text);
        }
    }
}