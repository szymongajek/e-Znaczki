package com.sz.znaczki.walidacja.klienta;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sz.znaczki.pojo.Klient;
import com.sz.znaczki.walidacja.StackOverflowUserWalidator;

@Component
public class WalidatorKlientaSOID implements WalidatorKlienta {

	StackOverflowUserWalidator soWalidator;

	public WalidatorKlientaSOID(RestTemplate restTemplate) {
		this.soWalidator = new StackOverflowUserWalidator(restTemplate, 200);

	}

	@Override
	public String waliduj(Klient doWaliadcji) {
		if (!soWalidator.waliduj(doWaliadcji.getStackOverflowUID())) {
			return "Błąd walidacji StackOVerflowID";
		}
		return null;

	}

}
