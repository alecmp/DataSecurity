package alessandro.datasecurity.activities.decrypt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.scottyab.aescrypt.AESCrypt;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.security.GeneralSecurityException;

import alessandro.datasecurity.utils.Constants;
import alessandro.datasecurity.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static alessandro.datasecurity.InboxFragment.MyPREFERENCES;

public class DecryptResultActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    @BindView(R.id.tvSecretMessage)
    TextView tvSecretMessage;

    @BindView(R.id.tvSecretSubject)
    TextView tvSecretSubject;

    @BindView(R.id.ivSecretImage)
    ImageView ivSecretImage;

    @BindView(R.id.bScanToDecode)
    Button bScanToDecode;


    private String secretImagePath;
    private String secretMessage, secretSubject, id;

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
            secretSubject =  bundle.getString(Constants.EXTRA_SECRET_SUBJECT_RESULT);
            id =  bundle.getString("id");
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
                .placeholder(R.drawable.no_img_placeholder)
                .into(ivSecretImage);
    }


    @OnClick(R.id.bScanToDecode)
    public void onButtonClick() {
        startScan();

    }

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
                        //tvSecretMessage.setText(barcode.rawValue);
                        tvSecretMessage.setText(decryptAES(barcode.rawValue, secretMessage));
                        tvSecretSubject.setText(decryptAES(barcode.rawValue, secretSubject));
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(id+"S", tvSecretSubject.getText().toString());
                        editor.putString(id+"M", tvSecretMessage.getText().toString());
                        editor.commit();




                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }


    private String decryptAES(String password, String message) {
        String messageAfterDecrypt = null;
        try {
            messageAfterDecrypt = AESCrypt.decrypt(password, message);

        } catch (GeneralSecurityException e) {
            //handle error - could be due to incorrect password or tampered encryptedMsg
        }
        return messageAfterDecrypt;
    }


}