package prueba.santo.prubaAccesoSanto;

import java.util.List;

public class ResultadoAgrupado {
	
	private String lote;
	private String descripcion_producto;
	private List<String> resultados_agrupados;
	
	
	public ResultadoAgrupado(String lote, String descripcion_producto, List<String> resultados_agrupados) {
		super();
		this.lote = lote;
		this.descripcion_producto = descripcion_producto;
		this.resultados_agrupados = resultados_agrupados;
	}
	
	
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getDescripcion_producto() {
		return descripcion_producto;
	}
	public void setDescripcion_producto(String descripcion_producto) {
		this.descripcion_producto = descripcion_producto;
	}
	public List<String> getResultados_agrupados() {
		return resultados_agrupados;
	}
	public void setResultados_agrupados(List<String> resultados_agrupados) {
		this.resultados_agrupados = resultados_agrupados;
	}
	
	
	

}
