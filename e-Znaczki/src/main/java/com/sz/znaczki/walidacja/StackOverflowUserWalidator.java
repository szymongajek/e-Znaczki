package com.sz.znaczki.walidacja;

import org.springframework.web.client.RestTemplate;

public class StackOverflowUserWalidator implements Walidator {

	private int min_reputacja;
	RestTemplate template;
	
	public StackOverflowUserWalidator(RestTemplate template, int min_reputacja) {
		this.min_reputacja = min_reputacja;
		this.template=template;
	}

	@Override
	public boolean waliduj(String str) {
		// TODO Auto-generated method stub
		return false;
	}

}
