package fr.sqli.paja.adccaconfigurator.propertiesLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
/**
 * 
 * @author sebouyahmed
 *
 */
public class PropertyLoader {
/**
 * @param filename
 * @return les paramètres de configuration
 * @throws IOException
 * @throws FileNotFoundException
 */
	public static Properties load(String filename) throws IOException{

		/*
		 * path c'est le chemin ou se trouve le fichier de configuration de configurator
		 *
		 */
		String path = System.getenv("HOME_CONFIGURATOR");
		Properties properties = new Properties();
		FileInputStream pATH = new FileInputStream(path + "application.properties");

		try {

			properties.load(pATH);
			return properties;

		}finally {
			pATH.close();

		}

	}

}
