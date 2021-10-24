package prueba.santo.prubaAccesoSanto.entity;

public class Formula {
	
	private String referencia_articulo;
	
	private String referencia_producto;
	
	private Double porcentajeGramos;

	public Formula(String referencia_articulo, String referencia_producto, Double porcentajeGramos) {
		super();
		this.referencia_articulo = referencia_articulo;
		this.referencia_producto = referencia_producto;
		this.porcentajeGramos = porcentajeGramos;
	}
	
	public Formula () {};

	public String getReferencia_articulo() {
		return referencia_articulo;
	}

	public void setReferencia_articulo(String referencia_articulo) {
		this.referencia_articulo = referencia_articulo;
	}

	public String getReferencia_producto() {
		return referencia_producto;
	}

	public void setReferencia_producto(String referencia_producto) {
		this.referencia_producto = referencia_producto;
	}

	public Double getPorcentajeGramos() {
		return porcentajeGramos;
	}

	public void setPorcentajeGramos(Double porcentaje) {
		this.porcentajeGramos = porcentaje;
	}
	
	
	

}
