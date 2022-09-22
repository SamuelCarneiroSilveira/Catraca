package main;

import Eventos.TesteDosComponentes;

public class inicio {
	
	public static void main(String[] args) throws Exception {
		TesteDosComponentes teste = new TesteDosComponentes();
		teste.testaPictogramaBuzzer();
		teste.testaSolenoideSensores();
	}

}
