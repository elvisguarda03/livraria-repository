package br.com.caelum.livraria.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;

import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;

public class PopulaBanco {

	public static void main(String[] args) {

		EntityManager em = new JPAUtil().getEntityManager();

		em.getTransaction().begin();

		Autor sergio = geraAutor("Sergio Lopes", "sergio.lopes@caelum.com.br");
		em.persist(sergio);
		
		Autor nico = geraAutor("Nico Steppat", "nico.steppat@caelum.com.br");
		em.persist(nico);

		Autor mauricio = geraAutor("Mauricio Aniche", "aniche@teste.com.br");
		em.persist(mauricio);

		Autor flavio = geraAutor("Flavio Almeida", "flavio.almeida@caelum.com.br");
		em.persist(flavio);
		
		Autor paulo = geraAutor("Paulo Silveira", "paulo.silveira@caelum.com.br");
		em.persist(paulo);

		Livro mean = geraLivro("1345663423", "MEAN",
				"26/02/2016", 49.9, flavio);
		Livro java = geraLivro("199999999999",
				"Arquitetura Java", "27/02/2016", 49.9, sergio, nico, paulo);

		em.persist(mean);
		em.persist(java);

		em.getTransaction().commit();
		em.close();
	}

	private static Autor geraAutor(String nome, String email) {
		Autor autor = new Autor();
		autor.setNome(nome);
		autor.setEmail(email);
		return autor;
	}

	private static Livro geraLivro(String isbn, String titulo, String data,
			double preco, Autor... autores) {
		Livro livro = new Livro();
		livro.setIsbn(isbn);
		livro.setTitulo(titulo);
		livro.setDataLancamento(parseData(data));
		livro.setPreco(preco);
		
		for (Autor a : autores) {
			livro.adicionaAutor(a);
		}
		
		return livro;
	}

	private static Calendar parseData(String data) {
		try {
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(data);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
