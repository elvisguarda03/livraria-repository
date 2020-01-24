package br.com.caelum.livraria.bean;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.modelo.LivroDataModel;

@ManagedBean
@ViewScoped
public class LivroBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 6825930412522332072L;
	
	private List<Livro> livros;
	private Livro livro = new Livro();
	private LivroDataModel livroDataModel = new LivroDataModel();
	
	private Integer autorId;
	private Integer livroId;

	public LivroDataModel getLivroDataModel() {
		return livroDataModel;
	}
	
	public void setLivroDataModel(LivroDataModel livroDataModel) {
		this.livroDataModel = livroDataModel;
	}
	
	public List<Autor> getAutoresLivro() {
		return this.livro.getAutores();
	}
	
	public Livro getLivro() {
		return livro;
	}
	
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	
	public Integer getAutorId() {
		return this.autorId;
	}
	
	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}
	
	public Integer getLivroId() {
		return this.livroId;
	}
	
	public void setLivroId(Integer livroId) {
		this.livroId = livroId;
	}

	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}
	
	//Executado na fase INVOKE_APPLICATION
	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			getFacesContext()
				.addMessage("autor", new FacesMessage("Livro deve ter pelo menos um Autor."));
		
			return;
		}
		
		DAO<Livro> dao = new DAO<Livro>(Livro.class);
		
		if (Objects.isNull(this.livro.getId())) {
			dao.adiciona(this.livro);
		} else {
			dao.atualiza(livro);
		}
		
		this.livros = dao.listaTodos();
		
		this.livro = new Livro();
	}

	public void remover(Livro livro) {
		System.out.println("Removendo o livro " + livro.getTitulo());
	
		new DAO<Livro>(Livro.class).remove(livro);
	}
	
	public void removerAutorLivro(Autor autor) {
		this.livro.removeAutor(autor);
	}
	
	public void carregar(Livro livro) {
		setLivro(livro);
	}
	
	public void startsWithOne(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		1 Parâmetro - É do tipo javax.faces.context.FacesContext e permite obter informações da view processada no momento.

//		2 Parâmetro - É do tipo javax.faces.component.UIComponent e é um referência ao componente que está sendo 
//		validado, normalmente um UIInput.

//		3 Parâmetro - É do tipo java.lang.Object e é um objeto que representa o valor digitado pelo usuário. 
//		O objeto já foi convertido.
		
		if ( !((String) value).startsWith("1") ) {
			throw new ValidatorException(new FacesMessage("Deveria começar com 1"));
		}
	}
	
	public boolean lessThan(Object columnValue, Object filter, Locale locale) {
	
		String text = (Objects.isNull(filter)) ? null : filter.toString().trim();
		
		if (Objects.isNull(text) || text.isBlank()) {
			return true;
		}
		
		if (Objects.isNull(columnValue)) {
			return false;
		}
		
		try {
			Double typedPrice = Double.valueOf(text);
			Double columnPrice = (Double)columnValue;
			
			return columnPrice.compareTo(typedPrice) < 0;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	public List<Livro> getLivros() {
		if (Objects.isNull(this.livros)) {
			this.livros = new DAO<Livro>(Livro.class).listaTodos();
		}
		
		return this.livros;
	}

	public void associarAutor() {
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(this.autorId);
        this.livro.adicionaAutor(autor);
	}
	
	public void carregarLivroPeloId() {
		this.livro = ofNullable(new DAO<Livro>(Livro.class).buscaPorId(this.getLivroId()))
				.orElse(new Livro());
	}

//	Chamar um método para navegar pode ser útil quando queremos uma navegação condicional.	
//	public String formAutor() {
//  Executado na fase APPLY_REQUEST_VALUES

//	return "autor?faces-redirect=true";
//	}
}