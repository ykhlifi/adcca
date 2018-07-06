package fr.sqli.paja.adccadetector.microservices;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cacheonix.Cacheonix;
import org.cacheonix.cache.Cache;
import org.json.JSONObject;

public class DetectionServices {

	// NB: mettre les urls dans fichier de conf et de les recuperer comme les services
   
	static URL urlserveurconf = null;
	static URL urlconfigurator = null;
	
    static String x="{\"ms\":\"vide\"}";
    static String message;   

    
//*********** CONTACT AVEC LE SERVEUR DE CONFIGURATION ******************************************
    
    public static void autoDetecteServices(final String urlsc,final String urlconf) throws IOException {
    	
		try {
			
			urlserveurconf = new URL(urlsc);
		}
		catch(MalformedURLException mal) {
			System.out.println("URL DE SERVEUR DE CONFIGURATION EST INCORRECT!!! "+mal);
		}	
		
		try {
			
			urlconfigurator = new URL(urlconf);
		}
		catch(MalformedURLException mal) {
			System.out.println("URL DE CONFIGURATOR EST INCORRECT!!! " + mal);
		}	
		
		 // Connect to the URL using java's native library
	    HttpURLConnection connexion = (HttpURLConnection) urlserveurconf .openConnection();
	    connexion.connect();
	    if(connexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
	    //System.out.println("Status de la connexion "+ connexion.getResponseCode());
	    }
	    
	    InputStream is = connexion.getInputStream(); 
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder builder = new StringBuilder();
	    for(String line = reader.readLine(); 
	    		line != null; 
	    		line = reader.readLine())
	       {
	    	// append pour la concaténation
	        builder.append(line + "\n");
	       }
	    is.close();
	    String readurl = builder.toString();
	    //System.out.println(readurl);
	  
	    try {
	     JSONObject objAction = new JSONObject(readurl);
	     String pr = objAction.getString("propertySources");     
	     //System.out.println(pr);
	     
//****************** TRAITEMENT LA CHAINE DE CARACTERES PR ET DE CREER UN OBJET JSON *******************************************    
	     String pr2 = pr.replaceAll("\\[", "");
	     String pr3 = pr2.replaceAll("\\]", "");
	     
//**********************TRAITEMENT LA CHAINE DE CARACTERES PR3 ET DE CREER UN OBJET JSON****************************************     
	    JSONObject objContent = new JSONObject(pr3);
	    String src = objContent.getString("source");  
	    	    
//****TRAITEMENT LA CHAINE DE CARACTERES SRC ET DE CREER UN OBJET JSON POUR LES MICROSERVICES **************************************
	    JSONObject objMicrServices = new JSONObject(src);
	    String ms = objMicrServices.getString("microservice");
	    	    
//**************** METHODE (buildMap) POUR METTRE LES SERVICES SOUS FORME D'UN OBJET JSON DANS UNE MAP **************   
	    final Map<String, String> map = buildMap(new JSONObject(ms)); 
	    for (Map.Entry<String, String> entry: map.entrySet()) {
	    	//System.out.println(entry.getKey() + ": " + entry.getValue());
	    }
	    
//**************** METHODE (buildMapCache) POUR METTRE LES SERVICES SOUS FORME D'UN OBJET JSON DANS UNE mémoire cache**************   
	    final Cache<String, String> cache = buildMapCache(new JSONObject(x)); 
	    for (Cache.Entry<String, String> entry: cache.entrySet()) {
	    	//System.out.println("LA MEMOIRE CACHE " + entry.getKey() + ": " + entry.getValue());
	    }

//******************** METTRE A JOUR LE CACHE A CHAQUE MODIFICATION AU NIVEAU SERVICES ***********************	   
	   if (!ms.equalsIgnoreCase(x)) {
		   cache.clear();
		   x = ms;	
		   System.out.println("UN CHANGEMENT AU NIVEAU DES SERVICES EST DETECTE");
		   System.out.println("le voila les micro services: " + ms);
		   
		   
 //**** FAIRE APPEL A LA METHODE callConfigurator pour envoyer les service vien url ************************* 	    
		 
		    callConfigurator(urlconf, ms);
		    System.out.println("L'envoie de requête vers Configurator est faite");
	   }

	    }catch (Exception evt){
	    	
	    	evt.printStackTrace();
	    }
	}
	
//**************** METHODE (buildMap) POUR METTRE LES SERVICES SOUS FORME D'UN OBJET JSON DANS UNE MAP ************** 
	/**
	 * Build <code>Map</code> from <code>JSONObject</code>
	 * @param jsonObj
	 * @throws Exception
	 * @return
	 */
	public static final Map<String, String> buildMap(final JSONObject jsonObj) throws Exception {
		final Map<String, String> map	= new HashMap<>();
		final Iterator<?> keys			= jsonObj.keys();
		while (keys.hasNext()) {
			final String key	= (String) keys.next();
			final String value	= (String) jsonObj.get(key);
			
			map.put(key, value);
		}
		
		return map;
	}
	
//**************** METHODE (buildMapCache) POUR METTRE LES SERVICES SOUS FORME D'UN OBJET JSON DANS UN CACHE  **************
	public static final Cache<String, String> buildMapCache(final JSONObject jsonObjCache) throws Exception {
		final Cacheonix cacheManager = Cacheonix.getInstance();
		final Cache<String, String> cache	= cacheManager.getCache("invoce.cache");		
		final Iterator<?> keys			= jsonObjCache.keys();
		while (keys.hasNext()) {
			final String key	= (String) keys.next();
			final String value	= (String) jsonObjCache.get(key);	
			
			cache.put(key, value);			
		}
		
		return cache;
	}

//*************** CONTACT AVEC LE CONFIGURATOR ************************************************************************
	
	 private static final void callConfigurator(final String urlconfigurator, final String value) throws Exception {
	    	
	    	
	    	String urlParameters                   = value;
	        byte[] postData                        = urlParameters.getBytes(StandardCharsets.UTF_8);
	        int postDataLength                     = postData.length;
	        String request                         = urlconfigurator;
	        URL url                                = new URL(request);
	        
	        HttpURLConnection connection           = (HttpURLConnection) url.openConnection();           
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
	        }
	    }

	
	
}





