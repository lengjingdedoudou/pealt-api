package com.peas.xinrui.common.spider;

import java.util.List;

/**
 * 网页解析类用来解析网页中的正文文本和链接
 */
public class Parser {

    private TextExtractor textExtractor;

    /**
     * 解析网页文本
     */
    public void parse() {
        textExtractor.extract();
    }

    public Parser(org.w3c.dom.Document doc) {
        textExtractor = new TextExtractor(doc);
    }

    public List<Paragraph> getParagraphs() {
        return textExtractor.getParagraphList();
    }

}
