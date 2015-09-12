package in.kyle.ftp.ui.components.filerenderers;

import in.kyle.ftp.internal.protocols.generic.FakeFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;

public class RemoteFileRender extends DefaultListCellRenderer {
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        FakeFile file = (FakeFile) value;
        label.setText(file.getName());
        label.setIcon(FileSystemView.getFileSystemView().getSystemIcon(file.getSudoFile()));
        label.setBorder(new EmptyBorder(3, 3, 3, 3));
        return label;
    }
}