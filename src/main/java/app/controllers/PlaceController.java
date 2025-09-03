package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.PlaceRegisterDTO;
import app.model.Place;
import app.services.PlaceService;

@RestController
@RequestMapping("/places")
public class PlaceController {

	@Autowired
	private PlaceService placeService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/")
	public ResponseEntity<?> registerNewPlace(@RequestBody PlaceRegisterDTO request) {
		try {
			Place place = placeService.create(
					request.name, 
					request.country, 
					request.city, 
					request.description, 
					request.hashtag);
			
			return ResponseEntity.ok(place);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
