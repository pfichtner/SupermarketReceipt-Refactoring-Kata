package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupermarketTest {

	// Todo: test all kinds of discounts are applied properly

	@Test
	public void noDiscount() {
		SupermarketCatalog catalog = new FakeCatalog();
		Product toothbrush = new Product("toothbrush", ProductUnit.Each);
		catalog.addProduct(toothbrush, 0.99);
		Product apples = new Product("apples", ProductUnit.Kilo);
		catalog.addProduct(apples, 1.99);
		Teller teller = new Teller(catalog);
		ShoppingCart cart = new ShoppingCart();
		cart.addItemQuantity(apples, 2.5);
		Receipt receipt = teller.checksOutArticlesFrom(cart);
		assertEquals(4.975, receipt.getTotalPrice(), 0.01);
		assertEquals(Collections.emptyList(), receipt.getDiscounts());
		assertEquals(1, receipt.getItems().size());
		ReceiptItem receiptItem = receipt.getItems().get(0);
		assertEquals(apples, receiptItem.getProduct());
		assertEquals(1.99, receiptItem.getPrice());
		assertEquals(2.5 * 1.99, receiptItem.getTotalPrice());
		assertEquals(2.5, receiptItem.getQuantity());
	}

	@Test
	public void tenPercentDiscountOnToothbrushes() {
		SupermarketCatalog catalog = new FakeCatalog();
		Product toothbrush = new Product("toothbrush", ProductUnit.Each);
		catalog.addProduct(toothbrush, 0.99);
		Product apples = new Product("apples", ProductUnit.Kilo);
		catalog.addProduct(apples, 1.99);
		Teller teller = new Teller(catalog);
		teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, toothbrush, 10.0);
		ShoppingCart cart = new ShoppingCart();
		cart.addItemQuantity(apples, 2.5);
		cart.addItemQuantity(toothbrush, 2);
		Receipt receipt = teller.checksOutArticlesFrom(cart);
		assertEquals(6.757, receipt.getTotalPrice(), 0.01);
		List<Discount> discounts = receipt.getDiscounts();
		assertEquals(1, discounts.size());
		Discount discount = discounts.get(0);
		assertEquals(toothbrush.getName(), discount.getProduct().getName());
		assertEquals(-0.198, discount.getDiscountAmount());
		assertEquals("10.0% off", discount.getDescription());
		assertEquals(2, receipt.getItems().size());
		ReceiptItem receiptItem1 = receipt.getItems().get(0);
		assertEquals(apples, receiptItem1.getProduct());
		assertEquals(1.99, receiptItem1.getPrice());
		assertEquals(2.5 * 1.99, receiptItem1.getTotalPrice());
		assertEquals(2.5, receiptItem1.getQuantity());
		assertEquals(2, receipt.getItems().size());
		ReceiptItem receiptItem2 = receipt.getItems().get(1);
		assertEquals(toothbrush, receiptItem2.getProduct());
		assertEquals(0.99, receiptItem2.getPrice());
		assertEquals(2 * 0.99, receiptItem2.getTotalPrice());
		assertEquals(2, receiptItem2.getQuantity());
	}

	@Test
	public void threeForTwoDiscountOnToothbrushes() {
		SupermarketCatalog catalog = new FakeCatalog();
		Product toothbrush = new Product("toothbrush", ProductUnit.Each);
		catalog.addProduct(toothbrush, 0.99);
		Product apples = new Product("apples", ProductUnit.Kilo);
		catalog.addProduct(apples, 1.99);
		Teller teller = new Teller(catalog);
		teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, 10.0);
		ShoppingCart cart = new ShoppingCart();
		cart.addItemQuantity(apples, 2.5);
		cart.addItemQuantity(toothbrush, 3);
		Receipt receipt = teller.checksOutArticlesFrom(cart);
		assertEquals(6.955, receipt.getTotalPrice(), 0.01);
		List<Discount> discounts = receipt.getDiscounts();
		assertEquals(1, discounts.size());
		Discount discount = discounts.get(0);
		assertEquals(toothbrush.getName(), discount.getProduct().getName());
		assertEquals(-0.99, discount.getDiscountAmount(), 0.01);
		assertEquals("3 for 2", discount.getDescription());
		assertEquals(2, receipt.getItems().size());
		ReceiptItem receiptItem1 = receipt.getItems().get(0);
		assertEquals(apples, receiptItem1.getProduct());
		assertEquals(1.99, receiptItem1.getPrice());
		assertEquals(2.5 * 1.99, receiptItem1.getTotalPrice());
		assertEquals(2.5, receiptItem1.getQuantity());
		assertEquals(2, receipt.getItems().size());
		ReceiptItem receiptItem2 = receipt.getItems().get(1);
		assertEquals(toothbrush, receiptItem2.getProduct());
		assertEquals(0.99, receiptItem2.getPrice());
		assertEquals(3 * 0.99, receiptItem2.getTotalPrice());
		assertEquals(3, receiptItem2.getQuantity());
	}

	@Test
	public void noThreeForTwoDiscountOnToothbrushes() {
		SupermarketCatalog catalog = new FakeCatalog();
		Product toothbrush = new Product("toothbrush", ProductUnit.Each);
		catalog.addProduct(toothbrush, 0.99);
		Product apples = new Product("apples", ProductUnit.Kilo);
		catalog.addProduct(apples, 1.99);
		Teller teller = new Teller(catalog);
		teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, 10.0);
		ShoppingCart cart = new ShoppingCart();
		cart.addItemQuantity(apples, 2.5);
		cart.addItemQuantity(toothbrush, 2);
		Receipt receipt = teller.checksOutArticlesFrom(cart);
		assertEquals(6.955, receipt.getTotalPrice(), 0.01);
		assertEquals(Collections.emptyList(), receipt.getDiscounts());
		assertEquals(2, receipt.getItems().size());
		ReceiptItem receiptItem1 = receipt.getItems().get(0);
		assertEquals(apples, receiptItem1.getProduct());
		assertEquals(1.99, receiptItem1.getPrice());
		assertEquals(2.5 * 1.99, receiptItem1.getTotalPrice());
		assertEquals(2.5, receiptItem1.getQuantity());
		assertEquals(2, receipt.getItems().size());
		ReceiptItem receiptItem2 = receipt.getItems().get(1);
		assertEquals(toothbrush, receiptItem2.getProduct());
		assertEquals(0.99, receiptItem2.getPrice());
		assertEquals(2 * 0.99, receiptItem2.getTotalPrice());
		assertEquals(2, receiptItem2.getQuantity());
	}

}
