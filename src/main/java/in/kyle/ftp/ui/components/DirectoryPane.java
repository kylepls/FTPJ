package in.kyle.ftp.ui.components;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import in.kyle.ftp.FTPJ;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kyle on 9/12/2015.
 */
public abstract class DirectoryPane {
    
    protected String directory;
    
    @Getter
    protected JPanel rootPanel = new JPanel();
    protected JList fileList = new JList();
    protected JTextField directoryPath = new JTextField();
    private JButton upDir = new JButton();
    protected FTPJ ftpj;
    
    public DirectoryPane(FTPJ ftpj) {
        this.ftpj = ftpj;
        $$$setupUI$$$();
        directoryPath.addActionListener(e -> {
            onDirectoryEnter(directoryPath.getText());
        });
        fileList.setComponentPopupMenu(getPopupMenu());
        
        upDir.addActionListener(e -> {
            if (canGoUpDirectory()) {
                try {
                    moveUpDirectory();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    new Error(e1);
                }
            }
        });
        
        upDir.setText("\u2191");
    }
    
    public abstract DirectoryPanePopupMenu getPopupMenu();
    
    public abstract void onDirectoryEnter(String path);
    
    public abstract boolean canGoUpDirectory();
    
    public abstract void moveUpDirectory() throws Exception;
    
    private void createUIComponents() {
    }
    
    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        final JScrollPane scrollPane1 = new JScrollPane();
        rootPanel.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | 
                GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fileList = new JList();
        scrollPane1.setViewportView(fileList);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 3), -1, -1));
        rootPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints
                .SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        directoryPath = new JTextField();
        directoryPath.setText("path");
        panel1.add(directoryPath, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, 
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        upDir = new JButton();
        upDir.setMargin(new Insets(2, 14, 2, 14));
        upDir.setText("↑");
        panel1.add(upDir, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints
                .SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, 
                false));
    }
    
    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}