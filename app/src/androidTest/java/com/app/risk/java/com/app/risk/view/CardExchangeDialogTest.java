package com.app.risk.java.com.app.risk.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.CardExchangeController;
import com.app.risk.model.Card;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

/**
 * This class is used to check whether the cards have been removed after it has been exchanged for armies
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class CardExchangeDialogTest {
    /**
     * context instance would hold the instance of the target activity
     */
    private Context context = null;
    /**
     * gameplay instances would hold the objects required for the test cases
     */
    private GamePlay gm = null;
    /**
     * cardList1 instances would hold the list of all the cards hold by the player1
     */
    ArrayList<Card> cardList1 = null;
    /**
     * cardList1 instances would hold the list of all the cards hold by the player2
     */
    ArrayList<Card> cardList2 = null;
    /**
     * cardList1 instances would hold the list of all the cards hold by the player3
     */
    ArrayList<Card> cardList3 = null;
    /**
     * This method gets executed before the test case
     * sets the gameplay instance with the values required for the testing and context of the test case
     */
    @Before
    public void setUp() {
        gm = new GamePlay();
        ArrayList<String> playerNames = new ArrayList<String>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        HashMap<String, Country> countryList = new HashMap<String, Country>();
        countryList.put("India", new Country("India", new Continent("Asia", 1)));
        countryList.put("Pakistan", new Country("Pakistan", new Continent("Africa", 2)));

        ArrayList<String> india = new ArrayList<String>();
        india.add("Pakistan");

        ArrayList<String> pakistan = new ArrayList<String>();
        pakistan.add("India");


        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);

        gm.setCountries(countryList);
        ArrayList<String> strategy=new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        gm.setPlayers(playerNames,strategy);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));

        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));

        gm.setCurrentPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setNoOfArmies(5);
        gm.getCountries().get("India").setNoOfArmies(4);
        cardList1=new ArrayList<Card>();
        Card card1=new Card("infantry");
        card1.setSelected(true);
        Card card2=new Card("infantry");
        card2.setSelected(true);
        Card card3=new Card("infantry");
        card3.setSelected(true);
        cardList1.add(card1);
        cardList1.add(card2);
        cardList1.add(card3);

        cardList2=new ArrayList<Card>();
        Card card4=new Card("infantry");
        card4.setSelected(true);
        Card card5=new Card("cavalry");
        card5.setSelected(true);
        Card card6=new Card("artillery");
        card6.setSelected(true);
        cardList2.add(card4);
        cardList2.add(card5);
        cardList2.add(card6);
        cardList3=new ArrayList<Card>();
        Card card7=new Card("infantry");
        card7.setSelected(true);
        Card card8=new Card("cavalry");
        card8.setSelected(true);
        Card card9=new Card("cavalry");
        card9.setSelected(true);
        cardList3.add(card7);
        cardList3.add(card8);
        cardList3.add(card9);
        gm.getCurrentPlayer().setCards(cardList1);
        context = InstrumentationRegistry.getTargetContext();
    }
    /**
     * This method check whether cards have been from the player card list after
     * it has been exchanged
     */
    @Test
    public void removeCardsAfterExchange()
    {

        int cardCount=gm.getCurrentPlayer().getCards().size();
        CardExchangeController cardExchange=CardExchangeController.init(gm.getCurrentPlayer());
        cardExchange.exchangeArmiesForCards(cardList1);
        cardExchange.removeExchangedCards(cardList1);
        assertTrue(gm.getCurrentPlayer().getCards().size()==cardCount-3);


    }
    /**
     * This method gets executed after the test case has been executed
     * its sets the game map to null
     */
    @After
    public void cleanUp()
    {
        gm=null;
    }
}
