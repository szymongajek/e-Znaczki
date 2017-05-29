package com.sz.znaczki.walidacja;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import com.sz.znaczki.TestUtils;
import com.sz.znaczki.walidacja.StackOverflowUserWalidator;


public class WalidacjeStackOverflowUserTest {

	 
		
	@Test
	public void walidacjaSprawdzaProgReputacji() throws IOException { 
		
		RestTemplate template = Mockito.mock(RestTemplate.class);
		
		String fileName33xx = "stackUser_get_3307553.json";
		String soUID33 = "3307553";
		zamokujOdpowiedzDlaUrl(template, fileName33xx, soUID33);
		
		String fileName44xx = "stackUser_get_4117496.json";
		String soUID41 = "4117496";
		
		zamokujOdpowiedzDlaUrl(template, fileName44xx, soUID41);
		
		
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
		
		RestTemplate template = Mockito.mock(RestTemplate.class);
		
		String fileName = "stackUser_get_0000_empty_result.json";
		String soUID = "0000";
		
		zamokujOdpowiedzDlaUrl(template, fileName, soUID);
		
		StackOverflowUserWalidator walidator10 = 
				new StackOverflowUserWalidator(template, 10);
		
		assertThat(walidator10.waliduj("0000")).isFalse();
	}
	
	@Test
	public void walidacjaDlaBlednegoIDNieWywolujeSerwisu() throws IOException { 
		
		RestTemplate template = Mockito.mock(RestTemplate.class);
		
		Mockito.when(template.getForObject(anyString(),any(Class.class),any(Object.class))).thenReturn(new String());
		
		StackOverflowUserWalidator walidator10 = 
				new StackOverflowUserWalidator(template, 10);
		
		assertThat(walidator10.waliduj(null)).isFalse();
		assertThat(walidator10.waliduj("1234aaa")).isFalse();
		assertThat(walidator10.waliduj("")).isFalse();
		assertThat(walidator10.waliduj("!@#$")).isFalse();
		
		verify(template, Mockito.never()).getForObject(anyString(),any(Class.class),any(Object.class));
	}


	private void zamokujOdpowiedzDlaUrl(RestTemplate template, String fileName, String soUID) throws IOException {
		String content = TestUtils.readFileTestResources(fileName);
		Mockito.when(template.getForObject(StackOverflowUserWalidator.STACK_OVERFLOW_USER_URL,
				String.class,soUID)).thenReturn(content);
	}
	

}
