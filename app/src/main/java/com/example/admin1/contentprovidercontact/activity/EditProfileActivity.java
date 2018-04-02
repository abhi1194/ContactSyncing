package com.example.admin1.contentprovidercontact.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin1.contentprovidercontact.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mNameEt,mNumberEt;
    private CircleImageView mUserPicCiv;
    private Button mSaveBtn;
    private  AlertDialog mAlertDialog;
    private static int SELECT_FILE = 100;
    private static int CHOOSE_CAMERA = 200;
    private Uri mUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();
        getDataFromMainActivity();
        mUserPicCiv.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
    }

    /**
     * Initialization of views
     */
    private void initViews(){
        mNameEt = findViewById(R.id.et_name);
        mNumberEt = findViewById(R.id.et_number);
        mUserPicCiv = findViewById(R.id.civ_pic);
        mSaveBtn = findViewById(R.id.btn_save);
        mAlertDialog= new AlertDialog.Builder(this).create();
    }

    /**
     * Getting data from MainActivity.java & set them on the views
     */
    private void getDataFromMainActivity(){
        Intent intent = getIntent();
        mNameEt.setText(intent.getStringExtra("name"));
        mNumberEt.setText(intent.getStringExtra("number"));
        Glide.with(this)
                .load(intent.getStringExtra("photo"))
                .error(R.drawable.ic_account_circle_black_24dp)
                .into(mUserPicCiv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                sendDataToMainActivity();
                break;
            case R.id.civ_pic:
                final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog,null);
                mAlertDialog.setView(dialogView);
                // function for initializing views
                initDialogViews(dialogView);
                mAlertDialog.show();
                break;
            case R.id.tv_gallery:
                galleryPermission();
                break;
            case R.id.tv_camera:
                cameraPermission();
                break;
            case R.id.tv_remove:
                Glide.clear(mUserPicCiv);
                mUserPicCiv.setImageResource(R.drawable.ic_account_circle_black_24dp);
                mAlertDialog.dismiss();
                break;
            case R.id.tv_cancel:
                mAlertDialog.dismiss();
                break;
        }
    }

    /**
     *Initializing views of AlertDialog
     * @param view R.layout.custom_dialog
     */
    private void initDialogViews(View view){
        TextView galleryTv,cameraTv,removeTv,cancelTv;
        galleryTv = view.findViewById(R.id.tv_gallery);
        cameraTv = view.findViewById(R.id.tv_camera);
        removeTv = view.findViewById(R.id.tv_remove);
        cancelTv = view.findViewById(R.id.tv_cancel);

        galleryTv.setOnClickListener(this);
        cameraTv.setOnClickListener(this);
        removeTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
    }

    /**
     * Opens gallery for selecting images
     */
    private void galleryIntent(){
        mAlertDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,"Select File"),SELECT_FILE);
    }

    /**
     * Runtime Permission for Gallery
     */
    private void galleryPermission(){
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},4);
            }else {
                galleryIntent();
            }
        }else{
            galleryIntent();
        }
    }

    /**
     * opening camera of the phone
     */
    private void cameraIntent(){
        mAlertDialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!= null){
            File photoFile = null;
            try {
                photoFile= createImageFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            //Continue if the File is created successfully
            if (photoFile!=null){
                mUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,mUri);
                startActivityForResult(intent, CHOOSE_CAMERA);
              }
        }
    }

    /**
     * Runtime Permission for Camera
     */
    private void cameraPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);
            }else if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},8);
            }else if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},8);
            }else {
                cameraIntent();
            }
        } else {
            cameraIntent();
        }
    }

    /**
     * on Request Granted
     * @param requestCode 4 & 8
     * @param permissions Gallery & Camera
     * @param grantResults Permission granted or denied
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 4:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    galleryIntent();
                }else {
                    Toast.makeText(this,"You have to give the permission",Toast.LENGTH_LONG).show();
                }
                break;
            case 8:
                if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    cameraIntent();
                }else if(grantResults.length==1 && ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},8);
                }else if(grantResults.length==1 && ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},8);
                }
                break;
        }
    }

    /**
     * Handling result of Gallery & Camera
     * @param requestCode SELECT_FILE , CHOOSE_CAMERA
     * @param resultCode RESULT_OK, RESULT_CANCEL
     * @param data uri of the images
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECT_FILE && resultCode==RESULT_OK){
            onGalleryImageResult(data);
        }else if(requestCode==CHOOSE_CAMERA && resultCode==RESULT_OK){
            onImageResult();
        }
    }

    /**
     * Set the image on the ImageView after creating the Uri
     */
    private void onImageResult(){
        Glide.with(this)
                    .load(mUri)
                    .error(R.drawable.ic_account_circle_black_24dp)
                    .into(mUserPicCiv);
    }

    /**
     * Set the image on the imageView from the gallery
     * @param intent data from onActivityResult through Gallery
     */
    private void onGalleryImageResult(Intent intent){
        mUri = intent.getData();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),mUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Glide.clear(mUserPicCiv);
        mUserPicCiv.setImageBitmap(bitmap);
    }

    /**
     * Creating file of the picture when taking from camera
     * @return File
     * @throws IOException when there is no input to create the file
     */
    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "MyContact" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    /**
     * Sending updated data back to MainActivity.java
     */
    private void sendDataToMainActivity()
    {
        Intent intent = new Intent();
        intent.putExtra("name",mNameEt.getText().toString());
        intent.putExtra("phone",mNumberEt.getText().toString());
        if (mUri!=null) {
            intent.putExtra("image", mUri.toString());
        }else {
            intent.putExtra("image","");
        }
        setResult(RESULT_OK,intent);
        finish();
    }
}
