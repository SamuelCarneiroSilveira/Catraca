package com.catraca_gpio.controller;

import java.util.concurrent.TimeUnit;

import com.catraca_gpio.enums.Comportamento;
import com.catraca_gpio.enums.Giro;

/**
 * Esta classe é responsável por traduzir os comandos para a classe
 * gpio.controller
 * 
 * @author Samuel C. Silveira
 *
 */
public class GateController {

	private final GpioController pictoEsquerda;
	private final GpioController pictoDireita;
	private final GpioController pictoNegado;
	private final GpioController buzzer;
	private final GpioController solenoideEsquerda;
	private final GpioController solenoideDireita;
	private final GpioController sensorEsquerda;
	private final GpioController sensorDireita;

	// Enums padrão
	private Giro giro;
	private Comportamento comportamento;

	/**
	 * Construtor, recebe os comportamentos padrões de funcionamento, e seta as
	 * gpios em seus comportamentos iniciais
	 * 
	 * @throws Exception
	 */
	public GateController(Comportamento comportamento, Giro giro) throws Exception {

		this.comportamento = comportamento;
		this.giro = giro;

		this.pictoEsquerda = new GpioController(3);
		this.pictoDireita = new GpioController(2);
		this.pictoNegado = new GpioController(10);
		this.buzzer = new GpioController(27);
		this.solenoideDireita = new GpioController(11);
		this.solenoideEsquerda = new GpioController(6);
		this.sensorEsquerda = new GpioController(4);
		this.sensorDireita = new GpioController(5);

		setarComportamentosIniciais();
	}

	/**
	 * -------------------------------------------------------------------------
	 * MÉTODOS PRIVADOS
	 * -------------------------------------------------------------------------
	 */

	/**
	 * Esta classe testa os elementos presentes na placa A3 - Pictograma e Buzzers -
	 * Sensores e Solenoides, é preciso testar os sensores manualmente quando
	 * solicitado
	 * 
	 * @throws Exception sensor
	 */
	public void testaPlaca() throws Exception {

		testaPlacaPrivate();
		sequenciaDeEncerramentoGpios();
	}

	/**
	 * Comportamento padrão Bloqueia entradas não autorizadas de acordo com o
	 * definido no int/enum comportamento Para alterar, usar a classe
	 * setComportamento, com o parâmetro enum direção
	 * 
	 * @throws Exception
	 */
	public void standard() throws Exception {

		switch (comportamento) {

		/**
		 * Comportamento nenhum livre, Bloqueia entradas não autorizadas a esquerda e a
		 * direita
		 */
		case NENHUM_LIVRE:

			nenhumLivrePrivate();
			break;

		/**
		 * Comportamento direita livre, Bloqueia entradas não autorizadas a esquerda
		 */
		case DIREITA_LIVRE:

			direitaLivrePrivate();
			break;

		/**
		 * Comportamento esquerda livre Bloqueia entradas não autorizadas a direita
		 */
		case ESQUERDA_LIVRE:

			esquerdaLivrePrivate();
			break;

		}
		sequenciaDeEncerramentoGpios();
	}

	/**
	 * Comportamento ticket negado Da uma indicação sonora e visual de bloqueio e
	 * bloqueia o lado que a pessoa tentar passar
	 * 
	 * @throws Exception
	 */
	public void TicketNegado() throws Exception {

		ticketNegadoPrivate();
		sequenciaDeEncerramentoGpios();
	}

	/**
	 * Classe que retorna se o giro foi feito ou não caso a pessoa tente girar para
	 * o lado errado, ela terá 3 tentativas para acertar caso o giro seja feito
	 * retorna true caso durante os 6 segundos não tente girar, ou falhe nas 3
	 * tentativas, ele retorna false.
	 * 
	 * @return Giro
	 * @throws Exception
	 */
	public boolean Giro() throws Exception {

		boolean resultado = false;

		switch (giro) {

		// Giro Liberado para os dois lados
		case AMBOS:

			resultado = giroAmbosPrivate();
			sequenciaDeEncerramentoGpios();
			return resultado;

		// Giro Liberado para direita
		case DIREITA:

			resultado = giroDireitaPrivate();
			sequenciaDeEncerramentoGpios();
			return resultado;

		// Giro Liberado para esquerda
		case ESQUERDA:

			resultado = giroEsquerdaPrivate();
			sequenciaDeEncerramentoGpios();
			return resultado;

		default:
			sequenciaDeEncerramentoGpios();
			return resultado;
		}

	}

	/**
	 * Define um novo comportamento padrão de giro para a catraca
	 * 
	 * @param giro
	 */
	public void setGiro(Giro giro) {
		this.giro = giro;
	}

	/**
	 * Define um novo comportamento padrão de bloqueia para a catraca
	 * 
	 * @param direcao
	 */
	public void setComportamento(Comportamento comportamento) {
		this.comportamento = comportamento;
	}

	/**
	 * -------------------------------------------------------------------------
	 * MÉTODOS PRIVADOS
	 * -------------------------------------------------------------------------
	 */

	/**
	 * Desliga as gpios temporariamente
	 * 
	 * @throws Exception
	 */
	public void sequenciaDeEncerramentoGpios() throws Exception {
		solenoideDireita.setValue("0");
		solenoideEsquerda.setValue("0");
		pictoNegado.setValue("0");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("0");
		buzzer.setValue("0");
	}

	/**
	 * Define o comportamento inicial das portas que vamos usar
	 * 
	 * @throws Exception
	 */
	private void setarComportamentosIniciais() throws Exception {
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
	 * Testa os componentes da placa
	 * 
	 * @throws Exception
	 */
	private void testaPlacaPrivate() throws Exception {
		int sensor = 0;
		int sleepMs = 100;
		int sleepMs2 = 200;
		int sleepS = 1;

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
			TimeUnit.MILLISECONDS.sleep(sleepMs2);
			solenoideDireita.setValue("1");
			pictoNegado.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs2);
			solenoideDireita.setValue("0");
			pictoNegado.setValue("0");
		}

		// Testando solenoide Esquerda

		for (int i = 0; i < 2; i++) {
			TimeUnit.MILLISECONDS.sleep(sleepMs2);
			solenoideEsquerda.setValue("1");
			pictoNegado.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs2);
			solenoideEsquerda.setValue("0");
			pictoNegado.setValue("0");
		}

		/**
		 * Para testar o funcionamento dos sensores estou usando como parâmetro os
		 * valores da variável sensor:
		 */

		System.out.println("Teste o Sensor da Esquerda!");
		pictoEsquerda.setValue("0");
		pictoDireita.setValue("1");

		while (sensor == 0) {

			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("0");

			if (sensorEsquerda.getValue()) {
				pictoDireita.setValue("0");
				sensor = 1;
			}
		}

		System.out.println("Sensor esquerda funcional");

		System.out.println("Teste o Sensor da Direita!");
		pictoEsquerda.setValue("1");
		pictoDireita.setValue("0");

		while (sensor == 1) {

			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("1");
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			buzzer.setValue("0");

			if (sensorDireita.getValue()) {
				pictoEsquerda.setValue("0");
				sensor = 0;
			}

		}
		System.out.println("Sensor direita funcional");
	}

	/**
	 * Comportamento NenhumLivre, Bloqueia entradas não autorizadas a esquerda e a
	 * direita
	 * 
	 * @throws Exception
	 */
	private void nenhumLivrePrivate() throws Exception {
		int sleepMsStandard = 300;
		int sleepMs = 100;

		// Sequência de espera
		pictoNegado.setValue("1");

		// Sequência de espera, 6 segundos

		// TODO Testar esse e se funcionar usar para os outros
		while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
			TimeUnit.MILLISECONDS.sleep(sleepMs);
		}

		System.out.println("Saindo da sequencia de espera"); // Essa parte provavelmente vai sair

		sequenciaDeEncerramentoGpios();

		// Sequência de trancamento
		if (sensorEsquerda.getValue()) {
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
			while (sensorEsquerda.getValue()) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}

		} else if (sensorDireita.getValue()) {

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
			while (sensorDireita.getValue()) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}
		}
		sequenciaDeEncerramentoGpios();

	}

	/**
	 * Comportamento DireitaLivre, Bloqueia entradas não autorizadas a esquerda e
	 * libera a direita
	 * 
	 * @throws Exception
	 */
	private void direitaLivrePrivate() throws Exception {

		int sleepMsStandard = 300;
		int sleepMs = 100;

		// Sequência de espera
		pictoDireita.setValue("1");

		// Sequência de espera, 6 segundos

		// TODO Testar esse e se funcionar usar para os outros
		while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
			TimeUnit.MILLISECONDS.sleep(sleepMs);
		}

		System.out.println("Saindo da sequencia de espera"); // Essa parte provavelmente vai sair

		sequenciaDeEncerramentoGpios();

		// Sequência de trancamento
		if (sensorEsquerda.getValue()) {

			pictoDireita.setValue("1");

			while (sensorDireita.getValue() || sensorEsquerda.getValue()) {
				TimeUnit.MILLISECONDS.sleep(sleepMs);
			}

		} else if (sensorDireita.getValue()) {

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
			while (sensorDireita.getValue()) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}
		}
		sequenciaDeEncerramentoGpios();
	}

	/**
	 * Comportamento EsquerdaLivre, Bloqueia entradas não autorizadas a direita e
	 * libera a esquerda
	 * 
	 * @throws Exception
	 */
	private void esquerdaLivrePrivate() throws Exception {

		int sleepMsStandard = 300;
		int sleepMs = 100;

		// Sequência de espera
		pictoEsquerda.setValue("1");

		// Sequência de espera, 6 segundos

		// TODO Testar esse e se funcionar usar para os outros
		while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
			TimeUnit.MILLISECONDS.sleep(sleepMs);
		}

		System.out.println("Saindo da sequencia de espera"); // Essa parte provavelmente vai sair

		sequenciaDeEncerramentoGpios();

		// Sequência de trancamento
		if (sensorEsquerda.getValue()) {
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
			while (sensorEsquerda.getValue()) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}
		} else if (sensorDireita.getValue()) {

			pictoEsquerda.setValue("1");

			while (sensorDireita.getValue() || sensorEsquerda.getValue()) {
				TimeUnit.MILLISECONDS.sleep(sleepMs);
			}
		}
		sequenciaDeEncerramentoGpios();

	}

	/**
	 * Comportamento ticket negado, Da uma indicação sonora e visual de bloqueio e
	 * bloqueia o lado que a pessoa tentar passar
	 * 
	 * @throws Exception
	 */
	// TODO Repensar essa lógica
	private void ticketNegadoPrivate() throws Exception {

		int sleepMsStandard = 300;

		int sleepMs = 100;
		int timer = 0;

		// Sequência de negação
		pictoNegado.setValue("1");
		buzzer.setValue("1");

		while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
			TimeUnit.MILLISECONDS.sleep(sleepMs);
			// a cada 300 ms alternar o buzzer
			timer += 100;

			if (timer == 200 || timer == 600 || timer == 1000) {
				buzzer.setValue("0");
				pictoNegado.setValue("0");
			}

			if (timer == 400 || timer == 800 || timer == 1200) {
				buzzer.setValue("1");
				pictoNegado.setValue("1");
			}

			if (timer >= 1200) {
				break;
			}
		}

		System.out.println("Saindo da sequencia de espera");
		sequenciaDeEncerramentoGpios();

		// Sequência de trancamento
		if (sensorDireita.getValue()) {

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
			while (sensorDireita.getValue()) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}

		} else if (sensorEsquerda.getValue()) {

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
			while (sensorEsquerda.getValue()) {
				pictoNegado.setValue("1");
				buzzer.setValue("1");
			}

		}
		sequenciaDeEncerramentoGpios();

	}

	/**
	 * Giro Liberado para os dois lados
	 * 
	 * @throws Exception
	 */
	
	// TODO Falta ajeitar o fracasso aqui
	private boolean giroAmbosPrivate() throws Exception {
		boolean resultado = false;

		int sleepMs = 100;

		int fracasso = 0;

		String ultimo = null;


		// Laço para dar 3 tentativas
		while (!resultado && fracasso < 3) {
			
			int sensD = 0;
			int sensE = 0;
			int timer = 0;
			boolean fim = false;

			System.out.println("Fracasso: " + fracasso);

			pictoDireita.setValue("1");
			pictoEsquerda.setValue("1");

			// Sequência de espera, 6 segundos
			while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
				TimeUnit.MILLISECONDS.sleep(sleepMs);
				timer += 100;

				if (timer >= 6000) {
					break;
				}
			}

			System.out.println("Saindo da sequencia de espera"); // Essa parte provavelmente vai sair

			if (timer >= 6000) {
				System.out.println("saindo por timer");
				break;
			}

			// limpar picto
			sequenciaDeEncerramentoGpios();

			// Sequência de liberação e contagem de giro
			// Primeiro identifica o lado do giro:

			// Sensor da direita primeiro, significa giro para a esquerda
			if (sensorDireita.getValue()) {

				while (!fim) {

					// Esse início faz a leitura recorrente dos sensores
					if (sensE != sensD) {
						if (sensD > sensE) {
							ultimo = "D";
						}
						if (sensE > sensD) {
							ultimo = "E";
						}
					}

					if (sensorDireita.getValue()) {
						sensD = 1;
					} else {
						sensD = 0;
					}

					if (sensorEsquerda.getValue()) {
						sensE = 1;
					} else {
						sensE = 0;
					}

					if (sensD == 0 && sensE == 0) {
						if (ultimo == "E") {
							resultado = true;
							fim = true;
						} else if (ultimo == "D") {
							resultado = false;
							fim = true;
						}
					}
				}

				// caso identifique o sensor da esquerda primeiro, significa que está
				// dando uma volta para a direita
			} else if (sensorEsquerda.getValue()) {

				// Sequência de liberação de giro para a direita
				while (!fim) {

					// Esse início faz a leitura recorrente dos sensores
					if (sensE != sensD) {
						if (sensD > sensE) {
							ultimo = "D";
						}
						if (sensE > sensD) {
							ultimo = "E";
						}
					}

					if (sensorDireita.getValue()) {
						sensD = 1;
					} else {
						sensD = 0;
					}

					if (sensorEsquerda.getValue()) {
						sensE = 1;
					} else {
						sensE = 0;
					}

					if (sensD == 0 && sensE == 0) {
						if (ultimo == "D") {
							resultado = true;
							fim = true;
						} else if (ultimo == "E") {
							resultado = false;
							fim = true;
						}
					}
				}
			}
			sequenciaDeEncerramentoGpios();

			if (fracasso == 2 && !resultado) {
				System.out.println("Saindo por fracasso");
			}
			fracasso++;

		}
		// Sequencia de encerramento
		sequenciaDeEncerramentoGpios();
		return resultado;
	}

	/**
	 * Giro Liberado para a direita
	 * 
	 * @throws Exception
	 */
	private boolean giroDireitaPrivate() throws Exception {

		boolean resultado = false;
		int sleepMs = 100;
		int sleepMsStandard = 300;
		int fracasso = 0;

		String ultimo = null;

		// Laço para dar 3 tentativas
		while (!resultado && fracasso < 3) {

			int sensD = 0;
			int sensE = 0;
			int timer = 0;
			boolean fim = false;

			System.out.println("Fracasso: " + fracasso);

			timer = 0;
			pictoDireita.setValue("1");

			// Sequência de espera, 6 segundos
			while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
				TimeUnit.MILLISECONDS.sleep(sleepMs);
				timer += 100;

				if (timer >= 6000) {
					break;
				}
			}

			System.out.println("Saindo da sequencia de espera"); // Essa parte provavelmente vai sair

			if (timer >= 6000) {
				System.out.println("saindo por timer");
				break;
			}

			// limpar picto
			sequenciaDeEncerramentoGpios();

			// Sequência de liberação e contagem de giro
			// Primeiro identifica o lado do giro:

			// Sensor da direita primeiro, significa giro para a esquerda
			if (sensorDireita.getValue()) {

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
				while (sensorDireita.getValue()) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}
				resultado = false;

				// caso identifique o sensor da esquerda primeiro, significa que está
				// dando uma volta para a direita
			} else if (sensorEsquerda.getValue()) {

				// Sequência de liberação de giro para a direita

				// TODO colocar um conta high, e caso o lado errado tenha mais highs,
				// da giro incompleto, caso contrário da giro completo

				while (!fim) {

					// Esse início faz a leitura recorrente dos sensores
					if (sensE != sensD) {
						if (sensD > sensE) {
							ultimo = "D";
						}
						if (sensE > sensD) {
							ultimo = "E";
						}
					}

					if (sensorDireita.getValue()) {
						sensD = 1;
					} else {
						sensD = 0;
					}

					if (sensorEsquerda.getValue()) {
						sensE = 1;
					} else {
						sensE = 0;
					}

					if (sensD == 0 && sensE == 0) {
						if (ultimo == "D") {
							resultado = true;
							fim = true;
						} else if (ultimo == "E") {
							resultado = false;
							fim = true;
						}
					}
				}
			}
			sequenciaDeEncerramentoGpios();

			if (fracasso == 2 && !resultado) {
				System.out.println("Saindo por fracasso");
			}
			fracasso++;

		}
		// Sequencia de encerramento
		sequenciaDeEncerramentoGpios();
		return resultado;
	}

	// Testando o giro esquerda
	private boolean giroEsquerdaPrivate() throws Exception {

		boolean resultado = false;
		int sleepMs = 100;
		int fracasso = 0;
		String ultimo = null;

		int sleepMsStandard = 300;

		// Laço para dar 3 tentativas
		while (!resultado && fracasso < 3) {

			int sensD = 0;
			int sensE = 0;
			int timer = 0;
			boolean fim = false;

			System.out.println("Fracasso: " + fracasso);

			pictoEsquerda.setValue("1");

			// Sequência de espera, 6 segundos
			while (sensorDireita.getValue() == sensorEsquerda.getValue()) {
				TimeUnit.MILLISECONDS.sleep(sleepMs);
				timer += 100;
				if (timer >= 6000) {
					break;
				}
			}

			System.out.println("Saindo da sequencia de espera");

			if (timer >= 6000) {
				System.out.println("saindo por timer");
				break;
			}

			// limpar picto
			sequenciaDeEncerramentoGpios();

			// Sequência de liberação e contagem de giro
			// Primeiro identifica o lado do giro:

			// Sensor da direita primeiro, significa giro para a esquerda
			if (sensorDireita.getValue()) {

				// Sequência de liberação de giro para a Esquerda

				while (!fim) {

					// Esse início faz a leitura recorrente dos sensores
					if (sensE != sensD) {
						if (sensD > sensE) {
							ultimo = "D";
						}
						if (sensE > sensD) {
							ultimo = "E";
						}
					}

					if (sensorDireita.getValue()) {
						sensD = 1;
					} else {
						sensD = 0;
					}

					if (sensorEsquerda.getValue()) {
						sensE = 1;
					} else {
						sensE = 0;
					}

					if (sensD == 0 && sensE == 0) {
						if (ultimo == "E") {
							resultado = true;
							fim = true;
						} else if (ultimo == "D") {
							resultado = false;
							fim = true;
						}
					}
				}

				// caso identifique o sensor da esquerda primeiro, significa que está
				// dando uma volta para a direita
			} else if (sensorEsquerda.getValue()) {

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
				while (sensorEsquerda.getValue()) {
					pictoNegado.setValue("1");
					buzzer.setValue("1");
				}
				resultado = false;
			}
			sequenciaDeEncerramentoGpios();

			if (fracasso == 2 && !resultado) {
				System.out.println("Saindo por fracasso");
			}
			fracasso++;
		}
		// Sequencia de encerramento
		sequenciaDeEncerramentoGpios();
		return resultado;

	}

}
