import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

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
		//if (rmiServidor.ingresarAlSistema(dni, clave)) {
	}

	
	
}
