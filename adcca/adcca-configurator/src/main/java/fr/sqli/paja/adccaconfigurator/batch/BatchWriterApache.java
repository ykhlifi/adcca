package fr.sqli.paja.adccaconfigurator.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
public class BatchWriterApache {

	final static Logger logger = Logger.getLogger(BatchWriterApache.class);

	private final String filename;

	/**
	 * Constructor
	 * 
	 * @param filename (path/filename.conf)
	 */
	public BatchWriterApache(String filename) {
		super();
		this.filename = filename;
	}

	/**
	 * Créer le model virtual host APACHE
	 * @param liste urls
	 */

	public void write(List<ModelUrl> modelUrls) {
		try {

			final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.filename)));
			final StringBuilder sb = new StringBuilder();
			final String header = this.buildVirtualHostHeader(); // tête de vhost
			final String footer = this.buildVirtualHostFooter(); // pied de vhost

			sb.append(header);

			sb.append(this.ServerName(modelUrls.get(0)));

			for (ModelUrl modelUrl : modelUrls) {
				sb.append(this.buildVirtualHostApache(modelUrl));
			}

			sb.append(footer);

			bw.write(sb.toString());
			bw.close();
		} catch (Exception e) {

			logger.warn("Chemin de fichier de conf de Serveur est incorrect ", e);
		}
	}

	/**
	 * ProxyPass and ProxyPassReverse
	 * 
	 * @param modelUrl
	 * @return
	 */
	public String buildVirtualHostApache(ModelUrl modelUrl) {
		final StringBuilder sb = new StringBuilder();

		sb.append("\n").append("\t\tProxyPass /").append(modelUrl.getService() + "  ")
				.append(modelUrl.getProtocol() + "://").append(modelUrl.getHost() + ":")
				.append(modelUrl.getPort() + "/").append(modelUrl.getService()).append("\n")
				.append("\t\tProxyPassReverse /").append(modelUrl.getService() + "  ")
				.append(modelUrl.getProtocol() + "://").append(modelUrl.getHost() + ":")
				.append(modelUrl.getPort() + "/").append(modelUrl.getService()).append("\n\n");

		return sb.toString();
	}

	/**
	 * recupérer le serverName
	 */
	public String ServerName(ModelUrl url) {
		final StringBuilder sb = new StringBuilder();
		sb.append("\tServerName     " + url.getServerName() + "\n");
		return sb.toString();
	}

	/**
	 * Build virtual host header (recupere le num port et server Admin)
	 */
	public String buildVirtualHostHeader() throws FileNotFoundException, IOException {
		final StringBuilder sb = new StringBuilder();
		/**
		 * charger les paramètres qui se trouve dans le fichier de conf de Configurator
		 * en appellant à la classe PropertyLoader
		 */
		Properties prop = PropertyLoader.load("application.properties");
		final String port = prop.getProperty("port");
		final String serverAdmin = prop.getProperty("serverAdmin");

		sb.append("<VirtualHost *:" + port + ">\n\n").append("\tServerAdmin  " + serverAdmin + "\n");

		return sb.toString();
	}

	/**
	 * Build virtual host footer
	 * 
	 * @return String
	 */
	public String buildVirtualHostFooter() {
		final StringBuilder sb = new StringBuilder();

		sb.append("\tProxyRequests Off\n\n").append("</VirtualHost>");

		return sb.toString();
	}
}
