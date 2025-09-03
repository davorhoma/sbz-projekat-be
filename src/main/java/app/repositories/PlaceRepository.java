package app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Place;

public interface PlaceRepository extends JpaRepository<Place, UUID> {
}
