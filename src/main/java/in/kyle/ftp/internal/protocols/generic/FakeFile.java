package in.kyle.ftp.internal.protocols.generic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Kyle on 9/12/2015.
 */
public class FakeFile extends File {
    
    private static File tempDirectory;
    
    static {
        try {
            tempDirectory = Files.createTempDirectory("").toFile();
            System.out.println("Temp directory set to " + tempDirectory.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private ProtocolFile protocolFile;
    private File sudoFile;
    
    public FakeFile(ProtocolFile protocolFile) {
        super(protocolFile.getPath());
        this.protocolFile = protocolFile;
        this.sudoFile = new File(tempDirectory, protocolFile.getPath());
        if (isDirectory()) {
            sudoFile.mkdirs();
        } else {
            sudoFile.getParentFile().mkdirs();
            try {
                sudoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public File getSudoFile() {
        return sudoFile;
    }
    
    @Override
    public String getName() {
        return protocolFile.getName();
    }
    
    @Override
    public boolean exists() {
        return true;
    }
    
    @Override
    public boolean isFile() {
        return protocolFile.isFile();
    }
    
    @Override
    public boolean isDirectory() {
        return protocolFile.isDirectory();
    }
    
    @Override
    public String getPath() {
        return sudoFile.getAbsolutePath();
    }
    
    public String getRemotePath() {
        return protocolFile.getPath();
    }
}
