package at.jku.esh.fishbone;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class DiscountHunter {

	/*
	 * 	This feature is not included due to problems with the set method (php)
	 * used for sending the search queries
	 */

	public DiscountHunter() {

	}

	public double getOffer(String name) throws IOException {
		String url = String.format("http://www.aktionsfinder.at/suche?searchfor=%s", name);
		Document doc = Jsoup.connect(url).get();

		Elements el = doc.getElementsByClass("headline");

		System.out.println(el);

		return 0;

	}

}
