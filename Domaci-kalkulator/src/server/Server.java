package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
	public static LinkedList<ClientHandler> korisnici = new LinkedList<>();
	public static LinkedList<Podaci> podaciKorisnika = new LinkedList<>();

	public static void main(String[] args) {
		int port = 8000;
		ServerSocket serverSoket = null;
		Socket soketZaKomunikaciju = null;

		try {
			try {
				BufferedReader citaj = new BufferedReader(new FileReader("Server.txt"));
				String unos = citaj.readLine();
				int i = 0;
				while (unos != null) {
					//char b;
					i = 0;
					int index = unos.indexOf(" ");
					String ime = "";
					while (i < index) {
						ime += unos.charAt(i);
						i++;
					}
					unos = unos.substring(index + 1);
					String loz = unos;
					BufferedReader c = new BufferedReader(new FileReader("Izvestaj_" + ime + ".txt"));
					String kalk = c.readLine();
					c.close();
					podaciKorisnika.add(new Podaci(ime, loz, kalk));
					unos = citaj.readLine();
				}
				citaj.close();
			} catch (Exception e) {
				System.out.println("Greska: " + e.getMessage());
			} 
			serverSoket = new ServerSocket(port);

			while (true) {
				System.out.println("Cekanje konekcije.");
				soketZaKomunikaciju = serverSoket.accept();
				System.out.println("Doslo je do konekcije.");

				ClientHandler klijent = new ClientHandler(soketZaKomunikaciju);
				korisnici.add(klijent);
				klijent.start();
			}
		} catch (IOException e) {
			System.out.println("Greska prilikom pokretanja servera!");
		}

	}
}
