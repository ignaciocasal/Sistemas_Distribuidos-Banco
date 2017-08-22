package main;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import modelo_db.DataAccess;

@SuppressWarnings("serial")
public class Servidor extends UnicastRemoteObject implements InterfazReceptorMensajes {

	Integer nroPuerto;
	String IP;
	Registry registro;
	
	
	protected Servidor(Integer numeroPuertoRemoto) throws RemoteException {
		
		try {
			IP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			System.out.println("No se encontró el servidor");
		}
		
		nroPuerto = numeroPuertoRemoto;
		try {
			registro = LocateRegistry.createRegistry(nroPuerto); //crea el registro
			registro.rebind("rmiServidor", this); //registra el servicio
		} catch (AccessException e) {
			System.out.println("Error al registrar el servicio - AccessException");
		} catch (RemoteException e) {
			System.out.println("Error al registrar el servicio - RemoteException");
		} 
		System.out.println("Servidor: "+IP);

	}
	
	@Override
	public boolean ingresarAlSistema(String dni, String clave) throws RemoteException {
		boolean valido = DataAccess.login(dni,clave);
		if (valido == true) {
			return true;
		}else {
			return false;
		}
		
	}
	
	@Override
	public Respuesta consultarDinero(String dni) throws RemoteException {
		Respuesta rta = new Respuesta();
		rta.valor =  DataAccess.consultarDinero(dni);
		if(rta.valor == null) {
			rta.codError = 1;
		} 
		//En cliente: Si llega codError null todo bien SINO ver cual es el error
		return rta;
	}

	@Override
	public Respuesta depositarDinero(String dni, Float dinero) throws RemoteException {
		Respuesta rta = new Respuesta();
		rta.valor = DataAccess.depositarDinero(dni, dinero);
		if (rta.valor == null) {
			rta.codError = 1; //No pudo depositar
		} 
		return rta;
	}


	@Override
	public Respuesta depositarDineroCuenta(String dni, Float dinero, Integer nroCuenta) throws RemoteException {
		String res = dinero.toString() + " pesos a la cuenta nro " + nroCuenta;
		//return (res);
		return null;
	}


	@Override
	public Respuesta extraerDinero(String dni, Float dinero) throws RemoteException {
		
		//return dinero.toString();
		return null;
	}


	@Override
	public Respuesta transferirDinero(String dni, Float dinero, String CBU) throws RemoteException {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test() throws RemoteException {
		DataAccess.bajarTabla();
	}


	

}
