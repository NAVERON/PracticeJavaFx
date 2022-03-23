package org.practicefx;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * Http 请求工具类 
 * 关于server 增加了验证的使用方法 
 * https://medium.com/@ashokmathankumar/basic-authentication-with-java-11-httpclient-e129bb749fd3 
 * @author eron
 *
 */
public class HttpClientUtils {
	
	private static final Logger LOGGER = Logger.getLogger(HttpClientUtils.class.getName());
	
	private static HttpClient client = null;
	
	// 单例模式 
	public static HttpClient getDefaultHttpClient() {
		if(client == null) {
			synchronized (HttpClientUtils.class) {
				if(client == null) {
					Authenticator authenticator = new Authenticator() {  // web security 
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication("wangyulong", "testing".toCharArray());
						}
					};
					
					client = HttpClient.newBuilder()
					        .followRedirects(Redirect.NORMAL)  // Redirect.SAME_PROTOCOL
					        .connectTimeout(Duration.ofMinutes(1))
					        //.proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
					        .authenticator(authenticator) // Authenticator.getDefault() 
					        .version(Version.HTTP_2)
					        .build();
				}
			}
		}
		
		return client;
	}
	
	/**
	 * 使用参考  https://openjdk.java.net/groups/net/httpclient/intro.html 
	 * @param url
	 * @param requestBody
	 * @return
	 */
	public static HttpResponse<String> httpPost(String url, String requestBody) {
		LOGGER.info("http POST URL ==> " + url);

		HttpRequest request = HttpRequest.newBuilder()
		        .uri(URI.create(url))
		        .header("Content-Type", "application/json")
		        .POST(BodyPublishers.ofString(requestBody))
		        .build();
		
		HttpResponse<String> response = null;
		try {
			response = getDefaultHttpClient().send(request, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			LOGGER.severe("ERROR : ==> \n" + e.toString());
		}
		
		LOGGER.info( "response ==> \nResponse Status ==> " + response.statusCode() 
					+ "\nResponse Body ==> " + response.body() );
		
		return response;
		
	}
	
	public static HttpResponse<String> httpGet(String url) {
		LOGGER.info("http GET URL ==> " + url);
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.GET()
				.build();
				
		HttpResponse<String> response = null;
		try {
			response = getDefaultHttpClient().send(request, BodyHandlers.ofString());
			
			LOGGER.info( "response ==> \nResponse Status ==> " + response.statusCode() 
						+ "\nResponse Body ==> " + response.body() );
		} catch (IOException | InterruptedException e) {
			LOGGER.severe("ERROR : ==> \n" + e.toString());
		}

		
		return response;
	}
	
	public static CompletableFuture<HttpResponse<String>> asyncHttpGet(String url) {
		LOGGER.info("http GET URL ==> " + url);
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		
		CompletableFuture<HttpResponse<String>> getAsyncResponse = getDefaultHttpClient().sendAsync(request, BodyHandlers.ofString()); 
		getAsyncResponse.whenComplete((response, error) -> {
			if(response != null) {
				LOGGER.info("get response => " + response.statusCode());
				LOGGER.info("get response body => " + response.body());
			}
			if(error != null) {
				LOGGER.info("get error => " + error.getMessage());
			}
		});
		
		return getAsyncResponse;
	}
	
	public static CompletableFuture<HttpResponse<String>> asyncHttpPost(String url, String requestBody){
		LOGGER.info("http POST URL ==> " + url);
		
		HttpRequest request = HttpRequest.newBuilder()
		        .uri(URI.create(url))
		        .header("Content-Type", "application/json")
		        .POST(BodyPublishers.ofString(requestBody))
		        .build();
		
		CompletableFuture<HttpResponse<String>> postAsyncResponse = getDefaultHttpClient().sendAsync(request, BodyHandlers.ofString());
		postAsyncResponse.whenComplete((response, error) -> {
			if(response != null) {
				LOGGER.info("post response => " + response.statusCode());
				LOGGER.info("post response body => " + response.body());
			}
			if(error != null) {
				LOGGER.info("post error => " + error.getMessage());
			}
		});
		
		return postAsyncResponse;
	}
	
}






