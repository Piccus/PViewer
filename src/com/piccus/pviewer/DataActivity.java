package com.piccus.pviewer;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DataActivity extends Activity {
	
	ImageView imgPreview = null;
	TextView author = null;
	TextView score = null;
	TextView tags = null;
	TextView id = null;
	TextView rate = null;
	TextView sampleSize = null;
	TextView sample = null;
	TextView jpegSize = null;
	TextView jpeg = null;
	TextView fileSize = null;
	TextView file = null;
	Button getSample = null;
	Button getJpeg = null;
	Button getFile = null;
	Item item = null;
	ImageLoader imageLoader = null;
	MenuItem about = null;
	
	DownloadManager downloadManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		
		String serviceString = Context.DOWNLOAD_SERVICE;
	    downloadManager = (DownloadManager)getSystemService(serviceString);
		
		imgPreview = (ImageView) findViewById(R.id.imagePreview);
		
		author = (TextView) findViewById(R.id.authorView);
		score = (TextView) findViewById(R.id.gradeView);
		tags = (TextView) findViewById(R.id.tagshowdetail);
		id = (TextView) findViewById(R.id.idView);
		rate = (TextView)findViewById(R.id.ratingView);
		sampleSize = (TextView) findViewById(R.id.sampleSizeView);
		sample = (TextView) findViewById(R.id.sampleView);
		jpegSize = (TextView) findViewById(R.id.jpegSizeView);
		jpeg = (TextView) findViewById(R.id.jpegView);
		fileSize = (TextView) findViewById(R.id.fileSizeView);
		file = (TextView) findViewById(R.id.fileView);
		
		getSample = (Button) findViewById(R.id.getSample);
		getJpeg = (Button) findViewById(R.id.getJpeg);
		getFile = (Button) findViewById(R.id.getFile);
			
		Intent intent = this.getIntent();
		item = new Item(intent.getExtras().getString("url"), intent.getExtras().getString("author"),
				intent.getExtras().getString("score"), intent.getExtras().getString("tags"),
				intent.getExtras().getString("id"),intent.getExtras().getString("rate"),
				intent.getExtras().getString("sample_url"), intent.getExtras().getString("jpeg_url"),
				intent.getExtras().getString("file_url"), intent.getExtras().getString("sample_size"),
				intent.getExtras().getString("jpeg_size"), intent.getExtras().getString("file_size"),
				intent.getExtras().getString("sample"), intent.getExtras().getString("jpeg"), 
				intent.getExtras().getString("file"));
		
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "PViewer/cache"); 
		
		DisplayImageOptions options; 
		options = new DisplayImageOptions.Builder() 
		.showImageOnLoading(R.drawable.load) //设置图片在下载期间显示的图片 
		.showImageForEmptyUri(R.drawable.load)//设置图片Uri为空或是错误的时候显示的图片 
		.showImageOnFail(R.drawable.load) 
		.cacheInMemory(true) //设置图片加载/解码过程中错误时候显示的图片 
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中 
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中 
		.build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.memoryCacheExtraOptions(3600,3600)
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.discCacheFileCount(100) //缓存的文件数量  
		.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径  
		.defaultDisplayImageOptions(options)
        .build();
		
		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance(); 
		
		showView();
		
		getSample.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse(item.sample_url);
				DownloadManager.Request request = request = new Request(uri);
				request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,item.id + "sample.jpg"); 
			    request.setTitle(item.id + "sample.jpg");
			    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			    downloadManager.enqueue(request);
			}	
		});
		getJpeg.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse(item.jpeg_url);
				DownloadManager.Request request = request = new Request(uri);
				request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,item.id + "jpeg.jpg"); 
			    request.setTitle(item.id + "jpeg.jpg");
			    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			    downloadManager.enqueue(request);
			}	
		});
		getFile.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse(item.file_url);
				DownloadManager.Request request = request = new Request(uri);
				request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,item.id + "file.jpg"); 
			    request.setTitle(item.id + "file.jpg");
			    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			    downloadManager.enqueue(request);
			}	
		});
		
		
	}

	private void showView() {
		author.setText(item.author);
		score.setText(item.score);
		tags.setText(item.tags);
		id.setText(item.id);
		rate.setText(item.rate);
		sampleSize.setText(item.sample_size);
		jpegSize.setText(item.jpeg_size);
		fileSize.setText(item.file_size);
		sample.setText(item.sample);
		jpeg.setText(item.jpeg);
		file.setText(item.file);
		imageLoader.displayImage(item.url, imgPreview);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data, menu);
		
about = (MenuItem) menu.findItem(R.id.action_settings);
		
		about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				
				Intent intent = new Intent(DataActivity.this, About.class);
				startActivity(intent);
				
				return true;
			}
		});
		
		return true;
	}

}
