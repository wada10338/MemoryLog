package wada.com.deliverables

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.security.AccessControlContext

public class CheckData {
    //入力値のチェック
    fun canDataIn(title: String, contents: String): Boolean {
        return if ("" == title) {
            false
        } else {
            "" != contents
        }
    }
    //位置情報を取得する際に必要なpermissionの確認
    fun  isLocationAvailable(context:Context):Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }
}