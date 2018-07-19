package fr.sqli.paja.adccaconfigurator.servicesreçu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.cacheonix.cache.Cache;
import org.json.JSONObject;

import fr.sqli.paja.adccaconfigurator.batch.BatchMapToString;
import fr.sqli.paja.adccaconfigurator.batch.BatchReader;
import fr.sqli.paja.adccaconfigurator.batch.BatchWriterApache;
import fr.sqli.paja.adccaconfigurator.batch.BatchWriterNginx;
import fr.sqli.paja.adccaconfigurator.propertiesLoader.PropertyLoader;
import fr.sqli.paja.adccaconfigurator.url.ModelUrl;
import fr.sqli.paja.adccaconfigurator.util.ListeApp;

/**
 * 
 * @author sebouyahmed
 *
 */
public class BuildFile {

	static String input;
	static String output;
	static String serveur;
	/**
	 * journalisation (message d'information, erreur, avertissement)
	 */
	final static Logger logger = Logger.getLogger(BuildFile.class);

	static String x = "{\"servive\":\"nomApp,serverName,http,localhost,port,service\"}";

	/**
	 * buildFlile pour créer les fichier de conf
	 * 
	 * @param value
	 * 
	 * @throws IOException
	 */
	public static void buildFile(String value) throws IOException {

		if (!value.equalsIgnoreCase(x)) {

			logger.info("Nouvelle configuration");
			/**
			 * Si la liste des microservices est vide on vas recevoir cette valeur => {}
			 */
			if (value.length() > 2) {
				try {
					/**
					 * Charger les paramètres de configuration de Configurator
					 */
					Properties prop = PropertyLoader.load("application.properties");
					/**
					 * Chemain ou se trouve le fichier pour récupérer la liste des microservices
					 */
					input = prop.getProperty("input");
					/**
					 * Chemain pour écrire un fichier de conf
					 */
					output = prop.getProperty("output");
					/**
					 * type de serveur web (Apache/Nginx)
					 */
					serveur = prop.getProperty("serveur");
					/**
					 * APPEL A LA METHODE (buildMapCache) POUR METTRE LES SERVICES DANS UN CACHE
					 */
					final Cache<String, String> cache = BuildCache.buildMapCache(new JSONObject(x));
					/**
					 * APPEL A LA METHODE (buildMapCache) POUR METTRE LES SERVICES DANS UN CACHE
					 */
					final Map<String, String> map = BuildCache.buildMap(new JSONObject(value));
					/**
					 * Comparaison entre la nouvelle valeur (configuration) et celle de la mémoire
					 * cache
					 */
					if (!map.equals(cache)) {
						for (String key : map.keySet()) {

							if (!cache.containsKey(key)) {

								cache.put(key, map.get(key));
							}
						}
						for (String key : cache.keySet()) {
							if (!map.containsKey(key)) {

								cache.remove(key, cache.get(key));
							}
						}
						/**
						 * CREER UN FICHIER EN SUIVANT CE PATH => INPUT, CE DERNIER SE TROUVE DANS LE
						 * FICHIER DE CONF DE CONFIGURATOR
						 */
						FileWriter fw = new FileWriter(new File(prop.getProperty("input")));
						fw.write(BatchMapToString.readMap(cache));
						fw.close();
						/**
						 * Créer une liste d'URL à partir d'un fichier CSV Le path de ce dernier est
						 * input Input est un paramètre qui se trouve dans le fichier de conf de
						 * configurator
						 */
						final List<ModelUrl> lst = new BatchReader(input).read();
						/**
						 * build une map (clé= nom app, valeur= liste des urls) à la base d'une liste en
						 * parametre
						 */
						final Map<String, List<ModelUrl>> map1 = ListeApp.processFromUrls(lst);

						for (Map.Entry<String, List<ModelUrl>> entry : map1.entrySet()) {
							// System.out.println(entry.getKey() + ": " + entry.getValue());
							switch (serveur) {
							case "apache":

								new BatchWriterApache(output + entry.getKey() + ".conf").write(entry.getValue());
								Thread.sleep(10000);
								break;

							case "nginx":
								new BatchWriterNginx(output + entry.getKey() + ".conf").write(entry.getValue());
								Thread.sleep(10000);
								break;
							}
						}
						/**
						 * RELANCE LE SERVEUR WEB POUR PRENDRE LA NOUVELLE CONFIGURATION
						 */
						Runtime runtime = Runtime.getRuntime();
						runtime.exec(prop.getProperty("reload").split(","));
					}
				} catch (Exception e) {
					logger.warn("Chemin des fichiers de conf incorrect ", e);
				}
			} else {
				logger.warn("Aucun fichier ajouté");
			}
		}
	}

}