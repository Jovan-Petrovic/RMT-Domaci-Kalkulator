package server;

public class Podaci {
	private String korisnickoIme, password;
	private String kalkulacije;
	private boolean prvi;

	public String getKalkulacije() {
		return kalkulacije;
	}

	public void putKalkulacije(String s) {
			kalkulacije += s + " ";
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Podaci(String korisnickoIme, String password) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.password = password;
		this.kalkulacije = "";
		this.prvi = true;
	}

	public String ispis() {
		return  kalkulacije;
	}

	public String ispisServer() {
		return kalkulacije;
	}

	public Podaci(String korisnickoIme, String password, String kalkulacije) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.password = password;
		this.kalkulacije = kalkulacije;
		if (kalkulacije == "")
			prvi = true;
		else
			prvi = false;
	}

}

