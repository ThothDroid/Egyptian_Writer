package com.blueapps.egyptianwriter.editor.vocab.cards;

import org.w3c.dom.Element;

public class Card {

    private Element element;
    private int index;

    public Card(Element element, int index){
        this.element = element;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
