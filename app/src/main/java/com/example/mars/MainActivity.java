package com.example.mars;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Api;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.ObjectUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Context context;
    private String pictureLocalPath = null;
    private final int INTENT_REQUEST_PHOTO = 101;
    private final int INTENT_REQUEST_GALLERY = 102;
    private static final int INTENT_REQUEST_GALLERY_AFTER_KITKAT = 103;
    MediaManager mediaManager;
    ProgressBar  progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView upload = findViewById(R.id.upload);
        TextView List = findViewById(R.id.list);
        progressBar  = findViewById(R.id.pbHeaderProgress);
        context =this;
        Map config = ObjectUtils.asMap(
                "cloud_name", "dm7*****",
                "api_key", "***********",
                "api_secret", "****************");
        try{
            mediaManager.init(context,config);
        }catch (IllegalMonitorStateException ex)
        {
            ex.printStackTrace();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                   checkforPermission();

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(MainActivity.this,Activity_ImageList.class));
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void ListVi(String fileName) throws Exception {
        Toast.makeText(MainActivity.this,"Please wait...You will get a message once image upload",Toast.LENGTH_LONG).show();
        String requestId = mediaManager.get().upload(Uri.parse(fileName).getPath()).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {


            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {


            }

            @Override
            public void onSuccess(String requestId, Map resultData) {

                Toast.makeText(MainActivity.this,"Image Uploaded",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {

            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        })
                .unsigned("*******")
                .option("folder", "xyz")
                .option("tags", "abc")
                .dispatch();
    }

    public  void selectImage()
    {
        final CharSequence[] options = {getString(R.string.option_take_photo), getString(R.string.option_choose_from_gallery), getString(R.string.dialog_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_photo);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                try {
                    if (options[item].equals(options[0])) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File photoFile = createImageFile();
                        pictureLocalPath = photoFile.getAbsolutePath();

                        // Continue only if the File was successfully created

                        Uri photoURI;

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            photoURI = Uri.fromFile(photoFile);
                        } else {
                            photoURI = FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getPackageName()+".fileprovider", photoFile);
                        }

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, INTENT_REQUEST_PHOTO);


                    } else if (options[item].equals(options[1])) {

                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, INTENT_REQUEST_GALLERY);

                        } else {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(intent, INTENT_REQUEST_GALLERY_AFTER_KITKAT);
                        }


                    } else if (options[item].equals(options[2])) {
                        dialog.dismiss();
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.show();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = MainActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }
    public void checkforPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, 0);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            if (requestCode == 0) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    selectImage();

                } else {
                    boolean camera = shouldShowRequestPermissionRationale(permissions[0]);
                    boolean storage = shouldShowRequestPermissionRationale(permissions[1]);
                    if (!camera && !storage ) {
                        Toast.makeText(MainActivity.this, "Please Allow Permissions", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {

                        ListVi(resultUri.toString());
                    }catch (Exception ex)
                    {
                        Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

            } else if (requestCode == INTENT_REQUEST_PHOTO) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = Uri.fromFile(new File(pictureLocalPath));
                    startCropImageActivity(imageUri);
                }

            } else if (requestCode == INTENT_REQUEST_GALLERY) {
                if (data != null && data.getData() != null) {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = MainActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        pictureLocalPath = c.getString(columnIndex);
                        c.close();
                    }

                    Uri imageUri = Uri.fromFile(new File(pictureLocalPath));
                    startCropImageActivity(imageUri);
                }

            } else if (requestCode == INTENT_REQUEST_GALLERY_AFTER_KITKAT) {
                if (data != null && data.getData() != null) {
                    Uri originalUri = data.getData();
                    if(originalUri.getAuthority().equalsIgnoreCase("com.google.android.apps.docs.storage")){
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(MainActivity.this.getContentResolver().openInputStream(originalUri));
                            File file = createImageFile();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                            startCropImageActivity(Uri.fromFile(file));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        // Check for the freshest data.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            MainActivity.this.getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
                        }
                        String id = originalUri.getLastPathSegment().split(":")[1];
                        final String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor cursor = MainActivity.this.getContentResolver().query(getUri(), filePath,
                                MediaStore.Images.Media._ID + "=" + id, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            pictureLocalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            cursor.close();
                        }


                        Uri imageUri = Uri.fromFile(new File(pictureLocalPath));
                        startCropImageActivity(imageUri);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();

        }

    }

    private void startCropImageActivity(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowRotation(true)
                .setActivityTitle("Profile Picture")
                .setMultiTouchEnabled(true)
                .setOutputUri(imageUri)
                .start( this);
    }
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

}
