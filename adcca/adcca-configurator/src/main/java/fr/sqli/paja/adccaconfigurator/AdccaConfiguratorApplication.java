package fr.sqli.paja.adccaconfigurator;

import java.util.List;
import java.util.Properties;

import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import fr.sqli.paja.adccaconfigurator.batch.BatchReader;
import fr.sqli.paja.adccaconfigurator.batch.BatchWriter;
import fr.sqli.paja.adccaconfigurator.propertiesLoader.PropertyLoader;
import fr.sqli.paja.adccaconfigurator.servicesreçu.BuildFile;
import fr.sqli.paja.adccaconfigurator.url.ModelUrl;

/*N'importe quel  Spring Bean qui est annoté (annotate) par  @RefreshScope sera rafraîchit à l'heure d'exécution (runtime). 
 * Et tous les composants qui les utilisent obtiendront une nouvelle instance lors de l'appel de méthode suivant,
 * entièrement initialisé et injecté avec toutes les dépendances.
 * */
@RefreshScope
@SpringBootApplication
public class AdccaConfiguratorApplication {
	
	static String input;
	static String output;
	
	public static void main(String[] args) throws JSONException, Exception {
		
/**
 * charger le fichier de properties
 */
				Properties prop = PropertyLoader.load("application.properties");
				input=prop.getProperty("input");
				output=prop.getProperty("output");
				
		// input utilisé dans la classe BatchReader******************************* 
		//System.setProperty("input", "file://" + new File("D:\\projets\\eclipse-workspace\\adcca-configurator\\src\\main\\resources\\cvs\\input\\microservices.csv").getAbsolutePath());
		SpringApplication.run(AdccaConfiguratorApplication.class, args);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						BuildFile.buildFile();
						final List<ModelUrl> lst = new BatchReader(input).read();// pour créer une liste à partir de fichier input
						new BatchWriter(output).write(lst);						
						Thread.sleep(10000);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();	
		
	}
	
}
