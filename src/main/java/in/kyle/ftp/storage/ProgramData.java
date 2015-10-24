package in.kyle.ftp.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.kyle.ftp.encoding.EncryptionUtils;
import in.kyle.ftp.internal.protocols.generic.Protocols;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Created by Kyle on 9/12/2015.
 */
@Data
public class ProgramData {
    
    private static final File saveFile = new File("data.txt");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static ProgramPassword programPassword;
    
    private String masterPasswordHash = "";
    private String masterPasswordSalt = "";
    private String host = "";
    private String user;
    private int port = 22;
    private char[] pass = new char[0];
    private Protocols protocol = Protocols.SFTP;
    private String lastLocalDirectory = System.getProperty("user.home");
    
    public void save() throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveFile));
        byte[] bytes = gson.toJson(this).getBytes(Charset.forName("UTF-8"));
        String encrypt = EncryptionUtils.encrypt(programPassword.getPassword(), bytes);
        bufferedWriter.write(encrypt);
        bufferedWriter.close();
    }
    
    public static ProgramData get(ProgramPassword programPassword) throws Exception {
        ProgramData.programPassword = programPassword;
        if (saveFile.exists())  {
            String text = new String(Files.readAllBytes(saveFile.toPath()), Charset.forName("UTF-8"));
            text = EncryptionUtils.decrypt(programPassword.getPassword(), text.getBytes());
            return gson.fromJson(text, ProgramData.class);
        } else {
            return new ProgramData();
        }
    }
}
