package com.catraca_gpio.controller;

import java.util.concurrent.TimeUnit;

/**
 * Esta classe é responsável por traduzir os comandos para a classe gpio.controller
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

	// Contadores
	private int contadorBloqueio = 0;
	private int contadorPassagem = 0;
	private int sensor = 0;
	private int contadorEsquerda = 0;
	private int contadorDireita = 0;
	private int contadorLowEsquerda = 0;
	private int contadorLowDireita = 0;
	private int sensD = 0;
	private int sensE = 0;
	private int retrocesso = 0;
	boolean fim = false;

	// Enums padrão
	public int Giro = 0;
	public int Comportamento = 0;

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
	 * Esta classe testa os elementos presentes na placa A3 - Pictograma e Buzzers -
	 * Sensores e Solenoides
	 * 
	 * @throws Exception sensor
	 */

	public void TestaPlaca() throws Exception {
		System.out.println("Testando pictograma e buzzer!");

		for (int i = 0; i < 3; i++) {

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


		// Testando solenoide Direita

		for (int i = 0; i < 2; i++) {
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideDireita.setValue("1");
			pictoNegado.setValue("1");
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideDireita.setValue("0");
			pictoNegado.setValue("0");
		}

		// Testando solenoide Esquerda

		for (int i = 0; i < 2; i++) {
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideEsquerda.setValue("1");
			pictoNegado.setValue("1");
			TimeUnit.MILLISECONDS.sleep(200);
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
		}

		/**
		 * Para testar o funcionamento dos sensores estou usando como parâmetro os
		 * valores da variável sensor:
		 */

		System.out.println("Teste o Sensor da Esquerda!");
		pictoEsquerda.setValue("1");
		pictoDireita.setValue("0");

		while (sensor == 0) {

			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("0");

			if (sensorEsquerda.getValue() == true) {
				pictoEsquerda.setValue("0");
				sensor = 1;
			}
		}

		System.out.println("Sensor esquerda funcional");

		System.out.println("Teste o Sensor da Direita!");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("1");

		while (sensor == 1) {

			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("0");

			if (sensorDireita.getValue() == true) {
				pictoDireita.setValue("0");
				sensor = 0;
			}

		}

		System.out.println("Sensor direita funcional");

	}

	/**
	 * Comportamento padrão Bloqueia entradas não autorizadas de acordo com o
	 * definido no int/enum comportamento Para alterar, usar a classe setComportamento, com o
	 * parâmetro enum direção
	 * 
	 * @throws Exception
	 */

	public void Standard() throws Exception {

		switch (Comportamento) {

		/**
		 * Comportamento: 0 NenhumLivre, Bloqueia entradas não autorizadas a esquerda e a direita ;
		 */
		case 0:

			while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
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

			// Sequência de trancamento esquerda
			if (sensorDireita.getValue() == true) {
				solenoideEsquerda.setValue("1");
				for (int i = 0; i < 3; i++) {
					pictoNegado.setValue("1");
					pictoEsquerda.setValue("1");
					buzzer.setValue("1");
					TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
					pictoNegado.setValue("0");
					pictoEsquerda.setValue("0");
					buzzer.setValue("0");
				}
				while (sensorDireita.getValue() == true) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}

				TimeUnit.SECONDS.sleep(sleepS);

				solenoideEsquerda.setValue("0");
				pictoNegado.setValue("0");
				buzzer.setValue("0");

			// Sequência de trancamento direita
			} else if (sensorEsquerda.getValue() == true) {
				solenoideDireita.setValue("1");
				for (int i = 0; i < 3; i++) {
					pictoNegado.setValue("1");
					pictoDireita.setValue("1");
					buzzer.setValue("1");
					TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
					pictoNegado.setValue("0");
					pictoDireita.setValue("0");
					buzzer.setValue("0");
				}
				while (sensorEsquerda.getValue() == true) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}

				TimeUnit.SECONDS.sleep(sleepS);

				solenoideDireita.setValue("0");
				pictoNegado.setValue("0");
				buzzer.setValue("0");
			}

			// Sequencia de encerramento
			solenoideDireita.setValue("0");
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			pictoDireita.setValue("0");
			buzzer.setValue("0");
			// bloco de código que será executado

			break;

		/**
		 * Comportamento: 1 direita livre, Bloqueia entradas não autorizadas a esquerda
		 */
		case 1:
			// Sequência de espera
			while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
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

			// Sequência de trancamento, direita livre
			if (sensorDireita.getValue() == true) {
				solenoideEsquerda.setValue("1");
				for (int i = 0; i < 3; i++) {
					pictoNegado.setValue("1");
					pictoEsquerda.setValue("1");
					buzzer.setValue("1");
					TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
					pictoNegado.setValue("0");
					pictoEsquerda.setValue("0");
					buzzer.setValue("0");
				}
				while (sensorDireita.getValue() == true) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}

				TimeUnit.SECONDS.sleep(sleepS);

				solenoideEsquerda.setValue("0");
				pictoNegado.setValue("0");
				buzzer.setValue("0");

			} else if (sensorEsquerda.getValue() == true) {

				pictoDireita.setValue("1");
				buzzer.setValue("1");
				// 2 seg
				TimeUnit.SECONDS.sleep(sleepSStandard);

				while (sensorEsquerda.getValue() == true) {
					pictoDireita.setValue("1");
					buzzer.setValue("1");
				}

				pictoDireita.setValue("0");
				buzzer.setValue("0");
			}

			// Sequencia de encerramento
			solenoideDireita.setValue("0");
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			pictoDireita.setValue("0");
			buzzer.setValue("0");

			break;

		/**
		 * Comportamento: 2 esquerda livre Bloqueia entradas não autorizadas a direita
		 */
		case 2:
			// Sequência de espera
			while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
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
			if (sensorEsquerda.getValue() == true) {
				solenoideDireita.setValue("1");
				for (int i = 0; i < 3; i++) {
					pictoNegado.setValue("1");
					pictoDireita.setValue("1");
					buzzer.setValue("1");
					TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
					pictoNegado.setValue("0");
					pictoDireita.setValue("0");
					buzzer.setValue("0");
				}
				while (sensorEsquerda.getValue() == true) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}

				TimeUnit.SECONDS.sleep(sleepS);

				solenoideDireita.setValue("0");
				pictoNegado.setValue("0");
				buzzer.setValue("0");

			} else if (sensorDireita.getValue() == true) {

				pictoEsquerda.setValue("1");
				buzzer.setValue("1");
				TimeUnit.SECONDS.sleep(sleepSStandard);
				while (sensorDireita.getValue() == true) {
					pictoEsquerda.setValue("1");
					buzzer.setValue("1");
				}

				pictoEsquerda.setValue("0");
				buzzer.setValue("0");

			}

			// Sequencia de encerramento
			solenoideDireita.setValue("0");
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			pictoDireita.setValue("0");
			buzzer.setValue("0");

			break;
		}
	}

	/**
	 * Comportamento ticket negado Da uma indicação sonora e visual de bloqueio e
	 * bloqueia o lado que a pessoa tentar passar
	 * 
	 * @throws Exception
	 */
	public void TicketNegado() throws Exception {

		// Sequência de negação
		while ((sensorDireita.getValue() == sensorEsquerda.getValue()) && contadorBloqueio != 5) {
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
		if (sensorDireita.getValue() == true) {
			solenoideEsquerda.setValue("1");
			for (int i = 0; i < 3; i++) {
				pictoNegado.setValue("1");
				pictoEsquerda.setValue("1");
				buzzer.setValue("1");
				TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
				pictoNegado.setValue("0");
				pictoEsquerda.setValue("0");
				buzzer.setValue("0");
			}
			while (sensorDireita.getValue() == true) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}

			TimeUnit.SECONDS.sleep(sleepS);

			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			buzzer.setValue("0");

		} else if (sensorEsquerda.getValue() == true) {
			solenoideDireita.setValue("1");
			for (int i = 0; i < 3; i++) {
				pictoNegado.setValue("1");
				pictoDireita.setValue("1");
				buzzer.setValue("1");
				TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
				pictoNegado.setValue("0");
				pictoDireita.setValue("0");
				buzzer.setValue("0");
			}
			while (sensorEsquerda.getValue() == true) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}

			TimeUnit.SECONDS.sleep(sleepS);

			solenoideDireita.setValue("0");
			pictoNegado.setValue("0");
			buzzer.setValue("0");
		}

		// Sequencia de encerramento
		solenoideDireita.setValue("0");
		solenoideEsquerda.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("0");
		buzzer.setValue("0");

	}

	/**
	 * Classe que retorna se o giro foi feito ou não, caso a pessoa tente girar para
	 * o lado errado, ou não tente girar, ele retorna false.
	 * 
	 * Recebe como parâmetro: O enum giro
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean Giro() throws Exception {

		/**
		 * Giro Liberado para os dois lados
		 */
		if (Giro == 0) {
			
			resultado = false;

			// Sequência de espera, 6 segundos
			while ((sensorDireita.getValue() == sensorEsquerda.getValue()) && contadorPassagem < 2) {
				pictoDireita.setValue("1");
				pictoEsquerda.setValue("1");
				TimeUnit.SECONDS.sleep(sleepS);
				pictoDireita.setValue("0");
				pictoEsquerda.setValue("0");
				TimeUnit.SECONDS.sleep(sleepS);

				contadorPassagem++;
			}
			
			// limpar picto
			pictoDireita.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			contadorPassagem = 0;

			// Sequência de liberação e contagem de giro
			// Primeiro identifica o lado do giro:
			
			// Sensor da direita primeiro, significa giro para a esquerda
			if (sensorDireita.getValue() == true) { 
				
				// Sequência de liberação de giro para a Esquerda 
				
				while (fim == false) {

					// Esse início faz a leitura recorrente dos sensores
					if (sensorDireita.getValue() == true) {
						sensD = 1;
						if (contadorDireita == 0) {
							contadorDireita = 1;
						}
					} else {
						sensD = 0;
						if (contadorLowDireita == 0) {
							contadorLowDireita = 1;
						}
					}

					if (sensorEsquerda.getValue() == true) {
						sensE = 1;

						if (contadorEsquerda == 0) {
							contadorEsquerda = 1;
						}
					} else {
						sensE = 0;
					}
					
					

					// Verifica se ouve um giro para a esquerda 				

					// Caso volte após ativar o da direita, volta não completa, retorna false
					if (contadorEsquerda == 0 && sensD == 0 && contadorDireita == 1) {
						resultado = false;
						fim = true;
					}

					// caso ative o da esquerda mas volte
					if (sensD == 1 && sensE == 0 && contadorEsquerda == 1) {
						retrocesso = 1;
					}

					// caso a volta seja completa, retorna true 
					if (sensD == 0 && sensE == 0 && contadorDireita == 1 && contadorEsquerda == 1
							&& contadorLowDireita == 1) {
						resultado = true;
						fim = true;
						// caso volte tudo, após ativar os dois, retorna false
					} else if(sensE == 0 && sensD == 0 && retrocesso == 1) {
							resultado = false;
							fim = true;
					}
				}
				
//				sequência de encerramento 
				sensD = 0;
				sensE = 0;
				contadorEsquerda = 0;
				contadorDireita = 0;
				contadorLowEsquerda = 0;
				contadorLowDireita = 0;
				retrocesso = 0;
				fim = false;

			// caso identifique o sensor da esquerda primeiro, significa que está
			// dando uma volta para a direita
			} else if (sensorEsquerda.getValue() == true) {

				// Sequência de liberação de giro para a direita

				while (fim == false) {

					// Esse início faz a leitura recorrente dos sensores
					if (sensorDireita.getValue() == true) {
						sensD = 1;
						if (contadorDireita == 0) {
							contadorDireita = 1;
						}
					} else {
						sensD = 0;
					}

					if (sensorEsquerda.getValue() == true) {
						sensE = 1;

						if (contadorEsquerda == 0) {
							contadorEsquerda = 1;
						}
					} else {
						sensE = 0;
						if (contadorLowEsquerda == 0) {
							contadorLowEsquerda = 1;
						}
					}

					// Verifica se ouve um giro para a direita

					// Caso volte após ativar o da esquerda, volta não completa, retorna false
					if (contadorEsquerda == 1 && sensE == 0 && contadorDireita == 0) {
						resultado = false;
						fim = true;
					}

					// caso ative o da direita mas volte
					if (sensE == 1 && sensD == 0 && contadorDireita == 1) {
						retrocesso = 1;
					}

					

					// caso a volta seja completa, retorna true 
					if (sensD == 0 && sensE == 0 && contadorDireita == 1 && contadorEsquerda == 1
							&& contadorLowEsquerda == 1) {
						resultado = true;
						fim = true;
						// caso volte tudo, após ativar os dois, retorna false
					} else if (sensE == 0 && sensD == 0 && retrocesso == 1) {
						resultado = false;
						fim = true;
					}
				}
				
//				sequência de encerramento 
				sensD = 0;
				sensE = 0;
				contadorEsquerda = 0;
				contadorDireita = 0;
				contadorLowEsquerda = 0;
				contadorLowDireita = 0;
				retrocesso = 0;
				fim = false;					
				
			}

			// Sequencia de encerramento
			solenoideDireita.setValue("0");
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			pictoDireita.setValue("0");
			buzzer.setValue("0");

			return resultado;

		}

			
			
		/**
		 * Giro Liberado para direita
		 */
		if (Giro == 1) {
			
			resultado = false;

			// Sequência de espera, 6 segundos
			while ((sensorDireita.getValue() == sensorEsquerda.getValue()) && contadorPassagem < 2) {
				pictoDireita.setValue("1");
				pictoEsquerda.setValue("1");
				TimeUnit.SECONDS.sleep(sleepS);
				pictoDireita.setValue("0");
				pictoEsquerda.setValue("0");
				TimeUnit.SECONDS.sleep(sleepS);

				contadorPassagem++;
			}
			
			// limpar picto
			pictoDireita.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			contadorPassagem = 0;

			// Sequência de liberação e contagem de giro
			// Primeiro identifica o lado do giro:
			
			// Sensor da direita primeiro, significa giro para a esquerda
			if (sensorDireita.getValue() == true) { 
				
				// Sequência de trancamento giro para a Esquerda 
				solenoideEsquerda.setValue("1");
				for (int i = 0; i < 3; i++) {
					pictoNegado.setValue("1");
					pictoEsquerda.setValue("1");
					buzzer.setValue("1");
					TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
					pictoNegado.setValue("0");
					pictoEsquerda.setValue("0");
					buzzer.setValue("0");
				}
				while (sensorDireita.getValue() == true) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}

				TimeUnit.SECONDS.sleep(sleepS);

				solenoideEsquerda.setValue("0");
				pictoNegado.setValue("0");
				buzzer.setValue("0");
				
				resultado = false;

			// caso identifique o sensor da esquerda primeiro, significa que está
			// dando uma volta para a direita
			} else if (sensorEsquerda.getValue() == true) {

				// Sequência de liberação de giro para a direita

				while (fim == false) {

					// Esse início faz a leitura recorrente dos sensores
					if (sensorDireita.getValue() == true) {
						sensD = 1;
						if (contadorDireita == 0) {
							contadorDireita = 1;
						}
					} else {
						sensD = 0;
					}

					if (sensorEsquerda.getValue() == true) {
						sensE = 1;

						if (contadorEsquerda == 0) {
							contadorEsquerda = 1;
						}
					} else {
						sensE = 0;
						if (contadorLowEsquerda == 0) {
							contadorLowEsquerda = 1;
						}
					}

					// Verifica se ouve um giro para a direita

					// Caso volte após ativar o da esquerda, volta não completa, retorna false
					if (contadorEsquerda == 1 && sensE == 0 && contadorDireita == 0) {
						resultado = false;
						fim = true;
					}

					// caso ative o da direita mas volte
					if (sensE == 1 && sensD == 0 && contadorDireita == 1) {
						retrocesso = 1;
					}

					

					// caso a volta seja completa, retorna true 
					if (sensD == 0 && sensE == 0 && contadorDireita == 1 && contadorEsquerda == 1
							&& contadorLowEsquerda == 1) {
						resultado = true;
						fim = true;
						// caso volte tudo, após ativar os dois, retorna false
					} else if (sensE == 0 && sensD == 0 && retrocesso == 1) {
						resultado = false;
						fim = true;
					}
				}
				
//				sequência de encerramento 
				sensD = 0;
				sensE = 0;
				contadorEsquerda = 0;
				contadorDireita = 0;
				contadorLowEsquerda = 0;
				contadorLowDireita = 0;
				retrocesso = 0;
				fim = false;					
				
			}

			// Sequencia de encerramento
			solenoideDireita.setValue("0");
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			pictoDireita.setValue("0");
			buzzer.setValue("0");

			return resultado;
			
		}
		

		/**
		 * Giro Liberado para esquerda
		 */
		if (Giro == 2){
			resultado = false;

			// Sequência de espera, 6 segundos
			while ((sensorDireita.getValue() == sensorEsquerda.getValue()) && contadorPassagem < 2) {
				pictoDireita.setValue("1");
				pictoEsquerda.setValue("1");
				TimeUnit.SECONDS.sleep(sleepS);
				pictoDireita.setValue("0");
				pictoEsquerda.setValue("0");
				TimeUnit.SECONDS.sleep(sleepS);

				contadorPassagem++;
			}
			
			// limpar picto
			pictoDireita.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			contadorPassagem = 0;

			// Sequência de liberação e contagem de giro
			// Primeiro identifica o lado do giro:
			
			// Sensor da direita primeiro, significa giro para a esquerda
			if (sensorDireita.getValue() == true) { 
				
				// Sequência de liberação de giro para a Esquerda 
				
				while (fim == false) {

					// Esse início faz a leitura recorrente dos sensores
					if (sensorDireita.getValue() == true) {
						sensD = 1;
						if (contadorDireita == 0) {
							contadorDireita = 1;
						}
					} else {
						sensD = 0;
						if (contadorLowDireita == 0) {
							contadorLowDireita = 1;
						}
					}

					if (sensorEsquerda.getValue() == true) {
						sensE = 1;

						if (contadorEsquerda == 0) {
							contadorEsquerda = 1;
						}
					} else {
						sensE = 0;
					}
					
					

					// Verifica se ouve um giro para a esquerda 				

					// Caso volte após ativar o da direita, volta não completa, retorna false
					if (contadorEsquerda == 0 && sensD == 0 && contadorDireita == 1) {
						resultado = false;
						fim = true;
					}

					// caso ative o da esquerda mas volte
					if (sensD == 1 && sensE == 0 && contadorEsquerda == 1) {
						retrocesso = 1;
					}

					// caso a volta seja completa, retorna true 
					if (sensD == 0 && sensE == 0 && contadorDireita == 1 && contadorEsquerda == 1
							&& contadorLowDireita == 1) {
						resultado = true;
						fim = true;
						// caso volte tudo, após ativar os dois, retorna false
					} else if(sensE == 0 && sensD == 0 && retrocesso == 1) {
							resultado = false;
							fim = true;
					}
				}
				
//				sequência de encerramento 
				sensD = 0;
				sensE = 0;
				contadorEsquerda = 0;
				contadorDireita = 0;
				contadorLowEsquerda = 0;
				contadorLowDireita = 0;
				retrocesso = 0;
				fim = false;

			// caso identifique o sensor da esquerda primeiro, significa que está
			// dando uma volta para a direita
			} else if (sensorEsquerda.getValue() == true) {

				solenoideDireita.setValue("1");
				for (int i = 0; i < 3; i++) {
					pictoNegado.setValue("1");
					pictoDireita.setValue("1");
					buzzer.setValue("1");
					TimeUnit.MILLISECONDS.sleep(sleepMsStandard);
					pictoNegado.setValue("0");
					pictoDireita.setValue("0");
					buzzer.setValue("0");
				}
				while (sensorEsquerda.getValue() == true) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}

				TimeUnit.SECONDS.sleep(sleepS);

				solenoideDireita.setValue("0");
				pictoNegado.setValue("0");
				buzzer.setValue("0");

							
				resultado = false;
			}

			// Sequencia de encerramento
			solenoideDireita.setValue("0");
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
			pictoEsquerda.setValue("0");
			pictoDireita.setValue("0");
			buzzer.setValue("0");

			return resultado;
			
		}else {			
			return resultado;
		}
				
	}

	
	/**
	 * Preencher com o enum Giro Exemplo: Giro.DIREITA.ordinal();
	 * 
	 * @param giro
	 */
	public void setGiro(int giro) {
		this.Giro = giro;
	}

	/**
	 * Preencher com o enum Direcao Exemplo: Direcao.DIREITA_LIVRE.ordinal();
	 * 
	 * @param direcao
	 */
	public void setComportamento(int comportamento) {
		this.Comportamento = comportamento;
	}

}
