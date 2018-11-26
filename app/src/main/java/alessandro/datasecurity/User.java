package alessandro.datasecurity;

public class User {
    private String name;
    private String email;
    private String uid;
    private String photo;


    public User() {

    }

    public User(String uid, String name, String email, String photo) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.photo = photo;
    }

    public User(String uid) {
        this.uid = uid;
    }

    public String getPhoto() { return photo; }

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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}