package fr.sqli.paja.adccaconfigurator.servicesreçu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.cacheonix.Cacheonix;
import org.cacheonix.cache.Cache;
import org.json.JSONObject;

import fr.sqli.paja.adccaconfigurator.propertiesLoader.PropertyLoader;

public class BuildFile {
	
	static String x="{\"ms\":\"vide\"}";
	static String ms= "{\"ms\":\"vide\"}";
	
    private static String services;
    
	public static final void setServices(final String value) {
		services = value;
	}
	
	public static final String getServices() {
		return services;
	}
	
	public static void buildFile() throws IOException{

		
		try {
			
				if(getServices()!=null && getServices()!=x) {
					
					ms=getServices();
					System.out.println("UN CHANGEMENT AU NIVEAU DES SERVICES EST DETECTE ");
						
		//************APPEL A LA METHODE (buildMap) POUR METTRE LES SERVICES SOUS FORME D'UN OBJET JSON DANS UNE MAP **************				
				    final Map<String, String> map = buildMap(new JSONObject(ms)); 
				    for (Map.Entry<String, String> entry: map.entrySet()) {
				    	System.out.println("MAP : "+entry.getKey() + "=" + entry.getValue());
			
				    }
			    
		//**********APPEL A LA METHODE (buildMapCache) POUR METTRE LES SERVICES SOUS FORME D'UN OBJET JSON DANS UNE mémoire cache**************   
				    final Cache<String, String> cache = buildMapCache(new JSONObject(x)); 
				    for (Cache.Entry<String, String> entry: cache.entrySet()) {
				    	System.out.println("LA MEMOIRE CACHE " +entry.getKey()+ "=" + entry.getValue());				    					    	
				    } 
	//******************************* METTRE A JOUR LE CACHE *****************************		    
				    if (!ms.equalsIgnoreCase(x)) {
						   cache.clear();
						   x = ms;	
				    }
				    
	//*********************** TRAITEMENT DE LA CHAINE DE CARACTERES *********************************************
				    
				     String microservices =map.toString();
				     String str = microservices.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll(", ", "\n")
				    		 .replaceAll("=", ",");
				     				     			    
	//************************CREER UN FICHIER DANS LE DOSSIER INDIQUER DANS LE FICHIER DE PROPERTIES(conf)  *************************			   
				     				     	     
				     Properties prop = PropertyLoader.load("application.properties");
				       
				       FileWriter fw = new FileWriter(new File(prop.getProperty("input")));
					   fw.write(str);
					   fw.close();
					   
	//*********************** RELANCE APACHE POUR PRENDRE LA NOUVELLE CONFIGURATION *******************************
					   
					   Runtime runtime = Runtime.getRuntime();
					   runtime.exec(prop.getProperty("reload").split(","));
					 
				}			 
		
		}catch (Exception evt){
	    	
	    	evt.printStackTrace();
	    } 
	}
	//**************** METHODE (buildMap) POUR METTRE LES SERVICES SOUS FORME D'UN OBJET JSON DANS UNE MAP ************** 

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
}
