
/*
 *  GpioController
 *  
 *  Esta classe traduz os comandos para comandos entendíveis pelo terminal linux
 * 
 */


package controllers;

public class GpioController {
	
	public int gpio;
	public String direction;
	public String value;
	boolean valueHigh;
	
	private ShellController shellController;
	
	public GpioController(int gpio) {
//		super();
		
		this.shellController = new ShellController();
		
		this.gpio = gpio;
		
		try {
			this.shellController.executeCommand("/bin/echo " + gpio + " > /sys/class/gpio/export");
		} catch (Exception ex){
			String msg = ex.getMessage(); // → dps print isso
			System.out.println("Exception: "+ msg);
		}
	}

	
	public String getDirection() throws Exception {
		try{
			this.direction = this.shellController.readLine("cat /sys/class/gpio/gpio" + this.gpio + "/direction");
		} catch (Exception ex){
				String msg = ex.getMessage(); // → dps print isso
				System.out.println("Exception: "+ msg);
		}
		return direction;
	}
	
	public void setDirection(String direction) throws Exception {
		try {
		this.shellController.executeCommand("/bin/echo " + direction + " > /sys/class/gpio/gpio" + this.gpio + "/direction");
		} catch (Exception ex){
			String msg = ex.getMessage(); // → dps print isso
			System.out.println("Exception: "+ msg);
	}
		this.direction = direction;
	}
	
	public boolean getValue() throws Exception {
		try{
			this.value = this.shellController.readLine("cat /sys/class/gpio/gpio" + this.gpio + "/value");
		} catch (Exception ex){
			String msg = ex.getMessage(); // → dps print isso
			System.out.println("Exception: "+ msg);
		}
		
		if(Integer.parseInt(this.value) == 1) {
			valueHigh = true;
		} else {
			valueHigh = false;
		}
		
		return valueHigh;
	}
	
	public void setValue(String value) throws Exception {
		try{
			this.shellController.executeCommand("/bin/echo " + value + " > /sys/class/gpio/gpio" + this.gpio + "/value");
		} catch (Exception ex){
			String msg = ex.getMessage(); // → dps print isso
			System.out.println("Exception: "+ msg);
	}
		this.value = value;
	}
	
// Essa parte é para teste da classe	
	
//	public void testaClasse(String testa) throws Exception {
//		String leitura = this.shellController.readLine("ls");
//		System.out.println("Do teste: " + leitura + " o testa " + testa);
//		
//		this.shellController.executeCommand("echo 20 > /home/carneiro/Documentos/guarda.txt");
//	}
}
