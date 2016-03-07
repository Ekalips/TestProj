package com.example.djqrj.allah;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

public class EditorActivity extends AppCompatActivity {
    private boolean isFullScreen = false;
    PhotoViewAttacher attacher;
    RecyclerView recyclerView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        context = this;
        final ImageView imageView = (ImageView) findViewById(R.id.imageEditView);
        final Uri uri = Uri.parse(getIntent().getStringExtra("urilol"));
        imageView.setImageURI(uri);

        final ImageButton imageButton = (ImageButton) findViewById(R.id.menuBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        String[] array = {"2007","Inv","Brightness","Tint"};
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(array);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        TextView textView = (TextView) view.findViewById(R.id.itemText);
                        Log.d("lele",textView.getText().toString() + "text");
                        switch (textView.getText().toString())
                        {
                            case "2007":
                            {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                PicHentai.doBrightness(bitmap,20);
                                //imageView.setImageBitmap(PicHentai.doGreyscale(bitmap));
                                //attacher.update();
                                break;
                            }
                            case "Inv": break;
                            case "Brightness": break;
                            case "Tint": break;
                        }
                    }
                })
        );



//        attacher = new PhotoViewAttacher(imageView);
//        attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//            @Override
//            public void onPhotoTap(View view, float x, float y) {
//
//            }
//        });


    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.edit_menu, popup.getMenu());
        popup.show();
    }
}