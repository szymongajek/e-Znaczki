package com.sz.znaczki.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sz.znaczki.NiepoprawneDaneException;
import com.sz.znaczki.ZnaczkiFasada;
import com.sz.znaczki.pojo.Zamowienie;


@RestController
@RequestMapping(value = "/zamowienia")
public class ZamowieniaController {

	private static Logger LOGGER = LogManager.getLogger(ZamowieniaController.class);
	
	@Autowired
	ZnaczkiFasada znaczkiFasada;
	
	@RequestMapping(value = "/nowe", method=RequestMethod.POST,
			params = { "imie", "nazwisko","mail","stackOverflowUID", "krajowe","zagraniczne"})
	public   ResponseEntity<?> nowe(@RequestParam("imie") String imie,
			@RequestParam("nazwisko") String nazwisko, @RequestParam("mail") String mail, @RequestParam("stackOverflowUID") String stackOverflowUID,
			@RequestParam("krajowe") int krajowe,@RequestParam("zagraniczne") int zagraniczne )  {
		LOGGER.info("nowe zamowienie start: "+
			"imie="+imie+ " nazwisko="+nazwisko+ " mail="+mail+ " stackOverflowUID="+stackOverflowUID+ " krajowe="+krajowe+ " zagraniczne="+zagraniczne);
	  try{
		  Zamowienie nowe = znaczkiFasada.stworzZamowienie(imie, nazwisko, mail, stackOverflowUID, krajowe, zagraniczne);
		  
		 return  new ResponseEntity<Zamowienie>(nowe, HttpStatus.CREATED); 
	  }catch(NiepoprawneDaneException ex){
		  return  new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST); 
	  }
	}
	
	@RequestMapping(value = "/wszystkie", method=RequestMethod.GET)
	public List<Zamowienie> wszystkie()  {
		LOGGER.info("wszystkie start");
	 
		return znaczkiFasada.wszystkie();

	}
	
	@RequestMapping(value = "/wszystkieKienta", method=RequestMethod.GET,
			params = { "imie", "nazwisko","mail" })
	public List<Zamowienie> wszystkieKienta(@RequestParam("imie") String imie,
			@RequestParam("nazwisko") String nazwisko, @RequestParam("mail") String mail)  {
		LOGGER.info("wszystkieKienta start");
	 
		return znaczkiFasada.wszystkieKienta(imie, nazwisko, mail);

	}
	
}




 