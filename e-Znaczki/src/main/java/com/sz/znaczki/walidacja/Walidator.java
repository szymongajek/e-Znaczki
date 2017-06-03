package com.sz.znaczki.walidacja;

@FunctionalInterface
public interface Walidator<E> {

	/**
	 * Walidacja obiektu
	 * 
	 * @param doWaliadcji
	 * @return false w przypadku niepoprawnych danych
	 */
	boolean waliduj(E doWaliadcji);
}
