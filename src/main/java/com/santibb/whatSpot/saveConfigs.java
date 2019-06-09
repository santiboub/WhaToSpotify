package com.santibb.whatSpot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class saveConfigs {
	public static boolean isConfig() {
		try {
			Scanner in = new Scanner(new File("config.txt"));
			if (in.hasNext()) {
				in.close();
				return true;
			}	
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void saveConfig(String inputf, String playlistId, String clientId, String clientSec, String clientAcc, String clientRefr) {
    	PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("config.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pw.println("Whatsapp file: "+inputf);
		pw.println("Playlist id to add: "+playlistId);
		pw.println("Client ID: "+clientId);
		pw.println("Client Secret: "+clientSec);
		pw.println("Client Access: "+clientAcc);
		pw.println("Client Refresh: "+clientRefr);
		pw.close();
	}
    public static String[] getConfig() {
    	Scanner in = null;
    	String [] result = new String[6];
		try {
			in = new Scanner(new File("config.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("The config file was not found");
			return result;
		}
		if (in.hasNext()) {
			for (int i = 0; i < result.length && in.hasNext(); i++) {
				result[i]= in.nextLine().trim().split(": ")[1];
//				System.out.println("Found: "+result[i]);
			}
		}
		in.close();
		return result;
	}
}
