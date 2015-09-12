package in.kyle.ftp.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.kyle.ftp.internal.protocols.generic.Protocols;
import lombok.Data;

import java.io.*;

/**
 * Created by Kyle on 9/12/2015.
 */
@Data
public class ProgramData {
    
    private static final File saveFile = new File("data.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private String host = "";
    private String user;
    private int port = 22;
    private char[] pass = new char[0];
    private Protocols protocol = Protocols.SFTP;
    private String lastLocalDirectory = System.getProperty("user.home");
    
    public void save() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
        writer.write(gson.toJson(this));
        writer.close();
    }
    
    public static ProgramData get() throws FileNotFoundException {
        if (saveFile.exists())  {
            return gson.fromJson(new FileReader(saveFile), ProgramData.class);
        } else {
            return new ProgramData();
        }
    }
}
