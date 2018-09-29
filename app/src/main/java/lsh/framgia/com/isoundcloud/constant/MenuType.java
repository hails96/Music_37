package lsh.framgia.com.isoundcloud.constant;

import android.support.annotation.IntDef;

@IntDef({
        MenuType.DOWNLOAD,
        MenuType.DELETE
})

public @interface MenuType {
    int DOWNLOAD = 0;
    int DELETE = 1;
}