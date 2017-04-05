package net.egemsoft.utils;

import net.egemsoft.MainApp;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbiya on 20.03.2017.
 */
public class CheckPlatformVersions {

    static Logger logger = Logger.getLogger(CheckPlatformVersions.class);

    public static boolean isUpToDate(String key, String newVersion) {

        File file = new File(MainApp.FILE_PATH);

        if (!file.exists()) {
            logger.info("version file does not exist!");
            MainApp.checkVersionFile();
            return false;
        }

        List<String> oldVersionList = versionFile();
        for (String line : oldVersionList) {

            if (line.contains(key)) {

                if (line.split("=").length == 2) {
                    if (line.split("=")[1].equalsIgnoreCase(newVersion)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static List<String> versionFile() {

        List<String> versionList = new ArrayList<String>();

        try {

            BufferedReader br = new BufferedReader(new FileReader(MainApp.FILE_PATH));
            String line;
            while ((line = br.readLine()) != null) {
                versionList.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return versionList;
    }

}
