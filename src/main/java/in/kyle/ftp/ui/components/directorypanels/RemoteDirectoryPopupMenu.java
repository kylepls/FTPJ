package in.kyle.ftp.ui.components.directorypanels;

import in.kyle.ftp.FTPJ;
import in.kyle.ftp.internal.protocols.generic.FakeFile;
import in.kyle.ftp.ui.components.DirectoryPanePopupMenu;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;

/**
 * Created by Kyle on 9/12/2015.
 */
public class RemoteDirectoryPopupMenu extends DirectoryPanePopupMenu {
    
    private JMenuItem download, delete;
    
    public RemoteDirectoryPopupMenu(FTPJ ftpj, JList list) {
        super(ftpj, list);
        
        download = new JMenuItem("Download");
        download.addActionListener(e -> {
            try {
                ftpj.getProtocol().download(getSelectedFakeFile().getRemotePath(), Files.newOutputStream(new File(ftpj.getFtpFrame().getLocalDirectory()
                        .getSelectedDirectory(), getSelectedFakeFile().getName()).toPath()));
                ftpj.getFtpFrame().getLocalDirectory().rebuildFiles();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        add(download);
        
        add(new JPopupMenu.Separator());
        
        delete = new JMenuItem("Delete");
        delete.addActionListener(e -> {
            try {
                ftpj.getProtocol().delete(getSelectedFakeFile().getRemotePath());
            } catch (Exception e1) {
                e1.printStackTrace();
                new Error(e1);
            }
        });
        add(delete);
    }
    
    @Override
    public void onOpen(File selectedFile) {
        if (selectedFile != null) {
            download.setVisible(true);
            delete.setVisible(true);
        } else {
            download.setVisible(false);
            delete.setVisible(false);
        }
    }
    
    private FakeFile getSelectedFakeFile() {
        return (FakeFile) getSelectedFile();
    }
}
