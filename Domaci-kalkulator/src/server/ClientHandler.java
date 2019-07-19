package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler extends Thread {

	Socket soketZaKomunikaciju = null;
	BufferedReader ulazniTokOdKlijenta = null;
	PrintStream izlazniTokKaKlijentu = null;
	String korisnickoIme;
	Kalkulator kalkulator;
	boolean gost;
	int brojac;
	boolean ispravnost;
	boolean losUnos;

	public ClientHandler(Socket soketZaKomunikaciju) {
		this.soketZaKomunikaciju = soketZaKomunikaciju;
		this.kalkulator = new Kalkulator(this);
		this.gost = false;
		this.brojac = 1;
		this.ispravnost = true;
		this.losUnos = false;
	}

	public void setLosUnos(boolean b) {
		losUnos = b;
	}

	public void setIspravnost(boolean i) {
		ispravnost = i;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public String citajKorisnickoIme() {
		String ime = "";

		return ime;
	}

	public void unosKorisnickogImena() {
		boolean validnost = false;
		boolean provera = true;

		do {
			provera = true;
			izlazniTokKaKlijentu.println("Unesite korisnicko ime: ");
			try {
				korisnickoIme = ulazniTokOdKlijenta.readLine();
			} catch (IOException e) {
				Server.korisnici.remove(this);
				// soketZaKomunikaciju.close();
			}

			if (korisnickoIme.contains(" ")) {
				izlazniTokKaKlijentu.println("Korisnicko ime ne sme da sadrzi prazno mesto! Pokusajte ponovo.");
			} else if (Objects.equals(korisnickoIme, "")) {
				izlazniTokKaKlijentu.println("Korisnicko ime ne sme da bude prazno! Pokusajte ponovo.");
			} else {
				for (Podaci podatak : Server.podaciKorisnika) {
					if (Objects.equals(korisnickoIme, podatak.getKorisnickoIme())) {
						izlazniTokKaKlijentu.println("Korisnicko ime vec postoji! Pokusajte ponovo.");
						provera = false;
						break;
					}
				}
				if (provera) {
					validnost = true;
				}
			}
		} while (!validnost);
	}

	@Override
	public void run() {
		try {
			ulazniTokOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));
			izlazniTokKaKlijentu = new PrintStream(soketZaKomunikaciju.getOutputStream(), true);

			izlazniTokKaKlijentu.println("Uspostavljena je konekcija sa serverom.");

			izlazniTokKaKlijentu.println("Izaberite jednu od mogucnosti:\n" + "1.Registracija.\n"
					+ "2.Prijavljivanje.\n" + "3.Koriscenje kalkulatora kao gost.\n");
			int opcija = 0;

			boolean radi = true;
			do {
				boolean broj = false;
				do {
					String ulaz = ulazniTokOdKlijenta.readLine();
					if (ulaz.length() == 1 && Character.isDigit(ulaz.charAt(0))) {
						opcija = Integer.parseInt(ulaz);
						broj = true;
					} else {
						izlazniTokKaKlijentu.println("Nekorektan unos. Pokusajte ponovo sa 1, 2 ili 3. Hvala!");
					}
				} while (!broj);
				switch (opcija) {
				case 1:
					unosKorisnickogImena();
					String lozinka = unosPassword();
					Podaci p = new Podaci(korisnickoIme, lozinka);
					Server.podaciKorisnika.add(p);
					PrintWriter fajl = new PrintWriter(new BufferedWriter(new FileWriter("Server.txt")));
					for (Podaci podatak : Server.podaciKorisnika) {
						fajl.println(podatak.getKorisnickoIme() + " " + podatak.getPassword());
					}
					fajl.close();
					PrintWriter f = new PrintWriter(new BufferedWriter(new FileWriter("Izvestaj_" + korisnickoIme + ".txt")));
					f.close();
					radi = false;
					izlazniTokKaKlijentu.println("Dobrodosli " + korisnickoIme + ",\nZa izlazak unesite ***quit.");
					break;
				case 2:
					boolean vrti = true;
					do {
						boolean postoji = proveraKorisnickogImena();
						if (!postoji) {
							izlazniTokKaKlijentu.println("Korisnicko ime koje ste uneli ne postoji! Pokusajte ponovo.");
						} else
							vrti = false;
					} while (vrti);
					vrti = true;
					do {
						boolean proveraPassword = proveraPasword();
						if (proveraPassword) {
							vrti = false;
						} else {
							izlazniTokKaKlijentu.println("Password nije odgovarajuci! Pokusajte ponovo.");
						}
					} while (vrti);
					radi = false;
					izlazniTokKaKlijentu.println("Dobrodosli " + korisnickoIme + ",\nZa izlazak unesite ***quit.");
					break;
				case 3:
					gost = true;
					radi = false;
					break;
				default:
					izlazniTokKaKlijentu.println("Nekorektan unos. Pokusajte ponovo sa 1, 2 ili 3. Hvala!");
				}
			} while (radi);

			String unos;
			if (!gost) {
				boolean izv = true;
				izlazniTokKaKlijentu.println("Zelite li izvestaj prethodnih kalkulacija? Ukucajte da ili ne.");
				String izvestaj = ulazniTokOdKlijenta.readLine();
				do {
					switch (izvestaj) {
					case "da":
					case "Da":
					case "DA":
						/*for (Podaci podatak : Server.podaciKorisnika) {
							if (Objects.equals(korisnickoIme, podatak.getKorisnickoIme())) {
								String naziv = "Izvestaj_" + korisnickoIme + ".txt";
								try {
									PrintWriter fajl = new PrintWriter(new BufferedWriter(new FileWriter(naziv)));
									fajl.println(podatak.ispis());
									fajl.close();
								} catch (Exception e) {
									System.out.println("Greska: " + e.getMessage());
								}
								// izlazniTokKaKlijentu.println(podatak.ispis());
							}
						}*/
						
						izlazniTokKaKlijentu.println("Vas izvestaj se nalazi u datoteci: Izvestaj_" + korisnickoIme);
						izv = false;
						break;
					case "ne":
					case "Ne":
					case "NE":
						izv = false;
						break;
					default:
						izlazniTokKaKlijentu.println("Pogresan unos! Unesite da ili ne.");
						izvestaj = ulazniTokOdKlijenta.readLine();
					}
				} while (izv);
			}
			if (gost) {
				izlazniTokKaKlijentu.println(
						"Vi ste gost na sajtu, imate mogucnost za najvise tri kalkulacije! Za izlazak unesite ***quit.");
			}
			izlazniTokKaKlijentu.println("Unesite zahtev u obliku: a operacija b. " + "Opreacije su: +, -, *, /\nNapomena: a moze biti negativan broj.");
			while (true) {

				if (gost && brojac == 4)
					break;
				if (gost)
					brojac++;
				unos = ulazniTokOdKlijenta.readLine();

				if (unos.startsWith("***quit")) {
					break;
				}

				int rezultat = kalkulator.izracunaj(unos);

				for (ClientHandler klijent : Server.korisnici) {
					if (klijent == this) {
						if (ispravnost && !losUnos)
							klijent.izlazniTokKaKlijentu.println("Rezultat je: " + rezultat);
						else if (!ispravnost) {
							klijent.izlazniTokKaKlijentu.println("Deljenje sa nulom nije dozvoljeno!");
						} else {
							klijent.izlazniTokKaKlijentu.println(
									"Nekorektan unos. Unesite zahtev u obliku: a operacija b :\\n\" + \"Opreacije su: +, -, *, /\nNapomena: a moze biti negativan broj.");
						}
						break;
					}
				}

				if (ispravnost && !losUnos && !gost) {
					unos += " = ";
					unos += Integer.toString(rezultat);
					Podaci p = null;
					for (Podaci podatak : Server.podaciKorisnika) {
						if (Objects.equals(korisnickoIme, podatak.getKorisnickoIme())) {
							podatak.putKalkulacije(unos);
							p = podatak;
							break;
						}
					}
					String naziv  = "Izvestaj_" + korisnickoIme + ".txt";
					PrintWriter fajl = new PrintWriter(new BufferedWriter(new FileWriter(naziv)));
					fajl.println(p.ispis());
					fajl.close();
				}
			}
			if (gost && brojac == 4)
				izlazniTokKaKlijentu
						.println(">>> Za duze koriscenje kalkulatora molimo Vas da se registrujete/prijavite.");
			else if (gost && brojac < 4)
				izlazniTokKaKlijentu.println(">>> Dovidjenja.");
			else
				izlazniTokKaKlijentu.println(">>> Dovidjenja " + korisnickoIme);

			Server.korisnici.remove(this);
			soketZaKomunikaciju.close();

		} catch (IOException e) {
			Server.korisnici.remove(this);
			// soketZaKomunikaciju.close();
		}

	}

	private boolean proveraPasword() {
		String lozinka = "";
		izlazniTokKaKlijentu.println("Unesite lozinku.");
		try {
			lozinka = ulazniTokOdKlijenta.readLine();
		} catch (IOException e) {
			// e.printStackTrace();
			return false;
		}
		for (Podaci klijent : Server.podaciKorisnika) {
			if (Objects.equals(klijent.getPassword(), lozinka)) {
				return true;
			}
		}
		return false;
	}

	private boolean proveraKorisnickogImena() {
		String ime = "";
		izlazniTokKaKlijentu.println("Unesite korisnicko ime:");
		try {
			ime = ulazniTokOdKlijenta.readLine();
		} catch (IOException e) {
			// e.printStackTrace();
			return false;
		}
		for (Podaci klijent : Server.podaciKorisnika) {
			if (Objects.equals(klijent.getKorisnickoIme(), ime)) {
				korisnickoIme = ime;
				return true;
			}
		}
		return false;
	}

	private String unosPassword() {
		String lozinka = "";
		izlazniTokKaKlijentu.println("Unesite password.");
		boolean vrti = true;
		boolean broj = false, velikoSlovo = false;
		do {
			try {
				lozinka = ulazniTokOdKlijenta.readLine();
			} catch (IOException e) {
				// e.printStackTrace();
				break;
			}
			broj = false;
			velikoSlovo = false;
				if (lozinka.length() < 8)
					izlazniTokKaKlijentu
							.println("Password mora imati minimum 8 karaktera! Pokusajte ponovo unos password-a.");
				else {

					for (char c : lozinka.toCharArray()) {
						if (Character.isDigit(c)) {
							broj = true;
						}
						if (Character.isUpperCase(c)) {
							velikoSlovo = true;
						}
					}

					if (broj && velikoSlovo)
						vrti = false;
					else if (!broj && !velikoSlovo) {
						izlazniTokKaKlijentu.println(
								"Password mora da sadrzi bar jedno veliko slovo i bar jednu cifru! Pokusajte ponovo.");
					} else if (!broj) {
						izlazniTokKaKlijentu.println("Password mora da sadrzi bar jednu cifru! Pokusajte ponovo.");
					} else {
						izlazniTokKaKlijentu
								.println("Password mora da sadrzi bar jedno veliko slovo! Pokusajte ponovo.");

					}
				}
		} while (vrti);
		return lozinka;
	}
}
