package in.kyle.ftp.ui.components.filerenderers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class LocalFileRender extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        File file = (File) value;
        label.setText(file.getName());
        label.setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
        label.setBorder(new EmptyBorder(3, 3, 3, 3));
        return label;
    }
}