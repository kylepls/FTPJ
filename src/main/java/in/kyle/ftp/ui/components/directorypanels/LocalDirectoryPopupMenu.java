package in.kyle.ftp.ui.components.directorypanels;

import in.kyle.ftp.FTPJ;
import in.kyle.ftp.internal.protocols.generic.Protocol;
import in.kyle.ftp.ui.components.DirectoryPanePopupMenu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Kyle on 9/12/2015.
 */
public class LocalDirectoryPopupMenu extends DirectoryPanePopupMenu {
    
    private JMenuItem upload, open, delete;
    private Protocol protocol;
    
    public LocalDirectoryPopupMenu(FTPJ ftpj, JList list) {
        super(ftpj, list);
        
        protocol = ftpj.getProtocol();
        
        upload = new JMenuItem("Upload");
        upload.addActionListener(e -> {
            try {
                protocol.upload(getSelectedFile(), protocol.getDirectory() + "/" + getSelectedFile().getName());
                ftpj.getFtpFrame().getRemoteDirectory().rebuildFiles();
            } catch (Exception e1) {
                e1.printStackTrace();
                new Error(e1);
            }
        });
        add(upload);
        
        delete = new JMenuItem("Delete");
        delete.addActionListener(e -> getSelectedFile().delete());
        add(delete);
        
        open = new JMenuItem("Open");
        open.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(getSelectedFile());
            } catch (IOException e1) {
                e1.printStackTrace();
                new Error(e1);
            }
        });
        add(open);
    }
    
    @Override
    public void onOpen(File selectedFile) {
        if (selectedFile != null) {
            upload.setVisible(true);
            delete.setVisible(true);
            open.setVisible(true);
        } else {
            upload.setVisible(false);
            delete.setVisible(false);
            open.setVisible(false);
        }
    }
}
