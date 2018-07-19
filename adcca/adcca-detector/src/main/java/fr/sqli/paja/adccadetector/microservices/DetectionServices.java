package fr.sqli.paja.adccadetector.microservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.cacheonix.cache.Cache;
import org.json.JSONObject;
import fr.sqli.paja.adccadetector.CallConf.CallConfigurator;
import fr.sqli.paja.adccadetector.modelurl.BatchRead;
import fr.sqli.paja.adccadetector.testUrls.TestUrl;
import org.apache.log4j.Logger;
/**
 * 
 * @author sebouyahmed
 *
 */
public class DetectionServices {

	static URL urlserveurconf = null;
	static URL urlconfigurator = null;

	static String x = "{\"cleServive\":\"nomApp,serverName,Protocol,HostName,port,service\"}";

	public static String micser = "";

	final static Logger logger = Logger.getLogger(DetectionServices.class);

	/**
	 * autoDetecteServices est une méthode qui détecte tout changement au niveau de
	 * fichier de configuration afin de l'envoyer au congigurator
	 * 
	 * @param url de Serveur de Configuration, url de Configurator
	 */
	public static void autoDetecteServices(final String urlsc, final String urlconf) throws IOException {
		/**
		 *vérifier le format des urlsc et urlconf 
		 */
		try {

			urlserveurconf = new URL(urlsc);
		} catch (Exception e) {

			logger.fatal("problème d'url de serveur de configuration, ", e);
		}

		try {

			urlconfigurator = new URL(urlconf);
		} catch (Exception e) {

			logger.fatal("problème d'url de configurator, ", e);
		}

		try {
			 /**
			  * Connect to the URL using java native library
			  */
			HttpURLConnection connexion = (HttpURLConnection) urlserveurconf.openConnection();
			connexion.connect();
			InputStream is = connexion.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder();
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				/**
				 * append pour la concaténation
				 */
				builder.append(line + "\n");
			}
			is.close();
			String readurl = builder.toString();

			try {
				JSONObject objAction = new JSONObject(readurl);
				String pr = objAction.getString("propertySources");
				/**
				 *  TRAITEMENT LA CHAINE DE CARACTERES PR ET DE CREER UN OBJET JSON
				 */
				String pr2 = pr.replaceAll("\\[", "");
				String pr3 = pr2.replaceAll("\\]", "");
				/**
				 * TRAITEMENT LA CHAINE DE CARACTERES PR3 ET DE CREER UN OBJET JSON
				 */
				JSONObject objContent = new JSONObject(pr3);
				String src = objContent.getString("source");

				JSONObject objMicrServices = new JSONObject(src);
				String ms = objMicrServices.getString("microservice");
				/**
				 * METHODE (buildMap) POUR METTRE LES SERVICES DANS UNE MAP
				 */  
				final Map<String, String> map = BuildCache.buildMap(new JSONObject(ms));
				/**
				 * METHODE (buildCache) POUR METTRE LES SERVICES DANS UN CACHE
				 */
				final Cache<String, String> cache = BuildCache.buildMapCache(new JSONObject(x));
				/**
				 * METTRE A JOUR LE CACHE A CHAQUE MODIFICATION DETECTEE
				 */
				  if (!ms.equalsIgnoreCase(x)) {
				
						logger.info("Connexion au serveur ");
						x = ms;
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
						 for (Cache.Entry<String, String> entry : cache.entrySet()) {
						 System.out.println("cache : " + entry.getValue());
						}*/
				    
					/**
					 * map contient une liste d'url de type string
					 */
					for (Map.Entry<String, String> entry : map.entrySet()) {
					    /**
					     * BatchRead.processUrl convertir un string url vers un modelURL
					     */
						String url = BatchRead.processUrl(entry.getKey(), entry.getValue());
						/**
						 * TestUrl.testurl test le statut de la connexion
						 */
						boolean rep = TestUrl.testurl(url);

						if (rep) {
							/**
							 * BatchRead.processService convertir un string url vers un modelService
							 */
							String microser = BatchRead.processService(entry.getKey(), entry.getValue());
							micser = micser + "," + microser;
						}
					}					
					/**
					 * Appel le Configurator en envoyant la nouvelle condiguration 
					 */
					CallConfigurator.callConfigurator(urlconf, micser);
					/**
					 * Remise la valeur micser à zéro
					 */
					micser = "";

				  }

			} catch (Exception evt) {

				evt.printStackTrace();
			}

		}catch(

	Exception e)
	{

		logger.fatal("Connexion au serveur de configuration a échoué, " + e);

	}

}}
