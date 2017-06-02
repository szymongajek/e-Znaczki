package com.sz.znaczki.walidacja;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import static org.assertj.core.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import com.sz.znaczki.MockRestTemplateProvider;
import com.sz.znaczki.walidacja.StackOverflowUserWalidator;


public class WalidacjeStackOverflowUserTest {

	static RestTemplate template;
	
	
	@BeforeClass
	public static void initMock() throws IOException{
		template = (new MockRestTemplateProvider()).mockRestTemplate();
	}
	
	@Test
	public void walidacjaSprawdzaProgReputacji() throws IOException { 
		
		
		StackOverflowUserWalidator walidator10 = 
				new StackOverflowUserWalidator(template, 10);
		
		assertThat(walidator10.waliduj("3307553")).isTrue();
		assertThat(walidator10.waliduj("4117496")).isTrue();
				
		StackOverflowUserWalidator walidator25 = 
				new StackOverflowUserWalidator(template, 25);
		
		assertThat(walidator25.waliduj("3307553")).isTrue();
		assertThat(walidator25.waliduj("4117496")).isTrue();
		
		StackOverflowUserWalidator walidator200 = 
				new StackOverflowUserWalidator(template, 200);
		
		assertThat(walidator200.waliduj("3307553")).isFalse();
		assertThat(walidator200.waliduj("4117496")).isTrue();
		
		StackOverflowUserWalidator walidator1000 = 
				new StackOverflowUserWalidator(template, 1000);
		
		assertThat(walidator1000.waliduj("3307553")).isFalse();
		assertThat(walidator1000.waliduj("4117496")).isFalse();
		
			
	}

	 
	@Test
	public void walidacjaZwracaFalseDlaNieistniejacegoUzytkownika() throws IOException { 
		
		StackOverflowUserWalidator walidator10 = 
				new StackOverflowUserWalidator(template, 10);
		
		assertThat(walidator10.waliduj("0000")).isFalse();
	}
	
	@Test
	public void walidacjaDlaBlednegoIDNieWywolujeSerwisu() throws IOException { 
		
		StackOverflowUserWalidator walidator10 = 
				new StackOverflowUserWalidator(template, 10);
		
		assertThat(walidator10.waliduj(null)).isFalse();
		assertThat(walidator10.waliduj("1234aaa")).isFalse();
		assertThat(walidator10.waliduj("")).isFalse();
		assertThat(walidator10.waliduj("!@#$")).isFalse();
		
		verify(template, Mockito.never()).getForObject(anyString(),any(Class.class),any(Object.class));
	}


	

}
