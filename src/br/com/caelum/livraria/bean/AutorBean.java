package br.com.caelum.livraria.bean;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;

@ManagedBean
@ViewScoped
public class AutorBean implements Serializable {
	private static final long serialVersionUID = 5962365339256649536L;

//	O n�mero � a identifica��o da �rvore de componentes. Com aquela identifica��o 
//	o controlador consegue recuperar a �rvore da HttpSession j� que na sess�o vai ter pelo menos uma �rvore para cada p�gina.
	
	private Autor autor = new Autor();
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
	
	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}

	public String gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());
		
		DAO<Autor> dao = new DAO<Autor>(Autor.class);
		if (Objects.isNull(this.autor.getId())) {
			dao.adiciona(this.autor);
		} else {
			dao.atualiza(autor);
		}
		
		this.autor = new Autor();
	
		return "livro?faces-redirect=true";
	}
	
	public void remover(Autor autor) {
		System.out.println("Removendo autor");
		
		new DAO<Autor>(Autor.class).remove(autor);
	}
	
	public void carregar(Autor autor) {
		this.autor = autor;
	}

	public void carregarAutorPeloId() {
		this.autor = ofNullable(new DAO<Autor>(Autor.class).buscaPorId(this.getAutorId()))
				.orElse(new Autor());
	}
}