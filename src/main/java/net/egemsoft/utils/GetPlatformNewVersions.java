package net.egemsoft.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by enbiya on 20.03.2017.
 */
public class GetPlatformNewVersions {

    private static String mtsTestUrl = "http://mtspre.ekocbiyik.com/";
    private static String mtsRegresyonUrl = "http://mts-bugfix.ekocbiyik.com/MTS/faces/index";
    private static String mtsBugFixUrl = "http://ekocbiyik.com:7003/MTS/faces/index";

    private static String netyuzTestUrl = "http://netyuzfr.ekocbiyik.com/netyuz/login.seam";
    private static String netyuzRegresyonUrl = "http://ekocbiyik.com:7722/netyuz/login.seam";
//    private String netyuzBugFixUrl = "";

    private static String iysTestUrl = "http://ekocbiyik.com/iys/app/login";
    private static String iysRegresyonUrl = "http://ekocbiyik.com:9001/iys/app/login";
    private static String iysBugFixUrl = "http://ekocbiyik.com:9101/iys/app/login";

    public static Map<String, String> init() {

        Map<String, String> versionMap = new HashMap<String, String>();

        //MTS
        versionMap.put(KeyParameters.MTS_TEST, FindPlatformVersions.getMTSVersion(mtsTestUrl));
        versionMap.put(KeyParameters.MTS_REG, FindPlatformVersions.getMTSVersion(mtsRegresyonUrl));
        versionMap.put(KeyParameters.MTS_BUGFIX, FindPlatformVersions.getMTSVersion(mtsBugFixUrl));

        //NETYUZ
        versionMap.put(KeyParameters.NETYUZ_TEST, FindPlatformVersions.getNETYUZVersion(netyuzTestUrl));
        versionMap.put(KeyParameters.NETYUZ_REG, FindPlatformVersions.getNETYUZVersion(netyuzRegresyonUrl));

        //IYS
        versionMap.put(KeyParameters.IYS_TEST, FindPlatformVersions.getIYSVersion(iysTestUrl));
        versionMap.put(KeyParameters.IYS_REG, FindPlatformVersions.getIYSVersion(iysRegresyonUrl));
        versionMap.put(KeyParameters.IYS_BUGFIX, FindPlatformVersions.getIYSVersion(iysBugFixUrl));

        return versionMap;
    }

}
