package anr.panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import anr.ui.GUIManager;
import anr.conn.ConnectionManager;

public class GeneralPanel {

    private static GeneralPanel instance = null;

    private JTextField participantTextField = new JTextField();
    private JTextField conditionTextField = new JTextField();
    private JButton startStopButton = new JButton();
    private JButton pauseResumeButton = new JButton();
    private JLabel statusLabel = new JLabel();
    private JLabel connectionLabel = new JLabel();
    private String driveStatus = "stop";
    private String connectionStatus = "disconnected";

    private GeneralPanel() 
    { 

    } 
  
    public static GeneralPanel getInstance() 
    { 
        if (instance == null) 
        instance = new GeneralPanel(); 
  
        return instance; 
    }

    /** 
     * Creates the General content panel.
     */
    public JPanel createGeneralPanel() {
        JPanel generalPanel = new JPanel();
        // generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));
        generalPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        // participant
        JPanel participantPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JLabel participantLabel = new JLabel("Participant ID");
        participantTextField = new JTextField("", 2);
        participantLabel.setFont(new Font(GUIManager.CUSTOM_FONT_NAME, Font.BOLD, GUIManager.CUSTOM_FONT_SIZE));
        participantTextField.setFont(new Font(GUIManager.CUSTOM_FONT_NAME, Font.BOLD, GUIManager.CUSTOM_FONT_SIZE));
        participantPanel.add(participantLabel);
        participantPanel.add(participantTextField);

        // condition
        JPanel conditionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JLabel conditionLabel = new JLabel("Condition ID");
        conditionTextField = new JTextField("", 2);
        conditionLabel.setFont(new Font(GUIManager.CUSTOM_FONT_NAME, Font.BOLD, GUIManager.CUSTOM_FONT_SIZE));
        conditionTextField.setFont(new Font(GUIManager.CUSTOM_FONT_NAME, Font.BOLD, GUIManager.CUSTOM_FONT_SIZE));
        conditionPanel.add(conditionLabel);
        conditionPanel.add(conditionTextField);

        // start/stop/pause/resume
        JPanel sequencePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        startStopButton = new JButton("Start");
        pauseResumeButton = new JButton("Pause");
        startStopButton.setPreferredSize(new Dimension(100, 50));
        pauseResumeButton.setPreferredSize(new Dimension(100, 50));
        startStopButton.setBackground(Color.GREEN);
        pauseResumeButton.setBackground(Color.ORANGE);
        pauseResumeButton.setEnabled(false);
        sequencePanel.add(startStopButton);
        sequencePanel.add(pauseResumeButton);

        // title and border
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Control Panel");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        sequencePanel.setBorder(titledBorder);

        // drive status
        statusLabel = new JLabel("Status: -");

        // set grid
        gbc.gridy = 0; // first row
        generalPanel.add(participantPanel, gbc);
        gbc.gridy = 1; // second row
        generalPanel.add(conditionPanel, gbc);
        gbc.gridy = 2; // third row
        generalPanel.add(Box.createVerticalStrut(20), gbc); // fixed height invisible separator
        gbc.gridy = 3; // fourth row
        generalPanel.add(sequencePanel, gbc);
        gbc.gridy = 4;
        generalPanel.add(statusLabel, gbc);

        // buttons
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (driveStatus.equals("start") || driveStatus.equals("pause") || driveStatus.equals("resume")) {
                    driveStatus = "stop";
                } else {
                    // check participant and condition id
                    if (participantTextField.getText() == null || participantTextField.getText().isEmpty()
                            || conditionTextField.getText() == null || conditionTextField.getText().isEmpty()) {
                        // show alert
                        GUIManager.getInstance().showWarningMessage("Invalid Input", "Please specify a Participant ID and Condition ID!", JOptionPane.WARNING_MESSAGE);

                        return;
                    } else {
                        ConnectionManager.getInstance().sendData("participant", participantTextField.getText());
                        ConnectionManager.getInstance().sendData("condition", conditionTextField.getText());
                    }

                    driveStatus = "start";
                }
                updateUI();
                ConnectionManager.getInstance().sendData("startStop", driveStatus);
            }
        });
        pauseResumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (driveStatus.equals("pause")) {
                    driveStatus = "resume";
                    // pauseResumeButton.setText("Pause");
                } else /* if (driveStatus.equals("resume")) */ {
                    driveStatus = "pause";
                    // pauseResumeButton.setText("Resume");
                }
                // statusLabel.setText("Status: " + driveStatus);
                updateUI();
                ConnectionManager.getInstance().sendData("startStop", driveStatus);
            }
        });

        return generalPanel;
    }

    /** 
     * Updates the UI.
     */
    public void updateUI() {
        // connection status
        connectionLabel.setText("Connection: " + connectionStatus);

        // drive status
        if (driveStatus.equals("start")) {
            startStopButton.setText("Stop");
            startStopButton.setBackground(Color.RED);
            pauseResumeButton.setEnabled(true);
            participantTextField.setEnabled(false);
            conditionTextField.setEnabled(false);
        } else if (driveStatus.equals("stop")) {
            startStopButton.setText("Start");
            startStopButton.setBackground(Color.GREEN);
            pauseResumeButton.setText("Pause");
            pauseResumeButton.setEnabled(false);
            participantTextField.setEnabled(true);
            conditionTextField.setEnabled(true);
        } else if (driveStatus.equals("pause")) {
            pauseResumeButton.setText("Resume");
        } else if (driveStatus.equals("resume")) {
            pauseResumeButton.setText("Pause");
        }
        statusLabel.setText("Status: " + driveStatus);
    }

    /** 
     * Updates the drive status text.
     * 
     * @param _status  drive status
     */
    public void setDriveStatus(String _status) {
        driveStatus = _status;
        updateUI();
    }

    /**
     * Updates the connection status text.
     * 
     * @param _status  connection status
     */
    public void setConnectionStatus(String _status) {
        connectionStatus = _status;
        updateUI();
    }
}