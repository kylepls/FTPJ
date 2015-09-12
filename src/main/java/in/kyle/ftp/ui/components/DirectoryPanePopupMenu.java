package in.kyle.ftp.ui.components;

import in.kyle.ftp.FTPJ;
import in.kyle.ftp.random.Clipboard;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.io.File;

/**
 * Created by Kyle on 9/12/2015.
 */
public abstract class DirectoryPanePopupMenu extends JPopupMenu implements PopupMenuListener {
    
    private FTPJ ftpj;
    private JList list;
    private JMenuItem copyPath, copyName;
    
    public DirectoryPanePopupMenu(FTPJ ftpj, JList list) {
        this.ftpj = ftpj;
        this.list = list;
        
        copyName = new JMenuItem("Copy file name to clipboard");
        copyName.addActionListener(e -> Clipboard.copy(getSelectedFile().getName()));
        add(copyName);
        
        copyPath = new JMenuItem("Copy path to clipboard");
        copyPath.addActionListener(e -> Clipboard.copy(getSelectedFile().getPath()));
        add(copyPath);
        
        addPopupMenuListener(this);
    }
    
    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        File file = getSelectedFile();
        onOpen(file);
        
        if (file == null) {
            copyPath.setVisible(false);    
            copyName.setVisible(false);
        } else {
            copyPath.setVisible(true);
            copyName.setVisible(true);
        }
    }
    
    public abstract void onOpen(File selectedFile);
    
    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }
    
    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
    }
    
    protected File getSelectedFile() {
        Object value = list.getSelectedValue();
        if (value != null) {
            return (File) value;
        } else {
            return null;
        }
    }
}
