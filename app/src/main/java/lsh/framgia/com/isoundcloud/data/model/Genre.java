package lsh.framgia.com.isoundcloud.data.model;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.constant.Constant;

public enum Genre {

    ALL_MUSIC(R.string.label_all_music, R.drawable.bg_all_music, Constant.KEY_ALL_MUSIC),
    ALL_AUDIO(R.string.label_all_audio, R.drawable.bg_all_audio, Constant.KEY_ALL_AUDIO),
    ALTERNATIVE_ROCK(R.string.label_alternative_rock, R.drawable.bg_alternative_rock, Constant.KEY_ALTERNATIVE_ROCK),
    AMBIENT(R.string.label_ambient, R.drawable.bg_ambient, Constant.KEY_AMBIENT),
    CLASSICAL(R.string.label_classical, R.drawable.bg_classical, Constant.KEY_CLASSICAL),
    COUNTRY(R.string.label_country, R.drawable.bg_country, Constant.KEY_COUNTRY);

    private int mNameResId;
    private int mArtworkResId;
    private String mKey;

    Genre(int stringId, int resId, String key) {
        mNameResId = stringId;
        mArtworkResId = resId;
        mKey = key;
    }

    public int getNameResId() {
        return mNameResId;
    }

    public int getArtworkResId() {
        return mArtworkResId;
    }

    public String getKey() {
        return mKey;
    }
}
