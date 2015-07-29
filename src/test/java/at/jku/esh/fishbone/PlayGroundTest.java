package at.jku.esh.fishbone;

import java.io.IOException;

import org.junit.Test;

public class PlayGroundTest {

	@Test
	public void playground() {

		String[] args = {};

		// try {
		// PlayGroundTest.tester(args);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public static void tester(String[] args) throws IOException {

		DiscountHunter offer = new DiscountHunter();
		System.out.println("Ask for offer");
		String prod = In.readLine();
		double price = offer.getOffer(prod);
		System.out.println("Best offer is " + price);

	}
}