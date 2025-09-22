package app.repositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import app.model.RestrictionType;
import app.model.User;
import app.model.UserRestriction;

public class InMemoryUserRestrictionRepository implements UserRestrictionRepository {

    private final List<UserRestriction> storage = new ArrayList<>();

    @Override
    public List<UserRestriction> findActiveRestrictions(User user, RestrictionType type, LocalDateTime now) {
        return storage.stream()
                .filter(r -> r.getUser().equals(user))
                .filter(r -> r.getType() == type)
                .filter(r -> r.getValidUntil() == null || r.getValidUntil().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public UserRestriction save(UserRestriction restriction) {
        storage.add(restriction);
        return restriction;
    }

	@Override
	public List<UserRestriction> findAll() {
		return null;
	}

	@Override
	public List<UserRestriction> findAll(Sort sort) {
		return null;
	}

	@Override
	public List<UserRestriction> findAllById(Iterable<UUID> ids) {
		return null;
	}

	@Override
	public <S extends UserRestriction> List<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public void flush() {
		
	}

	@Override
	public <S extends UserRestriction> S saveAndFlush(S entity) {
		return null;
	}

	@Override
	public void deleteInBatch(Iterable<UserRestriction> entities) {
		
	}

	@Override
	public void deleteAllInBatch() {
		
	}

	@Override
	public UserRestriction getOne(UUID id) {
		return null;
	}

	@Override
	public <S extends UserRestriction> List<S> findAll(Example<S> example) {
		return null;
	}

	@Override
	public <S extends UserRestriction> List<S> findAll(Example<S> example, Sort sort) {
		return null;
	}

	@Override
	public Page<UserRestriction> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public Optional<UserRestriction> findById(UUID id) {
		return Optional.empty();
	}

	@Override
	public boolean existsById(UUID id) {
		return false;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(UUID id) {
	}

	@Override
	public void delete(UserRestriction entity) {
		
	}

	@Override
	public void deleteAll(Iterable<? extends UserRestriction> entities) {
		
	}

	@Override
	public void deleteAll() {
		
	}

	@Override
	public <S extends UserRestriction> Optional<S> findOne(Example<S> example) {
		return Optional.empty();
	}

	@Override
	public <S extends UserRestriction> Page<S> findAll(Example<S> example, Pageable pageable) {
		return null;
	}

	@Override
	public <S extends UserRestriction> long count(Example<S> example) {
		return 0;
	}

	@Override
	public <S extends UserRestriction> boolean exists(Example<S> example) {
		return false;
	}
}
