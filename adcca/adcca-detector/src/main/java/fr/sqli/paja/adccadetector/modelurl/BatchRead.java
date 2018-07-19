package fr.sqli.paja.adccadetector.modelurl;

import org.apache.log4j.Logger;
import fr.sqli.paja.adccadetector.microservices.DetectionServices;
/**
 * 
 * @author sebouyahmed
 *
 */
public class BatchRead {
	
	final static  Logger logger =  Logger.getLogger(DetectionServices.class);
		
		/**
		 * Convert line string to ModelUrl object
		 * @param item
		 * @return
		 */
		public static String processUrl(String cle, String item) {
			ModelUrl model=null;
			String url =null;
			
			try {
				
					final String[] objs = item.split(",");
					 model = new ModelUrl(cle, objs[0], objs[1], objs[2], objs[3], objs[4], objs[5]);
					 
					 url = model.getProtocol()+"://"+model.getHost()+":"+model.getPort()+"/"+model.getService(); 	
						
				
			}catch(Exception e) {
			
				 logger.warn("Problème de fichier de configuration !, veuillez respecter ce model =>\"cléService\":\"nomApp,serverName,Protocol,HostName,port,service\" ",e);
			}
			
			return url;
		}
		
		/**
		 * Convert line string url to model service en réspectant le fichier de configuration
		 * @param url
		 * @return  Exemple : "servive":"nomApp,serverName,Protocol,HostName,port,service"
		 */
		public static String processService(String cle, String item) {
			
			ModelUrl model=null;
			String service =null;
			
			try {
				
					final String[] objs = item.split(",");
					 
					model = new ModelUrl(cle, objs[0], objs[1], objs[2], objs[3], objs[4], objs[5]);
					 
					service = "\""+ model.getCleService()+"\""+":"+"\""+model.getNomapp()+","+model.getServerName()+","+model.getProtocol()+","+
								model.getHost()+','+model.getPort()+","+model.getService()+"\""; 
						//System.out.println(service);
			}catch(Exception e) {
			
				 logger.warn("Problème de fichier de configuration !, veuillez respecter ce model =>\"cleServive\":\"nomApp,serverName,Protocol,HostName,port,service\"",e);
			}
			
			return service;
		}		
		
	}

