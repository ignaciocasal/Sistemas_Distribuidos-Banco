package main;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class ServicioSucursalImpl extends UnicastRemoteObject implements ServicioSucursal {

	Integer nroPuerto;
	String IP;
	Registry registro;
	ServicioBusqueda rmiBusqueda;
	
	
	protected ServicioSucursalImpl(Integer nroPuertoRemotoSucursal, Integer nroPuertoRemotoBusqueda) throws RemoteException, NotBoundException {
		
		try {
			IP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			System.out.println("No se encontró el servidor");
		}
		
		nroPuerto = nroPuertoRemotoSucursal;
		try {
			registro = LocateRegistry.createRegistry(nroPuerto); //crea el registro
			registro.rebind("rmiServidor", this); //registra el servicio
		} catch (AccessException e) {
			System.out.println("Error al registrar el servicio - AccessException");
		} catch (RemoteException e) {
			System.out.println("Error al registrar el servicio - RemoteException");
		} 
		System.out.println("Servidor: "+IP);
		
		//DATABASE - RMI BUSQUEDA 
		//Obtener registro
		registro = LocateRegistry.getRegistry(IP,nroPuertoRemotoBusqueda);
		//creando el objeto remoto
		rmiBusqueda = (ServicioBusqueda)
				(registro.lookup("rmiBusqueda"));

	}
	

	
	@Override
	public boolean ingresarAlSistema(String dni, String clave) throws RemoteException {
		boolean valido = rmiBusqueda.login(dni,clave);
		if (valido == true) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean existeCuenta(Integer nroCuenta) throws RemoteException {
		boolean result = rmiBusqueda.existeCuenta(nroCuenta);
		return result;
	}
	
	
	@Override
	public Respuesta consultarDinero(String dni) throws RemoteException {
		Respuesta rta = new Respuesta();
		rta.valor =  rmiBusqueda.consultarDinero(dni);
		if(rta.valor == null) {
			rta.codError = 1;
		} 
		//En cliente: Si llega codError null todo bien SINO ver cual es el error
		return rta;
	}

	@Override
	public Respuesta depositarDinero(String dni, Float dinero) throws RemoteException {
		Respuesta rta = new Respuesta();
		rta.valor = rmiBusqueda.depositarDinero(dni, dinero);
		if (rta.valor == null) {
			rta.codError = 1; //No pudo depositar
		} 
		return rta;
	}


	@Override
	public boolean depositarDineroCuenta(String dni, Float dinero, Integer nroCuenta) throws RemoteException {
		if (rmiBusqueda.depositarDineroCuenta(dni, dinero, nroCuenta)) {
			return true;
		}else {
			return false;
		}
	}


	@Override
	public Respuesta extraerDinero(String dni, Float dinero) throws RemoteException {
		return rmiBusqueda.extraerDinero(dni, dinero);
	}


	@Override
	public Respuesta transferirDinero(String dni, Float dinero, String CBU) throws RemoteException {
		Respuesta rta = new Respuesta();
		if (rmiBusqueda.existeCbu(CBU)) {
			rta = rmiBusqueda.extraerDinero(dni, dinero);
			if (rta.codError == null) {
				rta.codError = rmiBusqueda.depositarDineroPorCbu(CBU, dinero);
			}
		}else {
			rta.codError = 3;
		}
		return rta;
	}



	

}
