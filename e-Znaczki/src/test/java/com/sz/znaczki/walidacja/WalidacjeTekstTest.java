package com.sz.znaczki.walidacja;

import java.util.Arrays;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

import com.sz.znaczki.walidacja.ProstyTekstWalidator;

public class WalidacjeTekstTest {

	@Test
	public void dlugoscTekstu() { 
		ProstyTekstWalidator walidator = new ProstyTekstWalidator(3, 7);
		
		assertThat(walidator.waliduj("aaa")).isTrue();
		assertThat(walidator.waliduj("aaaaa")).isTrue();
		assertThat(walidator.waliduj("aaaaaaa")).isTrue();
		
		assertThat(walidator.waliduj(null)).isFalse();
		assertThat(walidator.waliduj("")).isFalse();
		assertThat(walidator.waliduj("aa")).isFalse();
		assertThat(walidator.waliduj("aaaaaaaa")).isFalse();
		
	}
	
	@Test
	public void brakNiedozwolonychZnakow() {
		
		ProstyTekstWalidator walidator = new ProstyTekstWalidator(0, 20);
		
//		Walidator w = str-> str.length()<=20 && str.length()>=0 && str.matches("");
		
		assertThat( Arrays.asList("szymon", "szymooooooooon", "SZYMON").stream().allMatch( walidator::waliduj) ).isTrue();
		assertThat( Arrays.asList("sz ymon", "sz123", "szy-mon").stream().noneMatch( walidator::waliduj) ).isTrue();
		
		
	}
	

}
