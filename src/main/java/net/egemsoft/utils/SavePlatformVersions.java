package net.egemsoft.utils;

import net.egemsoft.MainApp;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by enbiya on 20.03.2017.
 */
public class SavePlatformVersions {

    static Logger logger = Logger.getLogger(SavePlatformVersions.class);

    public static void save2File(String key, String value) {

        List<String> versionFile = CheckPlatformVersions.versionFile();

        String newFile = "";
        for (String line : versionFile) {

            if (line.contains(key)) {
                newFile = newFile + line.split("=")[0] + "=" + value + "\r\n";
            } else {
                newFile = newFile + line + "\r\n";
            }
        }

        FileOutputStream fileOut = null;
        try {

            fileOut = new FileOutputStream(MainApp.FILE_PATH);
            fileOut.write(newFile.getBytes());
            fileOut.close();

            logger.info(key + " " + value + "has been changed from version file: \n" + newFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
