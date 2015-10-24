package in.kyle.ftp.ui.popup;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import in.kyle.ftp.encoding.EncryptionUtils;
import in.kyle.ftp.storage.ProgramPassword;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;

public class PasswordPopup extends JDialog {
    
    private JPanel contentPane = new JPanel();
    private JButton buttonOK = new JButton();
    private JButton buttonCancel = new JButton();
    private JPanel setupWindow = new JPanel();
    private JPanel loginWindow = new JPanel();
    private JPasswordField setupPassword1 = new JPasswordField();
    private JPasswordField setupPassword2 = new JPasswordField();
    private JLabel status = new JLabel();
    private JLabel password = new JLabel();
    private JPasswordField passwordField3 = new JPasswordField();
    private boolean setupPassword = true;
    private ProgramPassword data;
    
    public PasswordPopup(ProgramPassword data) {
        this.data = data;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        
        setupPassword = data.getPasswordHash().isEmpty();
        
        buttonOK.addActionListener(e -> onOK());
        
        buttonCancel.addActionListener(e -> onCancel());
    
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        
        buttonOK.setEnabled(false);
        
        if (setupPassword) {
            loginWindow.setVisible(false);
            KeyAdapter keyAdapter = new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (setupPassword1.getPassword().length < 5) {
                        status.setText("Password must be longer than 5 characters");
                        status.setForeground(Color.RED);
                    } else {
                        checkSetupPasswordsMatch();
                    }
                }
            };
            
            setupPassword1.addKeyListener(keyAdapter);
            setupPassword2.addKeyListener(keyAdapter);
        } else {
            setupWindow.setVisible(false);
            checkPasswordMatchesHash();
        }
        
        
        passwordField3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkPasswordMatchesHash();
            }
        });
        
        setSize(450, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private boolean checkSetupPasswordsMatch() {
        boolean match = Arrays.equals(setupPassword1.getPassword(), setupPassword2.getPassword());
        if (match) {
            status.setForeground(Color.BLUE);
            status.setText("Passwords match");
            buttonOK.setEnabled(true);
        } else {
            status.setForeground(Color.RED);
            status.setText("Passwords do not match");
            buttonOK.setEnabled(false);
        }
        return match;
    }
    
    private boolean checkPasswordMatchesHash() {
        
        if (passwordField3.getPassword().length == 0) {
            status.setText("Enter your password");
            status.setForeground(Color.RED);
            return false;
        }
        
        boolean match = EncryptionUtils.checkPassword(data.getPasswordHash(), data.getPasswordSalt(), new String(passwordField3.getPassword()));
    
    
        if (match) {
            buttonOK.setEnabled(true);
            status.setForeground(Color.BLUE);
            status.setText("\u2713");
        } else {
            status.setForeground(Color.RED);
            status.setText("\u2715");
            buttonOK.setEnabled(false);
        }
        return match;
    }
    
    private void onOK() {
        if (setupPassword) {
            EncryptionUtils.PasswordHash hash = EncryptionUtils.createPasswordHash(new String(setupPassword1.getPassword()));
            data.setPasswordHash(hash.getHash());
            data.setPasswordSalt(hash.getSalt());
    
            try {
                data.save();
            } catch (IOException e) {
                e.printStackTrace();
                new Error(e);
                return;
            }
    
            setupWindow.setVisible(false);
            loginWindow.setVisible(true);
            setupPassword = false;
            checkPasswordMatchesHash();
        } else {
            System.out.println("Decode...");
            data.setPassword(new String(passwordField3.getPassword()).getBytes());
            dispose();
        }
    }
    
    private void onCancel() {
        dispose();
        System.exit(0);
    }
    
    {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }
    
    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints
                .SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Enter");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, 
                null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Close");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, 
                null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints
                .SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints
                .SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        setupWindow = new JPanel();
        setupWindow.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(setupWindow, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints
                .SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Before you can open the program you must setup your master password");
        setupWindow.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Password");
        setupWindow.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setupPassword1 = new JPasswordField();
        setupWindow.add(setupPassword1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        setupPassword2 = new JPasswordField();
        setupWindow.add(setupPassword2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Retype password");
        setupWindow.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loginWindow = new JPanel();
        loginWindow.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(loginWindow, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints
                .SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Before you can open the program you must enter your password");
        loginWindow.add(label4, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        password = new JLabel();
        password.setText("Password");
        loginWindow.add(password, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField3 = new JPasswordField();
        loginWindow.add(passwordField3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        status = new JLabel();
        status.setEnabled(true);
        status.setText("Enter password");
        panel3.add(status, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
    
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
