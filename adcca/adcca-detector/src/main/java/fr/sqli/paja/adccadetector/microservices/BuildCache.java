package fr.sqli.paja.adccadetector.microservices;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cacheonix.Cacheonix;
import org.cacheonix.cache.Cache;
import org.json.JSONObject;

import fr.sqli.paja.adccadetector.CallConf.CallConfigurator;
/**
 * 
 * @author sebouyahmed
 *
 */
public class BuildCache {
	
	final static  Logger logger =  Logger.getLogger(CallConfigurator.class);
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
			}catch(Exception e){
				logger.warn("Format de JSONObjet est incorrect");
			}
			return map;
		}
		
	/**
	 * Build <code>Cache</code> from <code>JSONObject</code>
	 * @param jsonObjCache
	 * @return
	 * @throws Exception
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
			}catch(Exception e){
				logger.warn("Format de JSONObjet est incorrect");
			}
			return cache;
		}
}
