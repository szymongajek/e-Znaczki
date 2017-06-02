package com.sz.znaczki;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sz.znaczki.dao.KlientDAO;
import com.sz.znaczki.dao.ZamowienieDAO;
import com.sz.znaczki.pojo.Klient;
import com.sz.znaczki.pojo.Zamowienie;
import com.sz.znaczki.walidacja.StackOverflowUserWalidator;
import com.sz.znaczki.walidacja.Walidator;

@Component 
public class ZnaczkiFasada {
	
	@Autowired
	KlientDAO klientDAO;
	
	@Autowired
	ZamowienieDAO zamowienieDAO;
	
	@Autowired
	RestTemplate restTemplate;
	
	
	
	public Zamowienie stworzZamowienie(String imie, String nazwisko, String mail, String stackOverflowUID, int liczbaKrajowych, int liczbaZagranicznych)
			throws NiepoprawneDaneException{
		
		Klient klient = klientDAO.findByImieAndNazwiskoAndMail(imie, nazwisko, mail);
		if (klient==null){
			
			Walidator imieNazwiskoWalidator = str-> str.length()<30 && str.length()>0 && str.matches("[a-zA-Z]*");
			
			Walidator mailWalidator = str-> str.length()<100 && str.length()>0 && str.contains("@");
			
			
			StackOverflowUserWalidator soWalidator = new StackOverflowUserWalidator(this.restTemplate, 200);
			
			if (!imieNazwiskoWalidator.waliduj(imie)){
				throw new NiepoprawneDaneException("Niepropawne imie");
			}
			if (!imieNazwiskoWalidator.waliduj(nazwisko)){
				throw new NiepoprawneDaneException("Niepropawne nazwisko");
			}
			
			if (!mailWalidator.waliduj(mail)){
				throw new NiepoprawneDaneException("Niepropawny mail");
			}
			if (!soWalidator.waliduj(stackOverflowUID)){
				throw new NiepoprawneDaneException("Blad walidacji stackOverflowUID");
			}
			
			klient = klientDAO.save(new Klient(imie, nazwisko, mail, stackOverflowUID));
		}
	
		Zamowienie nowe = new Zamowienie();
		nowe.setLiczbaKrajowych(liczbaKrajowych);
		nowe.setLiczbaZagranicznych(liczbaZagranicznych);
		nowe.setKlient(klient);
		nowe.setWartosc(0f);
		
		//zastosuj rabat
		nowe.setWartosc(0f);
		
		// KalkulatorCeny kalk = new KalkulatorCeny();
		// kalk.oblicz(zam1);
		//
		// AdHocRuleFactory
		//
		// zam.setemeents
		// kalk.obliczcene(zam)
		
		nowe = zamowienieDAO.save(nowe);
		
		return nowe;
	}

	public List<Zamowienie> wszystkie(){
		return zamowienieDAO.findAll();
	}
	
	public List<Zamowienie> wszystkieKienta(String imie, String nazwisko, String mail){
		
		return zamowienieDAO.findByDaneKlienta(imie, nazwisko, mail);
	}
	

}
