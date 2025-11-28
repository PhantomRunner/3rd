import java.io.Serializable;

public class Response implements Serializable {
    private boolean success;
    private String feedback;
    private Object data;

    public Response(boolean success, String message) {
        this.success = success;
        this.feedback = message;
    }

    public Response(boolean success) {
        this.success = success;
        this.feedback = "";
    }

    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.feedback = message;
        this.data = data;
    }


    public String getFeedback() {
        return feedback;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }
}
