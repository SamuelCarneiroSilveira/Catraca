package com.catraca_gpio.eventos;

import java.io.IOException;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import com.catraca_gpio.controller.GateController;
import com.catraca_gpio.controller.ShellController;
import com.catraca_gpio.enums.Comportamento;
import com.catraca_gpio.enums.Giro;

public class TesteMainEnums {

	public static void main(String[] args) throws Exception {

		GateController gate;
		Scanner input = new Scanner(System.in);
		Comportamento comportamento = null;
		Giro giro = null;

		int temporaria = 0;
		int temporariaInterna = 0;
		boolean exit = false;
		String textoComportamentoAtual = "nenhum";
		String textoGiroAtual = "nenhum";

		// Sequência de inicio

		while (!exit) {

			limpaTela();

			menuInicialComportamento();
			temporaria = input.nextInt();

			switch (temporaria) {
			case 1:
				comportamento = Comportamento.NENHUM_LIVRE;
				textoComportamentoAtual = "NENHUM_LIVRE";
				exit = true;
				break;
			case 2:
				comportamento = Comportamento.DIREITA_LIVRE;
				textoComportamentoAtual = "DIREITA_LIVRE";
				exit = true;
				break;
			case 3:
				comportamento = Comportamento.ESQUERDA_LIVRE;
				textoComportamentoAtual = "ESQUERDA_LIVRE";
				exit = true;
				break;
			default:
				System.out.println("Escolha uma opção válida");
				TimeUnit.SECONDS.sleep(1);
				break;
			}
			limpaTela();
		}

		temporaria = 0;
		exit = false;

		while (!exit) {

			menuInicialGiro();
			temporaria = input.nextInt();

			switch (temporaria) {
			case 1:
				giro = Giro.AMBOS;
				textoGiroAtual = "AMBOS";
				exit = true;
				break;
			case 2:
				giro = Giro.DIREITA;
				textoGiroAtual = "DIREITA";
				exit = true;
				break;
			case 3:
				giro = Giro.ESQUERDA;
				textoGiroAtual = "ESQUERDA";
				exit = true;
				break;
			default:
				System.out.println("Escolha uma opção válida");
				TimeUnit.SECONDS.sleep(1);
				break;
			}
			limpaTela();
		}

		temporaria = 0;
		exit = false;

		gate = new GateController(comportamento, giro);

		// Loop principal

		while (temporaria != 7) {

			exit = false;
			temporariaInterna = 0;

			menuPrincipal(textoComportamentoAtual, textoGiroAtual);
			temporaria = input.nextInt();
			limpaTela();

			switch (temporaria) {

			case 1:
				System.out.println("Modo giro: " + textoGiroAtual);
				for (int i = 0; i <= 1; i++) {
					if(i == 1) {
						System.out.println("tentando mais uma vez");
					}
					if (gate.Giro()) {
						System.out.println("Giro completo!");
						TimeUnit.SECONDS.sleep(1);
						limpaTela();
					} else {
						System.out.println("Giro falhou!");
						TimeUnit.SECONDS.sleep(1);
						limpaTela();
					}
				}

				break;
			case 2:
				System.out.println("Modo ticket negado");
				for (int i = 0; i <= 1; i++) {
					gate.TicketNegado(); // roda 2x
				}
				TimeUnit.SECONDS.sleep(1);
				break;
			case 3:
				System.out.println("Modo comportamento: " + textoComportamentoAtual); // roda 2x
				for (int i = 0; i <= 1; i++) {
					gate.standard();
				}
				break;
			case 4:
				System.out.println("Alterar comportamento atual");

				while (!exit) {
					menuAlterarComportamento();
					temporariaInterna = input.nextInt();
					limpaTela();

					switch (temporariaInterna) {
					case 1:
						gate.setComportamento(Comportamento.NENHUM_LIVRE);
						textoComportamentoAtual = "NENHUM_LIVRE";
						exit = true;
						break;
					case 2:
						gate.setComportamento(Comportamento.DIREITA_LIVRE);
						textoComportamentoAtual = "DIREITA_LIVRE";
						exit = true;
						break;
					case 3:
						gate.setComportamento(Comportamento.ESQUERDA_LIVRE);
						textoComportamentoAtual = "ESQUERDA_LIVRE";
						exit = true;
						break;
					default:
						System.out.println("Selecione uma opção válida");
						TimeUnit.SECONDS.sleep(1);
						break;
					}
				}
				break;
			case 5:
				System.out.println("Alterar giro atual");
				while (!exit) {
					menuAlterarGiro();
					temporariaInterna = input.nextInt();
					limpaTela();

					switch (temporariaInterna) {
					case 1:
						gate.setGiro(Giro.AMBOS);
						textoGiroAtual = "AMBOS";
						exit = true;
						break;
					case 2:
						gate.setGiro(Giro.DIREITA);
						textoGiroAtual = "DIREITA";
						exit = true;
						break;
					case 3:
						gate.setGiro(Giro.ESQUERDA);
						textoGiroAtual = "ESQUERDA";
						exit = true;
						break;
					default:
						System.out.println("Selecione uma opção válida");
						TimeUnit.SECONDS.sleep(1);
						break;
					}
				}
				exit = false;
				break;
			case 6:
				System.out.println("Testar a Placa");
				gate.testaPlaca();
				break;

			default:
				System.out.println("Informe uma opção válida");
				TimeUnit.SECONDS.sleep(1);
				break;
			}
			limpaTela();
		}
		gate.sequenciaDeEncerramentoGpios(); // mostrar essa aqui para o wesley
		input.close();
	}

	/**
	 * Limpa o prompt no windows e em sistemas unix-like
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void limpaTela() throws IOException, InterruptedException {

		ShellController shell = new ShellController();
		shell.executeCommand("clear");
	}

	public static void menuInicialComportamento() {

		System.out.println("------------------||  MENU INCIAL DE COMPORTAMENTO  ||------------------");
		System.out.println("");
		System.out.println("Escolha o comportamento padrão de funcionamento");
		System.out.println("");
		System.out.println("1 - Bloqueia entradas não autorizadas para Ambos os lados");
		System.out.println("2 - Libera entradas não autorizadas para a Direita");
		System.out.println("3 - Libera entradas não autorizadas para a Esquerda");

	}

	private static void menuInicialGiro() {
		
		System.out.println("------------------||  MENU SETUP DE GIRO  ||------------------");
		System.out.println("");
		System.out.println("Escolha o comportamento padrão de giro");
		System.out.println("");
		System.out.println("1 - Autoriza giro para os dois lados                 AMBOS()");
		System.out.println("2 - Autoriza giro para a direita                   DIREITA()");
		System.out.println("3 - Autoriza giro para a esquerda                 ESQUERDA()");
                                                                             
	}

	private static void menuPrincipal(String ComportamentoAtual, String GiroAtual) {

		System.out.println("------------------||  MENU PRINCIPAL  ||------------------");
		System.out.println("");
		System.out.println("1 - Ticket Autorizado                                 Giro()");
		System.out.println("2 - Ticket Negado                             TicketNegado()");
		System.out.println("3 - Padrão                                        standard()");
		System.out.println("4 - Altera padrão de funcionamento        setComportamento()");
		System.out.println("5 - Alterar padrão de giro                         setGiro()");
		System.out.println("6 - Testar a placa                              testaPlaca()");
		System.out.println("7 - Sair");
		System.out.println("");
		System.out.println("--------------------||   Modo Atual  ||--------------------");
		System.out.println("Comportamento padrão atual: " + ComportamentoAtual);
		System.out.println("Modo de giro atual: " + GiroAtual);

	}

	private static void menuAlterarComportamento() {
		System.out.println("------------------||  MENU ALTERAR COMPORTAMENTO ||------------------");
		System.out.println("");
		System.out.println("1 - Bloqueia entradas não autorizadas para os dois lados ");
		System.out.println("2 - Bloqueia entradas não autorizadas para a esquerda");
		System.out.println("3 - Bloqueia entradas não autorizadas para a direita");
	}

	private static void menuAlterarGiro() {
		System.out.println("--------------||  MENU ALTERAR PADRÃO DE GIRO ||--------------");
		System.out.println("");
		System.out.println("1 - Autoriza giro para os dois lados                 AMBOS()");
		System.out.println("2 - Autoriza giro para a direita                   DIREITA()");
		System.out.println("3 - Autoriza giro para a esquerda                 ESQUERDA()");
	}
}

// TODO Remover timmers no meio das leituras pra agilizar a leitura 

