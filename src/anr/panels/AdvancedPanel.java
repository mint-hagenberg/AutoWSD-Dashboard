package anr.panels;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import anr.ui.GUIManager;
import anr.conn.ConnectionManager;

public class AdvancedPanel {

    private static AdvancedPanel instance = null;

    private AdvancedPanel() 
    { 

    } 
  
    public static AdvancedPanel getInstance() 
    { 
        if (instance == null) 
        instance = new AdvancedPanel(); 
  
        return instance; 
    }

    /** 
     * Creates the Advanced content panel.
     */
    public JPanel createAdvancedPanel() {
        JPanel advancedPanel = new JPanel();
        advancedPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        // camera position Y (driver seat)
        JPanel cameraPositionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JLabel cameraPositionLabel = new JLabel("Camera Position Y");
        JButton increaseCameraPositionButton = new JButton("+");
        JButton decreaseCameraPositionButton = new JButton("-");
        JButton resetCameraPositionButton = new JButton("Reset");
        cameraPositionLabel.setFont(new Font(GUIManager.CUSTOM_FONT_NAME, Font.BOLD, GUIManager.CUSTOM_FONT_SIZE));
        cameraPositionPanel.add(cameraPositionLabel);
        cameraPositionPanel.add(increaseCameraPositionButton);
        cameraPositionPanel.add(decreaseCameraPositionButton);
        cameraPositionPanel.add(resetCameraPositionButton);

        // set grid
        gbc.gridy = 0; // first row
        advancedPanel.add(cameraPositionPanel, gbc);

        // buttons
        increaseCameraPositionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionManager.getInstance().sendData("cameraPositionY", "+");
            }
        });
        decreaseCameraPositionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionManager.getInstance().sendData("cameraPositionY", "-");
            }
        });
        resetCameraPositionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionManager.getInstance().sendData("cameraPositionY", "reset");
            }
        });

        return advancedPanel;
    }
}