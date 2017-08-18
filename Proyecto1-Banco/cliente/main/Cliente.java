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
			this.mostrarMenu();
		}else {
		System.out.println("No se encontró");
		}
	}
	
	private void mostrarMenu() {
		Scanner teclado = new Scanner(System.in);
		System.out.println("Seleccione una opcion del menu:");
		System.out.println("1 - Consultar saldo");
		System.out.println("2 - Depositar dinero en cuenta propia");
		System.out.println("3 - Depositar dinero en otra cuenta");
		System.out.println("4 - Extraer dinero");
		System.out.println("5 - Transferir dinero a una cuenta");
		
		int ingreso = 0;
		try { //consistencia para que ingrese solo integer
			ingreso = teclado.nextInt();
		} catch (Exception e) {
			System.out.println("Ingreso inválido");
			this.mostrarMenu();
		}
		
//		teclado.close();
		switch (ingreso) {
		case 1: //Consultar saldo
			System.out.println("Opción 1.");
			break;
		case 2: //Depositar dinero en cuenta propia
			System.out.println("Opción 2.");
			break;
		case 3: //Depositar dinero en otra cuenta
			System.out.println("Opción 3.");
			break;
		case 4: //Extraer dinero
			System.out.println("Opción 4.");
			break;
		case 5: //Transferir dinero a una cuenta
			System.out.println("Opción 5.");
			break;
		default: //Otra opción
			System.out.println("Opción inválida.");
			this.mostrarMenu();
			break;
		}
	}
	
	void testear() throws RemoteException{
		rmiServidor.test();
	}

	
	
}
