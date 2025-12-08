package K23CNTT_tvtDay08.repository;

import K23CNTT_tvtDay08.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}