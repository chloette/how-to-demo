package com.example.testapk;

import java.io.File;
import java.lang.reflect.Method;

import com.tabolt.reflect.ReflectResource;

import dalvik.system.DexClassLoader;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private ImageView img;
	private TextView text;
	private ReflectResource reflectResource;
	
	
	AssetManager mAssetManager = null;
	Resources mResources = null;

	//String libPath = Environment.getExternalStorageDirectory() + "/ResApk.apk";
	String libPath = Environment.getExternalStorageDirectory().toString()+ File.separator +"ResApk.apk";

	protected void loadResources() {
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getMethod(
					"addAssetPath", String.class);
			addAssetPath.invoke(assetManager,
					libPath);
			mAssetManager = assetManager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Resources superRes = super.getResources();
		mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
				superRes.getConfiguration());
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
        
        loadResources();
        
        reflectResource = new ReflectResource(mResources, "com.example.resapk");
        
        File tmpDir = getDir("dex", 0);
		DexClassLoader classLoader = new DexClassLoader(libPath, tmpDir.getAbsolutePath(),
				null, super.getClassLoader());
        
        targetContext = new TargetContext(this);
        targetContext.setContext(classLoader);
    }
    
    TargetContext targetContext = null;
    
    private void initView(){
    	img = (ImageView) findViewById(R.id.img);
    	text = (TextView) findViewById(R.id.text);
    	findViewById(R.id.getimg).setOnClickListener(this);
    	findViewById(R.id.getanim).setOnClickListener(this);
    	findViewById(R.id.getcolor).setOnClickListener(this);
    	findViewById(R.id.getdimens).setOnClickListener(this);
    	findViewById(R.id.getlayout).setOnClickListener(this);
    	findViewById(R.id.getstr).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getimg:
			Drawable drawable = reflectResource.getResApkDrawable("cmcc");
			img.setBackgroundDrawable(drawable);
			break;
		case R.id.getanim:
			Animation anim = reflectResource.getResApkAnim(MainActivity.this, "zoomin");
			img.startAnimation(anim);
			break;
		case R.id.getcolor:
			text.setTextColor(reflectResource.getResApkColor("bisque"));
			break;
		case R.id.getdimens:
			text.setTextSize(reflectResource.getResApkDimens("size_20"));
			break;
		case R.id.getlayout:
			showDialog();
			break;
		case R.id.getstr:
			text.setText(reflectResource.getResApkString("resapktest"));
			break;
		}
	}
	
	private void showDialog(){
		View view = reflectResource.getResApkLayoutView(targetContext, "activity_main");
//		final Button resbtn2 = (Button) reflectResource.getResApkWidgetView(view, "resbtn");
//		final Button resbtn1 = (Button) reflectResource.getResApkWidgetView(view, "restext");
		Dialog dialog = new Dialog(MainActivity.this);
		dialog.setTitle("资源Apk界面");
		dialog.setContentView(view);
		dialog.show();
//		resbtn1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(MainActivity.this, "I come from resApk,click btn1!", 0).show();
//			}
//		});
//		resbtn2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(MainActivity.this, "I come from resApk,click btn2!", 0).show();
//			}
//		});
	}
}
