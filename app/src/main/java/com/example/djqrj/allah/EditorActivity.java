package com.example.djqrj.allah;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class EditorActivity extends AppCompatActivity {
    private boolean isFullScreen = false;
    PhotoViewAttacher attacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ImageView imageView = (ImageView) findViewById(R.id.imageEditView);
        imageView.setImageURI(Uri.parse(getIntent().getStringExtra("urilol")));
        attacher = new PhotoViewAttacher(imageView);
        attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (!isFullScreen) {
                    if (Build.VERSION.SDK_INT < 16) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                    else {
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                        View decorView2 = getWindow().getDecorView();
                        int uiOptions2 = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView2.setSystemUiVisibility(uiOptions2);
                    }
                    isFullScreen = true;
                }
                else {
                    if (Build.VERSION.SDK_INT >= 16)
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    else{
                        View decorView2 = getWindow().getDecorView();
                        decorView2.setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                    isFullScreen = false;
                }

            }
        });
    }
}