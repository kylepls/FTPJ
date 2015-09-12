package in.kyle.ftp.internal.protocols.sftp;

import in.kyle.ftp.internal.protocols.generic.ProtocolCredentials;
import in.kyle.ftp.internal.protocols.generic.Protocol;
import in.kyle.ftp.internal.protocols.generic.ProtocolFile;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class SFTPUtil containing uploading, downloading, checking if file exists
 * and deleting functionality using Apache Commons VFS (Virtual File System)
 * Library
 *
 * @author Ashok
 * 
 * Modified by @Kyle
 */
public class ProtocolSFTP extends Protocol {
    
    private String username;
    private String password;
    private String hostname;
    private int port;
    private StandardFileSystemManager manager;
    private FileSystemOptions options;
    
    public ProtocolSFTP(ProtocolCredentials protocolCredentials) throws FileSystemException {
        super(protocolCredentials);
        this.username = protocolCredentials.getUsername();
        this.password = new String(protocolCredentials.getPassword());
        this.hostname = protocolCredentials.getAddress();
        this.port = protocolCredentials.getPort();
        this.manager = new StandardFileSystemManager();
        this.options = new FileSystemOptions();
        setDirectory("/home");
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(options, "no");
        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(options, false);
        SftpFileSystemConfigBuilder.getInstance().setTimeout(options, 10000);
    }
    
    @Override
    public void delete(String remoteFilePath) throws Exception {
        FileObject remoteFile = resolveRemoveFile(remoteFilePath);
        if (remoteFile.exists()) {
            remoteFile.delete();
        }
    }
    
    public boolean exist(String remoteFilePath) {
        try {
            FileObject remoteFile = resolveRemoveFile(remoteFilePath);
            return remoteFile.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public FileObject resolveRemoveFile(String path) throws FileSystemException {
        return manager.resolveFile(createConnectionString(path), options);
    }
    
    private String createConnectionString(String remoteFilePath) {
        return "sftp://" + username + ":" + password + "@" + hostname + "/" + remoteFilePath;
    }
    
    @Override
    public boolean connect() {
        try {
            manager.init();
            return true;
        } catch (FileSystemException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean exists(String file) throws FileSystemException {
        return resolveRemoveFile(file).exists();
    }
    
    @Override
    public List<ProtocolFile> listFiles(String directory) {
        try {
            FileObject fileObject = resolveRemoveFile(directory);
            System.out.println("File obj " + fileObject);
            return Arrays.stream(fileObject.getChildren()).map(SFTPFile::new).collect(Collectors.toList());
        } catch (FileSystemException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void moveObject(String from, String to) throws Exception {
        FileObject remoteFile = resolveRemoveFile(from);
        FileObject remoteDestFile = resolveRemoveFile(to);
    
        if (remoteFile.exists()) {
            remoteFile.moveTo(remoteDestFile);
        } else {
            throw new FileNotFoundException("Could not find in remote path " + from);
        }
    }
    
    @Override
    public void download(String path, File file) throws Exception {
        // Append _downlaod_from_sftp to the given file name.
        //String downloadFilePath = localFilePath.substring(0, localFilePath.lastIndexOf(".")) + "_downlaod_from_sftp" + 
        // localFilePath.substring(localFilePath.lastIndexOf("."), localFilePath.length());
    
        // Create local file object. Change location if necessary for new downloadFilePath
        FileObject localFile = manager.resolveFile(file.getAbsolutePath());
        FileObject remoteFile = resolveRemoveFile(path);
        localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
    }
    
    @Override
    public void upload(File file, String path) throws Exception {
        FileObject localFile = manager.resolveFile(file.getAbsolutePath());
        FileObject remoteFile = resolveRemoveFile(path);
        remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
    }
}