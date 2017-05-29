package com.sz.znaczki.walidacja;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.sz.znaczki.TestUtils;
import com.sz.znaczki.walidacja.StackOverflowUserWalidator;


public class WalidacjeStackOverflowUserTest {

	@Test
	public void walidacjaSprawdzaProgReputacji() throws IOException { 
		
		RestTemplate template = Mockito.mock(RestTemplate.class);
		
		String fileName33xx = "stackUser_get_3307553.json";
		String req33xxUrl = "https://api.stackexchange.com/2.2/users/3307553?order=desc&sort=reputation&site=stackoverflow";
		zamokujOdpowiedzDlaUrl(template, fileName33xx, req33xxUrl);
		
		String fileName44xx = "stackUser_get_4117496.json";
		String req41xxUrl = "https://api.stackexchange.com/2.2/users/4117496?order=desc&sort=reputation&site=stackoverflow";
		
		zamokujOdpowiedzDlaUrl(template, fileName44xx, req41xxUrl);
		
		
		StackOverflowUserWalidator walidator10 = 
				new StackOverflowUserWalidator(template, 10);
		
		assertThat(walidator10.waliduj("3307553")).isTrue();
		assertThat(walidator10.waliduj("4117496")).isTrue();
				
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
		String req00xxUrl = "https://api.stackexchange.com/2.2/users/0000?order=desc&sort=reputation&site=stackoverflow";
		
		zamokujOdpowiedzDlaUrl(template, fileName, req00xxUrl);
		
		StackOverflowUserWalidator walidator10 = 
				new StackOverflowUserWalidator(template, 10);
		
		assertThat(walidator10.waliduj("0000")).isFalse();
	}
	
	@Test
	public void walidacjaDlaBlednegoIDNieWywolujeSerwisu() throws IOException { 
		
		RestTemplate template = Mockito.mock(RestTemplate.class);
		
		Mockito.when(template.getForEntity(anyString(),any(Class.class))).thenReturn(Mockito.mock(ResponseEntity.class));
		
		StackOverflowUserWalidator walidator10 = 
				new StackOverflowUserWalidator(template, 10);
		
		assertThat(walidator10.waliduj(null)).isFalse();
		assertThat(walidator10.waliduj("1234aaa")).isFalse();
		assertThat(walidator10.waliduj("")).isFalse();
		assertThat(walidator10.waliduj("!@#$")).isFalse();
		
		verify(template, Mockito.never()).getForEntity(anyString(),any(Class.class));
	}


	private void zamokujOdpowiedzDlaUrl(RestTemplate template, String fileName, String reqUrl) throws IOException {
		String content = TestUtils.readFileTestResources(fileName);
		ResponseEntity<String> response = Mockito.mock(ResponseEntity.class);
		Mockito.when(response.getBody()).thenReturn(content);
		Mockito.when(template.getForEntity(reqUrl,
				String.class)).thenReturn(response);
	}
	

}
