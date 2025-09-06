package app.dtos;

import java.util.UUID;

public class RateRequest {

	public UUID placeId;
	public int score;
	public String description;
	public String hashtag;
	
	public RateRequest() {}
	
	public RateRequest(int score, String description, String hashtag) {
		super();
		this.score = score;
		this.description = description;
		this.hashtag = hashtag;
	}
}
