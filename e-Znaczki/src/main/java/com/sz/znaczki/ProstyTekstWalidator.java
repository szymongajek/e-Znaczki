package com.sz.znaczki;

public class ProstyTekstWalidator implements Walidator {

	private int min_dlugosc;
	private int max_dlugosc;
	
	public ProstyTekstWalidator(int min_dlugosc, int max_dlugosc) {
		this.min_dlugosc = min_dlugosc;
		this.max_dlugosc = max_dlugosc;
	}

	@Override
	public boolean waliduj(String str) {
		// TODO Auto-generated method stub
		return false;
	}

}
