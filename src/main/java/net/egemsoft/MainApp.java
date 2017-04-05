package net.egemsoft;

import net.egemsoft.utils.CheckPlatformVersions;
import net.egemsoft.utils.GetPlatformNewVersions;
import net.egemsoft.utils.PostNewVersion;
import net.egemsoft.utils.SavePlatformVersions;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by enbiya on 20.03.2017.
 */
public class MainApp {


    public static String FILE_PATH = System.getProperty("user.home") + "/ekocbiyik/oldversions.txt";

    private static Logger logger = Logger.getLogger(MainApp.class);

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        logger.info("Application has been started..");

//        new MainApp().start();
    }

    public static void checkVersionFile() {

        File file = new File(MainApp.FILE_PATH);
        try {

            if (file.exists()) {
                logger.info("version file already exist!");
                return;
            }

            file.createNewFile();
            String content = "MTS_TEST=Test20170317\r\n" +
                    "MTS_REG=4.5.7.4.3\r\n" +
                    "MTS_BUGFIX=4.5.7.3.3\r\n" +
                    "NETYUZ_TEST=7.6.7.1_FR7_B504_S4c9c0da\r\n" +
                    "NETYUZ_REG=7.5.5.0.0_FR7_B263_Safaee22\r\n" +
                    "IYS_TEST=5.22.2.0\r\n" +
                    "IYS_REG=5.23.0.1\r\n" +
                    "IYS_BUGFIX=5.22.1.0\r\n";


            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
            writer.write(content);
            writer.close();
            logger.info("version file has been created..");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "*/60 * * * * *")
    public void checkTaskIsUp() {
        logger.info("************** Program ayakta, çalışıyor: " + new Date() + " ***********************");
    }

    //    sec, min, hour, day, day_of_month, month, day_of_week, year
    @Scheduled(cron = "0 0/5 * * * *")
    public void startTask() {

//        "0 0 * * * *" = the top of every hour of every day.
//        "*/10 * * * * *" = every ten seconds.
//        "0 0/5 * * * ?" = Every 5 mins
//        "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
//        "0 0 6,19 * * *" = 6:00 AM and 7:00 PM every day.
//        "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
//        "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
//        "0 0 0 25 12 ?" = every Christmas Day at midnight
        logger.info("****************************Task Başladı: " + new Date() + "****************************");
        start();
        logger.info("****************************Task Bitti: " + new Date() + "****************************");

    }

    public void start() {

        //create version file
        checkVersionFile();

        Map<String, String> versionMap = GetPlatformNewVersions.init();
        logger.info("upToDate platform versions: " + versionMap);

        for (Map.Entry<String, String> entry : versionMap.entrySet()) {

            logger.info("checking for: " + entry.getKey() + "-" + entry.getValue());

            if (entry.getValue() != null) {

                if (!CheckPlatformVersions.isUpToDate(entry.getKey(), entry.getValue())) {

                    logger.info(entry.getKey() + "-" + entry.getValue() + " not upToDate!");

                    boolean result = PostNewVersion.sendVersion(entry.getKey(), entry.getValue());
                    if (result) {
                        SavePlatformVersions.save2File(entry.getKey(), entry.getValue());
                    }

                } else {
                    logger.info(entry.getKey() + "-" + entry.getValue() + " is already up to date!");
                }

            } else {
                logger.info(entry.getKey() + " için değer null olduğundan bu adımı atlayacak!");
            }
        }
    }

}
