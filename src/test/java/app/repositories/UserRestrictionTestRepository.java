package app.repositories;

import java.time.LocalDateTime;
import java.util.List;

import app.model.RestrictionType;
import app.model.User;
import app.model.UserRestriction;

public interface UserRestrictionTestRepository {
    List<UserRestriction> findActiveRestrictions(User user, RestrictionType type, LocalDateTime now);
    UserRestriction save(UserRestriction restriction);
}
