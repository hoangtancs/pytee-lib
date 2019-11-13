package vn.pytee.network;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class HttpUtil {
	public static String getContent(String surl, int timeout) {
        URL url = null;
        Scanner scanner = null;
        try {
            url = new URL(surl);
            
            URLConnection con = url.openConnection();
            con.setConnectTimeout(timeout);
            con.setReadTimeout(timeout);
            
            InputStream in = con.getInputStream();
            scanner = new Scanner(in, "UTF-8");
            String content = scanner.useDelimiter("\\A").next();

            return content;
        } catch (Exception ex) {
        	ex.printStackTrace();
            System.err.println(surl + " /*timeout = " + timeout + "*/");
        } finally {
            if (scanner != null) {
                scanner.close();
            }

        }
        return "";
    }
}