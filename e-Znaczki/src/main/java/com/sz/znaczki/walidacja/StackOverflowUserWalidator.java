package com.sz.znaczki.walidacja;

import java.io.IOException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class StackOverflowUserWalidator implements Walidator {

	static final Logger LOGGER = LogManager.getLogger(StackOverflowUserWalidator.class);
	public static final String STACK_OVERFLOW_USER_URL = 
			"https://api.stackexchange.com/2.2/users/{id}?order=desc&sort=reputation&site=stackoverflow";
	
	private static Walidator porpawneId = str-> str.length()>0 && str.matches("(\\d)*");
	
	private int min_reputacja;
	RestTemplate template;
	
	public StackOverflowUserWalidator(RestTemplate template, int min_reputacja) {
		this.min_reputacja = min_reputacja;
		this.template=template;
	}

	@Override
	public boolean waliduj(String soUserId) {
		
		if (soUserId==null || !StackOverflowUserWalidator.porpawneId.waliduj(soUserId))
			return false;
		String odpowiedz =  template.getForObject(STACK_OVERFLOW_USER_URL, String.class, soUserId); 
		ObjectMapper jsonMapper = new ObjectMapper();
		int reputacja;
		
		try {
			JsonNode root = jsonMapper.readTree(odpowiedz);
			Iterator<JsonNode> elementsIterator = root.get("items").elements();
			if(!elementsIterator.hasNext()){
				// nie znaleziono uzytkownika
				return false;
			}
			JsonNode firstUserObject = elementsIterator.next();
			reputacja= firstUserObject.get("reputation").asInt();
			
		} catch (IOException e) {
			LOGGER.error("blad parsowania odpowiedzi stack overflow");
			return false;
		}
		
		return reputacja>=min_reputacja;
	}

}
