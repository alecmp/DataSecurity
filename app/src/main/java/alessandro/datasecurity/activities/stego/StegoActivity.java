package alessandro.datasecurity.activities.stego;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import alessandro.datasecurity.MessageModel;
import alessandro.datasecurity.R;
import alessandro.datasecurity.utils.Constants;
import alessandro.datasecurity.utils.StandardMethods;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StegoActivity extends AppCompatActivity implements StegoView {


  @BindView(R.id.ivStegoImage)
  ImageView ivStegoImage;
  private String receiverId;

  @OnClick({R.id.bStegoSave, R.id.bStegoShare})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.bStegoSave:
        if(!isSaved) {
          isSaved = mPresenter.saveStegoImage(stegoImagePath);
        } else {
          showToast(R.string.image_is_saved);
        }
        break;
      case R.id.bStegoShare:
        shareStegoImage(stegoImagePath);
        break;
    }
  }


  private ProgressDialog progressDialog;
  private StegoPresenter mPresenter;

  private String stegoImagePath = "";
  private boolean isSaved = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stego);

    ButterKnife.bind(this);

    initToolbar();

    Intent intent = getIntent();

    if (intent != null) {
      Bundle bundle = intent.getExtras();
      receiverId = bundle.getString("receiverId");
      stegoImagePath = bundle.getString(Constants.EXTRA_STEGO_IMAGE_PATH);
    }

    setStegoImage(stegoImagePath);

    mPresenter = new StegoPresenterImpl(this);

    progressDialog = new ProgressDialog(StegoActivity.this);
    progressDialog.setMessage("Please wait...");
  }

  @Override
  public void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle("Stego Image");
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.send_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_send) {



      shareStegoImage(stegoImagePath);
         /* if(!isSaved) {
            isSaved = mPresenter.saveStegoImage(stegoImagePath);
          } else {
            showToast(R.string.image_is_saved);
          }*/
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void setStegoImage(String path) {
    showProgressDialog();
    Picasso.with(this)
      .load(new File(path))
      .fit()
      .placeholder(R.mipmap.ic_launcher)
      .into(ivStegoImage);
    stopProgressDialog();
  }

  @Override
  public void showToast(int message) {
    StandardMethods.showToast(this, message);
  }

  @Override
  public void showProgressDialog() {
    if (progressDialog != null && !progressDialog.isShowing()) {
      progressDialog.show();
    }
  }

  @Override
  public void stopProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  @Override
  public void saveToMedia(Intent intent) {
    sendBroadcast(intent);
  }

  @Override
  public void shareStegoImage(String path) {
    Uri imageURI = null;


    Bitmap icon =((BitmapDrawable)ivStegoImage.getDrawable()).getBitmap();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    icon.compress(Bitmap.CompressFormat.PNG, 100, baos);
    byte[] b = baos.toByteArray();
    String temp = Base64.encodeToString(b, Base64.DEFAULT);


    if(stegoImagePath != null) {
      imageURI = Uri.fromFile(new File(path));
     /* Intent share = new Intent(Intent.ACTION_SEND);
      share.setType("image/jpeg");
      imageURI = Uri.fromFile(new File(path));
      share.putExtra(Intent.EXTRA_STREAM, imageURI);
      startActivity(Intent.createChooser(share, "Share using..."));*/
    }

    ////////////////////////////////////////////////
    DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReference()
            .child("messages")
            .child(receiverId)
            .getRef();
    MessageModel mNewMessage = new MessageModel(1, "Asdrubale", "info top secret", temp, "22 Nov 2018", null);
    ref.push().setValue(mNewMessage);



    /////////////////////////////////////////////////
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if(!isSaved) {
      if(stegoImagePath != null) {
        new File(stegoImagePath).delete();
      }
    }
  }
}
