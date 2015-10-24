package in.kyle.ftp.internal.protocols.sftp;

import com.jcraft.jsch.ChannelSftp;
import in.kyle.ftp.internal.protocols.generic.ProtocolFile;

/**
 * Created by Kyle on 9/11/2015.
 */
public class SFTPFile extends ProtocolFile {
    
    private ChannelSftp.LsEntry file;
    private String directory;
    
    public SFTPFile(String directory, ChannelSftp.LsEntry file) {
        this.file = file;
        this.directory = directory;
        if (!this.directory.endsWith("/")) {
            this.directory += "/";
        }
    }
    
    @Override
    public String getName() {
        return file.getFilename();
    }
    
    @Override
    public String getPath() {
        return directory + file.getFilename();
    }
    
    @Override
    public boolean isFile() {
        return !file.getAttrs().isDir();
    }
    
    @Override
    public boolean isDirectory() {
        return file.getAttrs().isDir();
    }
}
