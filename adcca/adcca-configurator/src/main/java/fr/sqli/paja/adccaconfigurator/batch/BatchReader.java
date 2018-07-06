package fr.sqli.paja.adccaconfigurator.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import fr.sqli.paja.adccaconfigurator.url.ModelUrl;

public class BatchReader {
	
	private final String filename;
	
	public BatchReader(String filename) {
		super();
		this.filename = filename;
	}
	
	/**
	 * Convert line string to ModelUrl object
	 * @param item
	 * @return
	 */
	public ModelUrl process(String item) {
		final Object[] objs = item.split(",");
		
		return new ModelUrl(
			objs[0].toString(), 
			objs[1].toString(), 
			objs[2].toString(), 
			objs[3].toString(), 
			objs[4].toString()
		);
	}
	
	/**
	 * Read file and convert lines to ModelUrl list objects
	 * @return
	 */
	
	public List<ModelUrl> read() {
		
		final List<ModelUrl> lst = new ArrayList<>();
		
		try {
			final BufferedReader br = new BufferedReader(new FileReader(new File(this.filename)));
			String line				= null;
			
			while((line = br.readLine()) != null) {
				lst.add(this.process(line));
			}
			
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return lst;
	}
	
	
}
