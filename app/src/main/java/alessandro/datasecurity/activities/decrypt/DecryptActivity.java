package alessandro.datasecurity.activities.decrypt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import alessandro.datasecurity.R;
import alessandro.datasecurity.utils.Constants;
import alessandro.datasecurity.utils.GlideApp;
import alessandro.datasecurity.utils.StandardMethods;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DecryptActivity extends AppCompatActivity implements DecryptView {
  private FirebaseUser user;
  private String userId;
  FirebaseStorage storage;
  StorageReference storageRef;
  String url;
  String path;
  String from;
  String message;
//testing
  @BindView(R.id.ivStegoImage)
  ImageView ivStegoImage;

  /*@OnClick(R.id.ivStegoImage)
  public void onStegoImageClick() {
    if (ContextCompat.checkSelfPermission(getApplicationContext(),
      Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(DecryptActivity.this,
        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
        Constants.PERMISSIONS_EXTERNAL_STORAGE);

    } else {
      chooseImage();
    }

  }*/

  /*@OnClick(R.id.bDecrypt)
  public void onButtonClick() {
    if (isSISelected) {
      mPresenter.decryptMessage();
    } else {
      showToast(R.string.stego_image_not_selected);
    }
  }*/

  private ProgressDialog progressDialog;
  private DecryptPresenter mPresenter;
  private boolean isSISelected = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_decrypt);

    ButterKnife.bind(this);

    Intent intent = getIntent();
    String timeStamp = "20181129130629";
    if (intent != null) {
      Bundle bundle = intent.getExtras();
      timeStamp = bundle.getString("message");
      path = bundle.getString("path");
      from = bundle.getString("from");
      message = bundle.getString("message");
    }

    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Please wait...");
    //ivStegoImage.set;
    mPresenter = new DecryptPresenterImpl(this);
    initToolbar();



    user = FirebaseAuth.getInstance().getCurrentUser();
    if (user.getUid() != null) {
      userId = user.getUid();
    }

    url = null;
    storage = FirebaseStorage.getInstance();    //"mhfJdhpA2SMnjkf5RDI9IL5bdr22/20181129185331"
    storage.getReference().child(from+"/"+message).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
      @Override
      public void onSuccess(Uri uri) {
        // Got the download URL for 'users/me/profile.png' in uri

        GlideApp.with(getApplicationContext())
                .load(uri.toString())
                .into(ivStegoImage);
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception exception) {
        // Handle any errors
      }
    });


    mPresenter.selectImage(path);



   /* GlideApp.with(this *//* context *//*)
            .load(url)
            .into(ivStegoImage);*/
  }

  @Override
  public void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle("Decryption");
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    switch (requestCode) {
      case Constants.PERMISSIONS_EXTERNAL_STORAGE:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          chooseImage();
        }
        break;
    }
  }

  @Override
  public void chooseImage() {
    Intent intent = new Intent(
      Intent.ACTION_PICK,
      android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    intent.setType("image/*");
    startActivityForResult(
      Intent.createChooser(intent, getString(R.string.choose_image)),
      Constants.SELECT_FILE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      if (requestCode == Constants.SELECT_FILE) {
        Uri selectedImageUri = data.getData();
        String tempPath = getPath(selectedImageUri, DecryptActivity.this);
        if(tempPath != null) {
          mPresenter.selectImage(path);
        }
      }
    }
  }

  @Override
  public void startDecryptResultActivity(String secretMessage, String secretImagePath) {
    Intent intent = new Intent(DecryptActivity.this, DecryptResultActivity.class);

    if (secretMessage != null) {
      intent.putExtra(Constants.EXTRA_SECRET_TEXT_RESULT, secretMessage);
    }

    if (secretImagePath != null) {
      intent.putExtra(Constants.EXTRA_SECRET_IMAGE_RESULT, secretImagePath);
    }

    startActivity(intent);
  }

  public String getPath(Uri uri, Activity activity) {
    String[] projection = {MediaStore.MediaColumns.DATA};
    Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
    cursor.moveToFirst();
    //return cursor.getString(column_index);
    return path;
  }

  @Override
  public Bitmap getStegoImage() {
    return ((BitmapDrawable) ivStegoImage.getDrawable()).getBitmap();
  }

  @Override
  public void setStegoImage(File file) {
    showProgressDialog();
    Picasso.with(this)
      .load(file)
      .fit()
      .placeholder(R.mipmap.ic_launcher)
      .into(ivStegoImage);
    stopProgressDialog();
    isSISelected = true;
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
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.decryption_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_decrypt) {
      if (isSISelected) {
        mPresenter.decryptMessage();
      } else {
        showToast(R.string.stego_image_not_selected);
      }
    }

    return super.onOptionsItemSelected(item);
  }

}
