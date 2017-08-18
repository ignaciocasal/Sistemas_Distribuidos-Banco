package modelo_db;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import main.Cuenta;

public final class DataAccess {

	private DataAccess() {

	}

	public static void bajarTabla() {
		String[][] usuarios = new String[6][6];
		final String consulta = "SELECT * FROM  users";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			int i=0;
			while (res.next()) {
				System.out.println("FILA "+i);
				int j=0;
				System.out.println();
				usuarios[i][++j]  = res.getString("dni");
				System.out.println(usuarios[i][j]);
				usuarios[i][++j]  = res.getString("nombre");
				System.out.println(usuarios[i][j]);
				usuarios[i][++j]  = res.getString("apellido");
				System.out.println(usuarios[i][j]);
//				cuentas[i][++j]  = res.getString("cbu");
//				System.out.println(cuentas[i][j]);
//				cuentas[i][++j]  = res.getString("dni_cliente");
//				System.out.println(cuentas[i][j]);
				i++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static boolean login(String dni, String clave) {
		// tienen que ir los '' por que postgres necesita compara con String
		final String consulta = "SELECT * FROM  cuentas WHERE cuentas.dni_cliente = '"+dni+"' "
				+ "and cuentas.clave = '"+clave+"'";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			int i=0;
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
	
	public static Float consultarDinero(String dni) {
		final String consulta = "SELECT cuentas.saldo FROM cuentas WHERE cuentas.dni_cliente = '"+dni+"'";
		Float result;
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			int i=0;
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
	
	

}
