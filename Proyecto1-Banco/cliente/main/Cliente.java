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
	
	void start() throws RemoteException{
		System.out.println("Ingrese su DNI:");
		Integer intDni = this.ingresarInteger();
		if (intDni==null) {
				this.start();		
		}else{
		String dni = intDni.toString();
		System.out.println("Ingrese su clave:");
		String clave =  teclado.nextLine();
		this.iniciarSesion(dni, clave);
		}
	}
	
	void iniciarSesion(String dni, String clave) throws RemoteException{
		boolean existe = rmiServidor.ingresarAlSistema(dni,clave);
		if (existe == true) {
			this.dni=dni;
			this.mostrarMenu();
		}else {
			System.out.println("Usuario o contrase�a incorrectos");
			this.start();
		}
	}
	
	private Integer ingresarInteger() throws RemoteException{
		Integer ingreso = null;
		try { //consistencia para que ingrese solo integer
//			ingreso = teclado.nextInt();
			String nextIntString = teclado.nextLine(); //get the number as a single line
			ingreso = Integer.parseInt(nextIntString); //convert the string to an int
		} catch (Exception e) {
			System.out.println("Ingreso inv�lido");
		}
		return ingreso;
	}
	
	private Float ingresarFloat() throws RemoteException{
		Float ingreso = null;
		try { //consistencia para que ingrese solo float
//			ingreso = teclado.nextFloat();
			String nextIntString = teclado.nextLine(); //get the number as a single line
			ingreso = Float.parseFloat(nextIntString); //convert the string to an int
		} catch (Exception e) {
			System.out.println("Ingreso inv�lido");
		}
		return ingreso;
	}
	
	private void cierreOperacion() throws RemoteException{
		System.out.println("Seleccione una opcion:");
		System.out.println("1 - Cerrar sesi�n");
		System.out.println("2 - Volver al men� principal");
		Integer ingreso = this.ingresarInteger();
		if (ingreso==null) {
			this.cierreOperacion();
		}else{
			switch (ingreso) {
			case 1:
				System.out.println("-- SESION FINALIZADA --");
				System.exit(0); 
				break;
			case 2:
				this.mostrarMenu();
				break;

			default:
				System.out.println("Opci�n inv�lida.");
				this.cierreOperacion();
				break;
			}
		}
	}
	
	private void mostrarMenu() throws RemoteException {
		
		System.out.println("Seleccione una opcion del menu:");
		System.out.println("1 - Consultar saldo");
		System.out.println("2 - Depositar dinero en cuenta propia");
		System.out.println("3 - Depositar dinero en otra cuenta");
		System.out.println("4 - Extraer dinero");
		System.out.println("5 - Transferir dinero a una cuenta");
		System.out.println("6 - Cerrar sesi�n");
		
		Integer ingreso = this.ingresarInteger();
		if (ingreso==null) {
			this.mostrarMenu();
		}else{
		
		Respuesta res = null;
		Float dinero = null;
		Integer nroCuentaDeposito = null;
		
//		teclado.close();
		switch (ingreso) {
		case 1: //Consultar saldo
			System.out.println("Opci�n 1.");
			res = rmiServidor.consultarDinero(this.dni);
			if (res.codError == null) {
				System.out.println("Su saldo actual es de $"+res.valor);
			}else {
				System.out.println("Error al realizar la operaci�n. Intente nuevamente");
			}
			this.cierreOperacion();
			break;
		case 2: //Depositar dinero en cuenta propia
			System.out.println("Opci�n 2.");
			System.out.println("Ingrese el monto a depositar:");
			dinero = this.ingresarFloat();
			if (dinero==null) {
				this.mostrarMenu();
			}else{
				res = rmiServidor.depositarDinero(this.dni, dinero);
					if (res.codError == null) {
						System.out.println("Su saldo actual es de $"+res.valor);
					} else {
						System.out.println("Error al realizar la operaci�n. Intente nuevamente");
					}
			this.cierreOperacion();
			// SI TE PARECE ac� reutilizar c�digo, es mas o menos parecido lo que hacemos con la respuesta
			}
			break;
		case 3: //Depositar dinero en otra cuenta
			System.out.println("Opci�n 3.");
			System.out.println("Ingrese el nro de cuenta en la que desea depositar:");
			nroCuentaDeposito = this.ingresarInteger();
			if (nroCuentaDeposito==null) { //Si no ingreso nro cuenta
				this.mostrarMenu();
			}else{
				if (rmiServidor.existeCuenta(nroCuentaDeposito) == false) {
					//Si ingreso nroCuenta pero no existe
					this.mostrarMenu();
				}else {
					System.out.println("Existe");
					System.out.println("Ingrese el monto a depositar:");
					dinero = this.ingresarFloat();
					if (dinero==null) {
						this.mostrarMenu();
					}else{
					 if (rmiServidor.depositarDineroCuenta(this.dni, dinero, nroCuentaDeposito)) {
						 System.out.println("Se realiz� el doposito");
						 this.mostrarMenu();
					 } else {
						 System.out.println("No se realizo");
						 this.mostrarMenu();
					 }
					
				}
				
				}
			}
			break;
		case 4: //Extraer dinero
			System.out.println("Opci�n 4.");
			break;
		case 5: //Transferir dinero a una cuenta
			System.out.println("Opci�n 5.");
			break;
		case 6:
			System.out.println("-- SESION FINALIZADA --");
			System.exit(0); 
			break;
		default: //Otra opci�n
			System.out.println("Opci�n inv�lida.");
			this.mostrarMenu();
			break;
		}
		}
	}
	
	void testear() throws RemoteException{
		rmiServidor.test();
	}

	
	
}
