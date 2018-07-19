package fr.sqli.paja.adccadetector.propertiesloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
	
	/**
	    * Charger les paramètres de configuration de Detector
	    * @param filename (Path/nom_de_fichier) 
	    * @return 
	    */
	   public static Properties load(String filename) throws IOException, FileNotFoundException{
		   	   
	       Properties properties = new Properties();
	       /**
	        * path c'est le chemin ou se trouve le fichier de configuration de detector
	        *
	        */
	       String path = System.getenv("HOME_DETECTOR");
	       FileInputStream input = new FileInputStream(path+ "application.properties");
	       
	       try{
	    	   
	    	   properties.load(input);
	           return properties;

	      }

	           finally{
	           input.close();

	      }

	   }

}
