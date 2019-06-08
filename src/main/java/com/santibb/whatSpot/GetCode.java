package com.santibb.whatSpot;

import java.net.URI;
import java.util.Collections;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class GetCode {
	public static String getCode(String clientId, URI redirectUri) {
    	String page = String.format("https://accounts.spotify.com/authorize" +
    		  "?response_type=code" +
    		  "&client_id=" + clientId+ 
    		  "&scope="+"playlist-modify-private%%20playlist-modify-public"+
    		  "&redirect_uri="+redirectUri );
    	String redirected = openBrowser(page);
    	if(!redirected.equals("-1")) {
    		String result = redirected.split("/")[3].substring(6).split("#")[0];
    		return result;
    	} else {
    		System.out.println("Cannot login to page, please try again");
    		return getCode(clientId, redirectUri);
    	}
	}
	private static String openBrowser(String url) {
		WebDriverManager.chromedriver().version("75.0.3770.8")
					.setup();
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.addArguments("disable-infobars");
		options.addArguments("--start-maximized");
		WebDriver driver = new ChromeDriver(options);
		driver.get(url);
		try {
			while (!driver.getCurrentUrl().contains("http://localhost:8888/")) {}
			String res = driver.getCurrentUrl();
			//System.out.println("DEBUG: Adress is "+res);
			driver.close();
			return res;
		} catch(NoSuchWindowException nowin){
			return "-1";
        }
	}
}
