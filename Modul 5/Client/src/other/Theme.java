package other;

import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Theme
{
    public static Font getRegularFont(int fontSize)
    {
        try
        {
            return Font.loadFont(new FileInputStream(new File(
                    "src/files/Aller_Rg.ttf")), fontSize);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
