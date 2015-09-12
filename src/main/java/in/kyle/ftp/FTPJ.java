package in.kyle.ftp;

import in.kyle.ftp.event.EventManager;
import in.kyle.ftp.event.ShutDownEvent;
import in.kyle.ftp.internal.protocols.generic.Protocol;
import in.kyle.ftp.internal.protocols.generic.ProtocolCredentials;
import in.kyle.ftp.storage.ProgramData;
import in.kyle.ftp.ui.components.FTPFrame;
import in.kyle.ftp.ui.popup.LoginForm;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Kyle on 9/11/2015.
 */
public class FTPJ {
    
    private ProgramData data;
    private EventManager eventManager;
    private ProtocolCredentials credentials;
    private Protocol protocol;
    private FTPFrame ftpFrame;
    
    public static void main(String[] args) {
        try {
            new FTPJ();
        } catch (Exception e) {
            e.printStackTrace();
            new Error(e);
        }
    }
    
    public FTPJ() throws Exception {
        System.out.println("Loading data");
        data = ProgramData.get();
    
        System.out.println("Loading event manager");
        eventManager = new EventManager();
        
        System.out.println("Setting UI style");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    
        System.out.println("Getting credentials");
        credentials = new LoginForm(data).get();
        
        if (credentials != null) {
            protocol = credentials.getProtocol();
            protocol.connect();
            ftpFrame = new FTPFrame(this);
        }
    }
    
    public Protocol getProtocol() {
        return protocol;
    }
    
    public ProtocolCredentials getCredentials() {
        return credentials;
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
    
    public void shutdown()  {
        eventManager.fire(new ShutDownEvent());
        try {
            data.save();
        } catch (IOException e) {
            e.printStackTrace();
            new Error(e);
        }
        System.exit(0);
    }
    
    public ProgramData getData() {
        return data;
    }
    
    public FTPFrame getFtpFrame() {
        return ftpFrame;
    }
}
