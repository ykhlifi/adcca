package fr.sqli.paja.adccaconfigurator.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
public class LoadBalancingApache {

	final static Logger logger = Logger.getLogger(LoadBalancingApache.class);

	private final String filename;

	/**
	 * Constructor
	 * 
	 * @param filename (path/filename.conf)
	 */
	public LoadBalancingApache(String filename) {
		super();
		this.filename = filename;
	}

	/**
	 * Créer le model virtual host pour APACHE
	 * @param liste urls
	 */
	
	public void write(List<ModelUrl> modelUrls) {
		try {

			final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.filename)));
			final StringBuilder sb = new StringBuilder();
			final String header = this.buildVirtualHostHeader(); // tête de vhost
			final String footer = this.buildVirtualHostFooter(); // pied de vhost
			final String bheader = this.buildBalancerHeader(); // tête de load balancing
			final String bfooter = this.buildBalancerFooter();// pied de load balancing
			
			sb.append(header);
			
			sb.append(this.ServerName(modelUrls.get(0)));
			
			sb.append(bheader);
			
			sb.append(this.buildLoadBalancingApache(modelUrls));
		
			sb.append(bfooter);

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
	 * Load_balancing
	 * 
	 * @param list of ModelUrl
	 * @return
	 */

	public String buildLoadBalancingApache(List<ModelUrl> Urls) {
		
		//Map<String, ModelUrl> map = Collections.synchronizedMap(new HashMap<>());
		Map<String, ModelUrl> map = new HashMap<String, ModelUrl>();
		for (ModelUrl mu1: Urls) {	
			 String key		= mu1.getHost()+mu1.getPort();
			 ModelUrl values	= mu1;
			map.put(key, values);
			
		}
		
		 StringBuilder sb = new StringBuilder();
		for (Entry<String, ModelUrl> entry: map.entrySet()) {
			
			//System.out.println(" listServerAppls: "+entry.getKey() + "=" + entry.getValue());
			sb.append("\t\t\t\tBalancerMember  ")
			.append("\"" + entry.getValue().getProtocol() + "://")
			.append(entry.getValue().getHost() + ":")
			.append(entry.getValue().getPort() + "\"")
			//.append("  route="+entry.getValue().getServerName())
			.append("\n\n");

		}
		
	return sb.toString();

	}
	
	
	
	/**
	 * Build Balancer header
	 * 
	 * @return String
	 */
	public String buildBalancerHeader() {
		final StringBuilder sb = new StringBuilder();
		final String cookie_de_session ="\t\tHeader add Set-Cookie \"JSESSIONID=.%{BALANCER_WORKER_ROUTE}e; path=/\" env=BALANCER_ROUTE_CHANGED";
		final String  balancer ="\t\t<Proxy \"balancer://mybalancer\">";
		
		sb.append(cookie_de_session+"\n")
		.append(balancer+"\n\n");			

		return sb.toString();
	}
	/**
	 * Build Balancer footer
	 * 
	 * @return String
	 */
	public String buildBalancerFooter() {
		final StringBuilder sb = new StringBuilder();
		final String balancer ="\t\t\tProxySet stickysession=JSESSIONID";
		final String finproxy ="\t\t</Proxy>";	
	
		sb.append(balancer+"\n")
		.append(finproxy+"\n\n");				
				
		return sb.toString();
	}

	/**
	 * ProxyPass and ProxyPassReverse
	 * 
	 * @param modelUrl
	 * @return
	 */
	public String buildVirtualHostApache(ModelUrl modelUrl) {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("\n")
		.append("\t\tProxyPass          ")
		
				.append("\"/"+modelUrl.getNomapp()+"/")
				.append(modelUrl.getService() + "\"   ")
				
				.append("\"balancer://mybalancer/"+modelUrl.getNomapp()+"/")
				.append(modelUrl.getService() + "\"")
				
				.append("\n")
				
		.append("\t\tProxyPassReverse   ")
				
				.append("\"/"+modelUrl.getNomapp()+"/")
				.append(modelUrl.getService() + "\"   ")
				.append("\"balancer://mybalancer/"+modelUrl.getNomapp()+"/")
				.append(modelUrl.getService() + "\"")
				.append("\n\n");

		return sb.toString();
	}

	/**
	 * recupérer le serverName
	 */
	public String ServerName(ModelUrl url) {
		final StringBuilder sb = new StringBuilder();
		sb.append("\tServerName     " + url.getServerName() + "\n\n");
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

		sb.append("<VirtualHost *:" + port + ">\n\n")
		.append("\tServerAdmin  " + serverAdmin + "\n");

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

