package fr.sqli.paja.adccaconfigurator.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.sqli.paja.adccaconfigurator.url.ModelUrl;

/**
 * 
 * @author sebouyahmed
 *
 */
public class BatchReader {

	final static Logger logger = Logger.getLogger(BatchReader.class);

	private final String filename;

	public BatchReader(String filename) {
		super();
		this.filename = filename;
	}

	/**
	 * Convert line string to ModelUrl object
	 * 
	 * @param item
	 * @return ModelUrl
	 */
	public ModelUrl process(String item) {
		ModelUrl model = null;
		try {
			final Object[] objs = item.split(",");

			model = new ModelUrl(objs[0].toString(), objs[1].toString(), objs[2].toString(), objs[3].toString(),
					objs[4].toString(), objs[5].toString(), objs[6].toString());

		} catch (Exception e) {

			logger.warn(
					"Format incorrect, veuillez respecter ce model =>\"cléService\":\"nomApp,serverName,Protocol,HostName,port,service\" ",
					e);
		}

		return model;
	}

	/**
	 * Read file and convert lines to ModelUrls
	 * 
	 * @return list
	 */
	public List<ModelUrl> read() {

		final List<ModelUrl> lst = new ArrayList<>();

		try {
			final BufferedReader br = new BufferedReader(new FileReader(new File(this.filename)));
			String line = null;

			while ((line = br.readLine()) != null) {
				lst.add(this.process(line));
			}

			br.close();
		} catch (Exception e) {
			logger.info("Fichier CSV introuvable", e);

		}

		return lst;
	}

}
