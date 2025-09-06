package app.mappers;

import app.dtos.PlaceDTO;
import app.model.Place;
import app.model.Rating;

public class PlaceMapper {

	public static PlaceDTO toDto(Place place) {
		if (place == null) return null;
		
		PlaceDTO dto = new PlaceDTO(
				place.getId(),
				place.getName(),
				place.getCountry(),
				place.getCity(),
				place.getDescription(),
				place.getHashtag());
		
		if (place.getRatings() != null && !place.getRatings().isEmpty()) {
	        double avg = place.getRatings()
	                          .stream()
	                          .mapToInt(Rating::getScore)
	                          .average()
	                          .orElse(0.0);

	        dto.averageScore = avg;
	    } else {
	        dto.averageScore = 0.0;
	    }
		
		return dto;
	}
}
