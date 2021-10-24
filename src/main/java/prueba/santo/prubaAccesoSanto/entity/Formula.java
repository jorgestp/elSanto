package prueba.santo.prubaAccesoSanto.entity;

public class Formula {
	
	private String referencia_amasijo;
	
	private String referencia_materia_prima;
	
	private Double kilos;
	
	

	public Formula(String referencia_amasijo, String referencia_materia_prima, Double kilos) {
		super();
		this.referencia_amasijo = referencia_amasijo;
		this.referencia_materia_prima = referencia_materia_prima;
		this.kilos = kilos;
	}

	public String getReferencia_amasijo() {
		return referencia_amasijo;
	}

	public void setReferencia_amasijo(String referencia_amasijo) {
		this.referencia_amasijo = referencia_amasijo;
	}

	public String getReferencia_materia_prima() {
		return referencia_materia_prima;
	}

	public void setReferencia_materia_prima(String referencia_materia_prima) {
		this.referencia_materia_prima = referencia_materia_prima;
	}

	public Double getKilos() {
		return kilos;
	}

	public void setKilos(Double kilos) {
		this.kilos = kilos;
	}

	
}
