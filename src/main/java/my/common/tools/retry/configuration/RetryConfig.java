package my.common.tools.retry.configuration;

import java.util.Set;

/**
 * 
 * @author mariogiolo
 *
 */
public interface RetryConfig {
	
	boolean unlimited();
	
	//TODO Evoluir para user TimeUnit 
	int backoffInterval();
    
    int maxTimeBetweenRetries();
    
    int numberOfRetries();
    
    Set<Class<? extends Exception>> exceptions();
 
    boolean exponentialBackoff();
    
}