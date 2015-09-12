package in.kyle.ftp.ui.components.directorypanels;

import in.kyle.ftp.FTPJ;
import in.kyle.ftp.event.ShutDownEvent;
import in.kyle.ftp.random.FileWatcher;
import in.kyle.ftp.ui.components.DirectoryPane;
import in.kyle.ftp.ui.components.DirectoryPanePopupMenu;
import in.kyle.ftp.ui.components.filerenderers.LocalFileRender;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by Kyle on 9/12/2015.
 */
public class LocalDirectory extends DirectoryPane {
    
    @Getter
    private File selectedDirectory;
    private FileWatcher fileWatcher;
    
    public LocalDirectory(FTPJ ftpj) throws IOException {
        super(ftpj);
        
        File dir = new File(ftpj.getData().getLastLocalDirectory());
        if (!dir.exists()) {
            dir = new File(System.getProperty("user.home"));
        }
        selectedDirectory = dir;
        this.fileWatcher = new FileWatcher(this::rebuildFiles, selectedDirectory);
        setDirectory(selectedDirectory);
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList<File> list = fileList;
        
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    File file = list.getModel().getElementAt(index);
                    if (file.isDirectory()) {
                        setDirectory(file);
                    }
                }
            }
        });
        fileList.setCellRenderer(new LocalFileRender());
        ftpj.getEventManager().register(this);
    }
    
    @Override
    public DirectoryPanePopupMenu getPopupMenu() {
        return new LocalDirectoryPopupMenu(ftpj, fileList);
    }
    
    private void setDirectory(File file) {
        selectedDirectory = file;
        directoryPath.setText(selectedDirectory.getAbsolutePath());
        try {
            fileWatcher.setDirectory(selectedDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            new Error(e);
        }
        rebuildFiles();
    }
    
    public void rebuildFiles() {
        fileList.setListData(selectedDirectory.listFiles());
    }
    
    @Override
    public void onDirectoryEnter(String path) {
        File file = new File(path);
        if (file.exists()) {
            setDirectory(file);
        } else {
            directoryPath.setText(selectedDirectory.getAbsolutePath());
        }
    }
    
    @Override
    public boolean canGoUpDirectory() {
        return selectedDirectory.getParentFile().exists();
    }
    
    @Override
    public void moveUpDirectory() {
        setDirectory(selectedDirectory.getParentFile());
    }
    
    public void onClose(ShutDownEvent e) {
        ftpj.getData().setLastLocalDirectory(selectedDirectory.getAbsolutePath());
    }
}
