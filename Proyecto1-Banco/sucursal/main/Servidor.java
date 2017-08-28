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
	public boolean existeCuenta(Integer nroCuenta) {
		boolean result = DataAccess.existeCuenta(nroCuenta);
		return result;
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
	public boolean depositarDineroCuenta(String dni, Float dinero, Integer nroCuenta) throws RemoteException {
		if (DataAccess.depositarDineroCuenta(dni, dinero, nroCuenta)) {
			return true;
		}else {
			return false;
		}
	}


	@Override
	public Respuesta extraerDinero(String dni, Float dinero) throws RemoteException {
		return DataAccess.extraerDinero(dni, dinero);
	}


	@Override
	public Respuesta transferirDinero(String dni, Float dinero, String CBU) throws RemoteException {
		Respuesta rta = new Respuesta();
		if (DataAccess.existeCbu(CBU)) {
			rta = DataAccess.extraerDinero(dni, dinero);
			if (rta.codError == null) {
				rta.codError = DataAccess.depositarDineroPorCbu(CBU, dinero);
			}
		}else {
			rta.codError = 3;
		}
		return rta;
	}

	@Override
	public void test() throws RemoteException {
		DataAccess.bajarTabla();
	}


	

}
