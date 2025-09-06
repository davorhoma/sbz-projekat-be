package app.dtos;

import java.util.UUID;

public class PlaceDTO {

	public UUID id;
	public String name;
	public String country;
	public String city;
	public String description;
	public String hashtag;
	public double averageScore;
	
	public PlaceDTO() {}
	
	public PlaceDTO(UUID id, String name, String country, String city, String description, String hashtag) {
		super();
		this.id = id;
		this.name = name;
		this.country = country;
		this.city = city;
		this.description = description;
		this.hashtag = hashtag;
	}
}
