package com.blueapps.egyptianwriter.editor.vocab.cards;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

// Because some functions are only for utilit
@SuppressWarnings("unused")
public class Card {

    private Element element;
    private int index;

    public Card(Element element, int index){
        this.element = element;
        this.index = index;
    }

    protected String getChildString(Node rootNode){
        NodeList signNodes = rootNode.getChildNodes();
        for (int i = 0; i < signNodes.getLength(); i++){
            Node node = signNodes.item(i);
            if (node instanceof Text){
                Text text = (Text) node;
                return text.getWholeText();
            }
        }
        return null;
    }

    public Element getElement() {
        return element;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
