package fr.sqli.paja.adccaconfigurator.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import fr.sqli.paja.adccaconfigurator.url.ModelUrl;
/**
 * 
 * @author sebouyahmed
 *
 */
public final class ListeApp {
	
	private ListeApp() {
	}
	
	/**
	 * processFromUrls transformer une liste aux plusieurs listes selon le nomApp 
	 * @param List<ModelUrl> urls
	 * @return Map (clé= nom app, valeur= liste des urls)	
	 */
	public static final Map<String, List<ModelUrl>> processFromUrls(final List<ModelUrl> urls) {
		final Map<String, List<ModelUrl>> map = new HashMap<>();
		final List<ModelUrl> newUrls		  = urls.stream().filter(distinctByKey(ModelUrl::getNomapp)).collect(Collectors.toList());
		
		for (ModelUrl mu1: newUrls) {
			final List<ModelUrl> values = new ArrayList<>();
			final String key			= mu1.getNomapp();
			
			for (ModelUrl mu2: urls) {
				if (key.equalsIgnoreCase(mu2.getNomapp())) {
					values.add(mu2);
				}
			}
			
			map.put(key, values);
		}
		
		return map;
	}
	
	private static final <T> Predicate<T> distinctByKey(final Function<? super T, ?> keyExtractor) {
	    final Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
}
