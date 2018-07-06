package fr.sqli.paja.adccaconfigurator.url;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ModelUrl implements Serializable {
	private String service; // nom de service
	private String protocol;
	private String host; // localhost
	private String port;
	private String complement; // complément de service example : consultEnfant/1
	
	public ModelUrl() {
		super();
	}

	public ModelUrl(String service, String protocol, String host, String port, String complement) {
		super();
		this.service = service;
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.complement = complement;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
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

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	@Override
	public String toString() {
		return "ModelUrl [service=" + service + ", protocol=" + protocol + ", host=" + host + ", port=" + port
				+ ", complement=" + complement + "]";
	}
}
