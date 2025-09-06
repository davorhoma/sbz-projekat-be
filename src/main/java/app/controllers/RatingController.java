package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.PlaceDTO;
import app.dtos.RateRequest;
import app.services.PlaceService;

@RestController
@RequestMapping("/ratings")
public class RatingController {

	@Autowired
	private PlaceService placeService;
	
	@PutMapping("/rate")
	public ResponseEntity<PlaceDTO> ratePlace(@RequestHeader("Authorization") String authHeader,
			@RequestBody RateRequest request) {
		try {
			String token = authHeader.replace("Bearer ", "");
			PlaceDTO ratedPlace = placeService.addRating(token, request);
			
			return ResponseEntity.ok(ratedPlace);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
