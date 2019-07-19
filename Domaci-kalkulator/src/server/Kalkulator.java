package server;

public class Kalkulator {

	int broj1, broj2;
	char operacija;
	ClientHandler mojHendler;

	public Kalkulator(ClientHandler mojHendler) {
		this.mojHendler = mojHendler;
	}

	public int izracunaj(String s) {
		mojHendler.setIspravnost(true);
		prevedi(s);
		switch (operacija) {
		case '+':
			return broj1 + broj2;
		case '-':
			return broj1 - broj2;
		case '*':
			return broj1 * broj2;
		case '/':
			if (broj2 == 0) {
				mojHendler.setIspravnost(false);
				return 0;
			}
			return broj1 / broj2;
		default:
			return 0;
		}
	}

	public void prevedi(String s) {
		mojHendler.setLosUnos(false);
		boolean negativan = false;
		String br1 = "", br2 = "";
		char b;
		int i = 0;
		if (s.length()<3) {
			mojHendler.setLosUnos(true);
			return;
		}
		b = s.charAt(0);
		i++;
		if (b != '-' && b != '+' && !Character.isDigit(b)) {
			mojHendler.setLosUnos(true);
			return;
		}
		if (b == '-') {
			negativan = true;
		} else if (b == '+') {
		} else
			br1 += b;
		b = s.charAt(i);
		i++;
		while (Character.isDigit(b) && i<s.length()) {
			br1 += b;
			b = s.charAt(i);
			i++;
		}

		if (br1.length() == 0) {
			mojHendler.setLosUnos(true);
			return;
		}
		operacija = b;
		if (b != '-' && b != '+' && b != '*' && b != '/') {
			mojHendler.setLosUnos(true);
			return;
		}

		while (i < s.length()) {
			b = s.charAt(i);
			i++;
			if (!Character.isDigit(b)) {
				mojHendler.setLosUnos(true);
				return;
			}
			br2 += b;
		}

		if (negativan)
			broj1 = -Integer.parseInt(br1);
		else
			broj1 = Integer.parseInt(br1);
		broj2 = Integer.parseInt(br2);
	}
}
