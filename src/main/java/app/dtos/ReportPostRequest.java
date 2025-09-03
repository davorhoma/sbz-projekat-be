package app.dtos;

public class ReportPostRequest {
	private String postId;

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}
	
	public ReportPostRequest() {
	}
	
	public ReportPostRequest(String postId) {
		this.postId = postId;
	}
}