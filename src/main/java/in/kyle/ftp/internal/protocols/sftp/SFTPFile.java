package in.kyle.ftp.internal.protocols.sftp;

import in.kyle.ftp.internal.protocols.generic.ProtocolFile;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;

/**
 * Created by Kyle on 9/11/2015.
 */
public class SFTPFile extends ProtocolFile {
    
    private FileObject file;
    
    public SFTPFile(FileObject file) {
        this.file = file;
    }
    
    @Override
    public String getName() {
        return file.getName().getBaseName();
    }
    
    @Override
    public String getPath() {
        System.out.println("File " + file);
        return file.getName().getPath();
    }
    
    @Override
    public boolean isFile() {
        try {
            switch (file.getType()) {
                case FOLDER:
                    return false;
                case FILE:
                    return true;
                case FILE_OR_FOLDER:
                    return false;
                case IMAGINARY:
                    return false;
                default:
                    return false;
            }
        } catch (FileSystemException e) {
            e.printStackTrace();
            new Error(e);
            return false;
        }
    }
    
    @Override
    public boolean isDirectory() {
        try {
            return file.getType() == FileType.FOLDER;
        } catch (FileSystemException e) {
            e.printStackTrace();
            new Error(e);
            return false;
        }
    }
}
