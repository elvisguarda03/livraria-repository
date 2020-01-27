package br.com.caelum.livraria.bean;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDao;
import br.com.caelum.livraria.modelo.Autor;

@Named
@ViewScoped
public class AutorBean implements Serializable {
	private static final long serialVersionUID = 5962365339256649536L;

//	O n�mero � a identifica��o da �rvore de componentes. Com aquela identifica��o 
//	o controlador consegue recuperar a �rvore da HttpSession j� que na sess�o vai ter pelo menos uma �rvore para cada p�gina.
	
	private Autor autor = new Autor();
	
	@Inject
	private AutorDao dao;
	private Integer autorId;
	
	public Integer getAutorId() {
		return autorId;
	}
	
	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}
	
	public Autor getAutor() {
		return autor;
	}
	
	public void setAutor(Autor autor) {
		this.autor = autor;
	}
	
	public List<Autor> getAutores() {
		return dao.listaTodos();
	}

	public String gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());
		
		if (Objects.isNull(this.autor.getId())) {
			this.dao.adiciona(this.autor);
		} else {
			this.dao.atualiza(autor);
		}
		
		this.autor = new Autor();
	
		return "livro?faces-redirect=true";
	}
	
	public void remover(Autor autor) {
		System.out.println("Removendo autor");
		
		this.dao.remove(autor);
	}
	
	public void carregar(Autor autor) {
		this.autor = autor;
	}

	public void carregarAutorPeloId() {
		this.autor = ofNullable(this.dao.buscaPorId(this.getAutorId()))
				.orElse(new Autor());
	}
}