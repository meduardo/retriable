package my.common.tools.retry.configuration;

import java.util.Set;

/**
 * 
 * @author mariogiolo
 *
 */
public class RetryDefaultConfig implements RetryConfig {
	
	public static final RetryConfig DEFAULT_CONFIG = RetryConfigBuilder.with()
																	   .withExponentialBackoff()
																	   .timeBetweenRetries(20)
																	   .maxTimeBetweenRetries(300)
																	   .numberOfRetries(5)
																	   .limited()
																	   .create();
																	   
	private RetryDefaultConfig(){}
	
	@Override
	public boolean exponentialBackoff() {
		return DEFAULT_CONFIG.exponentialBackoff();
	}
	
	@Override
	public int backoffInterval() {
		return DEFAULT_CONFIG.backoffInterval();
	}
	
	@Override
	public int maxTimeBetweenRetries() {
		return DEFAULT_CONFIG.maxTimeBetweenRetries();
	}
	
	@Override
	public int numberOfRetries() {
		return DEFAULT_CONFIG.numberOfRetries();
	}
	
	@Override
	public Set<Class<? extends Exception>> exceptions() {
		return DEFAULT_CONFIG.exceptions();
	}

	@Override
	public boolean unlimited() {
		return DEFAULT_CONFIG.unlimited();
	}
	
}
