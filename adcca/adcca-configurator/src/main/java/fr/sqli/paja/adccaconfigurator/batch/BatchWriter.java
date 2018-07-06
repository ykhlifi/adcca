package fr.sqli.paja.adccaconfigurator.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import fr.sqli.paja.adccaconfigurator.url.ModelUrl;

public class BatchWriter {
	
	private final String filename;
	
	
	//**** Constructor avec @param filename
	 
	public BatchWriter(String filename) {
		super();
		this.filename = filename;
	}	
	
	// Write virtual host into file @param urls

	public void write(List<ModelUrl> modelUrls) {
		try {
			
			final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.filename)));
			final StringBuilder sb	= new StringBuilder();
			final String header		= this.buildVirtualHostHeader(); // tête de vhost
			final String footer		= this.buildVirtualHostFooter(); // pied de vhost
			
			sb.append(header);
			
			for (ModelUrl modelUrl: modelUrls) {
				sb.append(this.buildVirtualHost(modelUrl));
			}
			
			sb.append(footer);
			
			
			bw.write(sb.toString());
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	 // Build virtual host avec @param url
	 
	public String buildVirtualHost(ModelUrl modelUrl) {
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
	 	 
	public String buildVirtualHostHeader() {
		final StringBuilder sb = new StringBuilder();
		
		sb
			.append("<VirtualHost *:80>\n\n")
			.append("\tServerName localhost\n")
			.append("\tServerAdmin webmaster@localhost\n")
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
