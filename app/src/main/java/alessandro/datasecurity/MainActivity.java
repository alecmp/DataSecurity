package alessandro.datasecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import alessandro.datasecurity.activities.decrypt.DecryptActivity;
import alessandro.datasecurity.activities.encrypt.EncryptActivity;
import alessandro.datasecurity.auth.Login;
import alessandro.datasecurity.utils.Database;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
  FirebaseUser user;
  DatabaseReference myRef;
  static FirebaseDatabase database;
  List<FireModel> list;
  RecyclerView recycle;
  private String userId;

  @OnClick({R.id.bAMEncrypt, R.id.bAMDecrypt})
  public void onButtonClick(View view) {
    if(view.getId() == R.id.bAMEncrypt) {
      Intent intent = new Intent(MainActivity.this, EncryptActivity.class);
      startActivity(intent);
    } else if(view.getId() == R.id.bAMDecrypt) {
      Intent intent = new Intent(MainActivity.this, DecryptActivity.class);
      startActivity(intent);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    database = Database.getDatabase();
    myRef = database.getReference("message");
    user = FirebaseAuth.getInstance().getCurrentUser();

    if (user.getUid() != null) {
      userId = user.getUid();
    }
      DatabaseReference mNewDatabaseReference = FirebaseDatabase.getInstance().getReference();
    mNewDatabaseReference.child("users").child(userId).child("fiscalCode").setValue("aaaaaaaaaa");


    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    initToolbar();


    myRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        list = new ArrayList<FireModel>();
        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

          FireModel value = dataSnapshot1.getValue(FireModel.class);
          FireModel fire = new FireModel();
          String name = value.getName();
          String address = value.getAddress();
          String email = value.getEmail();
          fire.setName(name);
          fire.setEmail(email);
          fire.setAddress(address);
          list.add(fire);

        }

      }

      @Override
      public void onCancelled(DatabaseError error) {
        // Failed to read value
        Log.w("Hello", "Failed to read value.", error.toException());
      }
    });









    }

  public void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(false);
      actionBar.setTitle("StegoPoliba");
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.sign_out, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_signout:
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        break;

      default:

        return super.onOptionsItemSelected(item);

    }
    return false;
  }

}
