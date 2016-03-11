package com.example.djqrj.allah;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class StartActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    Context context;
    private String selectedImagePath;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        context = this;
        final RelativeLayout lay = (RelativeLayout) findViewById(R.id.mainRelativeLay);
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageBrowser();
            }
        });
        Button btn = (Button) findViewById(R.id.GoBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageBrowser();
            }
        });
    }

    private void OpenImageBrowser() {
        Intent intent = new Intent();
        intent.setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE)
            {
                Uri selectedImageUri = data.getData();

                Intent intent = new Intent(this,EditorActivity.class);
                intent.putExtra("urilol",selectedImageUri.toString());
                startActivity(intent);


            }
        }
    }

    private String getPath(Uri selectedImageUri) {
        if (selectedImageUri == null) return  null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(selectedImageUri,proj,null,null,null);
        if (cursor != null)
        {
            int collInd = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(collInd);
        }
        return selectedImageUri.getPath();
    }
}