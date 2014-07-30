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
		items.add(new Item(R.drawable.abyssal_blade, "��Ԩ֮��", 5));
		items.add(new Item(R.drawable.aegis, "����֮�ػ�", 5));
		items.add(new Item(R.drawable.ancient_janggo, "�͹�", 3));
		items.add(new Item(R.drawable.arcane_boots, "�ط�Ь", 2));
		items.add(new Item(R.drawable.armlet, "����", 4));
		items.add(new Item(R.drawable.assault, "ǿϮװ��", 5));
		items.add(new Item(R.drawable.band_of_elvenskin, "���ݱ�Ь", 1));
		items.add(new Item(R.drawable.basher, "��­��", 3));
		items.add(new Item(R.drawable.belt_of_strength, "������������", 1));
		items.add(new Item(R.drawable.bfury, "��ս��", 4));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
