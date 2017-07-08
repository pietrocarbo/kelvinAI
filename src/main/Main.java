package main;

import decks.forty.Card;
import decks.forty.Deck;
import decks.forty.Hand;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Deck italianDeck = new Deck();

        Hand hand = new Hand(italianDeck.shuffle().deal(3));


    }
}
