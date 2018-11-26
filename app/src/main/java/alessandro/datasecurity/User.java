package alessandro.datasecurity;

public class User {
    private String name;
    private String email;
    private String uid;
    private String picture;
    private int color = -1;


    public User() {

    }

    public User(String uid, String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.picture = picture;
    }

    public User(String uid) {
        this.uid = uid;
    }

    public String getPicture() { return picture; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getColor() { return color; }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}