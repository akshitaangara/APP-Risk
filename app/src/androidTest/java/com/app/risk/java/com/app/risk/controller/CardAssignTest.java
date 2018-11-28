package com.app.risk.java.com.app.risk.controller;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.model.Card;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
/**
 * This class is used check whether the card is assigned after the attack
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4.class)
public class CardAssignTest {
    private String fileLocation;
    Context context = null;
    GamePlay gm = null;

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
        playerNames.add("Player3");
        HashMap<String, Country> countryList = new HashMap<String, Country>();
        countryList.put("India", new Country("India", new Continent("Asia", 1)));
        countryList.put("Pakistan", new Country("Pakistan", new Continent("Asia", 1)));
        countryList.put("Bangladesh", new Country("Bangladesh", new Continent("Asia", 1)));
        countryList.put("Myammar", new Country("Myammar", new Continent("Africa", 2)));
        countryList.put("Nepal", new Country("Nepal", new Continent("Africa", 2)));
        countryList.put("Bhutan", new Country("Bhutan", new Continent("Africa", 2)));
        ArrayList<String> india = new ArrayList<String>();
        india.add("Pakistan");
        india.add("Nepal");
        ArrayList<String> pakistan = new ArrayList<String>();
        pakistan.add("India");
        pakistan.add("Bangladesh");
        ArrayList<String> bangladesh = new ArrayList<String>();
        bangladesh.add("Pakistan");
        ArrayList<String> nepal = new ArrayList<String>();
        nepal.add("India");
        nepal.add("Bhutan");
        nepal.add("Myammar");
        ArrayList<String> bhutan = new ArrayList<String>();
        bhutan.add("Myammar");
        ArrayList<String> myammar = new ArrayList<String>();
        myammar.add("Nepal");
        myammar.add("Bhutan");
        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);
        countryList.get("Bangladesh").setAdjacentCountries(bangladesh);
        countryList.get("Myammar").setAdjacentCountries(myammar);
        countryList.get("Nepal").setAdjacentCountries(nepal);
        countryList.get("Bhutan").setAdjacentCountries(bhutan);
        gm.setCountries(countryList);
        ArrayList<String> strategy=new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        gm.setPlayers(playerNames,strategy);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Nepal").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Bhutan").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Bangladesh").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Myammar").setPlayer(gm.getPlayers().get(2));
        gm.setCurrentPlayer(gm.getPlayers().get(0));
        gm.setCards();
        context = InstrumentationRegistry.getTargetContext();
    }

    /**
     * This method checks whether the card is assigned after the attack
     */
    @Test
    public void fortificationConnectedTest() {
        int intialcards=gm.getCards().size();
        FortificationPhaseController fc = FortificationPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        gm.getCurrentPlayer().assignCards(gm);
        int afterFortification=gm.getCards().size();
        System.out.println(":::::::::::::initial:::::::::"+intialcards);
        System.out.println(":::::::::::::after fortification:::::::::"+afterFortification);
        if(gm.getCurrentPlayer().isNewCountryConquered())
        assertTrue(afterFortification-intialcards==1);
        else
            assertTrue(afterFortification-intialcards==0);
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