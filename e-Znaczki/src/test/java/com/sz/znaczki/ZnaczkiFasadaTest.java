package com.sz.znaczki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

import com.sz.znaczki.kalkulatory.KalkulatorCeny;
import com.sz.znaczki.pojo.Zamowienie;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase
public class ZnaczkiFasadaTest {

	@Autowired
	ZnaczkiFasada fasada;

	@Test
	public void daneZamowieniaSaWalidowane() {
		assertThatThrownBy(() -> {
			fasada.stworzZamowienie("Jan", "Testowy", "test@pl.pl", "3307553", 1, 1);
		}).isInstanceOf(NiepoprawneDaneException.class);
		assertThatThrownBy(() -> {
			fasada.stworzZamowienie("Jan", "Testowy", "testxxx", "4117496", 1, 1);
		}).isInstanceOf(NiepoprawneDaneException.class);
		assertThatThrownBy(() -> {
			fasada.stworzZamowienie("Jan", "22222", "test@pl.pl", "4117496", 1, 1);
		}).isInstanceOf(NiepoprawneDaneException.class);
		assertThatThrownBy(() -> {
			fasada.stworzZamowienie("222222", "Testowy", "test@pl.pl", "4117496", 1, 1);
		}).isInstanceOf(NiepoprawneDaneException.class);

		Zamowienie zamowienie = fasada.stworzZamowienie("Jan", "Testowy", "test@pl.pl", "4117496", 1, 1);

		assertThat(zamowienie).isNotNull();
	}

	@Test
	public void cenaBezZnizkiJestObliczana() {
		{
			Zamowienie zamowienie = fasada.stworzZamowienie("Jan", "Testowy", "test@pl.pl", "4117496", 0, 0);
			assertThat(zamowienie.getWartosc()).isEqualTo(0f);
		}
		{
			Zamowienie zamowienie = fasada.stworzZamowienie("Jan", "Testowy", "test@pl.pl", "4117496", 10, 1);
			assertThat(zamowienie.getWartosc())
					.isEqualTo(KalkulatorCeny.CENA_KRAJOWEGO * 10 + KalkulatorCeny.CENA_ZAGRANICZNEGO);

		}
		{
			Zamowienie zamowienie = fasada.stworzZamowienie("Jan", "Testowy", "test@pl.pl", "4117496", 15, 0);
			assertThat(zamowienie.getWartosc()).isEqualTo(KalkulatorCeny.CENA_KRAJOWEGO * 15);

		}

	}

	@Test
	public void znizkaZaZnaczkiZagraniczneJestStosowan() {
		{
			Zamowienie zamowienie = fasada.stworzZamowienie("Jan", "Testowy", "test@pl.pl", "4117496", 10, 2);
			assertThat(zamowienie.getWartosc())
					.isEqualTo((KalkulatorCeny.CENA_KRAJOWEGO * 10 + KalkulatorCeny.CENA_ZAGRANICZNEGO * 2) * 0.8f);

		}
	}

}
