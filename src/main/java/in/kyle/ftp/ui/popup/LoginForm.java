package in.kyle.ftp.ui.popup;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import in.kyle.ftp.internal.protocols.generic.ProtocolCredentials;
import in.kyle.ftp.internal.protocols.generic.Protocol;
import in.kyle.ftp.internal.protocols.generic.Protocols;
import in.kyle.ftp.storage.ProgramData;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

public class LoginForm extends JDialog {
    
    private boolean canceled;
    private JPanel contentPane = new JPanel();
    private JButton buttonOK = new JButton();
    private JButton buttonCancel = new JButton();
    private JTextField host = new JTextField();
    private JTextField username = new JTextField();
    private JTextField port = new JTextField();
    private JPasswordField password = new JPasswordField();
    private JButton testButton = new JButton();
    private JLabel testValue = new JLabel();
    private JComboBox connectionType;
    private JCheckBox savePass;
    private CountDownLatch lock = new CountDownLatch(1);
    private ProgramData data;
    
    public LoginForm(ProgramData data) {
        this.data = data;
        
        $$$setupUI$$$();
        setSize(500, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        
        buttonOK.addActionListener(e -> onOK());
        
        buttonCancel.addActionListener(e -> onCancel());
        
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        
        ((AbstractDocument) port.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                for (char c : text.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        return;
                    }
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        
        testButton.addActionListener(e -> {
            testConnection();
        });
        
        username.setText(data.getUser());
        password.setText(new String(data.getPass()));
        host.setText(data.getHost());
        port.setText(data.getPort() + "");
        connectionType.setSelectedItem(data.getProtocol());
    }
    
    private boolean testConnection() {
        testValue.setVisible(true);
        testValue.setForeground(Color.RED);
        
        if (host.getText().isEmpty()) {
            testValue.setText("Host is empty");
            return false;
        }
    
        if (port.getText().isEmpty()) {
            testValue.setText("Port is empty");
            return false;
        }
        
        if (username.getText().isEmpty()) {
            testValue.setText("Username is empty");
            return false;
        }
    
        if (password.getPassword().length == 0) {
            testValue.setText("Password is empty");
            return false;
        }
        
        testValue.setForeground(Color.BLUE);
        testValue.setText("Connecting...");
        
        ProtocolCredentials protocolCredentials = getFromEnteredValues();
        Protocol protocol = protocolCredentials.getProtocol();
        
        boolean connected = protocol.connect();
        
        if (connected) {
            try {
                protocol.exists("/");
                testValue.setText("Connected!");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                testValue.setForeground(Color.RED);
                testValue.setText(e.getMessage());
                return false;
            }
        } else {
            testValue.setForeground(Color.RED);
            testValue.setText("Unable to connect");
            return false;
        }
    }
    
    private ProtocolCredentials getFromEnteredValues() {
        return new ProtocolCredentials(username.getText(), password.getPassword(), host.getText(), Integer.parseInt(port.getText()), (Protocols) connectionType.getSelectedItem());
    }
    
    public ProtocolCredentials get() throws Exception {
        setVisible(true);
        lock.await();
        
        if (canceled) {
            return null;
        } else {
            data.setHost(host.getText());
            data.setPort(Integer.parseInt(port.getText()));
            data.setUser(username.getText());
            data.setProtocol((Protocols) connectionType.getSelectedItem());
            
            if (savePass.isSelected()) {
                data.setPass(password.getPassword());
            }
            
            data.save();
            
            return getFromEnteredValues();
        }
    }
    
    private void onOK() {
        if (testConnection()) {
            dispose();
            lock.countDown();
        } else {
            testValue.setVisible(true);
            testValue.setText("Could not connect to server");
        }
    }
    
    private void onCancel() {
        dispose();
        canceled = true;
        lock.countDown();
    }
    
    public static void main(String[] args) {
        LoginForm dialog = new LoginForm(new ProgramData());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
    
    private void createUIComponents() {
        connectionType = new JComboBox(Protocols.values());
    }
    
    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(500, 200));
        contentPane.setRequestFocusEnabled(true);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints
                .SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Okay");
        panel2.add(buttonOK, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, 
                null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, 
                null, null, 0, false));
        testButton = new JButton();
        testButton.setText("Test");
        panel2.add(testButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, 
                null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints
                .SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Enter the required information");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints
                .SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Username");
        panel4.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Password");
        panel4.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Host name");
        panel4.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Port");
        panel4.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        username = new JTextField();
        panel4.add(username, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        host = new JTextField();
        panel4.add(host, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints
                .SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        port = new JTextField();
        panel4.add(port, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints
                .SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        password = new JPasswordField();
        panel4.add(password, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        panel4.add(connectionType, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Type");
        panel4.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Save (Unsecure)");
        panel4.add(label7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        savePass = new JCheckBox();
        savePass.setText("(Saving password is highly unrecommended and not secure)");
        panel4.add(savePass, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, 
                false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, 
                GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel3.add(spacer4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, 
                GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        testValue = new JLabel();
        testValue.setText("This is a label, lol");
        testValue.setVisible(false);
        contentPane.add(testValue, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label2.setLabelFor(username);
        label5.setLabelFor(port);
    }
    
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
