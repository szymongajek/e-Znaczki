package com.sz.znaczki;

import java.io.IOException;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sz.znaczki.testUtils.TestUtils;
import com.sz.znaczki.walidacja.StackOverflowUserWalidator;

@Component
public class MockRestTemplateProvider {

	
	@Bean
	@Profile("test")
	public RestTemplate mockRestTemplate() throws IOException {
		
	RestTemplate template = Mockito.mock(RestTemplate.class);
		
	String fileName = "stackUser_get_0000_empty_result.json";
	String soUID = "0000";
	
	zamokujOdpowiedzDlaUrl(template, fileName, soUID);
	
		String fileName33xx = "stackUser_get_3307553.json";
		String soUID33 = "3307553";
		zamokujOdpowiedzDlaUrl(template, fileName33xx, soUID33);
		
		String fileName44xx = "stackUser_get_4117496.json";
		String soUID41 = "4117496";
		
		zamokujOdpowiedzDlaUrl(template, fileName44xx, soUID41);
		
		
		return template;
	}
	
	private void zamokujOdpowiedzDlaUrl(RestTemplate template, String fileName, String soUID) throws IOException {
		String content = TestUtils.readFileTestResources(fileName);
		Mockito.when(template.getForObject(StackOverflowUserWalidator.STACK_OVERFLOW_USER_URL,
				String.class,soUID)).thenReturn(content);
	}

}
