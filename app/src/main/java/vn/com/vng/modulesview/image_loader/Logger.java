package vn.com.vng.modulesview.image_loader;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


/**
 * Logger <br>
 * <p/>
 * Author: Dat N. Truong<br>
 * Created date: 10/9/2015.
 */
public class Logger {

    public static final String DEF_TAG = "ants_support";

    static class LogDuration {
        String name;
        long start;
        long end;
    }

    private static final String TAG = Logger.class.getSimpleName();
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_FILE = false;
    private static final String LOG_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "as";

    private static Logger sInstance = new Logger();
    private HashMap<String, LogDuration> mLogMap = new HashMap<>();

    public static Logger getInstance() {
        return sInstance;
    }

    public static void log(String tag, String msg) {
        logInternal(tag, msg);
    }

    /**
     * Log console with default tag
     *
     * @param msg the message
     */
    public static void log(String msg) {
        logInternal(null, msg);
    }

    private static void logInternal(String tag, String msg) {
        if (DEBUG) {
            if (tag == null) {
                tag = DEF_TAG;
            }
            Log.d(tag, msg);
        }
    }

    public static synchronized void logFile(String tag, String msg) {
        if (!DEBUG_FILE) {
            return;
        }
        StringBuilder logContent = new StringBuilder();
        logContent.append(formatDate(System.currentTimeMillis(),
                "dd/MM/yyyy HH:mm:ss:SSS")).append(" ").append(tag).append(": ").append(msg).append("\n");
        try {
            File path = new File(LOG_PATH);
            if (!path.exists()) {
                path.mkdirs();
            }
            FileWriter writer = new FileWriter(path.getAbsoluteFile() + "/log.t"
                    , true);
            writer.write(logContent.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startLogDuration(String id, String title, long start) {
        if (!DEBUG_FILE) {
            return;
        }
        Log.d(TAG, "startLogDuration: " + id);
        // check if the current id has
        LogDuration logDuration = new LogDuration();
        logDuration.name = title;
        logDuration.start = start;
        mLogMap.put(id, logDuration);
    }

    public void endLogDuration(String id, long end) {
        if (!DEBUG_FILE) {
            return;
        }
        // check if the current one is valid
        LogDuration logDuration = mLogMap.get(id);
        // only handle log end when its valid
        if (logDuration != null) {
            logDuration.end = end;
            // start writing to file and remove this record
            logFile(TAG, String.format("Man hinh %s log data start: %d end: %d total: %d ms",
                    logDuration.name, logDuration.start, logDuration.end, (logDuration.end - logDuration.start)));
            // remove this record from the map
            mLogMap.remove(id);
            Log.d(TAG, "endLogDuration: removed " + id + " from the map");
        }
    }

    public static String formatDate(long time, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(time));
    }
}
