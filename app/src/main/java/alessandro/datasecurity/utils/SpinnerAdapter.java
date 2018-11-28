package alessandro.datasecurity.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import alessandro.datasecurity.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SpinnerAdapter extends ArrayAdapter<String> {

    String[] spinnerTitles;
    String[] spinnerImages;
    Context mContext;

    public SpinnerAdapter(@NonNull Context context, String[] titles, String[] images) {
        super(context, R.layout.contact_list_row);
        this.spinnerTitles = titles;
        this.spinnerImages = images;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return spinnerTitles.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.contact_list_row, parent, false);
            mViewHolder.mIconProfile = (ImageView) convertView.findViewById(R.id.icon_profile);
            mViewHolder.mIconText = (TextView) convertView.findViewById(R.id.icon_text);
            mViewHolder.mFullName = (TextView) convertView.findViewById(R.id.fullname);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mIconText.setText(spinnerTitles[position].substring(0, 1));
        applyProfilePicture(mViewHolder, spinnerImages[position]);

        return convertView;
    }


    private void applyProfilePicture(ViewHolder holder, String photoUrl) {
        if (!TextUtils.isEmpty(photoUrl)) {
            Glide.with(mContext).load(photoUrl)
                    .thumbnail(0.5f)
                    .transition(withCrossFade())
                    .apply(RequestOptions.bitmapTransform(new CircleTransform(mContext)))
                    .into(holder.mIconProfile);
            holder.mIconProfile.setColorFilter(null);
            holder.mIconText.setVisibility(View.GONE);
        } else {
            holder.mIconProfile.setImageResource(R.drawable.bg_circle);
            //holder.mIconProfile.setColorFilter(message.getColor());
            holder.mIconText.setVisibility(View.VISIBLE);
        }
    }

    private static class ViewHolder {
        ImageView mIconProfile;
        TextView mIconText;
        TextView mFullName;
    }
}