package com.example.djqrj.allah;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.djqrj.allah.Cropper.EditPhotoView;
import com.example.djqrj.allah.Cropper.EditableImage;
import com.example.djqrj.allah.Cropper.handler.OnBoxChangedListener;
import com.example.djqrj.allah.Cropper.model.ScalableBox;

public class CropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        final EditPhotoView imageView = (EditPhotoView) findViewById(R.id.editable_image);
        final EditableImage image = new EditableImage(this, R.drawable.puppy);

        imageView.initView(this, image);

        imageView.setOnBoxChangedListener(new OnBoxChangedListener() {
            @Override
            public void onChanged(int x1, int y1, int x2, int y2) {
                //TODO: cropping box updated
            }
        });

        Button btn = (Button) findViewById(R.id.cropBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = image.cropOriginalImageToBitmap();


            }
        });
    }
}
