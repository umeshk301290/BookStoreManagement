package com.bookstore.bookstoreapplication.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstore.bookstoreapplication.entity.BookStoreInformation;

/**
 * @author umeshkumar01
 *
 */
public interface BookStoreInformationRepository extends JpaRepository<BookStoreInformation, Long> {
@Lock(LockModeType.OPTIMISTIC)
@Query("SELECT t FROM BookStoreInformation t where t.isbn = :isbn")
public Optional<BookStoreInformation> findbyIsbn(@Param("isbn") String isbn);

@Override
@Lock(LockModeType.OPTIMISTIC)
public BookStoreInformation save(BookStoreInformation bookInformation);

@Query("SELECT t FROM BookStoreInformation t where t.author like %:author% ")
public List<BookStoreInformation> findbyAuthor(@Param("author") String author);

@Query("SELECT t FROM BookStoreInformation t where t.title like %:title% ")
public List<BookStoreInformation> findbyTitle(@Param("title") String title);

}
