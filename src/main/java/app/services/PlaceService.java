package app.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dtos.PlaceDTO;
import app.dtos.RateRequest;
import app.mappers.PlaceMapper;
import app.model.Place;
import app.model.Rating;
import app.repositories.PlaceRepository;
import app.utils.JwtUtil;

@Service
public class PlaceService {
	
	@Autowired
	private PlaceRepository placeRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public Place findById(UUID placeId) throws Exception {
		Optional<Place> place = placeRepository.findById(placeId);
		if (!place.isPresent()) {
			throw new Exception("Place does not exist");
		}
		
		return place.get();
	}
	
	public PlaceDTO create(String name, String country, String city, String description, String hashtag) throws Exception {
		try {
			Place newPlace = new Place(name, country, city, description, hashtag);
			Place saved = placeRepository.save(newPlace);			
			return PlaceMapper.toDto(saved);
		} catch (Exception e) {
			throw new Exception("Error creating new place");
		}
	}

	public List<PlaceDTO> getAll() {
		return placeRepository.findAll()
				.stream()
				.map(place -> PlaceMapper.toDto(place))
				.collect(Collectors.toList());
	}

	public PlaceDTO addRating(String token, RateRequest rateRequest) throws Exception {
		UUID userId = jwtUtil.extractUserId(token);
		
		Place place = placeRepository.findById(rateRequest.placeId).orElseThrow(() -> new Exception("Place not found"));
		
		Optional<Rating> optRating = place.getRatings().stream()
				.filter(rating -> rating.getUser().getId().equals(userId))
				.findFirst();
		
		if (optRating.isPresent()) {
			throw new Exception("User already rated this place");
		}
		
		Rating newRating = new Rating(
				rateRequest.score,
				rateRequest.description,
				rateRequest.hashtag, 
				place, 
				userService.findById(userId));
		
		place.addRating(newRating);
		Place saved = placeRepository.save(place);
		
		return PlaceMapper.toDto(saved);
	}
}
