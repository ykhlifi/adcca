package fr.sqli.paja.adccadetector.modelurl;

import java.io.Serializable;
/**
 * 
 * @author sebouyahmed
 *
 */
@SuppressWarnings("serial")
public class ModelUrl implements Serializable {

		private String cleService;// clé de service
		private String nomapp;// nom de l'application, nom de fichier de conf de Vhost dans le serveur web
		private String serverName; //servername au niveau de Vhost
		private String protocol;
		private String host; // localhost
		private String port;
		private String service; // complément de service example : consultEnfant/1
		
		/**
		 * Constructure sans paramètres
		 */
		public ModelUrl() {
			super();
		}
		/**
		 * Constructure avec paramètres
		 */
		public ModelUrl(String cleService,String nomapp, String serverName, String protocol, String host, String port, String service) {
			super();
			this.cleService=cleService;
			this.nomapp=nomapp;
			this.serverName=serverName;
			this.protocol = protocol;
			this.host = host;
			this.port = port;
			this.service = service;
		}
		/**
		 * Getters & Setters
		 * @return
		 */
		public String getCleService() {
			return cleService;
		}

		public void setCleService(String cleService) {
			this.cleService = cleService;
		}
		
		public String getNomapp() {
			return nomapp;
		}

		public void setNomapp(String nomapp) {
			this.nomapp = nomapp;
		}
		
		public String getServerName() {
			return serverName;
		}

		public void setServerName(String serverName) {
			this.serverName = serverName;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

		@Override
		public String toString() {
			return "ModelUrl [ cleService=" + cleService + ", nomapp=" + nomapp + ",serverName=" +serverName+ ",protocol=" + protocol + ", host=" + host + ", port=" + port
					+ ", service=" + service + "]";
		}
		
	}

	
