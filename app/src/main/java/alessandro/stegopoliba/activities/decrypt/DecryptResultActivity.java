package alessandro.stegopoliba.activities.decrypt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.squareup.picasso.Picasso;

import java.io.File;

import alessandro.stegopoliba.R;
import alessandro.stegopoliba.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DecryptResultActivity extends AppCompatActivity {

    @BindView(R.id.tvSecretMessage)
    TextView tvSecretMessage;

    @BindView(R.id.ivSecretImage)
    ImageView ivSecretImage;

    @BindView(R.id.bScanToDecode)
    Button bScanToDecode;


    private String secretImagePath;
    private String secretMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt_result);

        ButterKnife.bind(this);

        initToolbar();

        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            secretMessage = bundle.getString(Constants.EXTRA_SECRET_TEXT_RESULT);
            secretImagePath = bundle.getString(Constants.EXTRA_SECRET_IMAGE_RESULT);
        }

        if (secretMessage != null) {
            tvSecretMessage.setText(secretMessage);
        } else if (secretImagePath != null) {
            ivSecretImage.setVisibility(View.VISIBLE);
            setSecretImage(secretImagePath);
        }
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Decryption");
        }
    }

    public void setSecretImage(String path) {
        Picasso.with(this)
                .load(new File(path))
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .into(ivSecretImage);
    }


    @OnClick(R.id.bScanToDecode)
    public void onButtonClick() {
        startScan();

    }

    /*public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(android.os.Environment
                .getExternalStorageDirectory(), "temp.png");

        Uri imageUri = FileProvider.getUriForFile(this, "alexparunov", file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }*/


    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(DecryptResultActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        //   barcodeResult = barcode;
                        tvSecretMessage.setText(barcode.rawValue);
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }


}