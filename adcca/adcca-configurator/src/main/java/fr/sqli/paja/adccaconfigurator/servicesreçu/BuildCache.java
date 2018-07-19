package fr.sqli.paja.adccaconfigurator.servicesreçu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cacheonix.Cacheonix;
import org.cacheonix.cache.Cache;
import org.json.JSONObject;

import fr.sqli.paja.adccaconfigurator.batch.BatchReader;
/**
 * 
 * @author sebouyahmed
 *
 */
public class BuildCache {
	
	final static Logger logger = Logger.getLogger(BatchReader.class);
	/**
	 * Build <code>Cache</code> from <code>JSONObject</code>
	 * @param jsonObj
	 * @throws Exception
	 * @return
	 */
		public static final Cache<String, String> buildMapCache(final JSONObject jsonObjCache) throws Exception {
			
			final Cacheonix cacheManager = Cacheonix.getInstance();
			final Cache<String, String> cache	= cacheManager.getCache("invoce.cache");		
			final Iterator<?> keys			= jsonObjCache.keys();
			
			try {
				
			while (keys.hasNext()) {
				final String key	= (String) keys.next();
				final String value	= (String) jsonObjCache.get(key);	
				
				cache.put(key, value);			
			}
			}catch (Exception e) {
				logger.warn("Format JSONObjet incorrecte !",e);
			}
			return cache;
		}
		
		/**
		 * Build <code>Map</code> from <code>JSONObject</code>
		 * @param jsonObj
		 * @throws Exception
		 * @return
		 */
		public static final Map<String, String> buildMap(final JSONObject jsonObj) throws Exception {
			
			final Map<String, String> map	= new HashMap<>();
			final Iterator<?> keys			= jsonObj.keys();
			
			try {
			while (keys.hasNext()) {
				final String key	= (String) keys.next();
				final String value	= (String) jsonObj.get(key);
				
				map.put(key, value);
			}
			
			}catch (Exception e) {
				logger.warn("Format JSONObjet incorrecte !",e);
			}
			return map;
		}
}

