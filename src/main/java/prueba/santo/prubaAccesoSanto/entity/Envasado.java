package prueba.santo.prubaAccesoSanto.entity;

public class Envasado {
	
	private Long id_envasado;
	private Integer lote_envasado;
	private String referencia_articulo;
	private String descripcion;
	private Double peso;
	private Integer unidades;
	private Boolean finalizado;
	
	
	public Envasado(Long id_envasado, Integer lote_envasado, String referencia_articulo, String descripcion,Double peso, Integer unidades,
			Boolean finalizado) {
		super();
		this.id_envasado = id_envasado;
		this.lote_envasado = lote_envasado;
		this.referencia_articulo = referencia_articulo;
		this.descripcion = descripcion;
		this.peso = peso;
		this.unidades = unidades;
		this.finalizado = finalizado;
	}


	public Integer getLote_envasado() {
		return lote_envasado;
	}


	public void setLote_envasado(Integer lote_envasado) {
		this.lote_envasado = lote_envasado;
	}


	public String getReferencia_articulo() {
		return referencia_articulo;
	}


	public void setReferencia_articulo(String referencia_articulo) {
		this.referencia_articulo = referencia_articulo;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public Integer getUnidades() {
		return unidades;
	}


	public void setUnidades(Integer unidades) {
		this.unidades = unidades;
	}


	public Boolean getFinalizado() {
		return finalizado;
	}


	public void setFinalizado(Boolean finalizado) {
		this.finalizado = finalizado;
	}


	@Override
	public String toString() {
		return "Envasado [lote_envasado=" + lote_envasado + ", referencia_envasado=" + referencia_articulo
				+ ", descripcion=" + descripcion + ", unidades=" + unidades + ", finalizado=" + finalizado + "]";
	}


	public Long getId_envasado() {
		return id_envasado;
	}


	public void setId_envasado(Long id_envasado) {
		this.id_envasado = id_envasado;
	}


	public Double getPeso() {
		return peso;
	}


	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	

}
