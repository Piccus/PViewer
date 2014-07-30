package com.piccus.pviewer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
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
        TextView star;  
        TextView type;  
        ImageView img;  
    }  
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Item item = (Item)getItem(position);
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.itemlayout, null);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView)convertView.findViewById(R.id.img);
			viewHolder.star = (TextView)convertView.findViewById(R.id.star);
			viewHolder.type = (TextView)convertView.findViewById(R.id.type);
			convertView.setTag(viewHolder);  
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.img.setImageResource(item.img);
		viewHolder.type.setText(item.type);
		viewHolder.star.setText(String.valueOf(item.star));
		
		return convertView;
		
	}

}
