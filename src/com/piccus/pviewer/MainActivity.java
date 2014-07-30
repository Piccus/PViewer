package com.piccus.pviewer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	List<Item> items;
	ListView listView = null;
	ListAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ListView) findViewById(R.id.listView);
		
		
		showList();
	}

	private void showList() {
		this.initItems();
		adapter = new MyAdapter(this, items);
		
		listView.setAdapter(adapter);
	}

	private void initItems() {
		items = new ArrayList<Item>();
		items.add(new Item(R.drawable.abyssal_blade, "深渊之刃", 5));
		items.add(new Item(R.drawable.aegis, "不朽之守护", 5));
		items.add(new Item(R.drawable.ancient_janggo, "韧鼓", 3));
		items.add(new Item(R.drawable.arcane_boots, "秘法鞋", 2));
		items.add(new Item(R.drawable.armlet, "臂章", 4));
		items.add(new Item(R.drawable.assault, "强袭装甲", 5));
		items.add(new Item(R.drawable.band_of_elvenskin, "敏捷便鞋", 1));
		items.add(new Item(R.drawable.basher, "碎颅锤", 3));
		items.add(new Item(R.drawable.belt_of_strength, "巨人力量腰带", 1));
		items.add(new Item(R.drawable.bfury, "狂战斧", 4));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
