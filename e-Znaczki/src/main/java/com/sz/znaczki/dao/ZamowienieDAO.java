package com.sz.znaczki.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sz.znaczki.pojo.Klient;
import com.sz.znaczki.pojo.Zamowienie;

public interface ZamowienieDAO extends CrudRepository<Zamowienie, Long>{
	
	@Query("select z from Zamowienie z where z.klient in (select k from Klient k "
			+"where k.imie = :imie and k.nazwisko = :nazwisko and k.mail = :mail  )")
	public List<Zamowienie> findByDaneKlienta(@Param("imie") String imie, @Param("nazwisko") String nazwisko, @Param("mail") String mail);
	
	public List<Zamowienie> findByKlient(Klient klient);

}
