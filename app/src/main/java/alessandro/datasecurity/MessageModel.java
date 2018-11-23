package alessandro.datasecurity;

public class MessageModel {

    private String sender;
    private String timeStamp;
    private String message;


    public MessageModel() {
    }

    public MessageModel(String sender, String timeStamp, String message) {
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}