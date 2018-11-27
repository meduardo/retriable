package my.common.tools.retriable;

import org.junit.Test;

import my.common.tools.retry.Retry;

/**
 * 
 * @author Mario Eduardo Giolo
 *
 */


public class RetryExampleTest {

	@Test
	public void exampleUse() {
		String msg =		
		Retry.with(() -> "Example")
			 .create()
		     .exec();
		
		System.out.println(msg);
		
		msg =		
		Retry.with(() -> processoComProblema())
			 .onFail(() -> "Fallback")
			 .create()
			 .exec();
		
		System.out.println(msg);
		
	}
	
	private String processoComProblema() {
		throw new RuntimeException("Problema ao obter valor");
	}
}
