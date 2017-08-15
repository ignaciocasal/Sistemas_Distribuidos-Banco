package modelo_db;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public abstract class DataAccess {

	public static void limpiarTabla(JTable Tabla) {
		DefaultTableModel modelo = (DefaultTableModel) Tabla.getModel();
		while (modelo.getRowCount() > 0)
			modelo.removeRow(0);

		// TableColumnModel modCol = Tabla.getColumnModel();
		// while(modCol.getColumnCount()>0)modCol.removeColumn(modCol.getColumn(0));
	}

}
