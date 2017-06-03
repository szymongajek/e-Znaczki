package com.sz.znaczki.walidacja.klienta;

import org.springframework.stereotype.Component;

import com.sz.znaczki.pojo.Klient;
import com.sz.znaczki.walidacja.ProstyTekstWalidator;

@Component
public class WalidatorKlientaTekstu implements WalidatorKlienta {

	private static ProstyTekstWalidator imieNazwiskoWalidator = new ProstyTekstWalidator(3, 20);

	@Override
	public String waliduj(Klient doWaliadcji) {
		if (!imieNazwiskoWalidator.waliduj(doWaliadcji.getImie())) {
			return "Niepropawne imie";
		} else if (!imieNazwiskoWalidator.waliduj(doWaliadcji.getNazwisko())) {
			return "Niepropawne nazwisko";
		}
		return null;
	}
}
