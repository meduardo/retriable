package my.common.tools.retry;

import java.util.concurrent.Callable;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.common.tools.retry.configuration.RetryConfig;
import my.common.tools.retry.configuration.RetryDefaultConfig;

/**
 * Classe que permite de forma programática definir um processo
 * que poderá ser reexecutado, caso ocorra alguma falha na sua execução. 
 * 
 * @author mariogiolo
 *
 * @param <T>
 */
public final class Retry<T> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Retry.class);

	private final RetryConfig config;
    
	private final Callable<T> task;
	
	private final Callable<T> fallback;
    
	private final Predicate<Exception> isExpected;
	
	private final Predicate<Exception> isUnexpected;
	
	public Retry(final RetryConfig config, final Callable<T> task, final Callable<T> fallback) {
		this.config = config;
		this.isExpected = exc -> this.config.exceptions().isEmpty() || this.config.exceptions().contains(exc.getClass());
		this.isUnexpected = exc -> this.isExpected.negate().test(exc);
		
		this.task = task;
		this.fallback = fallback;
		
		//TODO mover para uma classe de checagem
		checkConfig();
	}

	private final int sleepTime(final int retryCount) {
		int backoffInterval = this.config.backoffInterval();
		
    	if (this.config.exponentialBackoff()){
    		return (int) (((Math.pow(2, retryCount) - 1) / 2) * backoffInterval);
    	} 
    	return backoffInterval;
    }
    
    private final int waitTime(final int retryCount){
    	int sleepTime = sleepTime(retryCount);
    	int maxTimeBetweenRetries = this.config.maxTimeBetweenRetries();
    	return sleepTime < maxTimeBetweenRetries ? sleepTime : maxTimeBetweenRetries;
    } 
    
    private void checkValidTask(){
    	if (task == null) {
    		throw new IllegalStateException("No Operation! At least one task must be defined.");
    	}
    }
    
    private void checkValidNumberOfRetries(){
    	if (this.config.numberOfRetries() < 0) {
    		throw new IllegalStateException("Number of retries must be 0 or more!");
    	}
    }
   
    //TODO  criar uma classe de checagem, antes de instanciar Retry.
    private void checkConfig(){
    	checkValidTask();
    	checkValidNumberOfRetries();
    }

	private void waitNextTry(int retryCount) throws InterruptedException {
		int sleepTime = waitTime(retryCount);
		Thread.sleep(sleepTime);
	}
	
	private void treatException(final int retryCount, final Exception exc) {
		LOGGER.debug("Error: {} ({}) : {}", retryCount, exc.getClass(), exc.getMessage());
		
		if (isUnexpected.test(exc)) {
			LOGGER.error("Unexpected Exception.", exc);
			throw new RetryException(exc);
    	}
		
		if (retryCount == config.numberOfRetries()) {
			LOGGER.warn("Number of retries finished");
			throw new RetryException(exc);
		}
    }
    
	public T exec() {
		
		for (int i = 0; config.unlimited() || i <= config.numberOfRetries(); i++) {
			try {
				if (Thread.interrupted()){
					throw new InterruptedException();
				}
				if (i == 0){
					return task.call();
				}

				waitNextTry(i);
				LOGGER.debug("Retrying... " + i);
				return fallback.call();

			} catch (InterruptedException interrupted){
				Thread.currentThread().interrupt();
				throw RetryException.with(interrupted);

			} catch (Exception e) {
				treatException(i, e);
			}
		}

		throw new RetryException();
	}

    public static final <T> RetryBuilder<T> with(final Callable<T> originalOperation) {
        return new RetryBuilder<>(originalOperation);
    }
    
	public static final class RetryBuilder<T> {
		
		private final Callable<T> task;
		
		private RetryConfig config = RetryDefaultConfig.DEFAULT_CONFIG;
		
		private Callable<T> fallback;

		public RetryBuilder(final Callable<T> task) {
			this.task = task;
			this.fallback = task;
		}
	
		public final RetryBuilder<T> onFail(final Callable<T> fallback) {
	    	this.fallback = fallback;
	        return this;
	    }
		
		public RetryBuilder<T> withConfig(final RetryConfig config){
			this.config = config;
			return this;
		}
		
		public final Retry<T> create(){
			return new Retry<>(config, task, fallback);
		}
	}
}