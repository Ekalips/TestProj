package com.example.djqrj.allah;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import uk.co.senab.photoview.PhotoViewAttacher;

public class EditorActivity extends AppCompatActivity implements GLSurfaceView.Renderer{
    private boolean isFullScreen = false;
    PhotoViewAttacher attacher;
    RecyclerView recyclerView;
    ImageButton imageButton;
    Context context;

    private GLSurfaceView mEffectView;
    private int[] mTextures = new int[2];
    private EffectContext mEffectContext;
    private Effect mEffect;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int mImageWidth;
    private int mImageHeight;
    private boolean mInitialized = false;
    int mCurrentEffect;
    Uri uri;
    EffectCollection effectCollection = new EffectCollection();
    public void setCurrentEffect(int effect) {
        mCurrentEffect = effect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        context = this;
        imageButton = (ImageButton) findViewById(R.id.menuBtn);
        uri = Uri.parse(getIntent().getStringExtra("urilol"));
        mEffectView = (GLSurfaceView) findViewById(R.id.effectsview);
        mEffectView.setEGLContextClientVersion(2);
        mEffectView.setRenderer(this);
        mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mCurrentEffect = 0;



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




        RecyclerViewAdapter adapter = new RecyclerViewAdapter(effectCollection);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        TextView textView = (TextView) view.findViewById(R.id.itemText);
                        Log.d("lele",textView.getText().toString());
                          setCurrentEffect(effectCollection.getEffect(position).id);
                          mEffectView.requestRender();
                    }
                })
        );



//        attacher = new PhotoViewAttacher(mEffectView);
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
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setCurrentEffect(item.getItemId());
                mEffectView.requestRender();
                return true;
            }
        });
        popup.show();
    }

    private void loadTextures() {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);

        // Load input bitmap
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Set texture parameters
        GLToolbox.initTexParams();
    }

    private void initEffect() {
        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        /**
         * Initialize the correct effect based on the selected menu/action item
         */
        switch (mCurrentEffect) {

            case 0:
                break;

            case 1:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_AUTOFIX);
                mEffect.setParameter("scale", 0.5f);
                break;

            case 2:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("black", .1f);
                mEffect.setParameter("white", .7f);
                break;

            case 3:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", 2.0f);
                break;

            case 4:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", 1.4f);
                break;

            case 5:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CROSSPROCESS);
                break;

            case 6:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_DOCUMENTARY);
                break;

            case 7:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_DUOTONE);
                mEffect.setParameter("first_color", Color.YELLOW);
                mEffect.setParameter("second_color", Color.DKGRAY);
                break;

            case 8:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", .8f);
                break;

            case 9:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FISHEYE);
                mEffect.setParameter("scale", .5f);
                break;

            case 10:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("vertical", true);
                break;

            case 11:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("horizontal", true);
                break;

            case 12:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", 1.0f);
                break;

            case 13:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAYSCALE);
                break;

            case 14:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_LOMOISH);
                break;

            case 15:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_NEGATIVE);
                break;

            case 16:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_POSTERIZE);
                break;

            case 17:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_ROTATE);
                mEffect.setParameter("angle", 180);
                break;

            case 18:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SATURATE);
                mEffect.setParameter("scale", .5f);
                break;

            case 19:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SEPIA);
                break;

            case 20:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SHARPEN);
                break;

            case 21:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_TEMPERATURE);
                mEffect.setParameter("scale", .9f);
                break;

            case 22:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_TINT);
                mEffect.setParameter("tint", Color.MAGENTA);
                break;

            case 23:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_VIGNETTE);
                mEffect.setParameter("scale", .5f);
                break;

            case 24:


            default:
                break;

        }
    }
    private void applyEffect() {
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    private void renderResult() {
        if (mCurrentEffect != 0) {
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        }
        else {
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (mTexRenderer != null) {
            mTexRenderer.updateViewSize(width, height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadTextures();
            mInitialized = true;
        }
        if (mCurrentEffect != 0 && mCurrentEffect != 24) {
            //if an effect is chosen initialize it and apply it to the texture
            initEffect();
            applyEffect();
        }
        renderResult();

        if (mCurrentEffect == 24)
        {
          //  Intent intent = new Intent(this, CropActivity.class);
          //  startActivityForResult(intent,1);

//            final Bitmap bitmap = takeScreenshot(gl);
//            this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    imageButton.setImageBitmap(bitmap);
//                }
//            });


            try {
                int w = mImageWidth ;
                int h = mImageHeight  ;

                Log.i("hari", "w:"+w+"-----h:"+h);

                int b[]=new int[(int) (w*h)];
                int bt[]=new int[(int) (w*h)];
                IntBuffer buffer=IntBuffer.wrap(b);
                buffer.position(0);
                GLES20.glReadPixels(0, 0, w, h,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE, buffer);
                for(int i=0; i<h; i++)
                {
                    //remember, that OpenGL bitmap is incompatible with Android bitmap
                    //and so, some correction need.
                    for(int j=0; j<w; j++)
                    {
                        int pix=b[i*w+j];
                        int pb=(pix>>16)&0xff;
                        int pr=(pix<<16)&0x00ff0000;
                        int pix1=(pix&0xff00ff00) | pr | pb;
                        bt[(h-i-1)*w+j]=pix1;
                    }
                }
                Bitmap inBitmap = null ;
                if (inBitmap == null || !inBitmap.isMutable()
                        || inBitmap.getWidth() != w || inBitmap.getHeight() != h) {
                    inBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                }
                //Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                inBitmap.copyPixelsFromBuffer(buffer);
                //return inBitmap ;
                // return Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
                inBitmap = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);

               // ByteArrayOutputStream bos = new ByteArrayOutputStream();
               // inBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);

                final ImageView imageView = (ImageView) findViewById(R.id.imageView);
                final Bitmap finalInBitmap = inBitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(finalInBitmap);
                    }
                });


            }
        catch(Exception e) {
            e.printStackTrace() ;
        }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setCurrentEffect(item.getItemId());
        mEffectView.requestRender();
        return true;
    }


    public Bitmap takeScreenshot(GL10 mGL) {
        final int mWidth = mEffectView.getWidth();
        final int mHeight = mEffectView.getHeight();
        IntBuffer ib = IntBuffer.allocate(mWidth * mHeight);
        IntBuffer ibt = IntBuffer.allocate(mWidth * mHeight);


        mGL.glReadPixels(0, 0, mWidth, mHeight, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

        // Convert upside down mirror-reversed image to right-side up normal
        // image.
        for (int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++) {
                ibt.put((mHeight - i - 1) * mWidth + j, ib.get(i * mWidth + j));
            }
        }

        Bitmap mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mBitmap.copyPixelsFromBuffer(ibt);
        return mBitmap;
    }
}