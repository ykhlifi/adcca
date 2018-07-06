package fr.sqli.paja.adccaconfigurator.propertiesLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
	
	/**
	    * Charge la liste des propriétés contenu dans le fichier spécifié
	    *
	    * @param filename le fichier contenant les propriétés
	    * @return un objet Properties contenant les propriétés du fichier
	    */
	   public static Properties load(String filename) throws IOException, FileNotFoundException{
		
		   /*
		   path c'est le chemin ou se trouve le fichier de configuration de configurator
	       *
		   */
		   String path = System.getenv("HOME_CONFIGURATOR"); 
	       Properties properties = new Properties();      
	       FileInputStream pATH = new FileInputStream(path + "application.properties");
	       
	       try{
	    	   
	    	   properties.load(pATH);
	           return properties;

	      }

	           finally{
	        	   pATH.close();

	      }

	   }

}
