package fr.sqli.paja.adccadetector.CallConf;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.cacheonix.Cacheonix;
import org.cacheonix.cache.Cache;

import fr.sqli.paja.adccadetector.microservices.DetectionServices;
public class CallConfigurator {
	
	final static  Logger logger =  Logger.getLogger(CallConfigurator.class);
	
	public static URL urlconf=null;
	
	/**
	 * CONTACT AVEC LE CONFIGURATOR 	
	 * @param urlconfigurator
	 * @param value
	 * @throws Exception
	 */
		 public static final void callConfigurator(final String urlconfigurator, final String value) throws Exception {
		    	
			 try {
					
					urlconf= new URL(urlconfigurator);
					
			 
			 try {
				final String value1= value.replaceFirst(",", "");
				final String value2= ("{"+value1+"}");
		    	String urlParameters                   = value2;
		        byte[] postData                        = urlParameters.getBytes(StandardCharsets.UTF_8);
		        int postDataLength                     = postData.length;
		        
		        HttpURLConnection connection           = (HttpURLConnection) urlconf.openConnection();           
		        connection.setDoOutput(true);
		        connection.setInstanceFollowRedirects(false);
		        connection.setRequestMethod("POST");
		        connection.setRequestProperty("Content-Type", "application/json"); 
		        connection.setRequestProperty("charset", "utf-8");
		        connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		        connection.setUseCaches(false);
		              
		        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
		           wr.write(postData);
		           connection.getInputStream().close();
		           connection.disconnect();
		           logger.info("Envoie de requête au Configurator");
		        }
			 }catch(Exception e) {
				 logger.fatal("Connexion au configurator a échoué, "+e);
			 }
			 
				
			 }catch(Exception e) {
					logger.fatal("problème d'url de configurator, ",e);
				}	
		    }

}
