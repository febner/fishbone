package at.jku.esh.fishbone.goods;

import at.jku.esh.fishbone.Goods;
import at.jku.esh.fishbone.Out;

public class Groceries implements Goods {

	private String name;
	private String picSource;
	private String ISBN;
	private String info;
	private int count;

	public Groceries(String ISBN, String name, int count, String picSource, String info) {
		this.ISBN = ISBN;
		this.name = name;
		this.count = count;
		this.picSource = picSource;
		this.info = info;
	}

	@Override
	public void print() {

		Out.println(String.format("%-20s %-10s %-10d %-10s %-10s", trimToSize(name, 20), trimToSize(info, 10), count,
				trimToSize(picSource, 30), trimToSize(ISBN, 10)));

	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {

		return name;
	}

	public void setPicSource(String picSource) {
		this.picSource = picSource;
	}

	public String getPicSource() {

		return picSource;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfo255() {
		return trimToSize(info, 255);
	}

	public String getInfo() {
		return info;
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

	public String getISBN() {
		return ISBN;
	}

	private static String trimToSize(String s, int size) {
		if (s.length() >= size) {
			s = s.substring(0, size);
		}

		return s;
	}

}
