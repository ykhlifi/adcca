package fr.sqli.paja.adccaconfigurator.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import fr.sqli.paja.adccaconfigurator.propertiesLoader.PropertyLoader;
import fr.sqli.paja.adccaconfigurator.url.ModelUrl;
/**
 * 
 * @author sebouyahmed
 *
 */

public class LoadBalancingNginx {

	final static Logger logger = Logger.getLogger(LoadBalancingNginx.class);

	private final String filename;

	/**
	 * Constructor
	 * 
	 * @param filename
	 *            (path/filename.conf)
	 */
	public LoadBalancingNginx(String filename) {
		super();
		this.filename = filename;
	}

	/**
	 * Créer le model virtual host NGINX
	 * 
	 * @param modelUrls
	 */
	public void write(List<ModelUrl> modelUrls) {
		try {
			/**
			 * charger les paramètres qui se trouve dans le fichier de conf de Configurator
			 * en appellant à la classe PropertyLoader
			 */
			Properties prop = PropertyLoader.load("application.properties");
			final String port = prop.getProperty("port");

			final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.filename)));
			final StringBuilder sb = new StringBuilder();
			sb.append("upstream  mybalancer {");
			sb.append("\n\n");
			sb.append(this.buildLoadBalancingNginx(modelUrls));
			sb.append("\n }");
			sb.append("\n");
			sb.append("server  {");
			sb.append("\n");
			sb.append("\t listen   " + port + ";");
			sb.append("\n");
			sb.append(this.chargeServerName(modelUrls.get(0)));// recupérer le ServerName de chaque liste
			sb.append("\n");

			for (ModelUrl modelUrl : modelUrls) {
				sb.append(this.buildVirtualHostNginx(modelUrl));
			}

			sb.append("\n }");

			bw.write(sb.toString());
			bw.close();
		} catch (Exception e) {
			logger.warn("Chemin de fichier de conf de Serveur est incorrect ", e);
		}
	}
	
	/**
	 * Load_balancing
	 * 
	 * @param list of ModelUrl
	 * @return
	 */

	public String buildLoadBalancingNginx(List<ModelUrl> Urls) {
		
		Map<String, ModelUrl> map = new HashMap<String, ModelUrl>();
		for (ModelUrl mu1: Urls) {	
			 String key		= mu1.getNomapp()+"-"+mu1.getHost()+"-"+mu1.getPort();
			 ModelUrl values	= mu1;
			map.put(key, values);
			
		}
		
		 StringBuilder sb = new StringBuilder();
		for (Entry<String, ModelUrl> entry: map.entrySet()) {
			
			System.out.println(" listServerAppls: "+entry.getKey() + "=" + entry.getValue());
			sb.append("server   ")
			.append(entry.getValue().getHost() + ":")
			.append(entry.getValue().getPort() + ";")
			.append("\n");

		}
		
	return sb.toString();

	}
	

	/**
	 * proxy_pass et proxy_redirect de virtual host avec
	 * @param modelUrl
	 * @return
	 */
	public String buildVirtualHostNginx(ModelUrl modelUrl) {
		final StringBuilder sb = new StringBuilder();

		sb.append("\n")
				// attention: doit pas avoir espace entre slash(/)
				.append("\t\t location   /"+modelUrl.getService() + "  {")
				.append("\n")
				.append("\t\t\t proxy_pass   ")
				.append(modelUrl.getProtocol() + "://")
				.append("mybalancer/"+modelUrl.getNomapp()+"/")
				.append(modelUrl.getService() + ";")
				.append("\n")
				.append("\t\t\t proxy_redirect          default;")
				.append("\n" + "\t\t}");

		return sb.toString();
	}

	/**
	 * recupérer le serverName
	 * @param url
	 * @return
	 */
	public String chargeServerName(ModelUrl url) {
		final StringBuilder sb = new StringBuilder();
		sb.append("\tserver_name     " + url.getServerName()+";" + "\n");
		return sb.toString();

	}

}
