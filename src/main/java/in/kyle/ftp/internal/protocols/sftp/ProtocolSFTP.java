package in.kyle.ftp.internal.protocols.sftp;

import com.jcraft.jsch.*;
import in.kyle.ftp.internal.protocols.generic.Protocol;
import in.kyle.ftp.internal.protocols.generic.ProtocolCredentials;
import in.kyle.ftp.internal.protocols.generic.ProtocolFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kyle on 9/13/2015.
 */
public class ProtocolSFTP extends Protocol {
    
    private Session session;
    private ChannelSftp channel;
    
    public ProtocolSFTP(ProtocolCredentials protocolCredentials) {
        super(protocolCredentials);
    }
    
    @Override
    public boolean connect() {
        try {
            session = new JSch().getSession(protocolCredentials.getUsername(), protocolCredentials.getAddress(), protocolCredentials
                    .getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(new String(protocolCredentials.getPassword()));
            session.connect();
    
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            return true;
        } catch (JSchException e) {
            return false;
        }
    }
    
    @Override
    public boolean exists(String file) throws Exception {
        try {
            channel.ls(file);
            return true;
        } catch (SftpException e) {
            if (e.id != 2) {
                throw e;
            }
            return false;
        }
    }
    
    @Override
    public List<ProtocolFile> listFiles(String directory) throws Exception {
        return (List<ProtocolFile>) channel.ls(directory).stream().map(o -> new SFTPFile(directory, (ChannelSftp.LsEntry) o)).collect
                (Collectors
                .toList());
    }
    
    @Override
    public void moveObject(String from, String to) throws Exception {
       // FileDirectory = "/appl/user/home/test/";
       /*
        channel.cd(FileDirectory+"temp/");
        if (sftp.get( newfile ) != null){
            sftp.rename(FileDirectory + "temp/" + newfile ,
                    FileDirectory + newfile );
            sftp.cd(FileDirectory);
            sftp.rm(existingfile );
        }
        */
    }
    
    @Override
    public void download(String path, OutputStream file) throws Exception {
        channel.get(path, file);
    }
    
    @Override
    public void upload(InputStream file, String path) throws Exception {
        channel.put(file, path);
    }
    
    @Override
    public void delete(String path) throws Exception {
        channel.rm(path);
    }
    
    @Override
    public String getHome() throws Exception {
        return channel.getHome();
    }
}
