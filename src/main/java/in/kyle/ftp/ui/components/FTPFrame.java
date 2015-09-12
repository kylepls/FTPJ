package in.kyle.ftp.ui.components;

import in.kyle.ftp.FTPJ;
import in.kyle.ftp.ui.components.directorypanels.LocalDirectory;
import in.kyle.ftp.ui.components.directorypanels.RemoteDirectory;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Kyle on 9/12/2015.
 */
public class FTPFrame extends JFrame {
    
    private JSplitPane splitPane;
    @Getter
    private LocalDirectory localDirectory;
    @Getter
    private RemoteDirectory remoteDirectory;
    
    public FTPFrame(FTPJ ftpj) throws Exception {
        super("FTPJ");
        
        localDirectory = new LocalDirectory(ftpj);
        remoteDirectory = new RemoteDirectory(ftpj);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, localDirectory.getRootPanel(), remoteDirectory.getRootPanel());
        
        setSize(900, 700);
        
        calculateDividerLocation();
        
        add(splitPane);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                calculateDividerLocation();
            }
        });
        
        setVisible(true);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ftpj.shutdown();
            }
        });
    }
    
    private void calculateDividerLocation() {
        splitPane.setDividerLocation(0.5);
    }
}
