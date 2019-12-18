package com.heynchy.douyin.videodownload.utils;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Android 6.0 内存卡权限读取
 *
 * @author CHY
 * Create at 2017/6/29 15:25.
 */

public class PermissionUtil {
    // Storage Permissions
    public static final int REQUEST_EXTERNAL_STORAGE = 123;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    /**
     * 判断是否拥有内存卡读取权限
     *
     * @param activity
     * @return
     */
    public static boolean hasStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 判断二维码权限是否获得
     *
     * @param activity
     * @return
     */
    public static boolean isHasPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            for (String permission : mPermissionList) {
                if (PackageManager.PERMISSION_GRANTED !=
                        ContextCompat.checkSelfPermission(activity, permission)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取内存卡读取权限
     */
    public static void getPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE);
    }
}
