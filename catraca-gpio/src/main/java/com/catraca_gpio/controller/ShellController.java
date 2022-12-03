/*
 * Esta classe tem duas funções
 * 
 * executeCommand, concatena a string comando recebida com o código 
 * necessário para a execução no terminal e executa.
 * 
 * exemplo:
 * shellController.executeCommand("/bin/echo "+direction+">/sys/class/gpio/gpio"+this.gpio +"/direction");
 * 
 * 
 * readLine, a função readLine é praticamente igual a executeCommand, 
 * a diferênça é que ela retorna a primeira string que aparecer no terminal,
 * ou seja, para usar ela para ler algo específico, ainda é necessário o uso
 * do comando cat, seguido do que quer ser lido.
 * 
 * como por exemplo: 
 * shellController.readLine("cat /sys/class/gpio/gpio" + this.gpio + "/direction");
 */



package com.catraca_gpio.controller;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
		
public class ShellController {

	/*
	 * Exemplo de como usar o comando para printar o resultado de um ls
	 * Para um resultado completo é necessário substituir o while onde se pede
	 * 
	 *	public static void main(String[] args) throws IOException {
	 *		final ShellController bora = new ShellController();
	 *		String util = bora.readLine("ls /home/carneiro");
	 *		System.out.println("O primeiro elemento da pasta é " +util);
	 *	}
	 */
		

	public String retorno = "";

	public String readLine(final String command) throws IOException {

		final ArrayList<String> commands = new ArrayList<String>();

		commands.add("/bin/bash");
		commands.add("-c");
		commands.add(command);
		BufferedReader br = null;

		// Retorna a ultima String
		try {
			final ProcessBuilder p = new ProcessBuilder(commands);
			final Process process = p.start();
			final InputStream is = process.getInputStream();
			final InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;
			
			if((line = br.readLine()) != null) {
				// System.out.println(line); //printa a linha no terminal
				retorno = line;
			} else {
				retorno = null;
			}
			/*
			 *  Para ler todos os elementos da pasta em vez do if/else acima pode-se usar
			 *			while ((line = br.readLine()) != null) {
			 *				System.out.println(line);
			 *			} 
			 */			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			secureClose(br);
		}
		
		return retorno;
		
	}

	/*
	 * 	Exemplo de uso desta função
	 * 
	 * 	public static void main(String[] args) throws IOException {
	 *		final ShellController bora = new ShellController();
	 *		bora.executeCommand("ls /home/carneiro/Documentos");		
	 *	}
	 */
	
	public void executeCommand(final String command) throws IOException {

		final ArrayList<String> commands = new ArrayList<String>();

		commands.add("/bin/bash");
		commands.add("-c");
		commands.add(command);
		BufferedReader br = null;
		try {

			final ProcessBuilder p = new ProcessBuilder(commands);
			final Process process = p.start();
			final InputStream is = process.getInputStream();
			final InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;

			// Imprime resultado/erro caso tenha algum
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			secureClose(br);
		}
				
	}	
	
	private void secureClose(final Closeable resource) {
		try {
			if (resource != null) {
				resource.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

