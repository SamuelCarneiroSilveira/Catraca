package com.catraca_gpio.controller;


import java.util.concurrent.TimeUnit;

/**
 * 
 * 
 * @author Samuel C. Silveira
 *
 */
public class GateController {

	private GpioController pictoEsquerda;
	private GpioController pictoDireita;
	private GpioController pictoNegado;
	private GpioController buzzer;
	private GpioController solenoideEsquerda;
	private GpioController solenoideDireita;
	private GpioController sensorEsquerda;
	private GpioController sensorDireita;
	
	// Sleep
	private int sleepMs = 100;
	private int sleepS = 1;
	private int sleepMsStandard = 300;
	private int sleepSStandard = 2;
	private int sleepLiberar = 4;
	
	// Contadores	
	private int contadorBloqueio = 0;
	private int contadorPassagem = 0;
	private int sensor = 0;
	// Falta usar
	//	private int contadorDeGiros = 0;
	
	// Others
	private boolean resultado = false;
	
	
	/**
	 * Classe que agrupa os comportamentos da catraca
	 * 
	 * @throws Exception
	 */
	public GateController() throws Exception {
		
		this.pictoEsquerda = new GpioController(3);
		this.pictoDireita = new GpioController(2);
		this.pictoNegado = new GpioController(10);
		this.buzzer = new GpioController(27);
		this.solenoideEsquerda = new GpioController(6);
		this.solenoideDireita = new GpioController(11);
		this.sensorEsquerda = new GpioController(4);
		this.sensorDireita = new GpioController(5);
		
		// Configurar valores padrão 
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
	
	/**
	 * Esta classe testa os elementos presentes na placa A3
	 * - Pictograma e Buzzers
	 * - Sensores e Solenoides 
	 * 
	 * @throws Exception sensor
	 */
	public void TestaPlaca() throws Exception{
		System.out.println("Testando pictograma e buzzer!");
		
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
		
		

		/**
		 * Para testar o funcionamento dos sensores estou usando como
		 * parâmetro os valores da variável sensor:
		 * 
		 * 0 - Não passou ainda
		 * 1 - Passou no sensor esquerda
		 * 2 - Passou no sensor direita
		 * 
		 */
		

		System.out.println("Teste o Sensor da Esquerda!");
		pictoEsquerda.setValue("1");
		pictoDireita.setValue("0");
		
		while(sensor == 0) {
			
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("0");
			
			if(sensorEsquerda.getValue() == true) {
				pictoEsquerda.setValue("0");
				sensor = 1;
			}
		}

		System.out.println("Sensor esquerda funcional");
		
		//Testando solenoide Esquerda 
		
		for(int i = 0; i < 2; i++) {
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideEsquerda.setValue("1");
			pictoNegado.setValue("1");
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
		}
		
		System.out.println("Teste o Sensor da Direita!");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("1");
						
		while(sensor == 1) {
			
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("0");
			
			if(sensorDireita.getValue() == true) {
				pictoDireita.setValue("0");
				sensor = 0;
			}
			
		}
		
		System.out.println("Sensor direita funcional");
		
		//Testando solenoide Direita
		
		for(int i = 0; i < 2; i++) {
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideDireita.setValue("1");
			pictoNegado.setValue("1");
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideDireita.setValue("0");
			pictoNegado.setValue("0");
		}
	}
	/**
	 * Comportamento padrão
	 * Bloqueia entradas não autorizadas nas duas direções
	 * 
	 * @throws Exception
	 */
	public void Standard() throws Exception{
		
		// Sequência de espera
		while(sensorDireita.getValue()==sensorEsquerda.getValue()){
			pictoDireita.setValue("1");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("1");
			TimeUnit.SECONDS.sleep(sleepS);
			pictoDireita.setValue("0");
			pictoNegado.setValue("1");
			pictoEsquerda.setValue("0");
			TimeUnit.SECONDS.sleep(sleepS);
		}
		
		pictoDireita.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		
		// Sequência de trancamento
		if(sensorDireita.getValue() == true) {
			solenoideDireita.setValue("1");
			for(int i = 0; i < 3; i++) {
				pictoNegado.setValue("1");
				pictoDireita.setValue("1");
				buzzer.setValue("1");
				TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
				pictoNegado.setValue("0");
				pictoDireita.setValue("0");
				buzzer.setValue("0");
			}
			while(sensorDireita.getValue()==true) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}
			
			TimeUnit.SECONDS.sleep(sleepS);	
			
			solenoideDireita.setValue("0");
			pictoNegado.setValue("0");
			buzzer.setValue("0");
			
		} else if(sensorEsquerda.getValue() == true) {
			solenoideEsquerda.setValue("1");
			for(int i = 0; i < 3; i++) {
				pictoNegado.setValue("1");
				pictoEsquerda.setValue("1");
				buzzer.setValue("1");
				TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
				pictoNegado.setValue("0");
				pictoEsquerda.setValue("0");
				buzzer.setValue("0");
			}
			while(sensorEsquerda.getValue()==true) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}
			
			TimeUnit.SECONDS.sleep(sleepS);	
			
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			buzzer.setValue("0");
		}		
		
		//Sequencia de encerramento
		solenoideDireita.setValue("0");
		solenoideEsquerda.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("0");
		buzzer.setValue("0");
		
	}
	
	
	/** 
	 * Comportamento direita livre
	 * Bloqueia entradas não autorizadas a esquerda
	 * 
	 * @throws Exception
	 */
	public void DireitaLivre() throws Exception{
		
		// Sequência de espera
		while(sensorDireita.getValue()==sensorEsquerda.getValue()){
			pictoDireita.setValue("1");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("1");
			TimeUnit.SECONDS.sleep(sleepS);
			pictoDireita.setValue("0");
			pictoNegado.setValue("1");
			pictoEsquerda.setValue("0");
			TimeUnit.SECONDS.sleep(sleepS);
		}
		
		pictoDireita.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		
		// Sequência de trancamento
		if(sensorEsquerda.getValue() == true) {
			solenoideEsquerda.setValue("1");
			for(int i = 0; i < 3; i++) {
				pictoNegado.setValue("1");
				pictoEsquerda.setValue("1");
				buzzer.setValue("1");
				TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
				pictoNegado.setValue("0");
				pictoEsquerda.setValue("0");
				buzzer.setValue("0");
			}
			while(sensorEsquerda.getValue()==true) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}
			
			TimeUnit.SECONDS.sleep(sleepS);			
			
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			buzzer.setValue("0");
			
		} else if(sensorDireita.getValue() == true) {

			pictoDireita.setValue("1");
			buzzer.setValue("1");
			TimeUnit.SECONDS.sleep(sleepSStandard);

			while(sensorDireita.getValue()==true) {
				pictoDireita.setValue("1");
				buzzer.setValue("1");
			}
			
			pictoDireita.setValue("0");
			buzzer.setValue("0");
		}	
		
		//Sequencia de encerramento
		solenoideDireita.setValue("0");
		solenoideEsquerda.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("0");
		buzzer.setValue("0");
		
	}
	
	/**
	 * Comportamento esquerda livre
	 * Bloqueia entradas não autorizadas a direita
	 * 
	 * @throws Exception
	 */
	public void EsquerdaLivre() throws Exception{
		
		// Sequência de espera
		while(sensorDireita.getValue()==sensorEsquerda.getValue()){
			pictoDireita.setValue("1");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("1");
			TimeUnit.SECONDS.sleep(sleepS);
			pictoDireita.setValue("0");
			pictoNegado.setValue("1");
			pictoEsquerda.setValue("0");
			TimeUnit.SECONDS.sleep(sleepS);
		}
		
		pictoDireita.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		
		// Sequência de trancamento
		if(sensorDireita.getValue() == true) {
			solenoideDireita.setValue("1");
			for(int i = 0; i < 3; i++) {
				pictoNegado.setValue("1");
				pictoDireita.setValue("1");
				buzzer.setValue("1");
				TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
				pictoNegado.setValue("0");
				pictoDireita.setValue("0");
				buzzer.setValue("0");
			}
			while(sensorDireita.getValue()==true) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}
			
			TimeUnit.SECONDS.sleep(sleepS);			
			
			solenoideDireita.setValue("0");
			pictoNegado.setValue("0");
			buzzer.setValue("0");
			
		} else if(sensorEsquerda.getValue() == true) {
			
			pictoEsquerda.setValue("1");
			buzzer.setValue("1");
			TimeUnit.SECONDS.sleep(sleepSStandard);
			while(sensorEsquerda.getValue()==true) {
				pictoEsquerda.setValue("1");
				buzzer.setValue("1");
			}
			
			pictoEsquerda.setValue("0");
			buzzer.setValue("0");
			
		}	
		
		//Sequencia de encerramento
		solenoideDireita.setValue("0");
		solenoideEsquerda.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("0");
		buzzer.setValue("0");
		
	}
	
	/**
	 * Comportamento ticket negado
	 * Da uma indicação sonora e visual de bloqueio
	 * e bloqueia o lado que a pessoa tentar passar  
	 * 
	 * @throws Exception
	 */
	public void TicketNegado() throws Exception{
			
			// Sequência de negação
			while((sensorDireita.getValue()==sensorEsquerda.getValue())&& contadorBloqueio != 5){
				pictoNegado.setValue("1");
				buzzer.setValue("1");
				TimeUnit.SECONDS.sleep(sleepMsStandard);
				pictoNegado.setValue("0");
				buzzer.setValue("1");
				TimeUnit.SECONDS.sleep(sleepMsStandard);
				
				contadorBloqueio++;
			}
			
			pictoDireita.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			buzzer.setValue("0");
			contadorBloqueio = 0;
			
			// Sequência de trancamento
			if(sensorDireita.getValue() == true) {
				solenoideDireita.setValue("1");
				for(int i = 0; i < 3; i++) {
					pictoNegado.setValue("1");
					pictoDireita.setValue("1");
					buzzer.setValue("1");
					TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
					pictoNegado.setValue("0");
					pictoDireita.setValue("0");
					buzzer.setValue("0");
				}
				while(sensorDireita.getValue()==true) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}
				
				TimeUnit.SECONDS.sleep(sleepS);	
				
				solenoideDireita.setValue("0");
				pictoNegado.setValue("0");
				buzzer.setValue("0");
				
			} else if(sensorEsquerda.getValue() == true) {
				solenoideEsquerda.setValue("1");
				for(int i = 0; i < 3; i++) {
					pictoNegado.setValue("1");
					pictoEsquerda.setValue("1");
					buzzer.setValue("1");
					TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
					pictoNegado.setValue("0");
					pictoEsquerda.setValue("0");
					buzzer.setValue("0");
				}
				while(sensorEsquerda.getValue()==true) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}
				
				TimeUnit.SECONDS.sleep(sleepS);	
				
				solenoideEsquerda.setValue("0");
				pictoNegado.setValue("0");
				buzzer.setValue("0");
			}		
			
			//Sequencia de encerramento
			solenoideDireita.setValue("0");
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			pictoDireita.setValue("0");
			buzzer.setValue("0");
			
	}
		
	/**
	 * 
	 * Classe que retorna se o giro foi feito ou não, caso a pessoa
	 * tente girar para o lado errado, ou não tente girar, ele 
	 * retorna false.
	 * 
	 * @return Se o giro foi feito ou não  
	 * @throws Exception
	 */
	public boolean GiroEsquerda() throws Exception{
		
		resultado = false;
		
		// Sequência de espera, 6 segundos
		while((sensorDireita.getValue()==sensorEsquerda.getValue())&& contadorPassagem < 2){
			pictoEsquerda.setValue("1");
			TimeUnit.SECONDS.sleep(sleepS);
			pictoEsquerda.setValue("0");
			TimeUnit.SECONDS.sleep(sleepS);
			
			contadorPassagem++;
		}
		
		pictoDireita.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		contadorPassagem = 0;
		
		// Sequência de trancamento
		if(sensorDireita.getValue() == true) {
			solenoideDireita.setValue("1");
			for(int i = 0; i < 3; i++) {
				pictoNegado.setValue("1");
				pictoDireita.setValue("1");
				buzzer.setValue("1");
				TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
				pictoNegado.setValue("0");
				pictoDireita.setValue("0");
				buzzer.setValue("0");
			}
			while(sensorDireita.getValue()==true) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}
			
			TimeUnit.SECONDS.sleep(sleepS);	
			
			solenoideDireita.setValue("0");
			pictoNegado.setValue("0");
			buzzer.setValue("0");
			resultado = false;
			
			
		} else if(sensorEsquerda.getValue() == true) {
			
			// permitir passagem a esquerda	
			// atualmente funciona com um timer pois preciso entender
			// como os sensores da catraca funcionam 
			
			
			
			// Adicionar um contador de giro aqui, para verificar a passagem
			// Se a passagem for concluída, contador de giros igual a 3
			
//			if(contadorDeGiros == 2) {
//				resultado = true;
//			} else {
//				resultado = false;
//			}
						
			TimeUnit.SECONDS.sleep(sleepLiberar);	
			resultado = true;
		}		
		
		//Sequencia de encerramento
		solenoideDireita.setValue("0");
		solenoideEsquerda.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("0");
		buzzer.setValue("0");
		
		return resultado;
	}
	
	/**
	 * 
	 * Classe que retorna se o giro foi feito ou não, caso a pessoa
	 * tente girar para o lado errado, ou não tente girar, ele 
	 * retorna false.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean GiroDireita() throws Exception{
		
		resultado = false;
		
		// Sequência de espera, 6 segundos
		while((sensorDireita.getValue()==sensorEsquerda.getValue())&& contadorPassagem < 2){
			pictoDireita.setValue("1");
			TimeUnit.SECONDS.sleep(sleepS);
			pictoDireita.setValue("0");
			TimeUnit.SECONDS.sleep(sleepS);
			
			contadorPassagem++;
		}
		
		pictoDireita.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		contadorPassagem = 0;
		
		// Sequência de trancamento
		if(sensorEsquerda.getValue() == true) {
			solenoideEsquerda.setValue("1");
			for(int i = 0; i < 3; i++) {
				pictoNegado.setValue("1");
				pictoEsquerda.setValue("1");
				buzzer.setValue("1");
				TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
				pictoNegado.setValue("0");
				pictoEsquerda.setValue("0");
				buzzer.setValue("0");
			}
			while(sensorEsquerda.getValue()==true) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}
			
			TimeUnit.SECONDS.sleep(sleepS);	
			
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			buzzer.setValue("0");
			resultado = false;
			
			
		} else if(sensorDireita.getValue() == true) {
			
			// permitir passagem a esquerda	
			// atualmente funciona com um timer pois preciso entender
			// como os sensores da catraca funcionam 
			
			
			
			// Adicionar um contador de giro aqui, para verificar a passagem
			// Se a passagem for concluída, contador de giros igual a 3
			
//			if(contadorDeGiros == 2) {
//				resultado = true;
//			} else {
//				resultado = false;
//			}
						
			TimeUnit.SECONDS.sleep(sleepLiberar);	
			resultado = true;
		}		
		
		//Sequencia de encerramento
		solenoideDireita.setValue("0");
		solenoideEsquerda.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("0");
		buzzer.setValue("0");
		
		return resultado;
		
	}
	
	
}

