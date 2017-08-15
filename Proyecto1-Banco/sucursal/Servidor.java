import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
	public String ingresarAlSistema(String dni, String clave) throws RemoteException {
		return null;
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String consultarDinero() throws RemoteException {
		String dineroDisponible = "";
		//dineroDisponible = Realizar consulta;
		
		return dineroDisponible;
	}

	@Override
	public String depositarDinero(Float dinero) throws RemoteException {
		Boolean ok = true;
		if (ok) {
			return "Se ha depositado el dinero en la cuenta";
		} else {
			return "No se pudo realizar operación";
		}
	}


	@Override
	public String depositarDineroCuenta(Float dinero, Integer nroCuenta) throws RemoteException {
		// TODO Auto-generated method stub
		return null;

	}


	@Override
	public String extraerDinero(Float dinero) throws RemoteException {
		return null;
		// TODO Auto-generated method stub
		
	}


	@Override
	public String transferirDinero(Float dinero, String CBU) throws RemoteException {
		return null;
		// TODO Auto-generated method stub
		
	}


	

}
