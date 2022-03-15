package org.practicefx;

import java.io.IOException;
import java.net.Authenticator;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * Http 请求工具类 
 * @author eron
 *
 */
public class HttpClientUtils {
	
	private static final Logger LOGGER = Logger.getLogger(HttpClientUtils.class.getName());
	
	private static HttpClient client = null;
	
	public static HttpClient getDefaultHttpClient() {
		if(client == null) {
			client = HttpClient.newBuilder()
			        .followRedirects(Redirect.NORMAL)
			        .connectTimeout(Duration.ofSeconds(5))
			        //.proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
			        //.authenticator(Authenticator.getDefault())
			        .version(Version.HTTP_2)
			        .build();
		}
		
		return client;
	}
	
	public static HttpResponse<String> httpPost(String url, String requestBody) {
		LOGGER.info("http POST URL ==> " + url);

		HttpClient client = HttpClientUtils.getDefaultHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
		        .uri(URI.create(url))
		        .header("Content-Type", "application/json")
		        .POST(BodyPublishers.ofString(requestBody))
		        .build();
		
		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			LOGGER.severe("ERROR : ==> \n" + e.toString());
		}
		
		LOGGER.info( "response ==> \nResponse Status ==> " + response.statusCode() 
					+ "\nResponse Body ==> " + response.body() );
		
		return response;
		
	}
	
	public static HttpResponse<String> httpGet(String url) {
		LOGGER.info("http GET URL ==> " + url);
		
		HttpClient client = HttpClientUtils.getDefaultHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.GET()
				.build();
				
		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
			
			LOGGER.info( "response ==> \nResponse Status ==> " + response.statusCode() 
						+ "\nResponse Body ==> " + response.body() );
		} catch (IOException | InterruptedException e) {
			LOGGER.severe("ERROR : ==> \n" + e.toString());
		}

		
		return response;
	}
	
}






