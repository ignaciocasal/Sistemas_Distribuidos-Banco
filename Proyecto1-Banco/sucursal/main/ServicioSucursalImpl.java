package main;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class ServicioSucursalImpl extends UnicastRemoteObject implements ServicioSucursal {

	Integer nroPuerto;
	String IP;
	Registry registro;
	ServicioBusqueda rmiBusqueda;
	
	
	
	public ServicioSucursalImpl(Integer nroPuertoRemotoSucursal, Integer nroPuertoRemotoBusqueda, String IPSv) throws RemoteException {
		
		if (IPSv.equals("")) {
			try {
				this.IP = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				System.out.println("UnknownHostException");
			}
		}else{
			this.IP = IPSv;
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
		try {
			registro = LocateRegistry.getRegistry(IP,nroPuertoRemotoBusqueda);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//creando el objeto remoto
		try {
			rmiBusqueda = (ServicioBusqueda)
					(registro.lookup("rmiBusqueda"));
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		System.out.println("El usuario "+dni+" ha consultado su saldo");
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
		System.out.println("El usuario "+dni+" ha depositado $"+dinero+" en su cuenta");
		return rta;
	}


	@Override
	public boolean depositarDineroCuenta(String dni, Float dinero, Integer nroCuenta) throws RemoteException {
		if (rmiBusqueda.depositarDineroCuenta(dni, dinero, nroCuenta)) {
			System.out.println("El usuario "+dni+" ha depositado $"+dinero+" en la cuenta N°"+nroCuenta);
			return true; //correcto
		}else {
			return false; //error
		}
	}


	@Override
	public Respuesta extraerDinero(String dni, Float dinero) throws RemoteException {
		System.out.println("El usuario "+dni+" ha extraído $"+dinero);
		return rmiBusqueda.extraerDinero(dni, dinero);
	}


	@Override
	public Integer transferirDinero(String dni, Float dinero, String cbu) throws SQLException, RemoteException {
		Integer codError;
		if (rmiBusqueda.existeCbu(cbu)) {
			codError = rmiBusqueda.transferirDinero(dni, dinero, cbu);
			System.out.println("El usuario "+dni+" ha transferido $"+dinero+" a la cuenta "+cbu);
		}else {
			codError = 3; //cuenta no existente
		}
		return codError;
	}



	

}
