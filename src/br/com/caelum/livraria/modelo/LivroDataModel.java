package br.com.caelum.livraria.modelo;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.caelum.livraria.dao.LivroDao;

public class LivroDataModel extends LazyDataModel<Livro> {
	private static final long serialVersionUID = 2948232237230849780L;
	
	@Inject
	private LivroDao dao;
	
	@PostConstruct
	void init() {
		super.setRowCount(dao.contaTodos());
	}
	
	@Override
	public List<Livro> load(int inicio, int quantidade, String campoOrdenacao, SortOrder sentidoOrdenacao, Map<String, Object> filtros) {
		String column = "titulo";
		String titulo = (String) filtros.get(column);
		
		return dao.listaTodosPaginada(inicio, quantidade, column, titulo);
	}
}