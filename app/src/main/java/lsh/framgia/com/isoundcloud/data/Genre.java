package lsh.framgia.com.isoundcloud.data;

import lsh.framgia.com.isoundcloud.R;

public enum Genre {

    ALL_MUSIC(R.string.label_all_music, R.drawable.bg_all_music),
    ALL_AUDIO(R.string.label_all_audio, R.drawable.bg_all_audio),
    ALTERNATIVE_ROCK(R.string.label_alternative_rock, R.drawable.bg_alternative_rock),
    AMBIENT(R.string.label_ambient, R.drawable.bg_ambient),
    CLASSICAL(R.string.label_classical, R.drawable.bg_classical),
    COUNTRY(R.string.label_country, R.drawable.bg_country);

    private int mNameResId;
    private int mArtworkResId;

    Genre(int stringId, int resId) {
        mNameResId = stringId;
        mArtworkResId = resId;
    }

    public int getNameResId() {
        return mNameResId;
    }

    public void setNameResId(int nameResId) {
        nameResId = nameResId;
    }

    public int getArtworkResId() {
        return mArtworkResId;
    }

    public void setArtworkResId(int martworkresid) {
        mArtworkResId = martworkresid;
    }
}
