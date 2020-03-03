package vn.pytee.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

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

	public void sendPost(String url) throws Exception {
		HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();

		// add reuqest header
		httpClient.setRequestMethod("POST");
		httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
		httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		httpClient.setDoOutput(true);
		try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
			wr.writeBytes(urlParameters);
			wr.flush();
		}

		int responseCode = httpClient.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		try (BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {

			String line;
			StringBuilder response = new StringBuilder();

			while ((line = in.readLine()) != null) {
				response.append(line);
			}

			System.out.println(response.toString());
		}
	}
}