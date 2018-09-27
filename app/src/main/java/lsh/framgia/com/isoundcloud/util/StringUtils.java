package lsh.framgia.com.isoundcloud.util;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import lsh.framgia.com.isoundcloud.BuildConfig;
import lsh.framgia.com.isoundcloud.constant.Constant;

public class StringUtils {

    public static String formatFetchingTrackUrl(String genre, int offset, int limit) {
        return String.format(Locale.ENGLISH, Constant.FORMAT_FETCHING_URL,
                genre, BuildConfig.ApiKey, offset, limit);
    }

    public static String formatStreamUrl(String uri) {
        return uri.contains(Constant.TEXT_HTTP) ? String.format
                (Locale.ENGLISH, Constant.FORMAT_STREAM_URL, uri, BuildConfig.ApiKey) : uri;
    }

    public static String formatSearchingTrackUrl(String query, int offset, int limit) {
        return String.format(Locale.ENGLISH, Constant.FORMAT_SEARCH_URL,
                BuildConfig.ApiKey, query, offset, limit);
    }

    public static String convertMillisToDuration(int millis) {
        return String.format(
                Locale.ENGLISH,
                Constant.FORMAT_DURATION,
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static String getOriginalUrl(String artworkUrl) {
        return artworkUrl.replace(Constant.TEXT_LARGE, Constant.TEXT_ORIGINAL);
    }

    public static String formatTrackFile(String fileName) {
        return String.format(Locale.ENGLISH, Constant.FORMAT_TRACK_FILE, fileName);
    }

    public static String formatSingleWhereClause(String clause) {
        return String.format(Locale.ENGLISH, Constant.FORMAT_SINGLE_WHERE_CLAUSE, clause);
    }

    public static String formatDoubleWhereClause(String clause1, String clause2) {
        return String.format(Locale.ENGLISH, Constant.FORMAT_DOUBLE_WHERE_CLAUSE, clause1, clause2);
    }

    public static String formatFilePath(String parentPath, String subPath) {
        return String.format(Locale.ENGLISH, Constant.FORMAT_FILE_PATH, parentPath, subPath);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
