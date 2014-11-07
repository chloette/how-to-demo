package com.example.testapk;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Environment;
import android.view.LayoutInflater;

public class TargetContext extends ContextWrapper {
	AssetManager mAssetManager = null;
	Resources mResources = null;
	LayoutInflater mLayoutInflater = null;
	Theme mTheme = null;
	ClassLoader mClassLoader = null;

	String packageName = "com.example.testapk";
	String libPath = Environment.getExternalStorageDirectory().toString()
			+ File.separator + "ResApk.apk";

	protected void loadResources() {
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getMethod(
					"addAssetPath", String.class);
			addAssetPath.invoke(assetManager, libPath);
			mAssetManager = assetManager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Resources superRes = super.getResources();
		mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
				superRes.getConfiguration());
	}

	public TargetContext(Context base) {
		super(base);
		loadResources();
	}
	
	public void setContext(ClassLoader loader) {
		mClassLoader = loader;
	}

	@Override
	public Resources getResources() {
		return mResources;
	}

	@Override
	public AssetManager getAssets() {
		return mAssetManager;
	}
	
	@Override
	public ClassLoader getClassLoader() {
		if(mClassLoader == null) {
			File tmpDir = getDir("dex", 0);
			mClassLoader = new DexClassLoader(libPath, tmpDir.getAbsolutePath(),
					null, super.getClassLoader());
		}
		
		return mClassLoader;
	}

	@Override
	public Object getSystemService(String name) {
		if (LAYOUT_INFLATER_SERVICE.equals(name)) {
			if (mLayoutInflater == null) {
				try {
					Class<?> cls = Class
							.forName("com.android.internal.policy.PolicyManager");
					Method m = cls.getMethod("makeNewLayoutInflater",
							Context.class);
					mLayoutInflater = (LayoutInflater) m.invoke(null, this);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			if (mLayoutInflater != null) {
				return mLayoutInflater;
			}
		}
		return super.getSystemService(name);
	}

	@Override
	public Resources.Theme getTheme() {
		if (mTheme != null) {
			return mTheme;
		}
		
        mTheme = mResources.newTheme();
		return mTheme;
	}
	
	@Override
	public String getPackageName() {
		return packageName;
	}
}
