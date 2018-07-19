package fr.sqli.paja.adccadetector.testUrls;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;

import org.apache.log4j.Logger;
/**
 * 
 * @author sebouyahmed
 *
 */
public class TestUrl {
	
	final static  Logger logger =  Logger.getLogger(TestUrl.class);
	
	static URL url=null;

	/**
	 * testurl test le statut de connexion d'url
	 * @param urlstr
	 * @return
	 * @throws IOException
	 */
	    public static final boolean  testurl(String urlstr) throws IOException {
	    	
	    	boolean rep= false;
	        
	        
	        try {
				
	        	url= new URL(urlstr);
			}
	        catch(Exception e) {
	        					
				logger.warn("Cette url est incorrecte :"+ urlstr);
			}	
	      
	      try {
		        HttpURLConnection connexion = (HttpURLConnection)url.openConnection();
		        InputStream flux = connexion.getInputStream();
		        logger.info("Connexion à cette url "+ connexion.getResponseCode()+" OK =>"+urlstr);
		        if (connexion.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
		        	
		        	rep =true;
		        	
		        	}
		        flux.close(); 
		        connexion.disconnect();
	      } 
	      catch(Exception e) {
	    	  
	    	  logger.warn("Connexion à cette url a echoué =>"+urlstr);
	          
	      }
	      return rep;
	      
	    }
	}
	

