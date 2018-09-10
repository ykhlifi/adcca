package fr.sqli.paja.adccaconfigurator.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import fr.sqli.paja.adccaconfigurator.propertiesLoader.PropertyLoader;
import fr.sqli.paja.adccaconfigurator.url.ModelUrl;

/**
 * 
 * @author sebouyahmed
 *
 */
public class BatchWriterNginxTomcat {

	final static Logger logger = Logger.getLogger(BatchWriterNginxTomcat.class);

	private final String filename;

	/**
	 * Constructor
	 * 
	 * @param filename
	 *            (path/filename.conf)
	 */
	public BatchWriterNginxTomcat(String filename) {
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
	 * proxy_pass et proxy_redirect de virtual host avec
	 * @param modelUrl
	 * @return
	 */
	public String buildVirtualHostNginx(ModelUrl modelUrl) {
		final StringBuilder sb = new StringBuilder();

		sb.append("\n")
				// attention: doit pas avoir espace entre slash(/)
				.append("\t\t location   /" + modelUrl.getService() + "  {")
				.append("\n").append("\t\t\t proxy_pass   ")
				.append(modelUrl.getProtocol() + "://")
				.append(modelUrl.getHost() + ":")
				.append(modelUrl.getPort() + "/")
				.append(modelUrl.getNomapp() + "/")
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
