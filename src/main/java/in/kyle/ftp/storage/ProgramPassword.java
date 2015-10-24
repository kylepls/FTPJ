package in.kyle.ftp.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.io.*;

/**
 * Created by Kyle on 9/13/2015.
 */
@Data
public class ProgramPassword {
    
    private static final File saveFile = new File("password.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private transient byte[] password;
    
    private String passwordSalt = "";
    private String passwordHash = "";
    
    public void save() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveFile));
        bufferedWriter.write(gson.toJson(this));
        bufferedWriter.close();
    }
    
    public static ProgramPassword get() throws FileNotFoundException {
        if (saveFile.exists())  {
            return gson.fromJson(new FileReader(saveFile), ProgramPassword.class);
        } else {
            return new ProgramPassword();
        }
    }
}
