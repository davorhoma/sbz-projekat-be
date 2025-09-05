package app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Like;

public interface LikeRepository extends JpaRepository<Like, UUID>{

}
