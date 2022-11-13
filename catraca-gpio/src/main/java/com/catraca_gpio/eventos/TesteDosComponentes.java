package com.catraca_gpio.eventos;

import java.util.concurrent.TimeUnit;
import com.catraca_gpio.controller.GpioController;

public class TesteDosComponentes {

	private GpioController pictoEsquerda;
	private GpioController pictoDireita;
	private GpioController pictoNegado;
	private GpioController buzzer;
	private GpioController solenoideEsquerda;
	private GpioController solenoideDireita;
	private GpioController sensorEsquerda;
	private GpioController sensorDireita;
		
	// Sleep
	int sleepMs = 50;
	int sleepS = 2;
	String atual = "sensor";

	public TesteDosComponentes() throws Exception {
		
		this.pictoEsquerda = new GpioController(3);
		this.pictoDireita = new GpioController(2);
		this.pictoNegado = new GpioController(10);
		this.buzzer = new GpioController(27);
		this.solenoideEsquerda = new GpioController(6);
		this.solenoideDireita = new GpioController(11);
		this.sensorEsquerda = new GpioController(4);
		this.sensorDireita = new GpioController(5);

	
		pictoEsquerda.setDirection("out");
		pictoEsquerda.setValue("0");
		pictoDireita.setDirection("out");
		pictoDireita.setValue("0");
		pictoNegado.setDirection("out");
		pictoNegado.setValue("0");
		
		buzzer.setDirection("out");
		buzzer.setValue("0");
		
		solenoideDireita.setDirection("out");
		solenoideDireita.setValue("0");
		solenoideEsquerda.setDirection("out");
		solenoideEsquerda.setValue("0");
		
		sensorEsquerda.setDirection("in");
		sensorDireita.setDirection("in");
	}
	

	public void testaPictogramaBuzzer() throws Exception {					
		
		System.out.println("Testando o pictograma!");
		
		for(int i = 0; i < 3; i++) {
		
			pictoEsquerda.setValue("0");
			pictoNegado.setValue("0");
			pictoDireita.setValue("1");
			buzzer.setValue("0");
			
			TimeUnit.MILLISECONDS.sleep(sleepMs);
		
			pictoEsquerda.setValue("0");
			pictoNegado.setValue("1");
			pictoDireita.setValue("0");
			buzzer.setValue("1");
			
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			
			pictoEsquerda.setValue("1");
			pictoNegado.setValue("0");
			pictoDireita.setValue("0");
			buzzer.setValue("0");
			
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			
			pictoEsquerda.setValue("0");
			pictoNegado.setValue("1");
			pictoDireita.setValue("0");
			buzzer.setValue("1");
			
			TimeUnit.MILLISECONDS.sleep(sleepMs);
		}
		pictoEsquerda.setValue("0");
		pictoNegado.setValue("0");
		pictoDireita.setValue("0");
		buzzer.setValue("0");
		
		TimeUnit.SECONDS.sleep(sleepS);
	}
		
	public void testaSolenoideSensores() throws Exception {
		
		pictoEsquerda.setValue("1");
		pictoDireita.setValue("0");
		
		System.out.println("Teste o sensor da esquerda!");
		
		while(atual == "sensor") {	
//			System.out.println(sensorEsquerda.getValue());	

			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("0");
			
			if(sensorEsquerda.getValue() == true) {
				pictoEsquerda.setValue("0");
				atual = "Sensor esquerda funcional";
				pictoEsquerda.setValue("0");
			}
		}
		
		System.out.println(atual);
		
		for(int i = 0; i < 2; i++) {
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideEsquerda.setValue("1");
			pictoNegado.setValue("1");
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
		}
		
		System.out.println("Teste o sensor da direita!");
		
		pictoDireita.setValue("1");
		
		while(atual == "Sensor esquerda funcional") {
//			System.out.println(sensorDireita.getValue());	
			
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("0");

			if(sensorDireita.getValue() == true) {
				pictoDireita.setValue("0");
				atual = "Sensor direita funcional";
				pictoDireita.setValue("0");
			}
		}
		
		System.out.println(atual);
		
		for(int i = 0; i < 2; i++) {
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideEsquerda.setValue("1");
			pictoNegado.setValue("1");
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
		}
		

	}
}