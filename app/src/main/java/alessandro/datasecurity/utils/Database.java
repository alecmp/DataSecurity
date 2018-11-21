package alessandro.datasecurity.utils;

import com.google.firebase.database.FirebaseDatabase;

public class Database {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);

        }

        return mDatabase;

    }

}
