package com.peas.xinrui.common.spider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 这个类将所有的正文文本从一篇HTML文档中提取出来，并且分成若干段落。 &#64;note 段落 是指一篇HTML文档中，语义相对集中的一段文字。
 * 每一个提取出来的段落将会获得一个权重。这个权重是该段落对HTML文档主题的贡献程度
 */
public class TextExtractor {

    /**
     * 这个标记最大允许的锚文本密度，该值目前为.5
     */
    public static final double MAX_ANCHOR_DEN = 0.5;
    /**
     * 该HTML文档中有多少正文文字？
     */
    private int totalTextLen = 0;
    /**
     * 该HTML文档中有多少超链接文字？
     */
    private int totalAnchorTextLen = 0;
    /**
     * 该HTML文档中有多少infoNodes?
     */
    private int totalNumInfoNodes = 0;
    /**
     * 标记列表
     */
    private List<TagWindow> windowsList = new ArrayList<TagWindow>();
    /**
     * 段落列表
     */
    private List<Paragraph> paragraphList = new ArrayList<Paragraph>();
    /**
     * w3cHTML文档模型
     */
    private Document doc;

    /**
     * 利用給定的W3C文檔對象模型構造一個TextExtractor
     * 
     * @param doc 所给定的W3C文档对象模型
     */
    public TextExtractor(Document doc) {
        super();
        this.doc = doc;

    }

    /**
     * 删除文档中的一些显然不会包含主题信息的节点，例如script,style,等等，它们将影响我们的文本抽取器的分析。
     * 
     * @param e 所需要清楚地w3c节点
     */
    private void cleanup(Element e) {
        NodeList c = e.getChildNodes();
        for (int i = 0; i < c.getLength(); i++) {
            if (c.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element t = (Element) c.item(i);
                if (Utility.isInvalidElement(t)) {
                    e.removeChild(c.item(i));
                } else {
                    cleanup(t);
                }
            }
        }
    }

    /**
     * 抽取HTML文本信息，并且分段，为每一段文本的主题相关性打分。
     * 
     * @return 所抽取出的主题信息
     */
    public void extract() {
        Node body = doc.getElementsByTagName("BODY").item(0);
        // cleanup, remove the invalid tags,
        cleanup((Element) body);

        totalTextLen = TagWindow.getInnerText(body, false).length();
        // get anchor text length
        totalAnchorTextLen = TagWindow.getAnchorText((Element) body).length();

        totalNumInfoNodes = TagWindow.getNumInfoNode((Element) body);

        extractWindows(body);

        String bodyText = "";
        if (windowsList.size() == 0) {
            bodyText = "";
        } else {
            // get the max score
            Collections.sort(windowsList, new Comparator<TagWindow>() {
                public int compare(TagWindow t1, TagWindow t2) {
                    if (t1.weight(totalTextLen, totalAnchorTextLen, totalNumInfoNodes) > t2.weight(totalTextLen,
                            totalAnchorTextLen, totalNumInfoNodes)) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            TagWindow max = windowsList.get(windowsList.size() - 1);
            bodyText = max.getInnerText(true);
        }
        // 我们所有的段落，别忘了加上标题
        String whole = TagWindow.getInnerText(body, true);

        if (!bodyText.trim().equals("")) {
            // extract all the paragraphs, add them to the paragraph list
            paragraphList = new ParagraphSplitter(bodyText, whole).split();
        }
    }

    /**
     * 遍历每个Node对象，把每个Node都存储到taglist当中。
     * 
     * @param node 所需要遍历的w3cNode对象
     */
    private void extractWindows(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return;
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (Utility.isInvalidElement(element)) {
                return;
            }
            if (!node.getTextContent().trim().equals("")) // add the tags
            {
                windowsList.add(new TagWindow(node));
            }
            NodeList list = element.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                extractWindows(list.item(i));
            }
        }
    }

    /**
     * 段落列表
     * 
     * @return 段落列表
     */
    public List<Paragraph> getParagraphList() {
        return paragraphList;
    }

}
