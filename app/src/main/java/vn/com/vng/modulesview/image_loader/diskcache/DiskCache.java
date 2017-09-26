package vn.com.vng.modulesview.image_loader.diskcache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import vn.com.vng.modulesview.image_loader.Logger;
import vn.com.vng.modulesview.image_loader.Util;


/**
 * Handle disk caching for images  <br>
 * Author: Dat N. Truong<br>
 * Created date: 9/30/2015.
 */
public class DiskCache {

    /**
     * The tag name of this class. Use for debug filter purpose
     */
    private static final String TAG = DiskCache.class.getSimpleName();

    /**
     * Path to the cache folder
     */
    private String mCachePath;
    /**
     * Currently, we use the number of week of year to save data,
     * so that the cleaning task be able to determent which folder is old and legal to delete.
     */
    private Calendar mCalendar;

    private int mMaxWidth;
    private int mMaxHeight;

    public DiskCache(String cachePath) {
        mCachePath = cachePath;
        mCalendar = Calendar.getInstance();
    }

    public void setMaxImageSize(int maxW, int maxH) {
        mMaxWidth = maxW;
        mMaxHeight = maxH;
    }

    private String makeParentPath() {
        // get current week in year. This info will be used as cache folder name
        int wiy = mCalendar.get(Calendar.WEEK_OF_YEAR);
        String path = mCachePath + File.separatorChar + wiy;
        File file = new File(path);
        // try to make the parent path
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * Cache the image to a cache folder
     *
     * @param key     the key that represent for an image (actually its a md5 hash of the image link)
     * @param bitmap  the bitmap data
     * @param format  the format of the image to save
     * @param quality the quality of image, the lower quality is, the lighter cache is.
     * @return true if the bitmap is cache successfully
     */
    public boolean put(String key, Bitmap bitmap,
                       Bitmap.CompressFormat format, int quality) {
        String path = makeParentPath();
        if (path == null) {
            Logger.log(TAG, "Can not make parent path");
            return false;
        }
        File file = new File(path + File.separatorChar + key);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(format, quality, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            // error occur, try to delete it now
            file.delete();
        }
        return false;
    }

    public boolean put(InputStream is, String key) {
        if (is == null) {
            return false;
        }
        String path = makeParentPath();
        if (path == null) {
            Logger.log(TAG, "Can not make parent path");
            return false;
        }
        File file = new File(path + File.separatorChar + key);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            // buffer size: 10KB
            byte[] buffer = new byte[10240];
            int bufferLength = 0;
            while ((bufferLength = is.read(buffer)) > 0) {
                fos.write(buffer, 0, bufferLength);
            }
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            // error occur, try to delete it now
            file.delete();
        }
        return false;
    }

    /**
     * Read the bitmap data from disk
     *
     * @param key the image key (a md5 hash of image link)
     * @return the bitmap data that is cache before or null if the image has not been cache before
     */
    public Bitmap get(String key) {
        //int wiy = mCalendar.get(Calendar.WEEK_OF_YEAR);
        //String path = mCachePath + File.separatorChar + wiy + File.separatorChar + key;
        String path = makeParentPath();
        if (path == null) {
            Logger.log(TAG, "Can not make parent path");
            return null;
        }
        path += File.separatorChar + key;
        File file = new File(path);
        if (file.exists()) {
            //FileInputStream fis = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // check the dimension first
            BitmapFactory.decodeFile(path, options);
            int oWidth = options.outWidth;
            int oHeight = options.outHeight;
            if (oWidth == 0 || oHeight == 0) {
                return null;
            }
            boolean checkWidth = oWidth > oHeight;

            if (mMaxWidth <= 0 || mMaxHeight <= 0 ||
                    (checkWidth ? oWidth <= mMaxWidth : oHeight <= mMaxHeight)) {
                Log.e("xxx", key + " - Original bitmap size: " + oWidth + "x" + oHeight + ". Need reduce: NO");
                return BitmapFactory.decodeFile(path);
            }
            Log.e("xxx", key + " - Original bitmap size: " + oWidth + "x" + oHeight + ". Need reduce: YES");
            // try to reduce the bitmap size
            // start with sample size: 2
            int sampleSize = 2;
            if (checkWidth) {
                int newW = oWidth;
                while ((newW = newW / 2) > mMaxWidth) {
                    sampleSize++;
                }
            } else {
                int newH = oHeight;
                while ((newH = newH / 2) > mMaxHeight) {
                    sampleSize++;
                }
            }
            Log.e("xxx", key + " - Sample size: " + sampleSize);
            options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            return bm;
        } else {
            Log.e("xxx", key + " - Does not exist");
        }
        return null;
    }

    /**
     * Read the bitmap data from disk with the limited in width and height
     *
     * @param key       the image key (a md5 hash of image link)
     * @param maxWidth  the max width
     * @param maxHeight the max height
     * @return the bitmap data that is cache before or null if the image has not been cache before
     */
    public Bitmap get(String key, int maxWidth, int maxHeight) {
        if (maxWidth <= 0) {
            maxWidth = mMaxWidth;
        }
        if (maxHeight <= 0) {
            maxHeight = mMaxHeight;
        }
        String path = makeParentPath();
        if (path == null) {
            Logger.log(TAG, "Can not make parent path");
            return null;
        }
        path += File.separatorChar + key;
        File file = new File(path);
        if (file.exists()) {
            //FileInputStream fis = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // check the dimension first
            BitmapFactory.decodeFile(path, options);
            int oWidth = options.outWidth;
            int oHeight = options.outHeight;
            if (oWidth == 0 || oHeight == 0) {
                return null;
            }
            boolean checkWidth = oWidth > oHeight;

            if (maxWidth <= 0 || maxHeight <= 0 ||
                    (checkWidth ? oWidth <= maxWidth : oHeight <= maxHeight)) {
                return BitmapFactory.decodeFile(path);
            }
            // try to reduce the bitmap size
            // start with sample size: 2
            int sampleSize = 2;
            if (checkWidth) {
                int newW = oWidth / 2;
                while ((newW / sampleSize) >= maxWidth) {
                    sampleSize *= 2;
                }
            } else {
                int newH = oHeight / 2;
                while ((newH / sampleSize) >= maxHeight) {
                    sampleSize *= 2;
                }
            }
            options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            return bm;
        }
        return null;
    }

    /**
     * Check if an image with a key is fully cached on the disk
     *
     * @param key the image key
     * @return true if the image is cached, false otherwise
     */
    public boolean isFullyCached(String key) {
        int wiy = mCalendar.get(Calendar.WEEK_OF_YEAR);
        String path = mCachePath + File.separatorChar + wiy + File.separatorChar + key;
        File file = new File(path);
        return file.exists();
    }

    /**
     * Delete an image on the disk
     *
     * @param key the image key
     * @return the number of image is deleted
     */
    public int delete(String key) {
        if (TextUtils.isEmpty(key)) {
            return 0;
        }

        String wiy = String.valueOf(mCalendar.get(Calendar.WEEK_OF_YEAR));
        final File rootFolder = new File(mCachePath);
        final File[] listCacheFolder = rootFolder.listFiles();
        if (listCacheFolder == null) {
            return 0;
        }
        int delCount = 0;
        String fullPath = null;
        for (File f : listCacheFolder) {
            fullPath = f.getAbsolutePath() + File.separatorChar + key;
            boolean delSuccess = deleteImage(fullPath);
            delCount += delSuccess ? 1 : 0;
        }
        return delCount;
    }

    private boolean deleteImage(@NonNull String fullPath) {
        if (TextUtils.isEmpty(fullPath)) {
            return false;
        }
        File file = new File(fullPath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Delete all cache folder that is created before the safeTime
     *
     * @param safeTime
     */
    public void deleteFolder(long safeTime) {
        String wiy = String.valueOf(mCalendar.get(Calendar.WEEK_OF_YEAR));
        final File rootFolder = new File(mCachePath);
        final File[] listCacheFolder = rootFolder.listFiles();
        if (listCacheFolder == null) {
            return;
        }
        for (File f : listCacheFolder) {
            if ((safeTime - f.lastModified() > 0) || !wiy.equals(f.getName())) {
                Util.log("deleteFolder: " + f.getName());
                Util.deleteContents(f);
            } else {
                Util.log("deleteFolder keep: " + f.getName());
            }
        }
    }

    /**
     * Delete a half of cache folder if the current size is reached the max cache threshold
     *
     * @param maxCache the max cache threshold
     */
    public void deleteFolderByMaxCache(long maxCache) {
        final File rootFolder = new File(mCachePath);
        long curSize = getFolderSize(rootFolder);
        Logger.log("deleteFolder", "Current size: " + curSize + ", max size: " + maxCache);
        if (curSize < maxCache) {
            // It is still OK this time. No image will be deleted. ahoho
            return;
        }
        final File[] listCacheFolder = rootFolder.listFiles();
        if (listCacheFolder == null) {
            return;
        }
        if (listCacheFolder.length == 1) {
            Util.deleteContents(listCacheFolder[0], 0.5f);
            return;
        }

        Arrays.sort(listCacheFolder, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                long dif = o1.lastModified() - o2.lastModified();
                return dif > 0 ? 1 : (dif == 0 ? 0 : -1);
            }
        });

        for (int i = 0; i < listCacheFolder.length / 2; i++) {
            Util.log("deleteFolder: " + listCacheFolder[i].getName());
            Util.deleteContents(listCacheFolder[i]);
        }
    }

    /**
     * Calculate the size of a given folder
     *
     * @param rootFolder the given folder
     * @return the size of folder in byte
     */
    private long getFolderSize(File rootFolder) {
        if (rootFolder == null) {
            return 0;
        }
        if (rootFolder.isFile()) {
            return rootFolder.length();
        }
        long size = 0;
        File[] files = rootFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    size += getFolderSize(file);
                } else if (file.isFile()) {
                    size += file.length();
                }
            }
        }
        return size;
    }

    /**
     * Find the cache path of a specific image key
     *
     * @param imgKey the image key
     * @return the absolute path or null if no image key found
     */
    public String findPath(String imgKey) {
        String path = makeParentPath();
        if (path == null) {
            Logger.log(TAG, "Can not make parent path");
            return null;
        }
        path += File.separatorChar + imgKey;
        File file = new File(path);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public String getCurrentCachePath() {
        return makeParentPath();
    }
}
