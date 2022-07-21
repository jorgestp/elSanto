package prueba.santo.prubaAccesoSanto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import prueba.santo.prubaAccesoSanto.entity.Envasado;
import prueba.santo.prubaAccesoSanto.entity.Fabricacion;
import prueba.santo.prubaAccesoSanto.entity.Formula;

/**
 * Hello world!
 *
 */
public class App {
	static Connection conn = null;
	static String driver = "sun.jdbc.odbc.JdbcOdbcDriver";

	/**
	 * PRODUCCION ROSA
	 */
	private final static String urlDocumentos = "jdbc:ucanaccess://C:\\Users\\USUARIO\\Documents\\elSanto.mdb";
	
	
//	private final static String urlDocumentos = "jdbc:ucanaccess://C:\\Users\\Jorge\\Documents\\trazabilidad\\elSanto.mdb";

	private static final String ENVASADOS_ID = "id_envasado";
	private static final String ENVASADOS_LOTE_ENVASADO = "lote_envasado";
	private static final String ENVASADOS_REF_ENVASADO = "referencia_articulo";
	private static final String ENVASADOS_DESCRIPCION = "descripcion";
	private static final String ENVASADOS_PESO = "peso";
	private static final String ENVASADOS_UNIDADES = "unidades";
	private static final String ENVASADOS_FECHA_FINALIZADO = "finalizado";

	private static final String FORMULA_REF_ARTICULO = "referencia_articulo";
	private static final String FORMULA_REF_PRODUCTO = "referencia_producto";
	private static final String FORMULA_PESO_GRAMOS = "peso_gramos";

	private static final String FABRICACION_ID = "id_fabricacion";
	private static final String FABRICACION_LOTE_PRODUCTO = "lote_fabricacion";
	private static final String FABRICACION_REF_PRODUCTO = "referencia_producto";
	private static final String FABRICACION_CANTIDAD_FABRICADA = "cantidad_fabricada";
	private static final String FABRICACION_CANTIDAD_DISPONIBLE = "cantidad_disponible";

	private static final String SELECT_ENVASADOS_SIN_FINALIZAR = "SELECT * FROM ENVASADOS WHERE FINALIZADO = 0";
	private static final String SELECT_FORMULA_BY_REF_ARTICULO = "SELECT * FROM FORMULA WHERE REFERENCIA_ARTICULO = ?";
	private static final String SELECT_FABRICACIONESBYREF_PRODUCTO_AND_CANTIDAD_DISPONIBLE = "SELECT * FROM FABRICACIONES "
			+ "WHERE REFERENCIA_PRODUCTO = ? AND cantidad_disponible > 0";

//	private static final String INSERT_RESULTADO = "INSERT INTO RESULTADOS (referencia_envasado, descripcion, lote_envasado, lote_fabricacion, cantidad_kg, producto) "
//			+ "VALUES (?,?,?,?,?,?)";

	private static final String UPDATE_FABIRCACION = "UPDATE FABRICACIONES SET CANTIDAD_DISPONIBLE = ? WHERE ID_FABRICACION = ?";

	private static final String UPDATE_ENVASADO_FINALIZADO = "UPDATE ENVASADOS SET FINALIZADO = 1 WHERE ID_ENVASADO = ?";
	private static final String INSERT_RESULTADOS_AGRUPADOS = "INSERT INTO RESULTADOS_AGRUPADOS (lote, descripcion, peso_kilos, operacion_envase) VALUES (?,?,?,?)";

	private static final Object MENSAJE_INFORMATIVO = "BIENVENIDO a la aplicación de trazabilidad de Mantecados El Santo. Se va a ejecutar la aplicación y, para visualizar los resultados, "
			+ "dirígase a la base de datos y consulte las tablas RESULTADOS o RESULTADOS AGRUPADOS."
			+ "Pulse ACEPTAR para comenzar el proceso trazabilidad.";
	private static final String TRAZABILIDAD_EL_SANTO = "Trazabilidad El Santo";

	public static Connection ejecutarConexion() throws SQLException {
		Connection conn = DriverManager.getConnection(urlDocumentos);
		return conn;
	}

	public static void main(String[] args) throws Exception {

		JOptionPane.showMessageDialog(null, MENSAJE_INFORMATIVO, TRAZABILIDAD_EL_SANTO,
				JOptionPane.INFORMATION_MESSAGE);

		Connection connection = null;
		try {
			connection = App.ejecutarConexion();

			// vemos si hay algun envasado por ejecutar
			List<Envasado> envasados = App.getListaEnvasadosSinTerminar(connection);

			if (CollectionUtils.isNotEmpty(envasados)) {

				recorrerLista(envasados, connection);
				
				if(App.getListaEnvasadosSinTerminar(connection).size() == 0) {
					
					JOptionPane.showMessageDialog(null, "Proceso terminado CORRECTAMENTE.", "Proceso terminado correctamente",
							JOptionPane.INFORMATION_MESSAGE);
				}else {
					
					JOptionPane.showMessageDialog(null, "Proceso terminado CORRECTAMENTE.", "Proceso terminado con algun artículo sin terminar "
							+ "su trazabilidad. Asegurate que haya productos fabricados suficientes para terminar el proceso. Gracias",
							JOptionPane.INFORMATION_MESSAGE);
				}

			} else {

				JOptionPane.showMessageDialog(null,
						"Todos los artículos a envasar hasta la fecha estan finalizados. Asegúrese de introducir nuevos articulos a envasar SIN FINALIZAR. Gracias",
						TRAZABILIDAD_EL_SANTO, JOptionPane.WARNING_MESSAGE);
			}

		} catch (Exception e) {

			if (connection == null) {
				JOptionPane.showMessageDialog(null,
						"No es posible encontrar la base de datos en el directorio DOCUMENTOS con el nombre elSanto. Por favor, revise que el archivo está en esa ruta con ese nombre.",
						TRAZABILIDAD_EL_SANTO, JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private static void recorrerLista(List<Envasado> envasados, Connection connection) throws SQLException {
		List<String> resultadosAgrupados;
		for (Envasado envasado : envasados) {
			resultadosAgrupados = new ArrayList<String>();
			List<Formula> formulaArticulo = buscarFormulaPorArticulo(connection, envasado.getReferencia_articulo());

			for (Formula formula : formulaArticulo) {

				List<Fabricacion> fabricacionesProducto = devuelveFabricaciones(connection,
						formula.getReferencia_producto());

				Double kgNecesarios = (envasado.getUnidades() * formula.getPorcentajeGramos())/1000;

				Double totalFabricacionDisponible = fabricacionesProducto.stream()
						.mapToDouble(Fabricacion::getCantidad_disponible).sum();

				if (totalFabricacionDisponible >= kgNecesarios) {

					Double auxKgNecesarios = kgNecesarios;

					for (Fabricacion fabricacion : fabricacionesProducto) {

						if (auxKgNecesarios > 0.0) {

							auxKgNecesarios = procesar(connection, resultadosAgrupados, envasado, formulaArticulo,
									auxKgNecesarios, fabricacion);
						}

					}

				}

			}

			if (CollectionUtils.isNotEmpty(resultadosAgrupados)) {

				insertarResultadosAgrupados(connection, new ResultadoAgrupado(envasado.getLote_envasado().toString(),
						envasado.getDescripcion(), (envasado.getPeso() * envasado.getUnidades())/1000, resultadosAgrupados));
			}

		}

	}

	private static Double procesar(Connection connection, List<String> resultadosAgrupados, Envasado envasado,
			List<Formula> formulaArticulo, Double auxKgNecesarios, Fabricacion fabricacion) throws SQLException {
		if (fabricacion.getCantidad_disponible() >= auxKgNecesarios) {// Si cantidadDisponible es mayot que kgnecesarios

			rellenarListaOperacionEnvase(resultadosAgrupados, formulaArticulo, auxKgNecesarios, fabricacion);

			auxKgNecesarios = procesarResultadocantidadDisponibleMayorKGNecesarios(connection, auxKgNecesarios,
					fabricacion, envasado);
			terminarProductoEnvase(connection, envasado, auxKgNecesarios);
		}

		// Si cantidadDisponible es menor que kg necesarios
		if (fabricacion.getCantidad_disponible() < auxKgNecesarios) {

			rellenarListaOperacionEnvase(resultadosAgrupados, formulaArticulo, fabricacion.getCantidad_disponible(),
					fabricacion);

			auxKgNecesarios = procesarResultadocantidadDisponibleMenorKGNecesarios(connection, auxKgNecesarios,
					fabricacion, envasado);
			terminarProductoEnvase(connection, envasado, auxKgNecesarios);
		}
		return auxKgNecesarios;
	}

	private static void terminarProductoEnvase(Connection connection, Envasado envasado, Double auxKgNecesarios)
			throws SQLException {
		if (auxKgNecesarios == 0.0) {
			// poner a 1 el finalizado del envasado
			atualizarFinalizadoEnvasado(connection, envasado.getId_envasado());
		}
	}

	private static void rellenarListaOperacionEnvase(List<String> resultadosAgrupados, List<Formula> formulaArticulo,
			Double auxKgNecesarios, Fabricacion fabricacion) {
		
		auxKgNecesarios = (Math.round(auxKgNecesarios*100.0)/100.0);
		if (formulaArticulo.size() > 1) {
			resultadosAgrupados.add(auxKgNecesarios + "(" + fabricacion.getLote_fabricacion() + "-"
					+ fabricacion.getReferencia_producto() + ")");
		} else {

			resultadosAgrupados.add(auxKgNecesarios + "(" + fabricacion.getLote_fabricacion() + ")");
		}
	}

	private static void insertarResultadosAgrupados(Connection connection, ResultadoAgrupado resultadoAgrupado)
			throws SQLException {

		PreparedStatement prepared = connection.prepareStatement(INSERT_RESULTADOS_AGRUPADOS);

		String resultado = resultadoAgrupado.getResultados_agrupados().stream().filter(StringUtils::isNotEmpty)
				.collect(Collectors.joining(", "));

		prepared.setString(1, resultadoAgrupado.getLote());
		prepared.setString(2, resultadoAgrupado.getDescripcion_producto());
		prepared.setDouble(3, resultadoAgrupado.getPesoTotal());
		prepared.setString(4, resultado);

		prepared.executeUpdate();

	}

	private static Double procesarResultadocantidadDisponibleMenorKGNecesarios(Connection connection,
			Double kgNecesarios, Fabricacion fabricacion, Envasado envasado) throws SQLException {

//		insertarResultado(connection, envasado.getReferencia_articulo(), envasado.getDescripcion(),
//				envasado.getLote_envasado(), fabricacion.getLote_fabricacion(), fabricacion.getCantidad_disponible(),
//				fabricacion.getReferencia_producto());

		// actualizar fabricacion
		actualizarFabricacion(connection, fabricacion.getId_fabricacion(), 0.0);

		return kgNecesarios - fabricacion.getCantidad_disponible();
	}

	private static Double procesarResultadocantidadDisponibleMayorKGNecesarios(Connection connection,
			Double kgNecesarios, Fabricacion fabricacion, Envasado envasado) throws SQLException {

		double cantidadDisponibleActualizada = fabricacion.getCantidad_disponible() - kgNecesarios;
		// Insertar en resultados;
//		insertarResultado(connection, envasado.getReferencia_articulo(), envasado.getDescripcion(),
//				envasado.getLote_envasado(), fabricacion.getLote_fabricacion(), kgNecesarios,
//				fabricacion.getReferencia_producto());

		// actualizar fabricacion
		actualizarFabricacion(connection, fabricacion.getId_fabricacion(), cantidadDisponibleActualizada);

		return 0.0;
	}

	private static void atualizarFinalizadoEnvasado(Connection connection, Long id_envasado) throws SQLException {

		PreparedStatement prepared = connection.prepareStatement(UPDATE_ENVASADO_FINALIZADO);

		prepared.setLong(1, id_envasado);

		prepared.executeUpdate();
	}

	private static void actualizarFabricacion(Connection connection, Long id_fabricacion,
			double cantidadDisponibleActualizada) throws SQLException {
		PreparedStatement prepared = connection.prepareStatement(UPDATE_FABIRCACION);

		prepared.setDouble(1, cantidadDisponibleActualizada);
		prepared.setLong(2, id_fabricacion);

		prepared.executeUpdate();

	}

//	private static void insertarResultado(Connection connection, String referencia_articulo, String descripcion,
//			Integer lote_envasado, Integer lote_fabricacion, Double kgNecesarios, String refProducto)
//			throws SQLException {
//
//		PreparedStatement prepared = connection.prepareStatement(INSERT_RESULTADO);
//
//		prepared.setString(1, referencia_articulo);
//		prepared.setString(2, descripcion);
//		prepared.setInt(3, lote_envasado);
//		prepared.setInt(4, lote_fabricacion);
//		prepared.setDouble(5, kgNecesarios);
//		prepared.setString(6, refProducto);
//
//		prepared.executeUpdate();
//	}

	private static List<Fabricacion> devuelveFabricaciones(Connection connection, String referencia_producto)
			throws SQLException {

		PreparedStatement prepared = connection
				.prepareStatement(SELECT_FABRICACIONESBYREF_PRODUCTO_AND_CANTIDAD_DISPONIBLE);
		prepared.setString(1, referencia_producto);
		ResultSet rs = prepared.executeQuery();
		List<Fabricacion> lista_fabricacion = new ArrayList<Fabricacion>();

		while (rs.next()) {
			lista_fabricacion.add(new Fabricacion(rs.getLong(FABRICACION_ID), rs.getInt(FABRICACION_LOTE_PRODUCTO),
					rs.getString(FABRICACION_REF_PRODUCTO), rs.getInt(FABRICACION_CANTIDAD_FABRICADA),
					rs.getDouble(FABRICACION_CANTIDAD_DISPONIBLE)));

		}

		return lista_fabricacion;
	}

	private static List<Formula> buscarFormulaPorArticulo(Connection connection, String ref_articulo)
			throws SQLException {
		PreparedStatement prepared = connection.prepareStatement(SELECT_FORMULA_BY_REF_ARTICULO);

		prepared.setString(1, ref_articulo);
		ResultSet rs = prepared.executeQuery();
		List<Formula> formulaProducto = new ArrayList<Formula>();
		while (rs.next()) {
			formulaProducto.add(new Formula(rs.getNString(FORMULA_REF_ARTICULO), rs.getNString(FORMULA_REF_PRODUCTO),
					rs.getDouble(FORMULA_PESO_GRAMOS)));
		}
		rs.close();
		prepared.close();

		return formulaProducto;
	}

	private static List<Envasado> getListaEnvasadosSinTerminar(Connection connection) throws SQLException {
		List<Envasado> envasados = new ArrayList<Envasado>();
		Statement s = connection.createStatement();
		ResultSet rs = s.executeQuery(SELECT_ENVASADOS_SIN_FINALIZAR);

		while (rs.next()) {

			envasados.add(new Envasado(rs.getLong(ENVASADOS_ID), rs.getInt(ENVASADOS_LOTE_ENVASADO),
					rs.getString(ENVASADOS_REF_ENVASADO), rs.getNString(ENVASADOS_DESCRIPCION),
					rs.getDouble(ENVASADOS_PESO), rs.getInt(ENVASADOS_UNIDADES),
					rs.getBoolean(ENVASADOS_FECHA_FINALIZADO)));
		}

		rs.close();
		s.close();

		return envasados;

	}
}
