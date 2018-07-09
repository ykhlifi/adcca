package fr.sqli.paja.adccaconfigurator.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import fr.sqli.paja.adccaconfigurator.propertiesLoader.PropertyLoader;
import fr.sqli.paja.adccaconfigurator.url.ModelUrl;

public class BatchWriterApache {
	
	private final String filename;
	
	//**** Constructor avec @param filename
	 
	public BatchWriterApache(String filename) {
		super();
		this.filename = filename;
	}	
	
	// Write virtual host APACHE into file @param urls

	public void write(List<ModelUrl> modelUrls) {
		try {
			
			final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.filename)));
			final StringBuilder sb	= new StringBuilder();
			final String header		= this.buildVirtualHostHeader(); // tête de vhost
			final String footer		= this.buildVirtualHostFooter(); // pied de vhost
			
			sb.append(header);
			
			for (ModelUrl modelUrl: modelUrls) {
				sb.append(this.buildVirtualHostApache(modelUrl));
			}
			
			sb.append(footer);
			
			
			bw.write(sb.toString());
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	 // Build virtual host avec @param url
	 
	public String buildVirtualHostApache(ModelUrl modelUrl) {
		final StringBuilder sb = new StringBuilder();
		
		sb
			.append("\n")
			.append("\t\tProxyPass /")
			.append(modelUrl.getService() + " ")
			.append(modelUrl.getProtocol())
			.append(modelUrl.getHost())
			.append(modelUrl.getPort())
			.append(modelUrl.getComplement())
			.append("\n")
			.append("\t\tProxyPassReverse /")
			.append(modelUrl.getService() + " ")
			.append(modelUrl.getProtocol())
			.append(modelUrl.getHost())
			.append(modelUrl.getPort())
			.append(modelUrl.getComplement())
			.append("\n\n")
		;
		
		return sb.toString();
	}
	
	 //Build virtual host header
	 	 
	public String buildVirtualHostHeader() throws FileNotFoundException, IOException {
		final StringBuilder sb = new StringBuilder();
		
		/**
	      * charger les parametre qui se trouve dans le fichier de conf de Configurator en appellant
	      * à la classe PropertyLoader
	      */				     
	     Properties prop = PropertyLoader.load("application.properties");    
		 final String port=prop.getProperty("port");
		 final String serveurName=prop.getProperty("serveurName");
		 final String serverAdmin=prop.getProperty("serverAdmin");
		
		sb
			.append("<VirtualHost *:"+port+">\n\n")
			.append("\tServerName  " +serveurName+ "\n")
			.append("\tServerAdmin  " +serverAdmin+ "\n")
		;
		
		return sb.toString();
	}
	
	
	 // Build virtual host footer

	public String buildVirtualHostFooter() {
		final StringBuilder sb = new StringBuilder();
		
		sb
			.append("\tProxyRequests Off\n\n")
			.append("</VirtualHost>")
		;
		
		return sb.toString();
	}
}
