package lsh.framgia.com.isoundcloud.constant;

import android.support.annotation.IntDef;

@IntDef({
        LoopMode.OFF,
        LoopMode.ONE,
        LoopMode.ALL
})

public @interface LoopMode {
    int OFF = 0;
    int ONE = 1;
    int ALL = 2;
}
