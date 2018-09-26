package lsh.framgia.com.isoundcloud.constant;

public class Constant {

    public static final String KEY_ALL_MUSIC = "all-music";
    public static final String KEY_ALL_AUDIO = "all-audio";
    public static final String KEY_ALTERNATIVE_ROCK = "alternativerock";
    public static final String KEY_AMBIENT = "ambient";
    public static final String KEY_CLASSICAL = "classical";
    public static final String KEY_COUNTRY = "country";

    public static final String FORMAT_FETCHING_URL =
            "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%%3Agenres%%3A%s&" +
                    "&client_id=%s&offset=%d&limit=%d";
    public static final String FORMAT_STREAM_URL = "%s/stream?client_id=%s";
    public static final String FORMAT_SEARCH_URL =
            "http://api.soundcloud.com/tracks?client_id=%s&q=%s&offset=%s&limit=%s";
    public static final String FORMAT_DURATION = "%02d:%02d";
    public static final String FORMAT_TRACK_FILE = "%s.mp3";
    public static final String FORMAT_SINGLE_WHERE_CLAUSE = "%s = ?";
    public static final String FORMAT_DOUBLE_WHERE_CLAUSE = "%s = ? AND %s = ?";

    public static final String NEW_LINE = "\n";
    public static final String TEXT_LARGE = "large";
    public static final String TEXT_ORIGINAL = "original";
    public static final String TEXT_NULL = "null";
}
