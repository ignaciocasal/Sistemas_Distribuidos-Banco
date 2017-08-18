package main;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import main.InterfazReceptorMensajes;

public class Cliente {

	private InterfazReceptorMensajes rmiServidor;
	private Registry registro;
	private String direccionServidor;
	private Integer puertoServidor;
	
	private String dni;
	private String nombre;
	private String apellido;
	private String cbu;
	private Integer nroCuenta;
	private Float saldo;
	
	Scanner teclado = new Scanner(System.in);
	
	
	public Cliente (String ip, Integer puerto) throws RemoteException, NotBoundException {
		direccionServidor =ip;
		puertoServidor = puerto;
		this.conectarseAlServidor();
	}
	
	public void conectarseAlServidor() throws RemoteException, NotBoundException {
		//Obtener registro
		registro = LocateRegistry.getRegistry(direccionServidor,puertoServidor);
		//creando el objeto remoto
		rmiServidor = (InterfazReceptorMensajes)
				(registro.lookup("rmiServidor"));
	}
	
	void iniciarSesion(String dni, String clave) throws RemoteException{
		boolean existe = rmiServidor.ingresarAlSistema(dni,clave);
		if (existe == true) {
			this.dni=dni;
			this.mostrarMenu();
		}else {
		System.out.println("No se encontró");
		}
	}
	
	private Integer ingresarInteger() throws RemoteException{
		Integer ingreso = 0;
		try { //consistencia para que ingrese solo integer
//			ingreso = teclado.nextInt();
			String nextIntString = teclado.nextLine(); //get the number as a single line
			ingreso = Integer.parseInt(nextIntString); //convert the string to an int
		} catch (Exception e) {
			System.out.println("Ingreso inválido");
			this.mostrarMenu();
		}
		return ingreso;
	}
	
	private Float ingresarFloat() throws RemoteException{
		Float ingreso = (float) 0.0;
		try { //consistencia para que ingrese solo float
//			ingreso = teclado.nextFloat();
			String nextIntString = teclado.nextLine(); //get the number as a single line
			ingreso = Float.parseFloat(nextIntString); //convert the string to an int
		} catch (Exception e) {
			System.out.println("Ingreso inválido");
			this.mostrarMenu();
		}
		return ingreso;
	}
	
	private void mostrarMenu() throws RemoteException {
		
		System.out.println("Seleccione una opcion del menu:");
		System.out.println("1 - Consultar saldo");
		System.out.println("2 - Depositar dinero en cuenta propia");
		System.out.println("3 - Depositar dinero en otra cuenta");
		System.out.println("4 - Extraer dinero");
		System.out.println("5 - Transferir dinero a una cuenta");
		
		Integer ingreso = this.ingresarInteger();
		
		String res = null;
		Float dinero = null;
		Integer nroCuentaDeposito = null;
		
//		teclado.close();
		switch (ingreso) {
		case 1: //Consultar saldo
			System.out.println("Opción 1.");
			res = rmiServidor.consultarDinero(this.dni);
			System.out.println(res);
			break;
		case 2: //Depositar dinero en cuenta propia
			System.out.println("Opción 2.");
			System.out.println("Ingrese el monto a depositar:");
			dinero = this.ingresarFloat();
			res = rmiServidor.depositarDinero(this.dni, dinero);
			System.out.println(res);
			break;
		case 3: //Depositar dinero en otra cuenta
			System.out.println("Opción 3.");
			System.out.println("Ingrese el nro de cuenta en la que desea depositar:");
			nroCuentaDeposito = this.ingresarInteger();
			System.out.println("Ingrese el monto a depositar:");
			dinero = this.ingresarFloat();
			res = rmiServidor.depositarDineroCuenta(this.dni, dinero, nroCuentaDeposito);
			System.out.println(res);
			break;
		case 4: //Extraer dinero
			System.out.println("Opción 4.");
			break;
		case 5: //Transferir dinero a una cuenta
			System.out.println("Opción 5.");
			break;
		default: //Otra opción
			System.out.println("Opción inválida."+ingreso);
			this.mostrarMenu();
			break;
		}
	}
	
	void testear() throws RemoteException{
		rmiServidor.test();
	}

	
	
}
