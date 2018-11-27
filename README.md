# SelectPicture

imgs:


![images](https://github.com/callmetang/SelectPicture/blob/master/imgs/device-2018-11-27-112320.png)

![images](https://github.com/callmetang/SelectPicture/blob/master/imgs/device-2018-11-27-112430.png)

How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.callmetang:SelectPicture:v0.0.5'
	}

Step 3. Open activity
	
	Intent intent = new Intent(this, SelectPictureActivity.class);
	intent.putExtra("maxCount", 9);
	startActivityForResult(intent, 100);

Step 4. Callback
 

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == SelectPictureActivity.RESULT_CODE_SELECT_FINISH) {
            if (data != null) {
                ArrayList<PictureBean> imgs = data.getParcelableArrayListExtra("imgs");
                Log.d("MainActivity", "imgs:" + imgs);
            }
        }
    }
