package com.cg.lib.dao;

import java.util.List;

import com.cg.lib.exception.LibException;
import com.cg.lib.model.Book;

public interface BookDao {
	void addBook(Book book) throws LibException;

	void updateBook(Book book) throws LibException;

	void deleteBook(int bookcode) throws LibException;

	List<Book> getAll();

	Book getBookById(int bookcode) throws LibException;

}
