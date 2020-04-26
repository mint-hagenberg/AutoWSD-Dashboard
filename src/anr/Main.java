package anr;

import anr.conn.ConnectionManager;
import anr.ui.GUIManager;

/**
 * GUI Dashboard for AutoWSD DriveSim Unity project.
 * Helpful for conducting user studies.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting Java AutoWSD Dashboard ...");

        GUIManager.getInstance().setupUI();
        ConnectionManager.getInstance().setupUdpConnection();
    } 
}
