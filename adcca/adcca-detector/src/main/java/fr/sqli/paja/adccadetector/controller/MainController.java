package fr.sqli.paja.adccadetector.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.sqli.paja.adccadetector.microservices.DetectionServices;

/*N'importe quel  Spring Bean qui est annoté (annotate) par  @RefreshScope sera rafraîchit à l'heure d'exécution (runtime).
 *  Et tous les composants qui les utilisent obtiendront une nouvelle instance lors de l'appel de méthode suivant,
 *  entièrement initialisé et injecté avec toutes les dépendances.
 * */

@RefreshScope
@RestController
// cette classe pour recupérer le contenu de ficher de configuration.
public class MainController {
	/*
	public static String mservice;
	
	@Value("${microservice}")
	private String microServices;
	
	@Value("${urlserveurconf}")
	private String urlserveurconf;
	
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
 
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
 
    @Value("${spring.datasource.username}")
    private String userName;
 
    @Value("${spring.datasource.password}")
    private String password;
  
 
    @RequestMapping("/showConfig")
    @ResponseBody
    public String showConfig() {
        String configInfo = "microservices =" + microServices //
        		+ "<br/>spring.datasource.driver-class-name=" + driverClassName //
                + "<br/>spring.datasource.url=" + datasourceUrl //
                + "<br/>spring.datasource.username=" + userName //
                + "<br/>spring.datasource.password=" + password;//
        return configInfo;
    }
    
    
     * service qui recupere les urls des microservices dans le fichier de configuration
     * 
    @RequestMapping("/microservices")
    @ResponseBody
    public String microService() {
        String ms ="{\"microservices\""+":"+microServices+"}";          
        return ms;
    }
   */
        
}
  