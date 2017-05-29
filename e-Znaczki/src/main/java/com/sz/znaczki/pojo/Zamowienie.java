package com.sz.znaczki.pojo;
import javax.persistence.*;

@Entity
@Table(name = "Zamowienia")
public class Zamowienie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="zamowienie_id",unique=true, nullable = false)
	private Long id;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	private Klient klient;
	
	private int liczbaKrajowych;
	
	private int liczbaZagranicznych;
	
	private float wartosc;


	public Zamowienie() {
	}
	
	public Klient getKlient() {
		return klient;
	}

	public void setKlient(Klient klient) {
		this.klient = klient;
	}

	public float getWartosc() {
		return wartosc;
	}
	
	public void setWartosc(float wartosc) {
		this.wartosc=wartosc;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLiczbaKrajowych() {
		return liczbaKrajowych;
	}

	public void setLiczbaKrajowych(int liczbaKrajowych) {
		this.liczbaKrajowych = liczbaKrajowych;
	}

	public int getLiczbaZagranicznych() {
		return liczbaZagranicznych;
	}

	public void setLiczbaZagranicznych(int liczbaZagranicznych) {
		this.liczbaZagranicznych = liczbaZagranicznych;
	}

	@Override
	public String toString() {
		return "Zamowienie [id=" + id + ", klient=" + klient + ", liczbaKrajowych=" + liczbaKrajowych
				+ ", liczbaZagranicznych=" + liczbaZagranicznych + ", wartosc=" + wartosc + "]";
	}
	
}
