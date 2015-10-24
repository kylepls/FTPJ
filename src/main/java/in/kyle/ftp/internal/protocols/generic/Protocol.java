package in.kyle.ftp.internal.protocols.generic;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Kyle on 9/11/2015.
 */
public abstract class Protocol {
    
    protected ProtocolCredentials protocolCredentials;
    @Getter
    @Setter
    private String directory = "/";
    
    public Protocol(ProtocolCredentials protocolCredentials) {
        this.protocolCredentials = protocolCredentials;
    }
    
    public abstract boolean connect();
    
    public abstract boolean exists(String file) throws Exception;
    
    public abstract List<ProtocolFile> listFiles(String directory) throws Exception;
    
    public abstract void moveObject(String from, String to) throws Exception;
    
    public abstract void download(String path, OutputStream file) throws Exception;
    
    public abstract void upload(InputStream file, String path) throws Exception;
    
    public abstract void delete(String path) throws Exception;
    
    public abstract String getHome() throws Exception;
}
