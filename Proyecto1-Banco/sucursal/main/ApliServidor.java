package main;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ApliServidor {

	public static void main(String[] args) {
		
		try {
			ServicioBusquedaImpl serverDB = new ServicioBusquedaImpl(7560);
			ServicioSucursalImpl server = new ServicioSucursalImpl(7559, 7560,"");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
