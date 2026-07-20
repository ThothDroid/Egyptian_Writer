package com.blueapps.egyptianwriter.editor.vocab.cards.fragments.edit;

import com.blueapps.egyptianwriter.editor.vocab.cards.Card;

import java.io.Serializable;

public interface CardListener extends Serializable {

    void OnCardChanged(Card card, int index);

}
