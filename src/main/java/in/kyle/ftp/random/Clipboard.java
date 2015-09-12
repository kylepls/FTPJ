package in.kyle.ftp.random;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * Created by Kyle on 9/12/2015.
 */
@UtilityClass
public class Clipboard {
    
    public static void copy(String text) {
        StringSelection selection = new StringSelection(text);
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
    
}
