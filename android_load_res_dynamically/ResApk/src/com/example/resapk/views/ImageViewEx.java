package com.example.resapk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageViewEx extends ImageView {
	public ImageViewEx(Context context) {
		super(context);
	}

	public ImageViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageViewEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onAttachedToWindow() {
		Toast.makeText(getContext(), "ImageViewEx here.", Toast.LENGTH_SHORT).show();
	}
}
