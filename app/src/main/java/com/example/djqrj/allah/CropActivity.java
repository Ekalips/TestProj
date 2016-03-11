package com.example.djqrj.allah;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.djqrj.allah.Cropper.EditPhotoView;
import com.example.djqrj.allah.Cropper.EditableImage;
import com.example.djqrj.allah.Cropper.handler.OnBoxChangedListener;
import com.example.djqrj.allah.Cropper.model.ScalableBox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class CropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        final EditPhotoView imageView = (EditPhotoView) findViewById(R.id.editable_image);
        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final EditableImage image = new EditableImage(bmp);


        imageView.initView(this, image);

        imageView.setOnBoxChangedListener(new OnBoxChangedListener() {
            @Override
            public void onChanged(int x1, int y1, int x2, int y2) {
                //TODO: cropping box updated
            }
        });
        final Context context= this;
        Button btn = (Button) findViewById(R.id.cropBtn);

        String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

        int permsRequestCode = 200;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = image.cropOriginalImageToBitmap();
                Log.d("imagesBitmap", bitmap.toString());
                try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

//you can create a new file name "test.jpg" in sdcard folder.
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "filteredImage" + new Random().nextInt() + ".jpg");

                    f.createNewFile();

//write the bytes in file
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());

// remember close de FileOutput
                    fo.close();
                    MediaStore.Images.Media.insertImage(getContentResolver(),f.getAbsolutePath(),f.getName(),f.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context,"Saved as jpg",Toast.LENGTH_LONG).show();


            }
        });


    }
}
