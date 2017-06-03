package com.sz.znaczki.pojo;

import javax.persistence.*;

@Entity
@Table(name = "Klienci", uniqueConstraints = { @UniqueConstraint(columnNames = { "imie", "nazwisko", "mail" }) })
public class Klient {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "klient_id", unique = true, nullable = false)
	private Long id;

	@Column(length = 30)
	private String imie;

	@Column(length = 30)
	private String nazwisko;

	@Column(length = 100)
	private String mail;

	@Column(length = 20)
	private String stackOverflowUID;

	public Klient(String imie, String nazwisko, String mail, String stackOverflowUID) {
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.mail = mail;
		this.stackOverflowUID = stackOverflowUID;
	}

	public Klient() {
	}

	public String getImie() {
		return imie;
	}

	public void setImie(String imie) {
		this.imie = imie;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getStackOverflowUID() {
		return stackOverflowUID;
	}

	public void setStackOverflowUID(String stackOverflowUID) {
		this.stackOverflowUID = stackOverflowUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
