package com.santibb.whatSpot;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

import java.io.*;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.AddTracksToPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;

import java.net.URI;
import java.net.URL;


public class WhaToSpot{
    private static String[][] array;
    private static String playlistId = null;
    private static String filename = null;
    private static String clientId = "98e0866a660c429b8dcced3f3028b02d";
    private static String clientSec = "d1808d9bc36b49b4bc9cb5ae4c51bfee";
    public static void main(String[] args) throws Exception{
    	loadConfig();
        if (args.length==2) {
        	if (args[0].equals("-input") && args[2].equals("-playlist")) {
        		filename = args[1];
        		playlistId = args[3];
        	}			
		}
        if (filename != null && playlistId != null) {			
        	start();
		}
        else{
            System.out.println("Specify an input name in config or with options:");
            System.out.println("-input (WhatsAppConv.txt) -playlist (spotifyPlaylistURI)");
        }
    }
    private static boolean validConfig() {
		
		return false;
	}
	private static void start() throws Exception{
        Scanner input = null;
        try {
            input = new Scanner( new File(filename) );
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace( System.err );
            System.exit(2);
        }
        QueueStrLinked list = new QueueStrLinked();

        while( input.hasNext() ) {
            String text= input.next();
            if(text.contains("https://open.spotify.com/track/")){
                // System.out.println(text);
            	//Check if valid ID!
            	if(isValid(text)) {
	                String[] arr = text.split("/");
	                String trackURI = arr[4];
	                if (trackURI.indexOf('?') >= 0) {
	                	trackURI = trackURI.substring(0, trackURI.indexOf('?'));
					}
	                String adder = "spotify:track:"+trackURI;
	                if(!list.contains(adder)) list.add(adder);
	                else System.out.println("DEBUG: Found a Duplicate!");
            	}
            }
        }
        input.close();
        // Check if the elements are already in playlist
        QueueStrLinked original = manageSongs(false);
        list.intersect(original);
        System.out.println("Songs managed! Found "+list.size()+" new songs.");
        if (!list.isEmpty()) {
        	array = new String[(list.size()/50)+1][];
        	for (int i = 0; !list.isEmpty(); i++) {
        		if (i == array.length-1) {
        			array[i] = new String[list.size()];	
        			System.out.println("Created a "+list.size()+" length");
        		} else {
        			array[i] = new String[50];	
        			System.out.println("Created a maximum length, i is "+i);
        		}
        		for (int j = 0; j <array[i].length && !list.isEmpty(); j++) {
        			array[i][j] = list.remove();
        		}
        	}
        	printArray(array);
        	manageSongs(true);
        }			
        else {
        	System.out.print("No new songs were found in the chat! Nothing added to playlist");
        }	
    }
    private static boolean isValid(String link) {
		try {
			URL url = new URL(link);
			HttpsURLConnection huc = (HttpsURLConnection) url.openConnection();
			huc.setRequestMethod("HEAD");
			int response = huc.getResponseCode();
			if (response!=200) throw new IOException();
//			Scanner in = new Scanner(url.openStream());
//			while(in.hasNext()) {
//				String text = in.next();
//				System.out.println(text);
//				if(text.equals("Lo sentimos")) throw new IOException();
//			}
			return true;
		} catch (IOException e) {
			System.out.println("Invalid Link: "+ link);
			return false;
		}
	}
	private static void printArray(String[][] list) {
        for (int i = 0; i < list.length; i++) {
        	for (int j = 0; j < list[i].length; j++) {
        		System.out.println("Array "+(i+1)+ ". Element "+ (j+1)+" is "+array[i][j]);
				
			}
		}
    }

    /**
     * Spotify module for adding songs
     * if the value of @addsongs is true, then modify playlist, 
     * if false just return songs array
     */
    private static QueueStrLinked manageSongs(boolean addSongs) {
    	URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8888/");
    	String[] tokens = getAccessRefresh();
//    	String accessToken = tokens[0];
    	String refreshToken = tokens[1];
    	SpotifyApi spotifyApi = null;
    	AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest=null;
    	AuthorizationCodeRequest authorizationCodeRequest=null;
    	if (refreshToken != null) {
    		spotifyApi = new SpotifyApi.Builder()
    		          .setClientId(clientId)
    		          .setClientSecret(clientSec)
    		          .setRefreshToken(refreshToken)
    		          .build();
    		authorizationCodeRefreshRequest= spotifyApi.authorizationCodeRefresh()
    		          .build();
		} else {
			spotifyApi = new SpotifyApi.Builder()
					.setClientId(clientId)
					.setClientSecret(clientSec)
					.setRedirectUri(redirectUri)
					.build();
			AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
					.build();
			authorizationCodeUriRequest.execute();
			authorizationCodeRequest = spotifyApi.authorizationCode(GetCode.getCode(clientId, redirectUri))
					.build();		
		}
			    	
        try {
        	AuthorizationCodeCredentials authorizationCodeCredentials=null;
        	if (authorizationCodeRefreshRequest != null) {
        		authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
        	}
        	else { 
        		authorizationCodeCredentials = authorizationCodeRequest.execute();
        	}
            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            if (refreshToken==null) {	
            	System.out.println("Saved Refresh Key: " + authorizationCodeCredentials.getRefreshToken());
            	saveRefresh(authorizationCodeCredentials.getAccessToken(), authorizationCodeCredentials.getRefreshToken());
            }
            
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
	        } catch ( SpotifyWebApiException e) {
	    		System.out.println("Error: " + e.getMessage());
	    		if (e.getMessage().equals("Invalid refresh token")) {
					saveRefresh("","");
					manageSongs(addSongs);
				}
	    	} catch ( IOException io) {
	    		System.out.println("Error: " + io.getMessage());
	    	}
    	if (addSongs) {			
    		for (int i = 0; i < array.length; i++) {
    			AddTracksToPlaylistRequest addTracksToPlaylistRequest = spotifyApi
    					.addTracksToPlaylist(playlistId, array[i])
    					.build();
    			addTracksToPlaylist_Sync(addTracksToPlaylistRequest);
    			System.out.println("Added Array "+(i+1));    		
    		}
    		return null;
		} else {
			return getTracksInPlaylist(spotifyApi);
		}
    }
    private static void addTracksToPlaylist_Sync(AddTracksToPlaylistRequest a) {
    	try {
    		final SnapshotResult snapshotResult = a.execute();
    		System.out.println("Adeed " + array.length+" Songs");
    		System.out.println("Snapshot ID: " + snapshotResult.getSnapshotId());
    	} catch ( SpotifyWebApiException e) {
    		System.out.println("Error: " + e.getMessage());
    	} catch ( IOException io) {
    		System.out.println("Error: " + io.getMessage());
    	}
    }
    private static QueueStrLinked getTracksInPlaylist(SpotifyApi spotifyApi) {
    	QueueStrLinked songs = new QueueStrLinked();
    	GetPlaylistRequest getPlaylistRequest = spotifyApi
    		  .getPlaylist(playlistId)
              .build();
    	try {
//	  		Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();
	  		Playlist playlist = getPlaylistRequest.execute();
	  		PlaylistTrack[] tracks = playlist.getTracks().getItems();
	  		System.out.println("Playlist "+ playlist.getName() +" contains a total of: " + tracks.length +" songs");
	  		for (int i = 0; i < tracks.length; i++) {
	  			songs.add(tracks[i].getTrack().getUri());
//	  			System.out.println("Playlist song " + i + " is " + songs.first());
	  		}
      	} catch (IOException e) {
      		System.out.println("Error: " + e.getMessage());
      	} catch (SpotifyWebApiException e) {
      		System.out.println("Error: " + e.getMessage());
      	} catch (Exception e) {
      		System.out.println("Error: " + e.getMessage());
		}
    	return songs;
    }
    private static void saveRefresh(String access, String ref) {
    	saveConfigs.saveConfig(filename, playlistId, clientId, clientSec, access, ref);
	}
    private static String[] getAccessRefresh() {
    	String [] result = new String[2];
		String[] config = saveConfigs.getConfig();
		if (config.length == 6) {
			result[0] = config[4];
			result[1] = config[5];
		}
		return result;
	}
    private static void loadConfig() {
		String[] config = saveConfigs.getConfig();
		filename = config[0];
		playlistId = config[1];
		clientId = config[2];
		clientSec = config[3];
	}
    
}
