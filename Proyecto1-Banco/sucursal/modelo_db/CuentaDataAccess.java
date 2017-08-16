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

public final class CuentaDataAccess extends DataAccess {

	private CuentaDataAccess() {

	}

	public static void bajarTabla() {
		String[][] cuentas = new String[6][6];
		final String consulta = "SELECT * FROM cuentas ";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			int i=0;
			while (res.next()) {
				System.out.println("FILA "+i);
				int j=0;
				cuentas[0][0]  = res.getString("nro");
				System.out.println(cuentas[i][j]);
				cuentas[i][++j]  = res.getString("saldo");
				System.out.println(cuentas[i][j]);
				cuentas[i][++j]  = res.getString("clave");
				System.out.println(cuentas[i][j]);
				cuentas[i][++j]  = res.getString("cbu");
				System.out.println(cuentas[i][j]);
				i++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
