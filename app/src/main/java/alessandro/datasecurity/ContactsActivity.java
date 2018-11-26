package alessandro.datasecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ContactsActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference;
    private String userId;
    ArrayList<User> contacts = new ArrayList<User>();
    TextView mEmptyView;
    FirebaseRecyclerAdapter<User, MyViewHolder> adapter;


    public ContactsActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);

        fillRecyclerView();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


    /*    mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {


            @Override
            public void onItemClick(View view, int position) {

                View w = mRecyclerView.getLayoutManager().findViewByPosition(position);
                TextView name =  w.findViewById(R.id.fullname);

                Intent intent = new Intent(view.getContext(), ExamDetailsActivity.class);
                intent.putExtra("name", name.getText().toString());
                startActivity(intent);


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
*/

        contacts = new ArrayList<User>();
        mDatabaseReference = database.getReference();


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        mDatabaseReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot examSnapshot : dataSnapshot.getChildren()) {

                            User contact = examSnapshot.getValue(User.class);
                            contacts.add(contact);
                            Log.e("Chat", "mDatabasereference: " + contacts.size());


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    public void fillRecyclerView() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mDatabaseReference = database.getReference();
        if (mRecyclerView != null) {

            mRecyclerView.setHasFixedSize(true);
        }


        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);*/


        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("News");
        Query personsQuery = personsRef.orderByKey();


        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<User>().setQuery(personsQuery, User.class).build();

        adapter = new FirebaseRecyclerAdapter<User, MyViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(MyViewHolder holder, final int position, final User model) {
                holder.setName(model.getName());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  final String url = model.getUrl();
                        /*Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
                        intent.putExtra("id", url);
                        startActivity(intent);*/
                    }
                });
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.contact_list_row, parent, false);

                return new MyViewHolder(view);
            }
        };

        mRecyclerView.setAdapter(adapter);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {

            String key = data.getStringExtra("key");
            mDatabaseReference.child(key).removeValue();


        } else {
        }


    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MyViewHolder(View v) {
            super(v);
            mView = itemView;

        }

        public void setName(String name) {
            TextView vname = mView.findViewById(R.id.fullname);
            vname.setText(name);
        }


    }

}
