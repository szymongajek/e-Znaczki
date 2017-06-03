package com.sz.znaczki.kalkulatory;

import com.sz.znaczki.pojo.Zamowienie;

public class KalkulatorCeny {

	public static float CENA_KRAJOWEGO = 2.20f;
	public static float CENA_ZAGRANICZNEGO = 5.20f;

	public void oblicziIUstawWartosc(Zamowienie z) {
		z.setWartosc(CENA_KRAJOWEGO * z.getLiczbaKrajowych() + CENA_ZAGRANICZNEGO * z.getLiczbaZagranicznych());
	}

}
