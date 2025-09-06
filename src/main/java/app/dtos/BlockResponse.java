package app.dtos;

public class BlockResponse {
    private boolean success;
    private String message;

    public BlockResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
