package vn.com.vng.modulesview.image_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import vn.com.vng.modulesview.image_loader.diskcache.DiskCache;


/**
 * An single ton class handle image caching, downloading, displaying  <br>
 * <p/>
 * Author: Dat N. Truong<br>
 * Created date: 9/30/2015.
 */
public class ImageLoader {

    public static final int ANIM_NONE = 0;
    public static final int ANIM_FADE = 1;

    public static final int SOURCE_NETWORK = 0;
    public static final int SOURCE_LOCAL = 10;

    /**
     * Error code means the source is malformed
     */
    public static final int ERROR_INVALID_SOURCE = 100;
    /**
     * Unknown error. At this time, this code means for any kind of error which is not {@link #ERROR_INVALID_SOURCE}
     */
    public static final int ERROR_UNKNOWN = 1000;

    /**
     * The thread pool size for the executive service
     */
    private static final int THREAD_POOL_SIZE = 5;
    private static final int THREAD_POOL_SIZE_LOW_P = 3;
    private static final int BUFFER_SIZE = 8 * 1024;
    private static final int MEM_CACHE_SIZE = 10 * 1024 * 1024;// 10MB

    private static final String TAG = ImageLoader.class.getSimpleName();

    private static ImageLoader sInstance;
    /**
     * We use Lru cache for better performance
     */
    private LruCache<String, Bitmap> mMemLruCache;
    /**
     * Lru cache is very limited so the disk cache is used to cache a large data
     */
    private DiskCache mDiskCache;
    private WeakReference<Context> mWeakRefContext;
    private ExecutorService mExecutorService;
    /**
     * Low priority thread pool.
     * Use this for low priority loaderTask
     */
    private ExecutorService mExecutorServiceLowP;
    private MyHandler mHandler;
    /**
     * This loaderTask map make sure only 1 image link is requested by a {@link LoaderTask}.
     * This reduce the number of request for the same images
     */
    private final HashMap<String, LoaderTask> mTaskMap;

    private final Object mLock = new Object();
    private boolean mDiskCacheStarting = true;

    public interface ImageLoaderCallback {
        /**
         * Triggered when the loader has successfully loaded the image
         *
         * @param bitmap the loaded bitmap
         */
        void onSuccess(Bitmap bitmap);

        /**
         * Triggered when the loader has failed to load the image
         *
         * @param errorCode the error code. This code could be either {@link #ERROR_INVALID_SOURCE} or {@link #ERROR_UNKNOWN}
         */
        void onFailed(int errorCode);
    }

    static {
        sInstance = new ImageLoader();
    }

    private ImageLoader() {
        mHandler = new MyHandler(Looper.getMainLooper());
        mTaskMap = new HashMap<>();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        mMemLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return value.getAllocationByteCount() / 1024;
                }
                return value.getByteCount() / 1024;
            }
        };
        mExecutorService = new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE,
                0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>());
        mExecutorServiceLowP = new ThreadPoolExecutor(THREAD_POOL_SIZE_LOW_P, THREAD_POOL_SIZE_LOW_P,
                0L, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>());
    }

    public static ImageLoader getInstance() {
        return sInstance;
    }

    /**
     * This method should be called in the worker thread rather than UI thread
     */
    public void init(File cacheDir) throws IOException {
        synchronized (mLock) {
            long start = System.currentTimeMillis();
            if (mDiskCache == null) {
                mDiskCache = new DiskCache(cacheDir.getAbsolutePath());
                mDiskCacheStarting = false;
                Util.log("Disk cache is ready");
                Util.log("Disk cache init tooks: " + (System.currentTimeMillis() - start));
            } else {
                Util.log("Disk cache is already init");
            }
            // OK for now, the disk cache is ready to use, notify all the waiting thread
            mLock.notifyAll();
        }
    }

    /**
     * Get the image from Lru cache
     *
     * @param link the image link
     * @return the cache bitmap data of this image link or null if this image does not exist
     */
    private Bitmap getMemCache(String link) {
        if (mMemLruCache != null) {
            return mMemLruCache.get(Util.encodeMD5(link));
        }
        return null;
    }

    public static class Builder {
        private ImageView mImageView;
        private String mLink;
        private int mLoadingId = -1;
        private int mErrorId = -1;
        private int mAnimType = ANIM_NONE;
        private boolean mIgnoreMemCache;
        private boolean mIgnoreIfNoRef;
        private int mMaxWidth;
        private int mMaxHeight;
        private ImageLoaderCallback mCallback;
        private boolean mStrongRefCallback;
        private boolean mLoadOnly;
        private boolean mLowPriority;
        private int mPriority;
        private int mSource;

        Builder() {
            mLoadOnly = true;
            mLowPriority = true;
            mPriority = 0;
        }

        Builder(ImageView imgView) {
            mImageView = imgView;
            mPriority = (int) (System.currentTimeMillis() % 1000000);
        }

        public Builder setLowPriority(boolean lowPriority) {
            mLowPriority = lowPriority;
            return this;
        }

        public Builder setPriority(int priority) {
            mPriority = priority;
            return this;
        }

        public Builder setLink(String link) {
            mLink = link;
            return this;
        }

        public Builder fromLocal(String path) {
            mLink = path;
            mSource = SOURCE_LOCAL;
            return this;
        }

        public Builder setLoadingDrawableId(int loadingId) {
            mLoadingId = loadingId;
            return this;
        }

        public Builder setErrorDrawableId(int errorId) {
            mErrorId = errorId;
            return this;
        }

        public Builder setAnimType(int animType) {
            mAnimType = animType;
            return this;
        }

        public Builder ignoreMemCache(boolean val) {
            mIgnoreMemCache = val;
            return this;
        }

        public Builder ignoreIfNoRef(boolean ignoreIfNoRef) {
            mIgnoreIfNoRef = ignoreIfNoRef;
            return this;
        }

        public Builder specificSize(int maxW, int maxH) {
            mMaxWidth = maxW;
            mMaxHeight = maxH;
            return this;
        }

        public Builder setCallback(ImageLoaderCallback callback) {
            return setCallback(callback, false);
        }

        public Builder setCallback(ImageLoaderCallback callback, boolean strongRef) {
            mCallback = callback;
            mStrongRefCallback = strongRef;
            return this;
        }


        @UiThread
        public void ok() {
            if (mLoadOnly) {
                getInstance().loadOnly(this);
            } else {
                getInstance().loadImage(this);
            }
        }

    }

    @UiThread
    public Builder startFor(ImageView imageView) {
        return new Builder(imageView);
    }

    @UiThread
    public Builder loadOnly() {
        //load only will ignore if no ref by default
        return new Builder().ignoreIfNoRef(true);
    }

    /**
     * This method should be called on UI thread.
     * The ImageLoader instance will take care about the loading loaderTask
     *
     * @param imgView   The imageview to display
     * @param link      the link of image to load
     * @param loadingId the drawable id of a image resource represent loading status
     * @param errorId   the drawable id of a image resource represent error status
     */
    @UiThread
    public void startLoadingImage(ImageView imgView, String link, int loadingId, int errorId) {
        loadImage(imgView, link, loadingId, errorId, ANIM_FADE, 0, 0, false, null, false, 0);
    }

    /**
     * This method should be called on UI thread.
     * The ImageLoader instance will take care about the loading loaderTask
     *
     * @param imgView The imageview to display
     * @param link    the link of image to load
     */
    @UiThread
    public void startLoadingImage(ImageView imgView, String link) {
        loadImage(imgView, link, 0, 0, ANIM_FADE, 0, 0, false, null, false, 0);
    }

    /**
     * This method should be called on UI thread.
     * The ImageLoader instance will take care about the loading loaderTask
     *
     * @param imgView The imageview to display
     * @param link    the link of image to load
     * @param anim    the animation type that will be applied for the imageview later
     */
    @UiThread
    public void startLoadingImage(ImageView imgView, String link, int anim) {
        loadImage(imgView, link, 0, 0, anim, 0, 0, false, null, false, 0);
    }

    /**
     * Start a loading flow. This flow is<br>
     * 1. Validate the image link. False-> exit<br>
     * 2. Check Lru cache. If exist, get it and set to the image view -> exit<br>
     * 3. Check if the link is loading by an loader. If yes, add the imageview to the list, false init the loader
     *
     * @param imgView   The imageview to display
     * @param link      the link of image to load
     * @param loadingId the drawable id of a image resource represent loading status
     * @param errorId   the drawable id of a image resource represent error status
     */
    @UiThread
    public void startLoadingImage(ImageView imgView, String link, int loadingId, int errorId, int anim) {
        loadImage(imgView, link, loadingId, errorId, anim, 0, 0, false, null, false, 0);
    }

    /**
     * Double loading is useful when you want to load the small image first and then the big one.
     * This will give a better user experience cause it makes the app "looks" faster<br>
     * The follow is:<br>
     * 1. Check if the big thumb is loaded. if yes, use the big one. Stop now<br>
     * 2. Load the small thumb<br>
     * 3. Load the big thumb<br>
     *
     * @param imgView
     * @param smallThumb
     * @param bigThumb
     * @param loadingId
     * @param errorId
     */
    @UiThread
    public void startLoadingDoubleImages(ImageView imgView, String smallThumb, String bigThumb, int loadingId, int errorId) {
        // check if we have a big thumb
        if (imgView != null) {
            if (!validateUrl(bigThumb)) {
                if (errorId == 0) {
                    imgView.setImageBitmap(null);
                } else {
                    imgView.setImageResource(errorId);
                }
                return;
            }
            String bigKey = Util.encodeMD5(bigThumb);
            // check lru
            Bitmap bm = getMemCache(bigThumb);
            if (bm != null) {
                imgView.setImageBitmap(bm);
                return;
            }
            // continue check local
            if (mDiskCache.isFullyCached(bigKey)) {
                // load the big only
                loadImage(imgView, bigThumb, loadingId, errorId, ANIM_NONE, 0, 0, false, null, false, 0);
            } else {
                loadImage(imgView, smallThumb, loadingId, errorId, ANIM_NONE, 0, 0, false, null, false, 0);
                loadImage(imgView, bigThumb, loadingId, errorId, ANIM_NONE, 0, 0, false, null, false, 0);
            }
        }
    }

    /**
     * Double loading is useful when you want to load the small image first and then the big one.
     * This will give a better user experience cause it makes the app "looks" faster<br>
     * The follow is:<br>
     * 1. Check if the big thumb is loaded. if yes, use the big one. Stop now<br>
     * 2. Load the small thumb<br>
     * 3. Load the big thumb<br>
     *
     * @param imgView
     * @param smallThumb
     * @param bigThumb
     * @param loadingId
     * @param errorId
     */
    @UiThread
    public void startLoadingDoubleImages(ImageView imgView, String smallThumb, String bigThumb,
                                         int loadingId, int errorId, int maxW, int maxH) {
        // check if we have a big thumb
        if (imgView != null) {
            if (!validateUrl(bigThumb)) {
                if (errorId == 0) {
                    imgView.setImageBitmap(null);
                } else {
                    imgView.setImageResource(errorId);
                }
                return;
            }
            String bigKey = Util.encodeMD5(bigThumb);
            // check lru
            Bitmap bm = getMemCache(bigThumb);
            if (bm != null) {
                imgView.setImageBitmap(bm);
                return;
            }
            // continue check local
            if (mDiskCache.isFullyCached(bigKey)) {
                // load the big only
                loadImage(imgView, bigThumb, loadingId, errorId, ANIM_NONE, maxW, maxH, false, null, false, 0);
            } else {
                loadImage(imgView, smallThumb, loadingId, errorId, ANIM_NONE, 0, 0, false, null, false, 0);
                loadImage(imgView, bigThumb, loadingId, errorId, ANIM_NONE, maxW, maxH, false, null, false, 0);
            }
        }
    }

    /**
     * Start a loading flow. This flow is<br>
     * 1. Validate the image link. False-> exit<br>
     * 2. Check Lru cache. If exist, get it and set to the image view -> exit<br>
     * 3. Check if the link is loading by an loader. If yes, add the imageview to the list, false init the loader
     *
     * @param imgView   The imageview to display
     * @param link      the link of image to load
     * @param loadingId the drawable id of a image resource represent loading status
     * @param errorId   the drawable id of a image resource represent error status
     * @param maxW      the max width of the image. If the image width is bigger than this max width, it might be decoded
     *                  * @param maxH      the max height of the image. If the image height is bigger than this max height, it might be decoded
     */
    @UiThread
    public void startLoadingImage(ImageView imgView, String link, int loadingId, int errorId, int anim, int maxW, int maxH) {
        loadImage(imgView, link, loadingId, errorId, anim, maxW, maxH, false, null, false, 0);
    }

    /**
     * Start a loading flow. This flow is<br>
     * 1. Validate the image link. False-> exit<br>
     * 2. Check Lru cache. If exist, get it and set to the image view -> exit<br>
     * 3. Check if the link is loading by an loader. If yes, add the imageview to the list, false init the loader
     *
     * @param imgView   The imageview to display
     * @param link      the link of image to load
     * @param loadingId the drawable id of a image resource represent loading status
     * @param errorId   the drawable id of a image resource represent error status
     */
    @UiThread
    private void loadImage(ImageView imgView, String link, int loadingId,
                           int errorId, int anim, int maxW, int maxH,
                           boolean ignoreMemCache, ImageLoaderCallback callback, boolean lowP, int priority) {
        if (imgView != null) {
            // validate url
            if (!validateUrl(link)) {
                Util.log("url is in valid");
                if (errorId == 0) {
                    imgView.setImageBitmap(null);
                } else {
                    imgView.setImageResource(errorId);
                }
                notifyOnFailed(ERROR_INVALID_SOURCE, callback);
                return;
            }
            link = resolveUrl(link);
            // check mem cache first
            if (!ignoreMemCache) {
                Bitmap bm = getMemCache(link);
                if (bm != null) {
                    Util.log("startLoadingImage: found img first time. use mem lru cache");
                    imgView.setImageBitmap(bm);
                    notifyOnSuccess(bm, callback);
                    return;
                }
                if (loadingId == 0) {
                    imgView.setImageBitmap(null);
                } else {
                    imgView.setImageResource(loadingId);
                }
            }
            // check if there is any loader loaderTask running for this image.
            String imgKey = Util.encodeMD5(link);
            LoaderTask task = mTaskMap.get(imgKey);
            if (task != null) {
                task.addImageView(imgView);
            } else {
                task = new LoaderTask(imgView, link);
                task.setLoadingResource(loadingId);
                task.setAnimType(anim);
                task.setSpecificSize(maxW, maxH);
                task.ignoreMemCache(ignoreMemCache);
                task.setCallback(callback);
                task.mPriority = priority;
                mTaskMap.put(imgKey, task);
                getExecutors(lowP).execute(task);
//                final LoaderTask finalTask = loaderTask;
                // Using asyn for executing the loaderTask seems a little bit faster.
                // We decide use this approach not because it is faster but more reliable
                // Average time is around ~70000 nano second
                // while using mExecutorService.execute(loaderTask); directly it sometime faster, sometime very slower thought (even > 16ms)

                //P/s: NQH fix: execute in a new Asynctask maybe take long time for waiting...
//                new AsyncTask<Void, Void, Void>() {
//nn
//                    @Override
//                    protected Void doInBackground(Void... params) {
//                        mExecutorService.execute(finalTask);
//                        return null;
//                    }
//                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private void loadImage(Builder builder) {
        if (builder == null) {
            // should never happen
            return;
        }
        if (builder.mImageView != null) {
            // validate url
            if (!validateSource(builder.mSource, builder.mLink)) {
                Util.log("url is in valid");
                if (builder.mErrorId >= 0) {
                    builder.mImageView.setImageResource(builder.mErrorId);
                }
                notifyOnFailed(ERROR_INVALID_SOURCE, builder.mCallback);
                return;
            }
            String link = resolveUrl(builder.mLink);
            // check mem cache first
            if (!builder.mIgnoreMemCache) {
                Bitmap bm = getMemCache(link);
                if (bm != null) {
                    Util.log("startLoadingImage: found img first time. use mem lru cache");
                    builder.mImageView.setImageBitmap(bm);
                    notifyOnSuccess(bm, builder.mCallback);
                    return;
                }
                if (builder.mLoadingId >= 0) {
                    builder.mImageView.setImageResource(builder.mLoadingId);
                }
            }
            // check if there is any loader loaderTask running for this image.
            String imgKey = Util.encodeMD5(link);
            LoaderTask task = mTaskMap.get(imgKey);
            if (task != null) {
                task.addImageView(builder.mImageView);
            } else {
                task = new LoaderTask(builder.mImageView, link);
                task.setLoadingResource(builder.mLoadingId);
                task.setErrorId(builder.mErrorId);
                task.setAnimType(builder.mAnimType);
                task.setSpecificSize(builder.mMaxWidth, builder.mMaxHeight);
                task.ignoreMemCache(builder.mIgnoreMemCache);
                task.setCallback(builder.mCallback, builder.mStrongRefCallback);
                task.ignoreIfNoRef(builder.mIgnoreIfNoRef);
                task.mPriority = builder.mPriority;
                task.mSource = builder.mSource;
                mTaskMap.put(imgKey, task);
                getExecutors(builder.mLowPriority).execute(task);
            }
        }
    }

    /**
     * This method is deprecated. Use {@link Builder} instead
     *
     * @param link the link to load
     */
    @Deprecated
    public void loadOnly(String link) {
        loadOnly(link, null, false, 0);
    }

    private void loadOnly(String link, ImageLoaderCallback callback, boolean lowP, int priority) {
        Util.log("loadOnly for: " + link);
        link = resolveUrl(link);
        // check mem cache first
        Bitmap bm = getMemCache(link);
        if (bm != null) {
            Util.log("loadOnly: ignored. already in mem lru cache");
            return;
        }
        // check if there is any loader loaderTask running for this image.
        String imgKey = Util.encodeMD5(link);
        LoaderTask task = mTaskMap.get(imgKey);
        if (task != null) {
            Util.log("loadOnly: ignored. already in progress");
        } else {
            synchronized (mTaskMap) {
                task = new LoaderTask(null, link);
                task.setCallback(callback);
                task.mLoadOnly = true;
                task.mPriority = priority;
                mTaskMap.put(imgKey, task);
                getExecutors(lowP).execute(task);
            }
//            final LoaderTask finalTask = loaderTask;
            // Using asyn for executing the loaderTask seems a little bit faster.
            // We decide use this approach not because it is faster but more reliable
            // Average time is around ~70000 nano second
            // while using mExecutorService.execute(loaderTask); directly it sometime faster, sometime very slower thought (even > 16ms)
//            new AsyncTask<Void, Void, Void>() {
//
//                @Override
//                protected Void doInBackground(Void... params) {
//                    mExecutorService.execute(finalTask);
//                    return null;
//                }
//            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            // DATTN 11/29/2016
        }
    }

    private void loadOnly(Builder builder) {
        if (builder == null) {
            return;
        }
        Util.log("loadOnly for: " + builder.mLink);
        String link = resolveUrl(builder.mLink);
        // check mem cache first
        Bitmap bm = getMemCache(link);
        if (bm != null) {
            Util.log("loadOnly: ignored. already in mem lru cache");
            return;
        }
        // check if there is any loader loaderTask running for this image.
        String imgKey = Util.encodeMD5(link);
        LoaderTask task = mTaskMap.get(imgKey);
        if (task != null) {
            Util.log("loadOnly: ignored. already in progress");
        } else {
            synchronized (mTaskMap) {
                task = new LoaderTask(null, link);
                task.setCallback(builder.mCallback, builder.mStrongRefCallback);
                task.mLoadOnly = true;
                task.mPriority = builder.mPriority;
                mTaskMap.put(imgKey, task);
                getExecutors(builder.mLowPriority).execute(task);
            }
        }
    }

    private ExecutorService getExecutors(boolean lowP) {
        if (lowP) {
            return mExecutorServiceLowP;
        }
        return mExecutorService;
    }

    /**
     * Valid the image URL
     *
     * @param link the url to validate
     * @return true if the url is valid, false otherwise
     */
    private boolean validateUrl(String link) {
        try {
            URL url = new URL(link);
            return true;
        } catch (MalformedURLException ignored) {
        }
        return false;
    }

    private boolean validateSource(int source, String link) {
        switch (source) {
            case SOURCE_NETWORK:
                return validateUrl(link);
            case SOURCE_LOCAL:
                return !TextUtils.isEmpty(link);
        }
        return false;
    }

    private String resolveUrl(String link) {
        if (link == null || link.length() < 1) {
            return link;
        }
        // exclude facebook avatar link
        if (link.startsWith("https://graph.facebook.com")) {
            return link;
        }
        try {
            return URI.create(link).toString();
        } catch (Exception ignored) {

        }
        return link;
    }

    /**
     * Remove the loaderTask from the loaderTask map
     *
     * @param link the image link (will be encoded to md5) that represent for a loaderTask.
     */
    public void removeTask(String link) {
        synchronized (mTaskMap) {
            String imgKey = Util.encodeMD5(link);
            mTaskMap.remove(imgKey);
        }
    }

    public int getTaskSize() {
        return mTaskMap == null ? 0 : mTaskMap.size();
    }

    /**
     * Start checking the old cache folder. All folder that is created before the safetime will be deleted
     *
     * @param safeTime the safe time for the folder to keep
     */
    public void checkOldCache(long safeTime) {
        Logger.log(TAG, "checkOldCache started");
        if (mDiskCache != null) {
            mDiskCache.deleteFolder(safeTime);
        }
    }

    /**
     * Start checking the current storage size of cache folder.
     * If it is greater than the given max size, then a half of old cache folder will be deleted
     *
     * @param maxSize the max size. In KB
     */
    public void checkMaxCacheSize(long maxSize) {
        Logger.log(TAG, "checkMaxCache started");
        if (mDiskCache != null) {
            mDiskCache.deleteFolderByMaxCache(maxSize);
        }
    }

    /**
     * Cancel the loading for a specific image
     *
     * @param view that image that request loading before
     */
    public void cancel(ImageView view) {
        if (mTaskMap != null && mTaskMap.size() > 0) {
            Set<Map.Entry<String, LoaderTask>> entries = mTaskMap.entrySet();
            if (entries.size() < 1) {
                return;
            }
            synchronized (mTaskMap) {
                for (Map.Entry<String, LoaderTask> entry : entries) {
                    if (entry.getValue().contains(view)) {
                        entry.getValue().removeImageView(view);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Remove the callback out of the loader task.
     * @param callback
     */
    public void removeCallback(ImageLoaderCallback callback){
        if (mTaskMap != null && mTaskMap.size() > 0) {
            Set<Map.Entry<String, LoaderTask>> entries = mTaskMap.entrySet();
            if (entries.size() < 1) {
                return;
            }
            synchronized (mTaskMap) {
                for (Map.Entry<String, LoaderTask> entry : entries) {
                    if (entry.getValue().containsCallback(callback)) {
                        entry.getValue().removeCallback(callback);
                        break;
                    }
                }
            }
        }
    }



    /**
     * Find the cache path of a specific image key
     *
     * @param imgKey the image key
     * @return the absolute path or null if no image key found
     */
    public String findCachePath(String imgKey) {
        if (mDiskCache != null) {
            return mDiskCache.findPath(imgKey);
        }
        return null;
    }

    /**
     * Clear the memory cache.
     */
    public void clearMemCache() {
        if (mMemLruCache != null) {
            mMemLruCache.evictAll();
        }
    }

    /**
     * Delete an image on the disk
     *
     * @param link the original image link
     * @return the number of image is deleted
     */
    public int delete(String link) {
        if (TextUtils.isEmpty(link)) {
            return 0;
        }
        if (mDiskCache != null) {
            String imgKey = Util.encodeMD5(link);
            return mDiskCache.delete(imgKey);
        }
        return 0;

    }

    /**
     * Shutdown the service
     */
    public void shutDown() {
        mExecutorService.shutdown();

        try {
            // wait for the loaderTask finished
            mExecutorService.awaitTermination(15, TimeUnit.SECONDS);
            mExecutorService.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mExecutorServiceLowP.shutdown();

        try {
            // wait for the loaderTask finished
            mExecutorServiceLowP.awaitTermination(15, TimeUnit.SECONDS);
            mExecutorServiceLowP.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void notifyOnFailed(int code, LoaderTask loaderTask) {
        ImageLoaderCallback callback = null;
        if (loaderTask != null) {
            // check the strong ref first
            callback = loaderTask.mStrongRefCallback;
            if (callback == null) {
                callback = loaderTask.mWeakRefCallback.get();
            }
        }
        if (callback != null) {
            callback.onFailed(code);
        }
    }

    private void notifyOnFailed(int code, ImageLoaderCallback callback) {
        if (callback != null) {
            callback.onFailed(code);
        }
    }

    private void notifyOnSuccess(LoaderTask loaderTask) {
        ImageLoaderCallback callback = null;
        if (loaderTask != null) {
            // check the strong ref first
            callback = loaderTask.mStrongRefCallback;
            if (callback == null) {
                callback = loaderTask.mWeakRefCallback.get();
            }
        }
        if (callback != null) {
            callback.onSuccess(loaderTask.mBitmap);
        }
    }

    private void notifyOnSuccess(Bitmap bm, ImageLoaderCallback callback) {
        if (callback != null) {
            callback.onSuccess(bm);
        }
    }

    /**
     * A Loader for loading image, caching and request display image
     */
    private class LoaderTask implements Runnable, Comparable<LoaderTask> {

        /**
         * Multiple image views can request the same image, in this case all that views will be grouped in this list
         */
        private SparseArray<WeakReference<ImageView>> mWeakRefImgViewList;
        /**
         * The final bitmap data
         */
        private Bitmap mBitmap;
        private String mLink;
        private int mLoadingId = -1;
        private int mErrorId = -1;
        private int mAnimType;
        private int mSpecificSizeWidth;
        private int mSpecificSizeHeight;
        private boolean mIgnoreIfNoRef;
        private boolean mIgnoreMemCache;
        private boolean mLoadOnly;
        private int mPriority;
        private int mSource = SOURCE_NETWORK;

        private WeakReference<ImageLoaderCallback> mWeakRefCallback;
        private ImageLoaderCallback mStrongRefCallback;

        LoaderTask(ImageView view) {
            mWeakRefImgViewList = new SparseArray<>();
            if (view != null) {
                mWeakRefImgViewList.put(view.hashCode(), new WeakReference<>(view));
            }
        }

        LoaderTask(ImageView view, String link) {
            this(view);
            mLink = link;
        }

        void fromLocal(String path) {
            mLink = path;
            mSource = SOURCE_LOCAL;
        }

        void setLoadingResource(int id) {
            mLoadingId = id;
        }

        public void setErrorId(int id) {
            mErrorId = id;
        }

        void setAnimType(int type) {
            mAnimType = type;
        }

        void setSpecificSize(int width, int height) {
            mSpecificSizeWidth = width;
            mSpecificSizeHeight = height;
        }

        int getAnimType() {
            return mAnimType;
        }

        void ignoreIfNoRef(boolean ignoreIfNoRef){
            mIgnoreIfNoRef = ignoreIfNoRef;
        }
        void ignoreMemCache(boolean yes) {
            mIgnoreMemCache = yes;
        }

        void addImageView(ImageView view) {
            if (view != null) {
                mWeakRefImgViewList.put(view.hashCode(), new WeakReference<>(view));
            }
        }

        void removeImageView(ImageView view) {
            if (view != null) {
                mWeakRefImgViewList.remove(view.hashCode());
            }
        }

        boolean contains(ImageView view) {
            return view != null && mWeakRefImgViewList.get(view.hashCode()) != null;
        }

        public void setCallback(ImageLoaderCallback callback) {
            setCallback(callback, false);
        }

        public void setCallback(ImageLoaderCallback callback, boolean keepStrongRef) {
            if (keepStrongRef) {
                mStrongRefCallback = callback;
                mWeakRefCallback = null;
            } else {
                mStrongRefCallback = null;
                mWeakRefCallback = new WeakReference<>(callback);
            }
        }

        public boolean containsCallback(ImageLoaderCallback callback){
            return mStrongRefCallback == callback || (mWeakRefCallback != null && mWeakRefCallback.get() == callback);
        }

        public void removeCallback(ImageLoaderCallback callback){
            mStrongRefCallback = null;
            mWeakRefCallback = null;
        }

        @Override
        public void run() {
            //remove this task
            ImageLoader.getInstance().removeTask(mLink);
            Util.log("TASK SIZE: " + ImageLoader.getInstance().getTaskSize());

            if(mIgnoreIfNoRef){
                boolean noImageViewRef = true;
                int count = mWeakRefImgViewList.size();
                for(int i = 0; i< count ; ++i)
                    if(mWeakRefImgViewList.get(i) != null){
                        noImageViewRef = false;
                        break;
                    }
                if(noImageViewRef) {
                    return;
                }
                if(mStrongRefCallback == null || mWeakRefCallback.get() == null){
                    return;
                }
            }

            mBitmap = null;
            String memCacheImgKey = createMemCacheImgKey(mLink, mSpecificSizeWidth, mSpecificSizeHeight);
            // the mem cache should be checked before this but still need to check again
            if (!mIgnoreMemCache) {
                if (mMemLruCache != null) {
                    mBitmap = getBitmapFromMemCache(memCacheImgKey);
                    if (mBitmap != null) {
                        Util.log("Found from mem cache. " + mLink + mSpecificSizeWidth + "_" + mSpecificSizeHeight);
                    } else {
                        Util.log("Not found from mem cache. " + mLink + mSpecificSizeWidth + "_" + mSpecificSizeHeight);
                    }
                }
            }

            if (mBitmap == null) {
                // check disk cache
                long start = System.currentTimeMillis();
                mBitmap = getBitmapFromDiskCache(mLink, mSpecificSizeWidth, mSpecificSizeHeight);
                if (mBitmap != null) {
                    Util.log(mLink + " - found from disk cache");
                    // try to cache to mem cache
                    if (!mLoadOnly) {
                        // we do not want to consume memory usage for load only mode
                        cacheMem(memCacheImgKey, mBitmap);
                    }
                    Logger.logFile(TAG, mLink + " Read disk cache time: " + (System.currentTimeMillis() - start) + " ms");
                } else {
                    Util.log(mLink + " - Not found from disk cache");
                    Util.log(mLink + " - Start downloading");
                    mBitmap = loadImageFromSource(mLink);
                    if (mBitmap == null) {
                        Util.log(mLink + " Failed to download");
                    }
                }
            }
            Message message = mHandler.obtainMessage(1, this);
            message.sendToTarget();
        }

        /**
         * Key from mem cache includes max size, so it is different than the normal
         *
         * @param imgKey
         * @return
         */
        private Bitmap getBitmapFromMemCache(String imgKey) {
            if (imgKey == null) {
                return null;
            }
            if (mMemLruCache != null) {
                return mMemLruCache.get(imgKey);
            }
            return null;
        }

        private String createMemCacheImgKey(String link, int maxW, int maxH) {
            return Util.encodeMD5(link + maxW + "_" + maxH);
        }

        /**
         * Try to find the image in the cache folder
         *
         * @param link the image link
         * @return the bitmap data or null
         */
        private Bitmap getBitmapFromDiskCache(String link, int maxW, int maxH) {
            synchronized (mLock) {
                // the disk cache is not started yet, wait for a bit
                while (mDiskCacheStarting) {
                    try {
                        mLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (mDiskCache != null) {
                    return mDiskCache.get(Util.encodeMD5(link), maxW, maxH);
                }
            }
            return null;
        }

        /**
         * In case the image has not been cached, the loader will download that from internet
         *
         * @param link the image link to download
         * @return the downloaded bitmap
         */
        private Bitmap loadImageFromSource(String link) {
            switch (mSource) {
                case SOURCE_NETWORK:
                    return download(link);
                case SOURCE_LOCAL:
                    return copyFromLocal(link);
            }
            return null;
        }

        private Bitmap download(String link) {
            HttpURLConnection connection = null;
            InputStream is = null;
            try {
                URL url = new URL(link);
                connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Util.log("RSP code: " + connection.getResponseCode());
                    return null;
                }
                is = connection.getInputStream();
                // try to save image first
                return putImageStreamToDiskCacheAndGet(is, link);
            } catch (IOException e) {
                //e.printStackTrace();
                Util.log("download failed: " + e.getMessage());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        private Bitmap copyFromLocal(String path) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(path);
                return putImageStreamToDiskCacheAndGet(fis, path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        /**
         * Save the stream as an image data, then retrieve it with options and cache
         *
         * @param is
         * @param link
         * @return the transformed bitmap
         */
        private Bitmap putImageStreamToDiskCacheAndGet(InputStream is, String link) {
            // try to save image first
            if (mDiskCache != null) {
                String key = Util.encodeMD5(link);
                mDiskCache.put(is, key);
                // reload it with decoding
                Bitmap bm = getBitmapFromDiskCache(link, mSpecificSizeWidth, mSpecificSizeHeight);
                //mBitmap = BitmapFactory.decodeStream(is);
                if (bm != null) {
                    cacheImage(bm);
                    return bm;
                }
            }
            return null;
        }

        /**
         * Caching image occurs here. First cache to the Mem, then disk
         *
         * @param bitmap the bitmap data
         */
        private void cacheImage(Bitmap bitmap) {
            if (bitmap == null) {
                return;
            }
            String memCacheImgKey = createMemCacheImgKey(mLink, mSpecificSizeWidth, mSpecificSizeHeight);
            if ("".equals(memCacheImgKey)) {
                Util.log("cacheImage failed. imgKey is invalid");
                return;
            }
            // cache to the mem lru cache first
            cacheMem(memCacheImgKey, bitmap);
            // cache to disk
            // Disk cache must be done in downloading process
//            if (mDiskCache != null) {
//                mDiskCache.put(imgKey, bitmap, mCompressFormat, mQuality);
//            }
        }

        @Override
        public int compareTo(@NonNull LoaderTask o) {
            if (this == o) {
                return 0;
            }
            if (mPriority == 0 && o.mPriority == 0) {
                // Because this priority blocking queue is not guarantee for 0,
                // so we return 1
                return 1;
            }
            // negative means the newer will be choose
            return o.mPriority - mPriority;
        }
    }

    private void cacheMem(String imgKey, Bitmap data) {
        if (mMemLruCache != null &&
                mMemLruCache.get(imgKey) == null) {
            mMemLruCache.put(imgKey, data);
        }
    }

    /**
     * This handler runs on UI thread for updating imageview
     */
    private static class MyHandler extends Handler {

        MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            LoaderTask task = (LoaderTask) msg.obj;
            if (task != null) {
                if (task.mBitmap != null) {
                    ImageLoader.getInstance().notifyOnSuccess(task);
                } else {
                    ImageLoader.getInstance().notifyOnFailed(ERROR_UNKNOWN, task);
                }
                SparseArray<WeakReference<ImageView>> imgViewList = task.mWeakRefImgViewList;
                if (imgViewList != null) {
                    for (int i = 0; i < imgViewList.size(); i++) {
                        WeakReference<ImageView> wView = imgViewList.get(imgViewList.keyAt(i));
                        if (wView != null) {
                            ImageView imgView = wView.get();
                            if (imgView != null) {
                                if (task.mBitmap != null) {
                                    imgView.setColorFilter(null);
                                    imgView.setImageBitmap(task.mBitmap);
                                } else {
                                    if (task.mErrorId >= 0) {
                                        imgView.setImageResource(task.mErrorId);
                                    }
                                }

                                switch (task.getAnimType()) {
                                    case ANIM_FADE:
                                        imgView.startAnimation(AnimationUtils.loadAnimation(imgView.getContext(),
                                                android.R.anim.fade_in));
                                        break;
                                }

                            } else {
                                Util.log("Imageview null...");
                            }
                        }
                    }
                }
            }
        }
    }
}
