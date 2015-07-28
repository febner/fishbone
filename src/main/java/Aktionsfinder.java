import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Aktionsfinder {

	public Aktionsfinder() {

	}

	public double getOffer(String name) throws IOException {
		String url = String.format("http://www.aktionsfinder.at/suche?searchfor=%s", name);
		Document doc = Jsoup.connect(url).get();

		Elements el = doc.getElementsByClass("headline");

		return 0;
		//Diese Funktion muss noch inkludiert werden. Aktionsfinder verwendet 
		//wsl set um Suche zu bearbeiten.
	}

}
