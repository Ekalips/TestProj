package com.crutchbike.ekalips.filtering;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Lantain on 07.03.2016.
 */
public class PicHentai {
    // RGB Color rape
    // double значения указывать не больше единицы. 0,9999999 = 99.99999%
    public static Bitmap doColorFilter(Bitmap src, double red, double green, double blue) {
        int width = src.getWidth();                                                 //К слову, планшет подвисает секунд на 10 во время выполнени этой фичи
        int height = src.getHeight();                                               // мб потому что пикча 1920х1080
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        int pixel;
        for (int x = 0; x < width; ++x)
            for (int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                bmOut.setPixel(x, y, Color.argb(Color.alpha(pixel), (int) (Color.red(pixel) * red), (int) (Color.green(pixel) * green), (int) (Color.blue(pixel) * blue)));
            }
        return bmOut;
    }
    //Возвращает пикче 2007.
    public static Bitmap doGreyscale(Bitmap src) {
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;
        // get image size
        int width = src.getWidth();
        int height = src.getHeight();

        // scan through every single pixel
        for (int x = 0; x < width; ++x)
            for (int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        return bmOut;
    }
    // инверсия
    public static Bitmap doInvert(Bitmap src) {
        // create new bitmap with the same settings as source bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();
        // scan through every pixel
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(Color.alpha(pixelColor), 255 - Color.red(pixelColor), 255 - Color.green(pixelColor), 255 - Color.blue(pixelColor)));
                // альфа остется прежней. так, на заметку
            }
        return bmOut;
    }
    //коррекция гаммы
    // тестилось автором со значениями RGB 1.8 и 0.6
    public static Bitmap doGamma(Bitmap src, double red, double green, double blue) {
        // create output image
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // get image size
        int width = src.getWidth();
        int height = src.getHeight();
        // color information
        int A, R, G, B;
        int pixel;
        // constant value curve
        final int MAX_SIZE = 256;
        final double MAX_VALUE_DBL = 255.0;
        final int MAX_VALUE_INT = 255;
        final double REVERSE = 1.0;

        // gamma arrays
        int[] gammaR = new int[MAX_SIZE];
        int[] gammaG = new int[MAX_SIZE];
        int[] gammaB = new int[MAX_SIZE];

        // setting values for every gamma channels
        for (int i = 0; i < MAX_SIZE; ++i) {
            gammaR[i] = (int) Math.min(MAX_VALUE_INT,
                    (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
            gammaG[i] = (int) Math.min(MAX_VALUE_INT,
                    (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
            gammaB[i] = (int) Math.min(MAX_VALUE_INT,
                    (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
        }

        // apply gamma table
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // look up gamma
                R = gammaR[Color.red(pixel)];
                G = gammaG[Color.green(pixel)];
                B = gammaB[Color.blue(pixel)];
                // set new color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }
    // что-то там сепия
    // НАДО ТЕСТИТЬ ПАРАМЕТРЫ. на сайте уазана возможность убрать синий или что-то в этом роде. Хз.
    public static Bitmap createSepiaToningEffect(Bitmap src, int depth, double red, double green, double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // constant grayscale
        final double GS_RED = 0.3;
        final double GS_GREEN = 0.59;
        final double GS_BLUE = 0.11;
        // color information
        int A, R, G, B;
        int pixel;
        // scan through all pixels
        for (int x = 0; x < width; ++x)
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // get color on each channel
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // apply grayscale sample
                B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                // apply intensity level for sepid-toning on each channel
                R += (depth * red);
                if (R > 255) {
                    R = 255;
                }

                G += (depth * green);
                if (G > 255) {
                    G = 255;
                }

                B += (depth * blue);
                if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        // return final image
        return bmOut;
    }
    // глубина цвета.                                  эта штука снизу принимает параметры 32, 64, 128. По крайней мере на сайте описаны только они
    public static Bitmap decreaseColorDepth(Bitmap src, int bitOffset) {
        // get image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // round-off color offset
                R = ((R + (bitOffset / 2)) - ((R + (bitOffset / 2)) % bitOffset) - 1);
                if (R < 0) {
                    R = 0;
                }
                G = ((G + (bitOffset / 2)) - ((G + (bitOffset / 2)) % bitOffset) - 1);
                if (G < 0) {
                    G = 0;
                }
                B = ((B + (bitOffset / 2)) - ((B + (bitOffset / 2)) % bitOffset) - 1);
                if (B < 0) {
                    B = 0;
                }

                // set pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    // поворот пикчи. Но что-то я сомневаюсь в работоспособности
    public static Bitmap rotate(Bitmap src, float degree) {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);

        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    //изменение яркости
    public static Bitmap doBrightness(Bitmap src, int value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += value;
                if (R > 255) {
                    R = 255;
                } else if (R < 0) {
                    R = 0;
                }

                G += value;
                if (G > 255) {
                    G = 255;
                } else if (G < 0) {
                    G = 0;
                }

                B += value;
                if (B > 255) {
                    B = 255;
                } else if (B < 0) {
                    B = 0;
                }
                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }
    //некий Tint
    public static Bitmap tintImage(Bitmap src, int degree) {
        final double RANGE = 256d;
        final double HALF_CIRCLE_DEGREE = 180d;
        final double FULL_CIRCLE_DEGREE = 360d;
        final double PI = 3.14159d;
        int width = src.getWidth();
        int height = src.getHeight();

        int[] pix = new int[width * height];
        src.getPixels(pix, 0, width, 0, 0, width, height);

        int RY, GY, BY, RYY, GYY, BYY, R, G, B, Y;
        double angle = (PI * (double) degree) / HALF_CIRCLE_DEGREE;

        int S = (int) (RANGE * Math.sin(angle));
        int C = (int) (RANGE * Math.cos(angle));

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                int r = (pix[index] >> 16) & 0xff;
                int g = (pix[index] >> 8) & 0xff;
                int b = pix[index] & 0xff;
                RY = (70 * r - 59 * g - 11 * b) / 100;
                GY = (-30 * r + 41 * g - 11 * b) / 100;
                BY = (-30 * r - 59 * g + 89 * b) / 100;
                Y = (30 * r + 59 * g + 11 * b) / 100;
                RYY = (S * BY + C * RY) / 256;
                BYY = (C * BY - S * RY) / 256;
                GYY = (-51 * RYY - 19 * BYY) / 100;
                R = Y + RYY;
                R = (R < 0) ? 0 : ((R > 255) ? 255 : R);
                G = Y + GYY;
                G = (G < 0) ? 0 : ((G > 255) ? 255 : G);
                B = Y + BYY;
                B = (B < 0) ? 0 : ((B > 255) ? 255 : B);
                pix[index] = 0xff000000 | (R << 16) | (G << 8) | B;
            }

        Bitmap outBitmap = Bitmap.createBitmap(width, height, src.getConfig());
        outBitmap.setPixels(pix, 0, width, 0, 0, width, height);

        pix = null;

        return outBitmap;
    }
    // закругить края. Мне просто понравилось
    public static Bitmap roundCorner(Bitmap src, float round) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create bitmap output
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // set canvas for painting
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0, 0, 0, 0);

        // config paint
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        // config rectangle for embedding
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        // draw rect to canvas
        canvas.drawRoundRect(rectF, round, round, paint);

        // create Xfer mode
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // draw source image to canvas
        canvas.drawBitmap(src, rect, rect, paint);

        // return final image
        return result;
    }/*
    // хз что этот инт означает
    public Bitmap replaceColor(int fromColor, int targetColor) {
        if(mImage == null) {
            return null;
        }

        int width = mImage.getWidth();
        int height = mImage.getHeight();
        int[] pixels = new int[width * height];
        mImage.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int x = 0; x < pixels.length; ++x) {
            pixels[x] = (pixels[x] == fromColor) ? targetColor : pixels[x];
        }

        Bitmap newImage = Bitmap.createBitmap(width, height, mImage.getConfig());
        newImage.setPixels(pixels, 0, width, 0, 0, width, height);

        return newImage;
    }
    */
    // усилить цвет. Значения инта смотри в теле метода
    public static Bitmap boost(Bitmap src, int type, float percent) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int pixel;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                if (type == 1) {
                    R = (int) (R * (1 + percent));
                    if (R > 255) R = 255;
                } else if (type == 2) {
                    G = (int) (G * (1 + percent));
                    if (G > 255) G = 255;
                } else if (type == 3) {
                    B = (int) (B * (1 + percent));
                    if (B > 255) B = 255;
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }
    // по твоему заказу
    public static Bitmap BRATSKE_SLIYANIE_GURREN_LAGANN(Bitmap one, Bitmap two){
        int width = one.getWidth();
        int height = one.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, one.getConfig());
        // color info
        int pixel;
        int pix;
        // scan through every pixel
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                // get one pixel
                pixel = one.getPixel(x, y);
                pix = two.getPixel(x, y);
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb((Color.alpha(pix) + Color.alpha(pixel))/2,(Color.red(pix) + Color.red(pixel))/2, (Color.green(pix) + Color.green(pixel))/2 , (Color.blue(pix) + Color.blue(pixel))/2));
                // альфа остется прежней. так, на заметку
            }
        return bmOut;
    }
}
