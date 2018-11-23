package alessandro.datasecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private FirebaseUser user;
    private DatabaseReference myRef;
    static FirebaseDatabase database;
    private LinearLayoutManager mLayoutManager;
    FirebaseRecyclerAdapter<MessageModel, MessageViewHolder> adapter;
    private String userId;
    private RecyclerView mRecyclerView;
    Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Database.getDatabase();
        myRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.getUid() != null) {
            userId = user.getUid();
        }
        //    DatabaseReference mNewDatabaseReference = FirebaseDatabase.getInstance().getReference();
        // mNewDatabaseReference.child("users").child(userId).child("fiscalCode").setValue("aaaaaaaaaa");
        mRecyclerView = findViewById(R.id.recycleView);


        query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userId)
                .child("messages")
                .getRef();

        ButterKnife.bind(this);
        initToolbar();

        FirebaseRecyclerOptions<MessageModel> options = new FirebaseRecyclerOptions
                .Builder<MessageModel>()
                .setQuery(query, MessageModel.class)
                .build();

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>(options) {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_item, parent, false);

                return new MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageModel model) {
                holder.sender.setText(String.valueOf(model.getSender()));
                holder.timeStamp.setText(String.valueOf(model.getTimeStamp()));
                holder.message.setText(String.valueOf(model.getMessage()));
            }
        };
        mRecyclerView.setAdapter(adapter);


        //   MessageModel mNewMessage = new MessageModel("alessandro", "11/05/2018", "we pirla");
        // myRef.child("users").child(userId).child("messages ").push().setValue(mNewMessage);


    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @OnClick({R.id.bAMEncrypt, R.id.bAMDecrypt})
    public void onButtonClick(View view) {
        if (view.getId() == R.id.bAMEncrypt) {
            Intent intent = new Intent(MainActivity.this, EncryptActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.bAMDecrypt) {
            Intent intent = new Intent(MainActivity.this, DecryptActivity.class);
            startActivity(intent);
        }
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


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView sender;
        TextView timeStamp;
        TextView message;
        View mView;

        public MessageViewHolder(View v) {
            super(v);
            mView = itemView;
            sender = v.findViewById(R.id.vsender);
            timeStamp = v.findViewById(R.id.vtimeStamp);
            message = v.findViewById(R.id.vmessage);
        }

    }


}
