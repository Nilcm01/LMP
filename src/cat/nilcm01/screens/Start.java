package cat.nilcm01.screens;

import javax.swing.*;

public class Start extends JFrame {
    private JPanel contentPane;
    private JLabel title;
    private JButton iniciaButton;
    private JLabel labelPathLibrary;
    private JFileChooser chooserPathLibrary;
    private JLabel labelPathDevice;
    private JTextField fieldPathLibrary;
    private JTextField fieldPathDevice;
    private JLabel subtitle;
    private JFileChooser chooserPathDevice;

    public Start() {
        JFrame frame = new JFrame("");
        setTitle("LMP - Llibreria Musical Portàtil");
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //pack();
        setSize(1280, 720);
        setLocationRelativeTo(null);
        //// START OF FRAME ////

        if (fieldPathLibrary.getText().isEmpty()) {
            fieldPathLibrary.setText("M:/HQ");
        }

        if (fieldPathDevice.getText().isEmpty()) {
            fieldPathDevice.setText("I:/Music");
        }

        iniciaButton.addActionListener(e -> {
            // Change the frame to Viewer
            Viewer viewer = new Viewer(fieldPathLibrary.getText(), fieldPathDevice.getText());

            // Hide the current frame
            setVisible(false);

            // Show the new frame
            viewer.setVisible(true);
        });

        //// END OF FRAME ////
        setVisible(true);
    }

    private void createUIComponents() {
        /*
        chooserPathLibrary = new JFileChooser();
        chooserPathLibrary.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooserPathLibrary.setAcceptAllFileFilterUsed(false);
        chooserPathLibrary.setDialogTitle("Selecciona la llibreria");
        //chooserPathLibrary.setCurrentDirectory(new java.io.File("M:/HQ"));
        chooserPathLibrary.setApproveButtonText("Selecciona");
        chooserPathLibrary.setApproveButtonToolTipText("Selecciona la llibreria");
        chooserPathLibrary.setMultiSelectionEnabled(false);

        chooserPathDevice = new JFileChooser();
        chooserPathDevice.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooserPathDevice.setAcceptAllFileFilterUsed(false);
        chooserPathDevice.setDialogTitle("Selecciona el dispositiu");
        //chooserPathDevice.setCurrentDirectory(new java.io.File("I:/Music"));
        chooserPathDevice.setApproveButtonText("Selecciona");
        chooserPathDevice.setApproveButtonToolTipText("Selecciona el dispositiu");
        chooserPathDevice.setMultiSelectionEnabled(false);
        */
    }
}
