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
}
