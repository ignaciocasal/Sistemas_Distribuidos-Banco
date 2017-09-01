package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class ServicioBusquedaImpl extends UnicastRemoteObject implements ServicioBusqueda {

	Integer nroPuerto;
	String IP;
	Registry registro;
	
	protected ServicioBusquedaImpl(Integer numeroPuertoRemoto) throws RemoteException {
		
		try {
			IP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			System.out.println("No se encontró el servidor");
		}
		
		nroPuerto = numeroPuertoRemoto;
		try {
			registro = LocateRegistry.createRegistry(nroPuerto); //crea el registro
			registro.rebind("rmiBusqueda", this); //registra el servicio
		} catch (AccessException e) {
			System.out.println("Error al registrar el servicio - AccessException");
		} catch (RemoteException e) {
			System.out.println("Error al registrar el servicio - RemoteException");
		} 
		System.out.println("Servidor: "+IP);

	}
	
	public boolean existeCuenta(Integer nroCuenta) {
		final String consulta = "SELECT * FROM  cuentas WHERE cuentas.nro = '"+nroCuenta+"'";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
		
	public boolean login(String dni, String clave) {
		// tienen que ir los '' porque postgres necesita compara con String
		final String consulta = "SELECT * FROM  cuentas WHERE cuentas.dni_cliente = '"+dni+"' "
				+ "and cuentas.clave = '"+clave+"'";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				System.out.println("Usuario "+dni+" ha iniciado sesión");
				return true;
			}
			
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Float consultarDinero(String dni) {
		final String consulta = "SELECT cuentas.saldo FROM cuentas WHERE cuentas.dni_cliente = '"+dni+"'";
		Float result; 
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				result = Float.parseFloat(res.getString("saldo"));
				return result;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Float depositarDinero(String dni, Float dinero) {
		Float saldoActual = this.consultarDinero(dni); // Para checkear si es correcto
		Float saldoAGuardar = saldoActual + dinero;
		final String consulta = "UPDATE cuentas SET saldo = '"+saldoAGuardar+"' WHERE dni_cliente = '"+dni+"'";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			statement.executeUpdate();	
			return saldoAGuardar;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer depositarDineroPorCbu(String cbu, Float dinero) {
		final String consulta = "UPDATE cuentas SET saldo = saldo +'"+dinero+"' WHERE cbu = '"+cbu+"'";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			statement.executeUpdate();	
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1; //Error al realizar consulta
	}
	
	public boolean depositarDineroCuenta(String dni, Float dinero, Integer nroCuenta) {
		final String consulta = "UPDATE cuentas SET saldo = saldo + '"+dinero+"' WHERE nro = '"+nroCuenta+"'";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			statement.executeUpdate();	
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Respuesta extraerDinero(String dni, Float dinero) {
		Respuesta rta = new Respuesta();
		Float saldoActual = this.consultarDinero(dni);
		if (saldoActual<dinero) {
			rta.codError = 2;  //dinero insuficiente
			return rta;
		}
		final String consulta = "UPDATE cuentas SET saldo = saldo - '"+dinero+"' WHERE dni_cliente = '"+dni+"'";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			statement.executeUpdate();	
			saldoActual = this.consultarDinero(dni);
			rta.valor = saldoActual; 
			return rta;		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rta.codError = 1; //error al realizar la consulta
		return rta;
	}
	
	public boolean existeCbu(String cbu) {
		final String consulta;
		consulta = "SELECT * FROM cuentas WHERE cbu = '"+cbu+"'";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public Integer transferirDinero(String dni, Float dinero, String cbu) throws SQLException {
		Integer codError = null;
		Float saldoActual = this.consultarDinero(dni);
		if (saldoActual<dinero) {
			codError = 2;  //dinero insuficiente
			return codError;
		}
		
		Connection c = BaseDeDatos.newConnection();
		
		final String consulta1 = "UPDATE cuentas SET saldo = saldo - '"+dinero+"' WHERE dni_cliente = '"+dni+"'";
		final String consulta2 = "UPDATE cuentas SET saldo = saldo +'"+dinero+"' WHERE cbu = '"+cbu+"'";
		try {
			
			c.setAutoCommit(false);
			
			PreparedStatement statement1 = c.prepareStatement(consulta1);
			statement1.executeUpdate();	
			
			PreparedStatement statement2 = c.prepareStatement(consulta2);
			statement2.executeUpdate();	
			
			c.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			c.rollback();
			codError = 1;
		}
		return codError;
	}
}
