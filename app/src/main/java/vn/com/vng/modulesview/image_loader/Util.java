package vn.com.vng.modulesview.image_loader;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * A util for the image loader  <br>
 * <p>
 * Author: Dat N. Truong<br>
 * Created date: 9/30/2015.
 */
public class Util {

    private static final boolean DEBUG = false;

    /**
     * Encode an string to a md5 string version
     *
     * @param link the string to hash
     * @return the md5 string or an empty string if error
     */
    public static String encodeMD5(String link) {
        if (link == null || "".equals(link)) {
            return "";
        }
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(link.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return "";
    }

    // taken from android source API level 21

    public static boolean deleteContents(File dir) {
        return deleteContents(dir, 0);
    }

    /**
     * @param dir
     * @param thresholdFactor how much data will be deleted. This will affect the next file level only
     * @return
     */
    public static boolean deleteContents(File dir, float thresholdFactor) {
        File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            int threshold = (int) (thresholdFactor * files.length);
            int index = 0;
            for (File file : files) {
                if (threshold > 0 && index >= threshold) {
                    return false;
                }
                if (file.isDirectory()) {
                    success &= deleteContents(file, 0);
                }
                if (!file.delete()) {
                    success = false;
                }
                index++;
            }
        }
        return success & dir.delete();
    }

    public static void log(String msg) {
        if (DEBUG) {
            Log.e(ImageLoader.class.getSimpleName(), msg);
        }
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                context.getExternalCacheDir() != null ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }
}
