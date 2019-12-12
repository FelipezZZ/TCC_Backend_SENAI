package vo;

import java.util.Date;

public class Anamnese {
	
	private int cod_pessoa;
	private int depressao;
	private int ansiedade;
	private int estresse;
	private Date dataAnamneses;
	
	public int getCod_pessoa() {
		return cod_pessoa;
	}
	public void setCod_pessoa(int cod_pessoa) {
		this.cod_pessoa = cod_pessoa;
	}
	public int getDepressao() {
		return depressao;
	}
	public void setDepressao(int depressao) {
		this.depressao = depressao;
	}
	public int getAnsiedade() {
		return ansiedade;
	}
	public void setAnsiedade(int ansiedade) {
		this.ansiedade = ansiedade;
	}
	public int getEstresse() {
		return estresse;
	}
	public void setEstresse(int estresse) {
		this.estresse = estresse;
	}
	public Date getDataAnamneses() {
		return dataAnamneses;
	}
	public void setDataAnamneses(Date dataAnamneses) {
		this.dataAnamneses = dataAnamneses;
	}
	
	

}
