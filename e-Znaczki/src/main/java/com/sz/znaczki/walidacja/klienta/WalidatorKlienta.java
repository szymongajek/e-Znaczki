package com.sz.znaczki.walidacja.klienta;

import com.sz.znaczki.pojo.Klient;

public interface WalidatorKlienta {

	/**
	 * Waliduje dane klienta
	 * 
	 * @param Klient
	 * @return null w przypadku poprawnych danych, komunikat bledu w przypadku
	 *         niepoprawnych
	 */
	String waliduj(Klient Klient);
}
