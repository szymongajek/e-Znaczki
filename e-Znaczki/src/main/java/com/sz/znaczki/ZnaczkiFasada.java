package com.sz.znaczki;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sz.znaczki.dao.KlientDAO;
import com.sz.znaczki.dao.ZamowienieDAO;
import com.sz.znaczki.kalkulatory.KalkulatorCeny;
import com.sz.znaczki.pojo.Klient;
import com.sz.znaczki.pojo.Zamowienie;
import com.sz.znaczki.walidacja.klienta.WalidatorKlienta;

@Component
public class ZnaczkiFasada {

	@Autowired
	KlientDAO klientDAO;

	@Autowired
	ZamowienieDAO zamowienieDAO;

	@Autowired
	List<WalidatorKlienta> walidatory;

	@Autowired
	KalkulatorCeny kalkulatorCeny;

	/**
	 * Dla podanych danych wyszukuje klienta w bazie. Jesli klient nie istnieje,
	 * jego dane sa walidowane i jest dodawany do bazy.<br/>
	 * Tworzy nowy obiekt zamowienia, oblicza cene i zapisuje do bazy.
	 * 
	 * @return w przypadku sukcesu zwraca nowe zamowienie
	 * @throws NiepoprawneDaneException
	 *             w przypadku bledu/bledow walidacji zwraca wyjatek zawierajacy
	 *             opis pierwszego znalezionego bledu
	 */
	public Zamowienie stworzZamowienie(String imie, String nazwisko, String mail, String stackOverflowUID,
			int liczbaKrajowych, int liczbaZagranicznych) throws NiepoprawneDaneException {

		Klient klient = klientDAO.findByImieAndNazwiskoAndMail(imie, nazwisko, mail);
		if (klient == null) {

			Klient nowyKlient = new Klient(imie, nazwisko, mail, stackOverflowUID);
			for (WalidatorKlienta walidator : walidatory) {
				String bladWalidacji = walidator.waliduj(nowyKlient);
				if (bladWalidacji != null) {
					throw new NiepoprawneDaneException(bladWalidacji);
				}
			}

			klient = klientDAO.save(nowyKlient);
		}

		Zamowienie nowe = new Zamowienie();
		nowe.setLiczbaKrajowych(liczbaKrajowych);
		nowe.setLiczbaZagranicznych(liczbaZagranicznych);
		nowe.setKlient(klient);

		kalkulatorCeny.oblicziIUstawWartosc(nowe);

		nowe = zamowienieDAO.save(nowe);

		return nowe;
	}

	public List<Zamowienie> wszystkieZamowienia() {
		return zamowienieDAO.findAll();
	}

	public List<Zamowienie> wszystkieZamowieniaKienta(String imie, String nazwisko, String mail) {

		return zamowienieDAO.findByDaneKlienta(imie, nazwisko, mail);
	}

}
