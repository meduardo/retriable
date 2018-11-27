package my.common.tools.retry.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author mariogiolo
 *
 */
public class RetryConfigBuilder {

	private boolean unlimited;
	
	private int backoffInterval;
    
	private int maxTimeBetweenRetries;
    
	private int numberOfRetries;
    
	private Set<Class<? extends Exception>> exceptions = new HashSet<>();
 
	private boolean exponentialBackoff;
		
	private RetryConfigBuilder() {}
	
	public static final RetryConfigBuilder with() {
		return new RetryConfigBuilder();
	}
		
	@SafeVarargs
    public final RetryConfigBuilder forExceptions(final Class<? extends Exception>... exceptions){
        this.exceptions.addAll(Arrays.asList(exceptions));
    	return this;
    }
    
	public final RetryConfigBuilder withoutExponentialBackoff() {
        this.exponentialBackoff = false;
        return this;
    }
    
	public final RetryConfigBuilder withExponentialBackoff() {
        this.exponentialBackoff = true;
        return this;
    }
	
	public final RetryConfigBuilder unlimited() {
        this.unlimited = true;
        return this;
    }
	
	public final RetryConfigBuilder limited() {
        this.unlimited = false;
        return this;
    }
	
    public final RetryConfigBuilder timeBetweenRetries(int backoffInterval) {
        this.backoffInterval = backoffInterval;
        return this;
    }
    
    public final RetryConfigBuilder maxTimeBetweenRetries(int maxTimeBetweenRetries) {
        this.maxTimeBetweenRetries = maxTimeBetweenRetries;
        return this;
    }

    public final RetryConfigBuilder numberOfRetries(int retries) {
        this.numberOfRetries = retries;
        return this;
    }
    
    public final RetryConfig create() {
    	return new RetryConfig() {
			
			@Override
			public boolean unlimited() {
				return unlimited;
			}
			
			@Override
			public int numberOfRetries() {
				return numberOfRetries;
			}
			
			@Override
			public int maxTimeBetweenRetries() {
				return maxTimeBetweenRetries;
			}
			
			@Override
			public boolean exponentialBackoff() {
				return exponentialBackoff;
			}
			
			@Override
			public Set<Class<? extends Exception>> exceptions() {
				return exceptions;
			}
			
			@Override
			public int backoffInterval() {
				return backoffInterval;
			}
		};
    }
}
