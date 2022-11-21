package com.shopping.utilities;

import android.os.Build;

/**
 * CheckOs give you functionality to compute Android OS Versions
 */
public class CheckOs {

    /**
     * Method help you for checking Marshmallow OS Version
     *
     * @return Status of Found
     */
    public static boolean checkBuildMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
