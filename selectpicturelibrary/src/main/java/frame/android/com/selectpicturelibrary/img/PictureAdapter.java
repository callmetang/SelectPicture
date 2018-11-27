package frame.android.com.selectpicturelibrary.img;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.File;
import java.util.List;

import frame.android.com.selectpicturelibrary.R;

/**
 * Created by callmetang on 2018/11/20.
 */

public class PictureAdapter extends CommonAdapter<PictureBean> {

    private int itemWidth = 0;

    /**
     * @Description 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public PictureAdapter(Context context, int layoutId, List<PictureBean> datas, int numColumns) {
        super(context, layoutId, datas);
        itemWidth = (ScreenUtils.getScreenWidth(context) - dip2px(context, 4)) / numColumns;
        Log.d("PictureAdapter", "itemWidth:" + itemWidth);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final PictureBean item, int position) {

        FrameLayout layout = viewHolder.getView(R.id.root);
        LinearLayout tack = viewHolder.getView(R.id.layout_tack);

        if (layout.getLayoutParams() != null) {
            layout.getLayoutParams().width = itemWidth;
            layout.getLayoutParams().height = itemWidth;
        }

        if (item.path.equals(SelectPictureActivity.TACK_PICTURE)) {
            tack.setVisibility(View.VISIBLE);
        } else {
            tack.setVisibility(View.GONE);



            View mask = viewHolder.getView(R.id.mask);

            mask.setVisibility(item.isChoice ? View.VISIBLE : View.GONE);

            ImageView imageView = viewHolder.getView(R.id.image);
            final ImageView check = viewHolder.getView(R.id.check);
            check.setImageResource(item.isChoice ? R.drawable.select_img_ic_check_box_black_24dp : R.drawable.select_img_ic_check_box_outline_blank_black_24dp);

//        Picasso.get().load(new File(item.path)).into(imageView);
            Glide.with(mContext).load(new File(item.path))
//                    .error(R.drawable.ic_launcher_background).placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);

        }


    }
}
