package com.sz.znaczki.walidacja.klienta;

import org.springframework.stereotype.Component;

import com.sz.znaczki.pojo.Klient;
import com.sz.znaczki.walidacja.Walidator;

@Component
public class WalidatorKlientaMaila implements WalidatorKlienta {

	Walidator<String> mailWalidator = str -> str.length() < 100 && str.length() > 0 && str.contains("@");

	@Override
	public String waliduj(Klient doWaliadcji) {
		if (!mailWalidator.waliduj(doWaliadcji.getMail())) {
			return "Niepropawny mail";
		}
		return null;
	}
}
