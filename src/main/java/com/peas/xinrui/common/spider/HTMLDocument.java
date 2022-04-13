package com.peas.xinrui.common.spider;

import java.util.List;

public class HTMLDocument {

    private List<Paragraph> paragraphs;

    /**
     * 利用指定的，符合HTML语法规则的字符串中构造一个HTML文档
     * 
     * @param str 构造
     * @param URL 所需要解析的HTML文档的链接地址是什么
     * @return 由指定字符串够早的HTML文档
     * @throws ParseException
     */
    public static HTMLDocument createHTMLDocument(String str) {
        // DOMParser parser = new DOMParser();
        // try
        // {
        // parser.parse(new InputSource(new StringReader(str)));
        // return new HTMLDocument(parser.getDocument());
        // } catch (SAXException ex)
        // {
        // ex.printStackTrace();
        // return null;
        // } catch (IOException ex)
        // {
        // ex.printStackTrace();
        // return null;
        // }
        return null;
    }

    /**
     * @param url     网页的URL地址
     * @param content 描述文档的HTML字符串
     * @param doc     由HTML字符串解析出的Dom文档。
     */
    protected HTMLDocument(org.w3c.dom.Document doc) {
        Parser p = new Parser(doc);
        p.parse();
        paragraphs = p.getParagraphs();
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }
}
