package in.kyle.ftp.internal.protocols.generic;

import lombok.EqualsAndHashCode;

/**
 * Created by Kyle on 9/11/2015.
 */
@EqualsAndHashCode
public abstract class ProtocolFile {
    
    public abstract String getName();
    
    public abstract String getPath();
    
    public abstract boolean isFile();
  
    public abstract boolean isDirectory();
    
    @Override
    public String toString() {
        return getName();
    }
}
