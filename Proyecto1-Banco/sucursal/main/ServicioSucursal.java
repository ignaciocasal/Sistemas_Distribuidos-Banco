package main;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioSucursal extends Remote {
	
	
	//Este es el metodo que implementara el servidor
	boolean existeCuenta(Integer nroCuenta)throws RemoteException;
	boolean ingresarAlSistema(String dni, String clave) throws RemoteException;
	Respuesta consultarDinero(String dni) throws RemoteException;
	Respuesta depositarDinero(String dni, Float dinero) throws RemoteException;
	boolean depositarDineroCuenta(String dni, Float dinero, Integer nroCuenta) throws RemoteException;
	Respuesta extraerDinero(String dni, Float dinero) throws RemoteException;
	Respuesta transferirDinero(String dni, Float dinero, String cbu) throws RemoteException;
	
}
