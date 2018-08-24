package fr.sqli.paja.adccaconfigurator;

import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
/**
 * 
 * @author sebouyahmed
 *
 */
@RefreshScope
@SpringBootApplication
public class TestConf{
	
	public static void main(String[] args) throws JSONException, Exception {
					
				SpringApplication.run(AdccaConfiguratorApplication.class, args);						
	}	
}

/**N'importe quel  Spring Bean qui est annoté (annotate) par  @RefreshScope sera rafraîchit à l'heure d'exécution (runtime). 
 * Et tous les composants qui les utilisent obtiendront une nouvelle instance lors de l'appel de méthode suivant,
 * entièrement initialisé et injecté avec toutes les dépendances.
 */