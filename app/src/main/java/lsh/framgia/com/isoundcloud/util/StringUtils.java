package lsh.framgia.com.isoundcloud.util;

import java.util.Locale;

import lsh.framgia.com.isoundcloud.BuildConfig;
import lsh.framgia.com.isoundcloud.constant.Constant;

public class StringUtils {

    public static String formatFetchingTrackUrl(String genre, int offset, int limit) {
        return String.format(Locale.ENGLISH, Constant.FORMAT_FETCHING_URL,
                genre, BuildConfig.ApiKey, offset, limit);
    }
}
