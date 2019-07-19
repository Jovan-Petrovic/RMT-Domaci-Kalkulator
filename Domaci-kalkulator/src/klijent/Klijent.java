package klijent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Klijent implements Runnable {

	static Socket soketZaKomunikaciju = null;
	static BufferedReader ulazniTokOdServera = null;
	static PrintStream izlazniTokKaServeru = null;
	static BufferedReader unosSaTastature = null;

	public static void main(String[] args) {
		try {
			soketZaKomunikaciju = new Socket("localhost", 8000);
			ulazniTokOdServera = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));
			izlazniTokKaServeru = new PrintStream(soketZaKomunikaciju.getOutputStream(), true);
			unosSaTastature = new BufferedReader(new InputStreamReader(System.in));

			new Thread(new Klijent()).start();

			String rezultat;
			while (true) {

				rezultat = ulazniTokOdServera.readLine();
				System.out.println(rezultat);

				if (rezultat.startsWith(">>>")) {
					break;
				}
			}

			soketZaKomunikaciju.close();

		} catch (IOException e) {
			System.out.println("Server je pao!");
		}

	}

	@Override
	public void run() {
		try {
			String unos;

			while (true) {

				unos = unosSaTastature.readLine();
				izlazniTokKaServeru.println(unos);

				if (unos.startsWith("***quit")) {
					break;
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

