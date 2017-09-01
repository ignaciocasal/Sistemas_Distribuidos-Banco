package main;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface ServicioBusqueda extends Remote {
	
	
	//Este es el metodo que implementara el servidor
	boolean existeCuenta(Integer nroCuenta) throws RemoteException;
	boolean login(String dni, String clave) throws RemoteException;
	Float consultarDinero(String dni) throws RemoteException;
	Float depositarDinero(String dni, Float dinero) throws RemoteException;
	boolean depositarDineroCuenta(String dni, Float dinero, Integer nroCuenta) throws RemoteException;
	Respuesta extraerDinero(String dni, Float dinero) throws RemoteException;
	boolean existeCbu(String cbu) throws RemoteException;
	Integer depositarDineroPorCbu(String cbu, Float dinero) throws RemoteException;
	Integer transferirDinero(String dni, Float dinero, String cbu) throws RemoteException,SQLException;
	
	
}
