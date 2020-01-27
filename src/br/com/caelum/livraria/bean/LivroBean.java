package br.com.caelum.livraria.bean;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDao;
import br.com.caelum.livraria.dao.LivroDao;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.modelo.LivroDataModel;

@Named
@ViewScoped
public class LivroBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 6825930412522332072L;
	
	@Inject
	private LivroDao livroDao;
	
	@Inject
	private AutorDao autorDao;
	
	@Inject
	private LivroDataModel livroDataModel;
	private List<Livro> livros;
	private Livro livro = new Livro();
	
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
		return this.autorDao.listaTodos();
	}

	// Executado na fase INVOKE_APPLICATION
	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			getFacesContext().addMessage("autor", new FacesMessage("Livro deve ter pelo menos um Autor."));

			return;
		}

		if (Objects.isNull(this.livro.getId())) {
			this.livroDao.adiciona(this.livro);
		} else {
			this.livroDao.atualiza(livro);
		}

		this.livros = this.livroDao.listaTodos();

		this.livro = new Livro();
	}

	public void remover(Livro livro) {
		System.out.println("Removendo o livro " + livro.getTitulo());

		this.livroDao.remove(livro);
	}

	public void removerAutorLivro(Autor autor) {
		this.livro.removeAutor(autor);
	}

	public void carregar(Livro livro) {
		setLivro(livro);
	}

	public void startsWithOne(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		1 Parâmetro - É do tipo javax.faces.context.FacesContext e permite obter informações da view processada no momento.

//		2 Parâmetro - É do tipo javax.faces.component.UIComponent e é uma referência ao componente que está sendo 
//		validado, normalmente um UIInput.

//		3 Parâmetro - É do tipo java.lang.Object e é um objeto que representa o valor digitado pelo usuário. 
//		O objeto já foi convertido.

		if (!((String) value).startsWith("1")) {
			throw new ValidatorException(new FacesMessage("Deveria começar com 1"));
		}
	}

	public boolean priceEqual(Object columnValue, Object filter, Locale locale) {
		String text = (Objects.isNull(filter)) ? null : filter.toString().trim();

		if (Objects.isNull(text) || text.isBlank()) {
			return true;
		}

		if (Objects.isNull(columnValue)) {
			return false;
		}

		try {
			Double typedPrice = Double.valueOf(text);
			Double columnPrice = (Double) columnValue;

			return columnPrice.compareTo(typedPrice) < 0;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public List<Livro> getLivros() {
		if (Objects.isNull(this.livros)) {
			this.livros = this.livroDao.listaTodos();
		}

		return this.livros;
	}

	public void associarAutor() {
		Autor autor = this.autorDao.buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
	}

	public void carregarLivroPeloId() {
		this.livro = ofNullable(this.livroDao.buscaPorId(this.getLivroId())).orElse(new Livro());
	}

//	Chamar um método para navegar pode ser útil quando queremos uma navegação condicional.	
//	public String formAutor() {
//  Executado na fase APPLY_REQUEST_VALUES

//	return "autor?faces-redirect=true";
//	}
}