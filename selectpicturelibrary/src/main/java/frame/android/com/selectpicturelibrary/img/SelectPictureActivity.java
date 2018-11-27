package frame.android.com.selectpicturelibrary.img;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import frame.android.com.selectpicturelibrary.R;

public class SelectPictureActivity extends AppCompatActivity {


    /**
     * 最多选择张数
     */
    private int maxCount;
    /**
     * 是否显示拍照按钮
     */
    private boolean showTackPicture;

    private GridView mGridView;
    private TextView mTvFinish;


    private PictureAdapter mPictureAdapter;
    private List<PictureBean> pictureBeans = new ArrayList<>();
    private List<PictureBean> allImages = new ArrayList<>();


    private TextView mTvChange;
    private String allName = "所有图片";
    public static final String TACK_PICTURE = "tackPicture";

    private List<PictureBean> dirs = new ArrayList<>();

    private boolean isAddDirs(PictureBean bean) {

        boolean b = false;
        for (PictureBean dirs : dirs) {
            if (dirs.dirName.equals(bean.dirName)) {
                b = true;
                break;
            }
        }
        return b;
    }
    private ImageView mIvBack;
    private TextView mTvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);

        maxCount = getIntent().getIntExtra("maxCount", 6);
        showTackPicture = getIntent().getBooleanExtra("showTackPicture", false);

        mGridView = (GridView) findViewById(R.id.grid_view);
        mTvChange = (TextView) findViewById(R.id.tv_change);
        mTvFinish = (TextView) findViewById(R.id.tv_finish);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvBack = (TextView) findViewById(R.id.tv_back);


        allImages.addAll(PictureUtils.getAllImages(this));
        pictureBeans.addAll(PictureUtils.getAllImages(this));


        PictureBean all = new PictureBean();
        all.dirName = allName;
        if (allImages.size() > 0) {
            all.path = allImages.get(0).path;
            all.count = allImages.size();
        }
        dirs.add(all);

        for (PictureBean bean : allImages) {

            if (!isAddDirs(bean)) {
                PictureBean pictureBean = new PictureBean();
                pictureBean.dirName = bean.dirName;
                dirs.add(pictureBean);
            }
        }


        for (PictureBean dirBean : dirs) {

            if (!dirBean.dirName.equals(allName)) {
                List<PictureBean> temp = new ArrayList<>();

                for (PictureBean bean : allImages) {
                    if (bean.dirName.equals(dirBean.dirName)) {
                        temp.add(bean);
                    }
                }

                dirBean.count = temp.size();
                if (temp.size() > 0) {
                    dirBean.path = temp.get(0).path;
                }
            }

        }

        Log.d("SelectPictureActivity", "dirs:" + dirs.toString());


        addTackPicture();
        mPictureAdapter = new PictureAdapter(this, R.layout.item_picture, pictureBeans, 3);
        mGridView.setAdapter(mPictureAdapter);


        initEvents();
    }

    private void addTackPicture() {
        if (showTackPicture) {
            PictureBean tack = new PictureBean();
            tack.path = TACK_PICTURE;
            pictureBeans.add(0, tack);
        }
    }

    public static final int RESULT_CODE_SELECT_FINISH = 1001010;

    private void initEvents() {
        mTvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<PictureBean> selectBeans = new ArrayList<>();

                for (PictureBean pb : allImages) {
                    if (pb.isChoice) {
                        selectBeans.add(pb);
                    }
                }
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("imgs", selectBeans);
                setResult(RESULT_CODE_SELECT_FINISH, intent);
                finish();
            }
        });
        mTvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pictureDirPopupWindow == null) {

                    pictureDirPopupWindow = new PictureDirPopupWindow(SelectPictureActivity.this, dirs);
                }
                pictureDirPopupWindow.showAtLocation(view);
                pictureDirPopupWindow.setPicItemClickCallBack(new PicItemClickCallBack() {
                    @Override
                    public void onCallBack(String dirName) {
                        pictureBeans.clear();

                        mTvChange.setText(dirName);
                        if (dirName.equals(allName)) {
                            addTackPicture();
                            pictureBeans.addAll(allImages);
                        } else {
                            for (PictureBean bean : allImages) {
                                if (bean.dirName.equals(dirName)) {
                                    pictureBeans.add(bean);
                                }
                            }
                        }

                        mPictureAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                int count = getSelectCount();

                Log.d("SelectPictureActivity", "count:" + count);

                PictureBean bean = pictureBeans.get(i);

                if (bean.path.equals(TACK_PICTURE)) {
                    tackPicture();
                    return;
                }

                if (!bean.isChoice) {
                    if (count > maxCount - 1) {
                        Toast.makeText(SelectPictureActivity.this, "最多选择" + maxCount + "张", Toast.LENGTH_SHORT).show();
                    } else {
                        bean.isChoice = true;
                    }
                } else {
                    bean.isChoice = false;
                }

                mPictureAdapter.notifyDataSetChanged();


                for (PictureBean all : allImages) {
                    if (bean.path.equals(all.path)) {
                        all.isChoice = bean.isChoice;
                        break;
                    }
                }
                count = getSelectCount();
                mTvFinish.setText(String.format("完成(%d/%d)", count, maxCount));


            }
        });
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvBack.performClick();
            }
        });
    }

    private String tackPath = "";

    private void tackPicture() {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        File cacheDir = getExternalCacheDir();
        if (cacheDir != null) {
            File file = new File(cacheDir.getAbsolutePath(), System.currentTimeMillis() + ".png");
            tackPath = file.getAbsolutePath();
            // 把文件地址转换成Uri格式
            Uri uri = Uri.fromFile(file);
            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, 10001);
        } else {
            Log.d("SelectPictureActivity", "拍照保存的路径不存在..");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            Log.d("SelectPictureActivity", "tackPath : " + tackPath);

            ArrayList<PictureBean> selectBeans = new ArrayList<>();

            PictureBean tack = new PictureBean();
            tack.path = tackPath;
            selectBeans.add(tack);
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("imgs", selectBeans);
            setResult(RESULT_CODE_SELECT_FINISH, intent);
            finish();
        }
    }

    private int getSelectCount() {
        int count = 0;
        for (int j = 0; j < allImages.size(); j++) {
            if (allImages.get(j).isChoice) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
    }

    private PictureDirPopupWindow pictureDirPopupWindow;

}
