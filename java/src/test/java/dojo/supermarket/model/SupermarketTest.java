package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupermarketTest {

	// Todo: test all kinds of discounts are applied properly

	SupermarketCatalog catalog = new FakeCatalog();
	Teller teller = new Teller(catalog);

	Product toothbrush = new Product("toothbrush", ProductUnit.Each);
	Product apples = new Product("apples", ProductUnit.Kilo);

	ShoppingCart cart = new ShoppingCart();

	@Test
	public void noDiscount() {
		givenProductInCatalog(toothbrush, 0.99);
		givenProductInCatalog(apples, 1.99);
		givenProductInCart(apples, 2.5);
		Receipt receipt = whenCartIsCheckedout();
		thenReceiptHasTotalPrice(receipt, 4.975);
		thenReceiptHasNoDiscount(receipt);
		thenReceiptContainsItems(receipt, new ReceiptItem(apples, 2.5, 1.99, 2.5 * 1.99));
	}

	@Test
	public void tenPercentDiscountOnToothbrushes() {
		givenProductInCatalog(toothbrush, 0.99);
		givenProductInCatalog(apples, 1.99);
		givenSpecialOffer(SpecialOfferType.TenPercentDiscount, toothbrush, 10.0);
		givenProductInCart(apples, 2.5);
		givenProductInCart(toothbrush, 2);
		Receipt receipt = whenCartIsCheckedout();
		thenReceiptHasTotalPrice(receipt, 6.757);
		thenReceiptContainsDiscount(receipt, new Discount(toothbrush, "10.0% off", -0.198));
		thenReceiptContainsItems(receipt, //
				new ReceiptItem(apples, 2.5, 1.99, 2.5 * 1.99), //
				new ReceiptItem(toothbrush, 2, 0.99, 2 * 0.99) //
		);
	}

	@Test
	public void threeForTwoDiscountOnToothbrushes() {
		givenProductInCatalog(toothbrush, 0.99);
		givenProductInCatalog(apples, 1.99);
		givenSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, 10.0);
		givenProductInCart(apples, 2.5);
		givenProductInCart(toothbrush, 3);
		Receipt receipt = whenCartIsCheckedout();
		thenReceiptHasTotalPrice(receipt, 6.955);
		thenReceiptContainsDiscount(receipt, new Discount(toothbrush, "3 for 2", -0.99));
		thenReceiptContainsItems(receipt, //
				new ReceiptItem(apples, 2.5, 1.99, 2.5 * 1.99), //
				new ReceiptItem(toothbrush, 3, 0.99, 3 * 0.99) //
		);
	}

	@Test
	public void noThreeForTwoDiscountOnToothbrushes() {
		givenProductInCatalog(toothbrush, 0.99);
		givenProductInCatalog(apples, 1.99);
		givenSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, 10.0);
		givenProductInCart(apples, 2.5);
		givenProductInCart(toothbrush, 2);
		Receipt receipt = whenCartIsCheckedout();
		thenReceiptHasTotalPrice(receipt, 6.955);
		thenReceiptHasNoDiscount(receipt);
		thenReceiptContainsItems(receipt, //
				new ReceiptItem(apples, 2.5, 1.99, 2.5 * 1.99), //
				new ReceiptItem(toothbrush, 2, 0.99, 2 * 0.99) //
		);
	}

	private void givenProductInCatalog(Product product, double price) {
		catalog.addProduct(product, price);
	}

	private void givenProductInCart(Product product, double quantity) {
		cart.addItemQuantity(product, quantity);
	}

	private void givenSpecialOffer(SpecialOfferType type, Product product, double argument) {
		teller.addSpecialOffer(type, product, argument);
	}

	private Receipt whenCartIsCheckedout() {
		return teller.checksOutArticlesFrom(cart);
	}

	private void thenReceiptHasTotalPrice(Receipt receipt, double expected) {
		assertEquals(expected, receipt.getTotalPrice(), 0.01);
	}

	private void thenReceiptHasNoDiscount(Receipt receipt) {
		assertEquals(Collections.emptyList(), receipt.getDiscounts());
	}

	private void thenReceiptContainsDiscount(Receipt receipt, Discount... expectedDiscounts) {
		List<Discount> discounts = receipt.getDiscounts();
		assertEquals(expectedDiscounts.length, discounts.size());
		for (int i = 0; i < discounts.size(); i++) {
			Discount discount = discounts.get(i);
			Discount expectedDiscount = expectedDiscounts[i];
			assertEquals(expectedDiscount.getProduct().getName(), discount.getProduct().getName());
			assertEquals(expectedDiscount.getDiscountAmount(), discount.getDiscountAmount(), 0.01);
			assertEquals(expectedDiscount.getDescription(), discount.getDescription());
		}
	}

	private void thenReceiptContainsItems(Receipt receipt, ReceiptItem... receiptItems) {
		assertEquals(Arrays.asList(receiptItems), receipt.getItems());
	}
}
