package alessandro.datasecurity;

import alessandro.datasecurity.utils.Utils;

public class MessageModel {

    private long id;
    private String from;
    private String subject;
    private String message;
    private String timeStamp;
    private String picture;
    private boolean isImportant;
    private boolean isRead;
    private int color = -1;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MessageModel() {
    }

    public MessageModel(String from, String subject, String message, String picture, String path) {
        this.id = Long.parseLong(Utils.getIdCurrentTimestamp());;
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.timeStamp = Utils.getCurrentTimestamp();
        this.picture = picture;
        this.path = path;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getFrom() { return from; }

    public void setFrom(String from) { this.from = from; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getPicture() { return picture; }

    public void setPicture(String picture) { this.picture = picture; }

    public boolean isImportant() { return isImportant; }

    public void setImportant(boolean important) { isImportant = important; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public int getColor() { return color; }

    public void setColor(int color) { this.color = color; }

    public String getTimeStamp() { return timeStamp; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}