package frame.android.com.selectpicturelibrary.img;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by callmetang on 2018/11/20.
 */

public class PictureBean implements Parcelable {
    public int count;
    public String path;//图片地址
    public String dirName;//图片所在文件夹
    public boolean isChoice;//是否选中

    @Override
    public String toString() {
        return "PictureBean{" +
                "count=" + count +
                ", path='" + path + '\'' +
                ", dirName='" + dirName + '\'' +
                ", isChoice=" + isChoice +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeString(this.path);
        dest.writeString(this.dirName);
        dest.writeByte(this.isChoice ? (byte) 1 : (byte) 0);
    }

    public PictureBean() {
    }

    protected PictureBean(Parcel in) {
        this.count = in.readInt();
        this.path = in.readString();
        this.dirName = in.readString();
        this.isChoice = in.readByte() != 0;
    }

    public static final Creator<PictureBean> CREATOR = new Creator<PictureBean>() {
        @Override
        public PictureBean createFromParcel(Parcel source) {
            return new PictureBean(source);
        }

        @Override
        public PictureBean[] newArray(int size) {
            return new PictureBean[size];
        }
    };
}