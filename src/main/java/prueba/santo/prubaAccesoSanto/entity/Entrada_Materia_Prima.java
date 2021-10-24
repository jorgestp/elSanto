package prueba.santo.prubaAccesoSanto.entity;

import java.util.Date;

public class Entrada_Materia_Prima {

	private Long id_fabricacion;
	private Integer lote_fabricacion;
	private String referencia_producto;
	private Date fecha_fabricacion;
	private Integer cantidad;
	private Double cantidad_disponible;
	
	
	
	
	
	public Entrada_Materia_Prima(Long id_fabricacion, Integer lote_fabrica, String referencia_producto,  Integer cantidad,
			Double cantidad_disponible) {
		super();
		this.id_fabricacion =  id_fabricacion;
		this.lote_fabricacion = lote_fabrica;
		this.referencia_producto = referencia_producto;
		
		this.cantidad = cantidad;
		this.cantidad_disponible = cantidad_disponible;
	}
	public Integer getLote_fabricacion() {
		return lote_fabricacion;
	}
	public void setLote_fabricacion(Integer lote_fabrica) {
		this.lote_fabricacion = lote_fabrica;
	}
	public String getReferencia_producto() {
		return referencia_producto;
	}
	public void setReferencia_producto(String referencia_producto) {
		this.referencia_producto = referencia_producto;
	}
	public Date getFecha_fabricacion() {
		return fecha_fabricacion;
	}
	public void setFecha_fabricacion(Date fecha_fabricacion) {
		this.fecha_fabricacion = fecha_fabricacion;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Double getCantidad_disponible() {
		return cantidad_disponible;
	}
	public void setCantidad_disponible(Double cantidad_disponible) {
		this.cantidad_disponible = cantidad_disponible;
	}
	public Long getId_fabricacion() {
		return id_fabricacion;
	}
	public void setId_fabricacion(Long id_fabricacion) {
		this.id_fabricacion = id_fabricacion;
	}
	
	
}
