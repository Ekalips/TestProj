package com.crutchbike.ekalips.filtering;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

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
    public int bitmapX;
    float ScaleBar = 0.5f;
    private float ScaleBar2 = 0.5f;
    int ColorBar = Color.MAGENTA;
    int ColorBar2 = Color.YELLOW;
    ImageView imageView;
    int SeecBar = 90;
    private boolean mInitialized = false;
    int mCurrentEffect;
    public int mImageViewHeight,mImageViewWidth;
    List<EffectCollection.Effect> effectsList = new ArrayList<>();
    TextView textIndicator;
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
        uri = Uri.parse(getIntent().getStringExtra("urilol"));

        mEffectView = (GLSurfaceView) findViewById(R.id.effectsview);
        mEffectView.setVisibility(View.INVISIBLE);
        mEffectView.setEGLContextClientVersion(2);
        mEffectView.setRenderer(this);
        mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mCurrentEffect = 0;

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmap);
        final ImageView imageButton = (ImageView) findViewById(R.id.menuBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentEffect = 24;
                mEffectView.requestRender();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(effectCollection);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        TextView textView = (TextView) view.findViewById(R.id.itemText);
                        Log.d("lele", new Integer(position).toString());
                        effectsList.add(effectCollection.getEffect(position));
                        //textIndicator.setText(effectsList.size());
                            if( position==1 ||
                                position==3 ||
                                position==4 ||
                                position==8 ||
                                position==9 ||
                                position==12 ||
                                position==18 ||
                                position==21 ||
                                position==23
                            ){
                                Log.d("lele", "COMON");
                                recyclerView.animate().translationY(recyclerView.getHeight()).start();
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.seekBarLay);
                                linearLayout.setPadding(0, 0, 0, 26);
                                SeekBar bar = new SeekBar(context);
                                bar.setVisibility(View.VISIBLE);
                                bar.setProgress(50);
                                bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                  //  int pro = 50;
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){}

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar){
                                        //seekBar.setProgress(pro);
                                    }
                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar){
                                     //   pro = seekBar.getProgress();

                                        ScaleBar = ((float)seekBar.getProgress()/100f);
                                        mEffectView.requestRender();
                                    }
                                });
                                linearLayout.addView(bar);
                               // linearLayout.animate().translationY(-200).start();
                            }
                        else if (position == 22)
                            {
                                recyclerView.animate().translationY(recyclerView.getHeight()).start();
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.seekBarLay);
                                linearLayout.setPadding(0, 0, 0, 26);
                                ColorPickerSeekBar bar = new ColorPickerSeekBar(context);
                                bar.setVisibility(View.VISIBLE);
                                bar.setProgress(50);
                                bar.setOnColorSeekbarChangeListener(new ColorPickerSeekBar.OnColorSeekBarChangeListener() {
                                    @Override
                                    public void onColorChanged(SeekBar seekBar, int color, boolean b) {
                                        ColorBar = color;
                                        mEffectView.requestRender();
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });
                                linearLayout.addView(bar);
                            }
                        else if (position == 2)
                            {
                                recyclerView.animate().translationY(recyclerView.getHeight()).start();
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.seekBarLay);
                                linearLayout.setPadding(0, 0, 0, 26);
                                SeekBar bar = new SeekBar(context);
                                bar.setVisibility(View.VISIBLE);
                                bar.setProgress(70);
                                ScaleBar = ((float)bar.getProgress()/100f);
                                bar.setPadding(bar.getPaddingLeft(),bar.getPaddingTop(),bar.getPaddingRight(),20);
                                bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    //  int pro = 50;
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){}

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar){
                                        //seekBar.setProgress(pro);
                                    }
                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar){
                                        //   pro = seekBar.getProgress();

                                        ScaleBar = ((float)seekBar.getProgress()/100f);
                                        mEffectView.requestRender();
                                    }
                                });
                                linearLayout.addView(bar);

                                SeekBar bar2 = new SeekBar(context);
                                bar2.setVisibility(View.VISIBLE);
                                bar2.setProgress(10);
                                ScaleBar2 = ((float)bar2.getProgress()/100f);
                                bar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    //  int pro = 50;
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){}

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar){
                                        //seekBar.setProgress(pro);
                                    }
                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar){
                                        //   pro = seekBar.getProgress();

                                        ScaleBar2 = ((float)seekBar.getProgress()/100f);
                                        mEffectView.requestRender();
                                    }
                                });
                                linearLayout.addView(bar2);
                            }
                        else if (position == 7)
                            {
                                recyclerView.animate().translationY(recyclerView.getHeight()).start();
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.seekBarLay);
                                linearLayout.setPadding(0, 0, 0, 26);
                                ColorPickerSeekBar bar = new ColorPickerSeekBar(context);
                                bar.setVisibility(View.VISIBLE);
                                bar.setProgress(70);
                                bar.setPadding(bar.getPaddingLeft(),bar.getPaddingTop(),bar.getPaddingRight(),20);
                                bar.setOnColorSeekbarChangeListener(new ColorPickerSeekBar.OnColorSeekBarChangeListener() {
                                    @Override
                                    public void onColorChanged(SeekBar seekBar, int color, boolean b) {
                                        ColorBar = color;
                                        mEffectView.requestRender();
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });
                                linearLayout.addView(bar);

                                ColorPickerSeekBar bar2 = new ColorPickerSeekBar(context);
                                bar2.setVisibility(View.VISIBLE);
                                bar2.setProgress(20);
                                bar2.setOnColorSeekbarChangeListener(new ColorPickerSeekBar.OnColorSeekBarChangeListener() {
                                    @Override
                                    public void onColorChanged(SeekBar seekBar, int color, boolean b) {
                                        ColorBar2 = color;
                                        mEffectView.requestRender();
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });
                                linearLayout.addView(bar2);
                            }

                        setCurrentEffect(effectCollection.getEffect(position).id);
                        mEffectView.requestRender();
                    }
                })
        );
    }


    public void Save(GL10 gl)
    {
        final Bitmap finalInBitmap = createBitmapFromGLSurface(0,0,mImageViewWidth,mImageViewHeight,gl);
        try {
            //Write file
            String filename = "bitmap.png";
            FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            finalInBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            stream.close();
            finalInBitmap.recycle();

            //Pop intent
            Intent in1 = new Intent(this, CropActivity.class);
            in1.putExtra("image", filename);
            startActivityForResult(in1,2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        mCurrentEffect = 0;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ImageView img = (ImageView) findViewById(R.id.imageView);
        mImageViewHeight = img.getHeight();
        mImageViewWidth = img.getWidth();

//     Log.d("imagesRenderH1", "height : " + mEffectView.getHeight());

       // ViewGroup.LayoutParams layoutParams=mEffectView.getLayoutParams();
       // layoutParams.width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mImageViewWidth, getResources().getDisplayMetrics());;
       // layoutParams.height=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mImageViewHeight, getResources().getDisplayMetrics());;

       // mEffectView.setLayoutParams(layoutParams);

        Log.d("images", "height : " + img.getHeight());
        mEffectView.setVisibility(View.VISIBLE);
        Log.d("imagesRenderH2", "height : " + mEffectView.getHeight());
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.edit_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
//        imageView = (ImageView) findViewById(R.id.imageView);
//        final Bitmap finalBitmap = bitmap;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                imageView.setImageBitmap(finalBitmap);
//        }
//        });

        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);
//        mTexRenderer.updateViewSize(mImageViewWidth,mImageViewHeight);
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
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
                mEffect.setParameter("scale", ScaleBar);
                break;

            case 2:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("black", ScaleBar2);
                mEffect.setParameter("white", ScaleBar);
                break;

            case 3:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", ScaleBar*2f);
                break;

            case 4:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", ScaleBar*1.5f);
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
                mEffect.setParameter("first_color", ColorBar);
                mEffect.setParameter("second_color", ColorBar2);
                break;

            case 8:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", ScaleBar);
                break;

            case 9:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FISHEYE);
                mEffect.setParameter("scale", ScaleBar);
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
                mEffect.setParameter("strength", ScaleBar);
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
                mEffect.setParameter("scale", ScaleBar);
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
                mEffect.setParameter("scale", ScaleBar);
                break;

            case 22:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_TINT);
                mEffect.setParameter("tint", ColorBar);
                break;

            case 23:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_VIGNETTE);
                mEffect.setParameter("scale", ScaleBar);
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
        Log.d("lol","no");
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
            setCurrentEffect(0);
            mEffectView.requestRender();
        }
        if (mCurrentEffect != 0 && mCurrentEffect != 24) {
            //if an effect is chosen initialize it and apply it to the texture
            initEffect();
            applyEffect();
        }
        if (mCurrentEffect == 24)
        {
            Save(gl);
        }
        renderResult();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setCurrentEffect(item.getItemId());
        mEffectView.requestRender();
        return true;
    }
    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }
}