package in.kyle.ftp.ui.components.directorypanels;

import in.kyle.ftp.FTPJ;
import in.kyle.ftp.internal.protocols.generic.FakeFile;
import in.kyle.ftp.internal.protocols.generic.Protocol;
import in.kyle.ftp.ui.components.DirectoryPane;
import in.kyle.ftp.ui.components.DirectoryPanePopupMenu;
import in.kyle.ftp.ui.components.filerenderers.RemoteFileRender;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Kyle on 9/12/2015.
 */
public class RemoteDirectory extends DirectoryPane {
    
    private Protocol protocol;
    
    public RemoteDirectory(FTPJ ftpj) throws Exception {
        super(ftpj);
        this.protocol = ftpj.getProtocol();
    
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList<FakeFile> list = (JList<FakeFile>) e.getSource();
            
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    FakeFile file = list.getModel().getElementAt(index);
                    if (file.isDirectory()) {
                        try {
                            setDirectory(file.getRemotePath());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        
        fileList.setCellRenderer(new RemoteFileRender());
        
        setDirectory("/");
    }
    
    public void setDirectory(String path) throws Exception {
        if (path.length() == 0) {
            path = "/";
        }
        directoryPath.setText(path);
        protocol.setDirectory(path);
        rebuildFiles();
    }
    
    public void rebuildFiles() throws Exception {
        FakeFile[] collect = protocol.listFiles(protocol.getDirectory()).stream().map(FakeFile::new).toArray(FakeFile[]::new);
        fileList.setListData(collect);
    }
    
    @Override
    public DirectoryPanePopupMenu getPopupMenu() {
        return new RemoteDirectoryPopupMenu(ftpj, fileList);
    }
    
    @Override
    public void onDirectoryEnter(String path) {
        protocol.setDirectory(path);
        try {
            rebuildFiles();
        } catch (Exception e1) {
            e1.printStackTrace();
            new Error(e1);
        }
    }
    
    @Override
    public boolean canGoUpDirectory() {
        return protocol.getDirectory().length() > 1;
    }
    
    
    
    @Override
    public void moveUpDirectory() throws Exception {
        setDirectory(protocol.getDirectory().substring(0, protocol.getDirectory().lastIndexOf("/")));
    }
}
