package com.sz.znaczki.kalkulatory;

import org.springframework.stereotype.Component;

import com.sz.znaczki.pojo.Zamowienie;

@Component
public class PromocyjnyKalkulatorCeny extends KalkulatorCeny {

	@Override
	public void oblicziIUstawWartosc(Zamowienie z) {
		super.oblicziIUstawWartosc(z);
		if (z.getLiczbaZagranicznych() > 1) {
			z.setWartosc(z.getWartosc() * 0.8f);
		}
	}

}
