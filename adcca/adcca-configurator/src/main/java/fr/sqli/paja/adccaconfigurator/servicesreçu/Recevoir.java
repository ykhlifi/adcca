package fr.sqli.paja.adccaconfigurator.servicesreçu;

//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

// cette classe pour recevoir les infos envoyer par Detector.
public class Recevoir{
	
	/*@GetMapping("servicesConf")
	public String get() {
		return BuildFile.getServices();
	}*/
	
//****************** SERVICE QUI VAS RECEVOIR LES MICROSERVICES ENVOYER PAR DETECTOR ************************	
    @PostMapping("/microservices")
    public String microService(@RequestBody String value) {
    	BuildFile.setServices(value);
        return value; 
    }
}

