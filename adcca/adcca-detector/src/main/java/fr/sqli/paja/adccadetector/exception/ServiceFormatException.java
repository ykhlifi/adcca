package fr.sqli.paja.adccadetector.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * 
 * @author sebouyahmed
 *
 */
	/**
	 *  Si cette exception est levée, le code status retourné sera 406
	 * @author sebouyahmed
	 *
	 */
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	public class ServiceFormatException extends RuntimeException {
		
		public ServiceFormatException() {
			super("Veuillez respectez le format des microservices dans le fichier de configuration!");
		}
		
		public ServiceFormatException(String message) {
			super(message);
		}
		
		public ServiceFormatException(Throwable throwable) {
			super(throwable);
		}
	}



