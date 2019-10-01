package com.cg.lib.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cg.lib.exception.LibException;
import com.cg.lib.model.Book;

public class BookDaoImpl implements BookDao {
	private Map<Integer, Book> booksMap;

	public BookDaoImpl() {
		booksMap = new TreeMap<>();
	}

	@Override
	public void addBook(Book book) throws LibException {
		
		if (book != null && !booksMap.containsKey(book.getBookcode())) {
			booksMap.put(book.getBookcode(), book);
		} else {
			throw new LibException("Either no book or duplicate book received");
		}

	}

	@Override
	public void updateBook(Book book) throws LibException {
		
		if (book != null && booksMap.containsKey(book.getBookcode())) {
			booksMap.replace(book.getBookcode(), book);
		} else {
			throw new LibException(" no such book found");
		}

	}

	@Override
	public void deleteBook(int bookcode) throws LibException {
		
		if (booksMap.containsKey(bookcode)) {
			booksMap.remove(bookcode);
		} else {
			throw new LibException("no such book found");
		}
	}

	@Override
	public List<Book> getAll() {
		
		ArrayList<Book> bookList = new ArrayList<Book>(booksMap.values());
		return bookList;
	}

	@Override
	public Book getBookById(int bookcode) throws LibException {
		
		

		return booksMap.get(bookcode);
	}

}
