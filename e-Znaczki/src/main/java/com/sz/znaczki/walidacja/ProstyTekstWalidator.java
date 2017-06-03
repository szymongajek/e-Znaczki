package com.sz.znaczki.walidacja;

public class ProstyTekstWalidator implements Walidator<String> {

	private int min_dlugosc;
	private int max_dlugosc;

	public ProstyTekstWalidator(int min_dlugosc, int max_dlugosc) {
		this.min_dlugosc = min_dlugosc;
		this.max_dlugosc = max_dlugosc;
	}

	@Override
	public boolean waliduj(String str) {
		return str != null && str.length() <= max_dlugosc && str.length() >= min_dlugosc && str.matches("[a-zA-Z]*");
	}

}
