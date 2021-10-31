package prueba.santo.prubaAccesoSanto.entity;

import java.util.Date;

public class Entrada_Materia_Prima {

	private Long id_materia_prima;
	private Integer lote;
	private String ref_materia_prima;
	private Double entrada;
	private Double disponible;
	private String proveedor;
	private String lote_externo;
	private String albaran;
	
	
	
	public Entrada_Materia_Prima(Long id_materia_prima, Integer lote, String ref_materia_prima, Double entrada,
			Double disponible, String proveedor, String lote_externo, String albaran) {
		super();
		this.id_materia_prima = id_materia_prima;
		this.lote = lote;
		this.ref_materia_prima = ref_materia_prima;
		this.entrada = entrada;
		this.disponible = disponible;
		this.proveedor = proveedor;
		this.lote_externo = lote_externo;
		this.albaran = albaran;
	}
	public Long getId_materia_prima() {
		return id_materia_prima;
	}
	public void setId_materia_prima(Long id_materia_prima) {
		this.id_materia_prima = id_materia_prima;
	}
	public Integer getLote() {
		return lote;
	}
	public void setLote(Integer lote) {
		this.lote = lote;
	}
	public String getRef_materia_prima() {
		return ref_materia_prima;
	}
	public void setRef_materia_prima(String ref_materia_prima) {
		this.ref_materia_prima = ref_materia_prima;
	}
	public Double getEntrada() {
		return entrada;
	}
	public void setEntrada(Double entrada) {
		this.entrada = entrada;
	}
	public Double getDisponible() {
		return disponible;
	}
	public void setDisponible(Double disponible) {
		this.disponible = disponible;
	}
	public String getProveedor() {
		return proveedor;
	}
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	public String getLote_externo() {
		return lote_externo;
	}
	public void setLote_externo(String lote_externo) {
		this.lote_externo = lote_externo;
	}
	public String getAlbaran() {
		return albaran;
	}
	public void setAlbaran(String albaran) {
		this.albaran = albaran;
	}
	
	
}
