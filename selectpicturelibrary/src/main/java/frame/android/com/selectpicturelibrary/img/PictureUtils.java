package frame.android.com.selectpicturelibrary.img;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by callmetang on 2018/11/20.
 */

public class PictureUtils {

    public static List<PictureBean> getAllImages(Context context) {

        List<PictureBean> pictureBeans = new ArrayList<>();

        if (context != null) {
//            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            //只查询jpeg和png的图片
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED+" desc");
            if (cursor != null) {
                while (cursor.moveToNext()) {
//                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String dirName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    PictureBean pb = new PictureBean();
                    pb.path = path;
                    pb.dirName = dirName;
                    pictureBeans.add(pb);
                    Log.d("PictureUtils", pb.toString());
                }
            }
        }

        Log.d("PictureUtils", "pictureBeans:" + pictureBeans.size());
        return pictureBeans;

    }

    /**
     * 获取全部图片
     *
     * @param cr
     * @return
     */
    public static List<Uri> GetAllPic(ContentResolver cr) {
        List<Uri> uriList = new ArrayList<>();
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_MODIFIED};

//查询条件是DATE_MODIFIED 的倒序
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cursor == null) {
            return null;
        }
//遍历游标存入列表
        while (cursor.moveToNext()) {
            String image_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            File file = new File(image_path);
            if (file.exists() && file.length() != 0)
                uriList.add(Uri.fromFile(file));
        }
        cursor.close();
        return uriList;
    }
}
