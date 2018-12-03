package alessandro.datasecurity.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    private static FirebaseUser user;
    private DatabaseReference myRef;
    static FirebaseDatabase database;
    private static String userId;

    public static String getIdCurrentTimestamp() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormatGmt.format(new Date());
    }


    public static String getCurrentTimestamp() {//"yyyy/MM/dd/HH/mm/ss"
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd MMM");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormatGmt.format(new Date());
    }

    public static String getTimestamp(String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
            Date date = sdf.parse(timestamp);
            long currentDate = date.getTime();

            long millis = TimeZone.getDefault().getOffset(currentDate);
            long hour = (millis / (1000 * 60 * 60)) % 24;
            long minutes = (millis / (1000 * 60)) % 60;

            String[] timestampPart = timestamp.split("/");
            long h = Long.parseLong(timestampPart[3]);
            long m = Long.parseLong(timestampPart[4]);
            h += hour;
            h %= 24;
            m += minutes;
            m %= 60;

            String output = h + ":" + m;
            if (h < 10) {
                if (m < 10) {
                    output = "0" + h + ":0" + m;
                } else {
                    output = "0" + h + ":" + m;
                }
            } else if (m < 10) {
                output = h + ":0" + m;
            }
            return output;

        } catch (ParseException e) {

        }
        return null;
    }

    public static String getUser(){

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid() != null) {
            userId = user.getUid();
        }

        return userId;
    }





}
