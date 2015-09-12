package in.kyle.ftp.internal.protocols.generic;

import in.kyle.ftp.internal.protocols.sftp.ProtocolSFTP;

/**
 * Created by Kyle on 9/12/2015.
 */
public enum Protocols {
    
    SFTP(ProtocolSFTP.class);
    
    private final Class aClass;
    
    Protocols(Class<? extends Protocol> aClass) {
        this.aClass = aClass;
    }
    
    public Protocol getProtocol(ProtocolCredentials protocolCredentials) {
        try {
            return (Protocol) aClass.getConstructor(ProtocolCredentials.class).newInstance(protocolCredentials);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
