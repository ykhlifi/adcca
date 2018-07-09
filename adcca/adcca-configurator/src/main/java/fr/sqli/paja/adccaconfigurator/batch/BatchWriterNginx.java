package fr.sqli.paja.adccaconfigurator.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

import fr.sqli.paja.adccaconfigurator.propertiesLoader.PropertyLoader;
import fr.sqli.paja.adccaconfigurator.url.ModelUrl;

public class BatchWriterNginx {
	
	private final String filename;
	
	public BatchWriterNginx(String filename) {
		super();
		this.filename = filename;
	}	
	
	
	// Write virtual host NGINX into file @param urls

	public void write(List<ModelUrl> modelUrls) {
		try {
			
			Properties prop = PropertyLoader.load("application.properties");    
			final String port=prop.getProperty("port");
			final String serveurName=prop.getProperty("serveurName");
			
			final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.filename)));
			final StringBuilder sb	= new StringBuilder();
			
			sb.append("\n");
			sb.append("server  {");
			sb.append("\n");
			sb.append("\t listen  "+port+";");
			sb.append("\n");
			sb.append("\t server_name  "+serveurName+";");
			sb.append("\n");
			
			for (ModelUrl modelUrl: modelUrls) {
				sb.append(this.buildVirtualHostNginx(modelUrl));
			}
			
			sb.append("\n }");
			
			bw.write(sb.toString());
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	 // Build virtual host avec @param url
	 
		public String buildVirtualHostNginx(ModelUrl modelUrl) {
			final StringBuilder sb = new StringBuilder();
			
			sb
				.append("\n")
				.append("\t\t location /"+ modelUrl.getService()+"{")
				.append("\n")
				.append("\t\t\t Proxy_Pass  ")
				.append(modelUrl.getProtocol())
				.append(modelUrl.getHost())
				.append(modelUrl.getPort())
				.append(modelUrl.getComplement()+";")
				.append("\n")
				.append("\t\t\t proxy_redirect          default;")
				.append("\n"+"\t\t}");
					
			return sb.toString();
		}

}
