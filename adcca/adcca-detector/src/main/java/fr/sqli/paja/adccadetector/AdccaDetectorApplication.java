package fr.sqli.paja.adccadetector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import fr.sqli.paja.adccadetector.controller.MainController;
import fr.sqli.paja.adccadetector.microservices.DetectionServices;
import fr.sqli.paja.adccadetector.propertiesloader.PropertyLoader;

/*N'importe quel  Spring Bean qui est annoté (annotate) par  @RefreshScope sera rafraîchit à l'heure d'exécution (runtime). 
 * Et tous les composants qui les utilisent obtiendront une nouvelle instance lors de l'appel de méthode suivant,
 * entièrement initialisé et injecté avec toutes les dépendances.
 * */
@RefreshScope
@SpringBootApplication
public class AdccaDetectorApplication  {
	//public static String url="http://localhost:8888/bus/refresh";	
	//http://localhost:8888/bus/refresh
	static String urlsc;
	static String urlconf;

	public static void main(String[] args) throws  IOException {
		
		/**
		 * charger le fichier de properties
		 */
		Properties prop = PropertyLoader.load("application.properties");
		urlsc=prop.getProperty("urlserveurconfiguration");
		urlconf=prop.getProperty("urlconfigurator");
		
		System.out.println("URL de Serveur de configuration est :"+ urlsc);
		System.out.println("URL configurato est :"+ urlconf);
		
		SpringApplication.run(AdccaDetectorApplication.class, args);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						
						DetectionServices.autoDetecteServices(urlsc,urlconf);
						Thread.sleep(10000);
				
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
