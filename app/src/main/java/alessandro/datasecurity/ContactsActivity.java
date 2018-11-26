package alessandro.datasecurity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import alessandro.datasecurity.utils.CircleTransform;
import alessandro.datasecurity.utils.Database;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ContactsActivity extends AppCompatActivity {
    private String userId;
    private FirebaseUser user;
    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    static FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseRecyclerAdapter<User, ContactsViewHolder> mContactsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        setTitle("Nuovo messaggio");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.contacts_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);

        database = Database.getDatabase();
        myRef = FirebaseDatabase.getInstance().getReference();
        //"News" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");
        mDatabase.keepSynced(true);

        mPeopleRV = (RecyclerView) findViewById(R.id.recycler_view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid() != null) {
            userId = user.getUid();
        }

       Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .getRef();

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        mContactsRVAdapter = new FirebaseRecyclerAdapter<User, ContactsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(ContactsViewHolder holder, final int position, final User model) {
                holder.setName(model.getName());

                applyProfilePicture(holder, model);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* final String url = model.getUrl();
                        Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
                        intent.putExtra("id", url);
                        startActivity(intent);*/
                    }
                });
            }

            @Override
            public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.contact_list_row, parent, false);

                return new ContactsViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mContactsRVAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mContactsRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mContactsRVAdapter.stopListening();

    }


    private void applyProfilePicture(ContactsViewHolder holder, User contact) {
        if (!TextUtils.isEmpty(contact.getPicture())) {
            Glide.with(this).load(contact.getPicture())
                    .thumbnail(0.5f)
                    .transition(withCrossFade())
                    .apply(RequestOptions.bitmapTransform(new CircleTransform(this)))
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
            holder.iconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(contact.getColor());
            holder.iconText.setVisibility(View.VISIBLE);
        }
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView iconText;
        public ImageView imgProfile;
        public ContactsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            iconText =  mView.findViewById(R.id.icon_text);
            imgProfile = mView.findViewById(R.id.icon_profile);
        }
        public void setName(String name){
            TextView vname =  mView.findViewById(R.id.fullname);
            vname.setText(name);
        }

    }
}