import java.io.IOException;

public class PlayGround {

	public static void main(String[] args) throws IOException {

		Aktionsfinder offer = new Aktionsfinder();
		System.out.println("Ask for offer");
		String prod = In.readLine();
		double price = offer.getOffer(prod);
		System.out.println("Best offer is " + price);

	}
}