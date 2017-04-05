package net.egemsoft.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by enbiya on 20.03.2017.
 */
public class FindPlatformVersions {

    public static String getMTSVersion(String url) {

        List<String> mtsPage = getResponseList(url);
        if (mtsPage == null) {
            return null;
        }

        String version = null;
        for (String line : mtsPage) {

            if (line.contains("Beni Hat") && line.contains("Telaura")) {
                version = line.split(";;white-space:nowrap\">Telaura")[1].split("</span>")[0].trim();
                break;
            }
        }

        return version;
    }

    public static String getNETYUZVersion(String url) {

        List<String> netyuzPage = getResponseList(url);
        if (netyuzPage == null) {
            return null;
        }

        String version = null;
        for (String line : netyuzPage) {

            if (line.contains("<span id=\"loginForm:menuVersiyon\">Version :")) {
                version = line.split("Version : ")[1].split("</span>")[0].trim();
                break;
            }
        }

        return version;
    }

    public static String getIYSVersion(String  url) {

        List<String> iysPage = getResponseList(url);
        if (iysPage == null) {
            return null;
        }

        String version = null;
        for (String line : iysPage) {

            if (line.contains("<div style=\"text-align: right\">Versiyon:")) {
                version = line.split("Versiyon:")[1].split("</div>")[0].trim();
                break;
            }
        }

        return version;
    }

    public static List<String> getResponseList(String url) {

        try {

            HttpGet httpGet = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {

                BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                return IOUtils.readLines(bf);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
