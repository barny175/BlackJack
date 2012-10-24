/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mbarnas
 */
public class CardHandTest {


	@Test
	public void testHardSum() {
		System.out.println("hardSum");
		CardHand cardHand = new CardHand();
		cardHand.addCard(Card.ACE);
		cardHand.addCard(Card.TWO);
		cardHand.addCard(Card.TEN);
		cardHand.addCard(Card.ACE);
		assertEquals(14, cardHand.hardSum());
	}

	/**
	 * Test of softSum method, of class CardHand.
	 */
	@Test
	public void testSoftSum() {
		System.out.println("softSum");
		CardHand cardHand = new CardHand();
		cardHand.addCard(Card.ACE);
		cardHand.addCard(Card.TWO);
		cardHand.addCard(Card.TWO);
		assertEquals(15, cardHand.softSum());
	}
}
