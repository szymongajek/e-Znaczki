package com.sz.znaczki;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.sz.znaczki.pojo.Klient;
import com.sz.znaczki.pojo.Zamowienie;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase
public class EndToEndTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void skladanieIPobieranieZamowienEndToEnd() {

		ResponseEntity<Zamowienie> response;
		{// dodaj nowe zamowienie
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("imie", "Andrzej");
			map.add("nazwisko", "Testowy");
			map.add("mail", "jan.testowy@test.pl");
			map.add("stackOverflowUID", "4117496");
			map.add("krajowe", "2");
			map.add("zagraniczne", "12");

			HttpEntity<?> requestEntity = new HttpEntity<Object>(map, new HttpHeaders());

			response = this.restTemplate.postForEntity("/zamowienia/nowe", requestEntity, Zamowienie.class);
		}
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		Zamowienie noweZamowienie = response.getBody();

		assertThat(noweZamowienie.getId()).isNotNull();
		assertThat(noweZamowienie.getLiczbaKrajowych()).isEqualTo(2);
		assertThat(noweZamowienie.getLiczbaZagranicznych()).isEqualTo(12);

		Klient nowyKlient = noweZamowienie.getKlient();
		assertThat(nowyKlient.getImie()).isEqualTo("Andrzej");
		assertThat(nowyKlient.getNazwisko()).isEqualTo("Testowy");
		assertThat(nowyKlient.getStackOverflowUID()).isEqualTo("4117496");

		{// pobierz wszystkie zamowienia
			ResponseEntity<List<Zamowienie>> wszystkieResponse = this.restTemplate.exchange("/zamowienia/wszystkie",
					HttpMethod.GET, new HttpEntity<Object>(new HttpHeaders()),
					new ParameterizedTypeReference<List<Zamowienie>>() {
					});
			assertThat(wszystkieResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

			List<Zamowienie> wszystkieLista = wszystkieResponse.getBody();
			assertThat(wszystkieLista.stream().filter(el -> el.getId().equals(noweZamowienie.getId())).count())
					.isEqualTo(1);
		}

		{// pobierz zamowienia klienta
			UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/zamowienia/wszystkieKienta")
					.queryParam("imie", nowyKlient.getImie()).queryParam("nazwisko", nowyKlient.getNazwisko())
					.queryParam("mail", nowyKlient.getMail());

			ResponseEntity<List<Zamowienie>> wszystkieKlientaResponse = this.restTemplate.exchange(
					builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()),
					new ParameterizedTypeReference<List<Zamowienie>>() {
					});

			assertThat(wszystkieKlientaResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

			List<Zamowienie> wszystkieKlientaLista = wszystkieKlientaResponse.getBody();
			assertThat(wszystkieKlientaLista).hasSize(1);
		}

	}

	@Test
	public void niepoprawneZamowienieEndToEnd() {

		ResponseEntity<String> response;
		{// dodaj nowe zamowienie
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("imie", "####");
			map.add("nazwisko", "222");
			map.add("mail", "jan.testowy@test.pl");
			map.add("stackOverflowUID", "4117496");
			map.add("krajowe", "2");
			map.add("zagraniczne", "12");

			HttpEntity<?> requestEntity = new HttpEntity<Object>(map, new HttpHeaders());

			response = this.restTemplate.postForEntity("/zamowienia/nowe", requestEntity, String.class);
		}
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

}