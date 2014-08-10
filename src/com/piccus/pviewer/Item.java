package com.piccus.pviewer;

public class Item {
	public String url;
	public String author;
	public String tags;
	public String score;
	public String id;
	public String rate;
	public String sample_url;
	public String jpeg_url;
	public String file_url;
	public String sample_size;
	public String jpeg_size;
	public String file_size;
	public String sample;
	public String jpeg;
	public String file;
	
	public Item(String url, String author, String id, String score, String rate){
		this.url = url;
		this.author = author;
		this.id = id;
		this.score = score;
		this.rate = rate;
	}
	
	public Item(String url, String author, String tags, String score,
			String id, String rate,String sample_url, String jpeg_url, String file_url,
			String simple_width, String simple_height, String jpeg_width,
			String jpeg_height, String width, String height,
			String sample, String jpeg, String file){
		this.url = url;
		this.author = author;
		this.tags = tags;
		this.score = score;
		this.id = id;
		this.rate = rate;
		this.sample_url = sample_url;
		this.jpeg_url = jpeg_url;
		this.file_url = file_url;
		this.sample_size = simple_width + "X" + simple_height;
		this.jpeg_size = jpeg_width + "X" + jpeg_height;
		this.file_size = width + "X" + height;
		this.sample = (Integer.valueOf(sample) / 1024) + "kb";
		this.jpeg = (Integer.valueOf(jpeg) / 1024) + "kb";
		this.file = (Integer.valueOf(file) / 1024) + "kb";
	}
	public Item(String url, String author, String tags, String score,
			String id, String rate,String sample_url,String jpeg_url,
			String file_url,String sample_size, String jpeg_size,
			String size,
			String sample, String jpeg, String file){
		this.url = url;
		this.author = author;
		this.tags = tags;
		this.score = score;
		this.id = id;
		this.rate = rate;
		this.sample_url = sample_url;
		this.jpeg_url = jpeg_url;
		this.file_url = file_url;
		this.sample_size = sample_size;
		this.jpeg_size = jpeg_size;
		this.file_size = size;
		this.sample = sample;
		this.jpeg = jpeg;
		this.file = file;
	}
}
