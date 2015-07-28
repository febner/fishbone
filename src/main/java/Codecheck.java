import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Codecheck {

	public static Groceries loadInformation(String barcode) throws IOException {

		String url = String.format("http://www.codecheck.info/product.search?q=%s&OK=Suchen", barcode);

		Document doc = Jsoup.connect(url).get();

		if (!doc.title().contains("-")) { // if true, the product could not be
											// found!
			System.out.println("Barcode existiert nicht auf Codecheck.info");
			return null;
		}
		String uri = doc.baseUri();

		String name = doc.title().substring(0, doc.title().lastIndexOf(" -"));

		Elements nf = doc.getElementsByClass("nf");
		Element pic = nf.get(0);
		Element picture = pic.child(1);
		String picSource = "";
		try {
			String relPicSource = picture.attributes().asList().get(2).toString();
			picSource = uri + relPicSource.substring(6, relPicSource.length() - 1);
		} catch (Exception e) {
			System.out.println("Bild konnte nicht geladen werden");
			// e.printStackTrace();
		} finally {
		}
		String info = "\t"; // empty

		Elements prodInfo = doc.getElementsByClass("product-info-item");

		if (prodInfo.size() > 1 && prodInfo.get(1).text().length() > 20 && prodInfo.get(1).text().startsWith("Zusatzinformationen")) {
			info = prodInfo.get(1).text().substring(19);
		}
		return new Groceries(barcode, name, 1, picSource, info);

	}

}
