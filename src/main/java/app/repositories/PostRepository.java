package app.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.model.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {
	@Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.user.id = :userId")
	List<Post> findAllByUserId(@Param("userId") UUID userId);
}
