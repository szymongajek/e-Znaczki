package com.sz.znaczki.dao;

import org.springframework.data.repository.CrudRepository;
import com.sz.znaczki.pojo.Klient;

public interface KlientDAO extends CrudRepository<Klient, Long>{
	
	public Klient findByImieAndNazwiskoAndMail(String imie,String nazwisko, String mail);
	
}
