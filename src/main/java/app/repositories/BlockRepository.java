package app.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.model.Block;

public interface BlockRepository extends JpaRepository<Block, UUID> {

	Optional<Block> findByBlocker_IdAndBlocked_Id(UUID blockerId, UUID blockedId);
}
