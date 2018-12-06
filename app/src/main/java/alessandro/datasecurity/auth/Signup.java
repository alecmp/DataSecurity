package alessandro.datasecurity.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import alessandro.datasecurity.MainActivity;
import alessandro.datasecurity.MessageModel;
import alessandro.datasecurity.R;
import alessandro.datasecurity.User;
import alessandro.datasecurity.utils.Database;

/**
 * Activity to sign on
 */
public class Signup extends AppCompatActivity {
    static FirebaseDatabase database;
    private DatabaseReference myRef;
    private static String fullname;
    static {
        fullname = null;
    }
    private EditText inputFullName, inputEmail, inputPassword;
    Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        auth = FirebaseAuth.getInstance();
        database = Database.getDatabase();
        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        inputFullName = findViewById(R.id.fullname);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullname = inputFullName.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();


                if (TextUtils.isEmpty(fullname)) {
                    Toast.makeText(getApplicationContext(), "Inserisci nome completo", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Inserisci indirizzo email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Inserisci password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password troppo corta, inserire almeno 6 caratteri", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Signup.this, "Autenticazione fallita." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(fullname)
                                            .setPhotoUri(Uri.parse("https://4.bp.blogspot.com/-hO4f0VwbBXM/V4USBgrQk7I/AAAAAAAACOE/-CqTJ1ONmFIh9szArjRNdQuWm1bLSTY3ACLcB/s1600/Tiana2.NEF"))
                                            .build();
                                    //.setPhotoUri(Uri.parse("https://4.bp.blogspot.com/-hO4f0VwbBXM/V4USBgrQk7I/AAAAAAAACOE/-CqTJ1ONmFIh9szArjRNdQuWm1bLSTY3ACLcB/s1600/Tiana2.NEF"))
                                    if (user != null) {
                                        user.updateProfile(profileUpdates);
                                    }
                                    String userId = user.getUid();
                                    /* add user to Db */
                                    DatabaseReference ref = FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("users")
                                            .child(userId)
                                            .getRef();
                                    ref.setValue(new User(userId, fullname, email, null));


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    Intent intent = new Intent(Signup.this, MainActivity.class);
                                    intent.putExtra("fullname", fullname);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    public static String getFullname() {
        return fullname;
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


}