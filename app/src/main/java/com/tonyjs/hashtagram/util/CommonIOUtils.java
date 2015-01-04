package com.tonyjs.hashtagram.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by JunSeon Park on 14. 3. 17.
 */
public class CommonIOUtils {
    private static final int MAX_WIDTH = 720;

    public static Bitmap getBitmapRotateIfNeed(Bitmap bitmap, String filePath) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exifInterface != null) {
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            int degree = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                degree = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                degree = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                degree = 270;
            }

//            Log.e("jsp", "degree = " + degree);

            if (Math.abs(degree) > 0) {
                Matrix matrix = new Matrix();
                matrix.setRotate(degree);
                Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return rotateBitmap;
            }
        }

        return bitmap;
    }

    public static Bitmap getBitmapFromFile(String filePath) {
        return getBitmapAvoidOOM(filePath);
    }

    public static Bitmap getResizeBitmap(Bitmap bitmap, int width, int height) {
        return bitmap != null ? bitmap.createScaledBitmap(bitmap, width, height, true) : null;
    }

    public static Bitmap getMaxScaledBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

//            Log.e("jsp", "original bitmap width = " + width);
//            Log.e("jsp", "original bitmap height = " + height);

            float ratio = height / (float) width;

            if (width > MAX_WIDTH) {
//                Log.d("jsp", "width > MAX_WIDTH");
                width = MAX_WIDTH;
                height = (int) (width * ratio);
                Bitmap newBitmap = getResizeBitmap(bitmap, width, height);
                Log.d("jsp", "original bitmap width = " + newBitmap.getWidth());
                Log.d("jsp", "original bitmap height = " + newBitmap.getHeight());
                bitmap.recycle();
                return newBitmap;
            }
        }

        return bitmap;
    }

    public static Bitmap decodeUriAndDownSampleIfNeed(Context context, Uri data) {
        if (data == null) {
            return null;
        }

        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (is != null) {
//            Log.e("jsp", "is is not null");
            try {
//                Log.e("jsp", "try");
                bitmap = BitmapFactory.decodeStream(is);
//                Log.e("jsp", "good");
            } catch (OutOfMemoryError e0) {
//                Log.e("jsp", "OutOfMemory0");
                bitmap.recycle();
                try {
                    bitmap = getInSampleSizedBitmap(is, 2);
                } catch (OutOfMemoryError e1) {
//                    Log.e("jsp", "OutOfMemory1");
                    bitmap.recycle();
                    try {
                        bitmap = getInSampleSizedBitmap(is, 4);
                    } catch (OutOfMemoryError e2) {
//                        Log.e("jsp", "OutOfMemory2");
                        bitmap.recycle();
                        try {
                            bitmap = getInSampleSizedBitmap(is, 8);
                        } catch (OutOfMemoryError e3) {
//                            Log.e("jsp", "OutOfMemory3");
                            bitmap.recycle();
                            try {
                                bitmap = getInSampleSizedBitmap(is, 16);
                            } catch (OutOfMemoryError e4) {
//                                Log.e("jsp", "OutOfMemory4");
                                bitmap.recycle();
                                bitmap = null;
                            }
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
//            Log.e("jsp", "is is null");
        }

        if (bitmap != null) {
//            Log.e("jsp", "bitmap is not null");

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            float ratio = height / (float) width;
            if (width > MAX_WIDTH) {
                width = MAX_WIDTH;
                height = (int) (width * ratio);
            }

            bitmap = getResizeBitmap(bitmap, width, height);
        }

        return bitmap;
    }

    private static Bitmap getBitmapAvoidOOM(String filePath) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(filePath);
        } catch (OutOfMemoryError e0) {
            bitmap.recycle();
            try {
                bitmap = getInSampleSizedBitmap(filePath, 2);
            } catch (OutOfMemoryError e1) {
                bitmap.recycle();
                try {
                    bitmap = getInSampleSizedBitmap(filePath, 4);
                } catch (OutOfMemoryError e2) {
                    bitmap.recycle();
                    try {
                        bitmap = getInSampleSizedBitmap(filePath, 8);
                    } catch (OutOfMemoryError e3) {
                        try {
                            bitmap = getInSampleSizedBitmap(filePath, 16);
                        } catch (OutOfMemoryError e4) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }


    private static Bitmap getInSampleSizedBitmap(InputStream is, int sampleSize) throws OutOfMemoryError {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeStream(is, null, options);
    }

    private static Bitmap getInSampleSizedBitmap(String filePath, int sampleSize)
            throws OutOfMemoryError {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
