package fr.sqli.paja.adccaconfigurator.servicesreçu;

import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author sebouyahmed
 * 
 /**
 * cette classe pour recevoir les infos envoyer par Detector.
 */
@RestController
public class Recevoir{	
/**
 *  SERVICE QUI VAS RECEVOIR LES MICROSERVICES ENVOYER PAR DETECTOR ************************	
 * @param value
 * @return
 * @throws IOException
 */
    @PostMapping("/microservices")
    public String microService(@RequestBody String value) throws IOException {
    	
    	BuildFile.buildFile(value);
    	
        return value; 
    }
}

