package com.bookstore.bookstoreapplication.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author umeshkumar01
 *
 */
@Entity
@Table(name = "bookstoreinformation", uniqueConstraints = @UniqueConstraint(columnNames = { "isbn" }))
@Setter
@Getter
@ToString
public class BookStoreInformation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "bookid")
	Long id;

	@Column(name = "isbn")
	@NotBlank(message = "Isbn cannot be blank")
	String isbn;

	@Column(name = "title")
	@NotBlank(message = "title cannot be blank")
	String title;

	@Column(name = "author")
	@NotBlank(message = "author cannot be blank")
	String author;

	@Column(name = "price")
	@NotNull(message = "price cannot be blank")
	@DecimalMin(value ="10.0", message="price should be greater than or equal to {value}")
	BigDecimal price;

	@Column(name = "copies")
	@Min(value = 1, message="numberOfCopies should be greater than or equal to {value}" )
	Integer numberOfCopies;


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		BookStoreInformation bookInformation = (BookStoreInformation) o;

		return new EqualsBuilder().appendSuper(super.equals(o)).append(getId(), bookInformation.getId())
				.append(getIsbn(), bookInformation.getIsbn()).append(getAuthor(), bookInformation.getAuthor())
				.append(getTitle(), bookInformation.getTitle()).append(getPrice(), bookInformation.getPrice())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getId()).append(getIsbn())
				.append(getAuthor()).append(getTitle()).append(getPrice()).toHashCode();
	}
}
