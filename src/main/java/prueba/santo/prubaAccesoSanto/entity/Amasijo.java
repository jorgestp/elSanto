package prueba.santo.prubaAccesoSanto.entity;

public class Amasijo {
	
	private Long id_amasijo;
	private Integer lote;
	private String referencia_amasijo;
	private String descripcion;
	private Double peso_unitario;
	private double cantidad_amasijo;
	private Double peso_total;
	private Boolean finalizado;
	
	
	
	
	public Amasijo(Long id_amasijo, Integer lote, String referencia_amasijo, String descripcion, Double peso_unitario,
			double cantidad_amasijo, Double peso_total, Boolean finalizado) {
		super();
		this.id_amasijo = id_amasijo;
		this.lote = lote;
		this.referencia_amasijo = referencia_amasijo;
		this.descripcion = descripcion;
		this.peso_unitario = peso_unitario;
		this.cantidad_amasijo = cantidad_amasijo;
		this.peso_total = peso_total;
		this.finalizado = finalizado;
	}
	public Long getId_amasijo() {
		return id_amasijo;
	}
	public void setId_amasijo(Long id_amasijo) {
		this.id_amasijo = id_amasijo;
	}
	public Integer getLote() {
		return lote;
	}
	public void setLote(Integer lote) {
		this.lote = lote;
	}
	public String getReferencia_amasijo() {
		return referencia_amasijo;
	}
	public void setReferencia_amasijo(String referencia_amasijo) {
		this.referencia_amasijo = referencia_amasijo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getPeso_unitario() {
		return peso_unitario;
	}
	public void setPeso_unitario(Double peso_unitario) {
		this.peso_unitario = peso_unitario;
	}
	public double getCantidad_amasijo() {
		return cantidad_amasijo;
	}
	public void setCantidad_amasijo(double cantidad_amasijo) {
		this.cantidad_amasijo = cantidad_amasijo;
	}
	public Double getPeso_total() {
		return peso_total;
	}
	public void setPeso_total(Double peso_total) {
		this.peso_total = peso_total;
	}
	public Boolean getFinalizado() {
		return finalizado;
	}
	public void setFinalizado(Boolean finalizado) {
		this.finalizado = finalizado;
	}
	@Override
	public String toString() {
		return "Amasijo [id_amasijo=" + id_amasijo + ", lote=" + lote + ", referencia_amasijo=" + referencia_amasijo
				+ ", descripcion=" + descripcion + ", peso_unitario=" + peso_unitario + ", cantidad_amasijo="
				+ cantidad_amasijo + ", peso_total=" + peso_total + ", finalizado=" + finalizado + "]";
	}
	

	
	

}
