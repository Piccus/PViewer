package com.piccus.pviewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String BASE_URL = "https://yande.re/post.json?limit=10" ;
	private static final String PLUS_URL = "&page=";
	private static final String TAGS_URL = "&tags=";
	private static final int MSG_SUCCESS = 0;
	private static final int MSG_FAILURE = 1;
	
	boolean TAGS_ON_OFF = false;
	String tags = "";
	String jsonfile = "";
	int page = 1;
	List<Item> items;
	ListView listView = null;
	ListAdapter adapter = null;
	ImageLoader imageLoader = null;
	Button next = null;
	Button front = null;
	Button index = null;
	MenuItem search = null;
	MenuItem home = null;
	Item itemPass = null;
	View footerView = null;
	MenuItem about = null;
	
	
	private Handler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_main);
		
		listView = (ListView) findViewById(R.id.list);
		
		
		footerView = getLayoutInflater().inflate(R.layout.footer, null);
		listView.addFooterView(footerView);
		
		
		next = (Button) footerView.findViewById(R.id.next);
		front = (Button) footerView.findViewById(R.id.front);
		index = (Button) footerView.findViewById(R.id.index);
		
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
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.discCacheFileCount(100) //缓存的文件数量  
		.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径  
		.defaultDisplayImageOptions(options)
        .build();
		
		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance(); 
		mHandler = new Handler() {
	        public void handleMessage (Message msg) {
	            switch(msg.what) {
	            case MSG_SUCCESS:
	            	initItems((String)msg.obj);
	        		showList();
	                break;
	 
	            case MSG_FAILURE:
	            	Toast.makeText(getApplicationContext(), "网络连接错误！", Toast.LENGTH_SHORT)
	            	.show();
	                break;
	            }
	        }
	    };
		
		new GetJsonThread(BASE_URL).start();
		
		next.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				page++;
				if(TAGS_ON_OFF == true){
					new GetJsonThread(BASE_URL + TAGS_URL + tags + PLUS_URL + page).start();
				}else{
					new GetJsonThread(BASE_URL + PLUS_URL + page).start();
				}
			}
		});
		front.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(page != 1){
					page--;
					if(TAGS_ON_OFF == true){
						new GetJsonThread(BASE_URL + TAGS_URL + tags + PLUS_URL + page).start();
					}else{
						new GetJsonThread(BASE_URL + PLUS_URL + page).start();
					}
				}
			}
			
		});
		index.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(page != 1){
					page = 1;
					if(TAGS_ON_OFF == true){
						new GetJsonThread(BASE_URL + TAGS_URL + tags + PLUS_URL + page).start();
					}else{
						new GetJsonThread(BASE_URL + PLUS_URL + page).start();
					}
				}
			}
			
		}); 
		
		listView.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				try {
					JSONArray jsonarray = new JSONArray(jsonfile);
					JSONObject json = new JSONObject(jsonarray.getString(position));
					itemPass = new Item(json.getString("preview_url"), json.getString("author"),
							json.getString("score"), json.getString("tags"), json.getString("id"),
							json.getString("rating"),json.getString("sample_url"), json.getString("jpeg_url"),
							json.getString("file_url"),json.getString("sample_width"), json.getString("sample_height"),
							json.getString("jpeg_width"), json.getString("jpeg_height"), json.getString("width"),
							json.getString("height"), json.getString("sample_file_size"), json.getString("jpeg_file_size"),
							json.getString("file_size"));
					Intent intent = new Intent(MainActivity.this, DataActivity.class);
					
					intent.putExtra("url", itemPass.url);
					intent.putExtra("author", itemPass.author);
					intent.putExtra("score", itemPass.score);
					intent.putExtra("tags", itemPass.tags);
					intent.putExtra("id", itemPass.id);
					intent.putExtra("rate", itemPass.rate);
					intent.putExtra("sample_url", itemPass.sample_url);
					intent.putExtra("jpeg_url", itemPass.jpeg_url);
					intent.putExtra("file_url", itemPass.file_url);
					intent.putExtra("sample_size", itemPass.sample_size);
					intent.putExtra("jpeg_size", itemPass.jpeg_size);
					intent.putExtra("file_size", itemPass.file_size);
					intent.putExtra("sample", itemPass.sample);
					intent.putExtra("jpeg", itemPass.jpeg);
					intent.putExtra("file", itemPass.file);
					startActivity(intent);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
	}
	
	

	private void showList() {
		adapter = new MyAdapter(this, items);
		
		listView.setAdapter(adapter);
	}

	private void initItems(String js) {
		items = new ArrayList<Item>();
		try {
			JSONArray jsonarray = new JSONArray(js);
			JSONObject json = null;
			for(int i = 0;i < jsonarray.length(); i++){
				json = new JSONObject(jsonarray.getString(i));
				items.add(new Item(json.getString("preview_url"), json.getString("author"), json.getString("id"),
						json.getString("score"), json.getString("rating")));
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		home = (MenuItem) menu.findItem(R.id.home);
		search = (MenuItem) menu.findItem(R.id.search);
		about = (MenuItem) menu.findItem(R.id.action_settings);
		
		about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				
				Intent intent = new Intent(MainActivity.this, About.class);
				startActivity(intent);
				
				return true;
			}
		});
		
		home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				TAGS_ON_OFF = false;
				page = 1;
				new GetJsonThread(BASE_URL).start();
				
				return  true;
			}
		});
		
		search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				final EditText et = new EditText(MainActivity.this);  
				
				new AlertDialog.Builder(MainActivity.this)
				.setTitle("Tags:")
				.setView(et)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								TAGS_ON_OFF = true;
								page = 1;
								tags = et.getText().toString();
								new GetJsonThread(BASE_URL + TAGS_URL + tags).start();
							}
						})
				.setNegativeButton("取消", null).show();
				return true;
			}
		});
		
		return true;
	}
	
	
	class GetJsonThread extends Thread {
		String url;
		public GetJsonThread(String url){
			this.url = url;
		}
		public void run() {			
				StringBuilder sb = new StringBuilder();
		        HttpClient httpClient = new DefaultHttpClient();
		        HttpGet httpGet = new HttpGet(url);
		        try{
		        	HttpResponse httpResponse = httpClient.execute(httpGet);
			        HttpEntity httpEntity = httpResponse.getEntity();
			        if (httpEntity != null) {
			            InputStream instream = httpEntity.getContent();
			            BufferedReader reader = new BufferedReader(
			            		new InputStreamReader(instream));
			            String line = null;
			            while ((line = reader.readLine()) != null) {
			                sb.append(line);			                
			            }
			        jsonfile = sb.toString();
			        }
		        }catch(Exception e){
		        	mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
		        	return;
		        }
		        mHandler.obtainMessage(MSG_SUCCESS, sb.toString()).sendToTarget();
		}
	}
	
	private class MyAdapter extends BaseAdapter{
		private List<Item> items;
		Context context;
		
		
		public MyAdapter(Context context, List<Item> items){
			this.context = context;
			this.items = items;
		}


		
		@Override
		public int getCount() {
			
			return (items == null)?0:items.size();
		}

		@Override
		public Object getItem(int position) {
		
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}


		public class ViewHolder{  
	        TextView author;  
	        TextView id;  
	        TextView score;
	        TextView rate;
	        ImageView img;  
	    }  
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Item item = (Item)getItem(position);
			ViewHolder viewHolder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.ilayout, null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView)convertView.findViewById(R.id.img);
				viewHolder.author = (TextView)convertView.findViewById(R.id.upView);
				viewHolder.score = (TextView)convertView.findViewById(R.id.scoreView);
				viewHolder.rate = (TextView)convertView.findViewById(R.id.rateView);
				viewHolder.id = (TextView)convertView.findViewById(R.id.IDView);
				convertView.setTag(viewHolder);  
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.author.setText(item.author);
			viewHolder.rate.setText(item.rate);
			viewHolder.score.setText(item.score);
			viewHolder.id.setText(item.id);
			imageLoader.displayImage(item.url, viewHolder.img);
			return convertView;
			
		}
	}
}
