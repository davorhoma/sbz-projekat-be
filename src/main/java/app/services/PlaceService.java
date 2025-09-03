package app.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.Place;
import app.repositories.PlaceRepository;

@Service
public class PlaceService {
	
	@Autowired
	private PlaceRepository placeRepository;
	
	public Place findById(UUID placeId) throws Exception {
		Optional<Place> place = placeRepository.findById(placeId);
		if (!place.isPresent()) {
			throw new Exception("Place does not exist");
		}
		
		return place.get();
	}
	
	public Place create(String name, String country, String city, String description, String hashtag) throws Exception {
		try {
			Place newPlace = new Place(name, country, city, description, hashtag);
			return placeRepository.save(newPlace);			
		} catch (Exception e) {
			throw new Exception("Error creating new place");
		}
	}

	public List<Place> getAll() {
		return placeRepository.findAll();
	}
}
