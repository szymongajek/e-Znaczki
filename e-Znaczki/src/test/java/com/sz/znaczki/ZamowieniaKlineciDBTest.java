package com.sz.znaczki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sz.znaczki.dao.KlientDAO;
import com.sz.znaczki.dao.ZamowienieDAO;
import com.sz.znaczki.pojo.Klient;
import com.sz.znaczki.pojo.Zamowienie;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase
public class ZamowieniaKlineciDBTest {

	@Autowired
	ZamowienieDAO zamowienieDAO;

	@Autowired
	KlientDAO klientDAO;

	@PersistenceContext
	private EntityManager entityManager;

	@Before
	public void dodajDoBazy() {
		Klient jan = new Klient("Jan", "Kowalski", "jank@aaa.pl", "9999999");
		Klient maria = new Klient("Maria", "Kowalska", "mk@aaa.pl", "222");

		Zamowienie zam1 = new Zamowienie();
		zam1.setLiczbaKrajowych(2);
		zam1.setLiczbaZagranicznych(1);
		zam1.setKlient(jan);
		zam1.setWartosc(39.5f);
		zamowienieDAO.save(zam1);

		Zamowienie zam2 = new Zamowienie();
		zam2.setLiczbaKrajowych(5);
		zam2.setLiczbaZagranicznych(0);
		zam2.setKlient(jan);
		zam2.setWartosc(1.5f);
		zamowienieDAO.save(zam2);

		Zamowienie zam3 = new Zamowienie();
		zam3.setLiczbaKrajowych(0);
		zam3.setLiczbaZagranicznych(3);
		zam3.setKlient(jan);
		zam3.setWartosc(500.12f);
		zamowienieDAO.save(zam3);

		Zamowienie zam4 = new Zamowienie();
		zam4.setLiczbaKrajowych(5);
		zam4.setLiczbaZagranicznych(15);
		zam4.setKlient(maria);
		zam4.setWartosc(15f);
		zamowienieDAO.save(zam4);

		Klient adam = new Klient("Adam", "Mickiewicz", "am@aaa.pl", "111");

		klientDAO.save(adam);

	}

	@After
	public void wyczyscWszystko() {
		zamowienieDAO.deleteAll();
		klientDAO.deleteAll();
	}

	@Test
	public void znajdujeWszystkie() {

		assertThat(zamowienieDAO.findAll()).hasSize(4);
	}

	@Test
	public void znajdujePoId() {

		Long id = zamowienieDAO.findAll().iterator().next().getId();

		Zamowienie zam = zamowienieDAO.findOne(id);
		assertThat(zam.getId()).isEqualTo(id);

	}

	@Test
	public void zmienionaWartoscZapisujeSieNaBazie() {

		Zamowienie zam = zamowienieDAO.findAll().iterator().next();
		Long id = zam.getId();

		float nowaWart = 999;
		zam.setWartosc(nowaWart);
		zamowienieDAO.save(zam);
		assertThat(zamowienieDAO.findOne(id).getWartosc()).isEqualTo(nowaWart);

	}

	@Test
	public void usuwanieZamowieniaNieUsuwaKlienta() {

		List<Zamowienie> jana = zamowienieDAO.findByDaneKlienta("Jan", "Kowalski", "jank@aaa.pl");
		assertThat(jana).hasSize(3);

		zamowienieDAO.delete(jana.get(2));

		List<Zamowienie> jana2 = zamowienieDAO.findByDaneKlienta("Jan", "Kowalski", "jank@aaa.pl");
		assertThat(jana2).hasSize(2);
		assertThat(jana2.get(0).getKlient().getImie()).isEqualTo("Jan");
	}

	@Test
	public void wysuszkujePoDanychKlienta() {

		List<Zamowienie> jana = zamowienieDAO.findByDaneKlienta("Jan", "Kowalski", "jank@aaa.pl");
		assertThat(jana).hasSize(3);

		List<Zamowienie> marii = zamowienieDAO.findByDaneKlienta("Maria", "Kowalska", "mk@aaa.pl");
		assertThat(marii).hasSize(1);

		List<Zamowienie> puste = zamowienieDAO.findByDaneKlienta("2222", "Kowalski", "jank@aaa.pl");
		assertThat(puste).hasSize(0);

	}

	@Test
	public void wysuszkujePoKliencie() {

		Klient jan = klientDAO.findByImieAndNazwiskoAndMail("Jan", "Kowalski", "jank@aaa.pl");

		List<Zamowienie> jana = zamowienieDAO.findByKlient(jan);
		assertThat(jana).hasSize(3);

	}

	@Test
	public void wczytujeElementyIKlientadlaZamowienia() {

		List<Zamowienie> marii = zamowienieDAO.findByDaneKlienta("Maria", "Kowalska", "mk@aaa.pl");
		assertThat(marii).hasSize(1);

		assertThat(marii.get(0).getLiczbaKrajowych()).isEqualTo(5);
		assertThat(marii.get(0).getLiczbaZagranicznych()).isEqualTo(15);
		assertThat(marii.get(0).getKlient()).isEqualToComparingOnlyGivenFields(
				new Klient("Maria", "Kowalska", "mk@aaa.pl", "222"), "imie", "nazwisko", "mail", "stackOverflowUID");
	}

	@Test
	public void wyszukiwanieKlientowPoDanych() {

		assertThat(klientDAO.findByImieAndNazwiskoAndMail("Jan", "Kowalski", "jank@aaa.pl")).isNotNull();
		assertThat(klientDAO.findByImieAndNazwiskoAndMail("Jan", "Kowalski", "222@aaa.pl")).isNull();

	}

	@Test
	public void usuwanieZamowienPotemKlienta() {
		Klient jan = klientDAO.findByImieAndNazwiskoAndMail("Jan", "Kowalski", "jank@aaa.pl");

		List<Zamowienie> jana = zamowienieDAO.findByKlient(jan);
		zamowienieDAO.delete(jana);

		klientDAO.delete(jan);

		assertThat(klientDAO.findByImieAndNazwiskoAndMail("Jan", "Kowalski", "jank@aaa.pl")).isNull();

		assertThat(zamowienieDAO.findByDaneKlienta("Jan", "Kowalski", "jank@aaa.pl")).isEmpty();
		;
	}

	@Test
	public void nieMoznaDuplikowacKlientow() {

		Klient jan1 = klientDAO.findByImieAndNazwiskoAndMail("Jan", "Kowalski", "jank@aaa.pl");

		// inne nazwisko
		Klient innyJanNazw = klientDAO.save(new Klient("Jan", "xxxx", "jank@aaa.pl", "11"));
		assertThat(jan1.getId()).isNotEqualTo(innyJanNazw.getId());

		// inny mail
		Klient innyJanMail = klientDAO.save(new Klient("Jan", "Kowalski", "xxxx@aaa.pl", "11"));
		assertThat(jan1.getId()).isNotEqualTo(innyJanMail.getId());

		// te same dane
		Klient jan2 = new Klient("Jan", "Kowalski", "jank@aaa.pl", "11");

		assertThatThrownBy(() -> {
			klientDAO.save(jan2);
		}).isInstanceOf(Exception.class);
		entityManager.clear();

	}

}
