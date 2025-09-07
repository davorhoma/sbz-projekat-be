package app.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.model.RestrictionType;
import app.model.User;
import app.model.UserRestriction;

public interface UserRestrictionRepository extends JpaRepository<UserRestriction, UUID> {

	@Query("SELECT r FROM UserRestriction r WHERE r.user = :user AND r.type = :type AND (r.validUntil IS NULL OR r.validUntil > :now)")
    List<UserRestriction> findActiveRestrictions(@Param("user") User user, 
                                                 @Param("type") RestrictionType type,
                                                 @Param("now") LocalDateTime now);
}
