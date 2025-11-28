import java.io.Serializable;

// Response from the server to the client
public class Response implements Serializable {

    private boolean success; // Indicates if the request was successful
    private String feedback; // Optional message describing the result
    private Object data;     // Optional data returned (e.g., list of cars, total value)

    // Constructor with success flag and message
    public Response(boolean success, String message) {
        this.success = success;
        this.feedback = message;
    }

    // Constructor with only success flag
    public Response(boolean success) {
        this.success = success;
        this.feedback = "";
    }

    // Constructor with success, message, and additional data
    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.feedback = message;
        this.data = data;
    }

    // Returns the feedback message
    public String getFeedback() {
        return feedback;
    }

    // Returns true if request succeeded
    public boolean isSuccess() {
        return success;
    }

    // Returns any additional data
    public Object getData() {
        return data;
    }
}