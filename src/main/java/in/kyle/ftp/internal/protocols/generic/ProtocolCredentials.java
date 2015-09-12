package in.kyle.ftp.internal.protocols.generic;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Kyle on 9/11/2015.
 */
@Data
@AllArgsConstructor
public class ProtocolCredentials {
    
    private String username;
    private char[] password;
    private String address;
    private int port;
    private Protocols protocols;
    
    public Protocol getProtocol() {
        return protocols.getProtocol(this);
    }
}
