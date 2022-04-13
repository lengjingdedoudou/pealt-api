package com.peas.xinrui.common.spider;

/**
 * 用来描述文本中的一个段落，段落的概念是，一个独立语义的，可以单独划分的段落。
 * 在不同的文档中可能对不同的段落有不同的定义。但一个最基本的定义是：段落是介于篇章和 句子之间的文字块，
 * 每个文字块因为对文档的贡献不同而可以被赋予不同的值，称之为权重。
 */
public class Paragraph {

    /**
     * 段落的文本。
     */
    private String text;
    /**
     * 段落的权重。
     */
    private double weight;

    public Paragraph() {
    }

    /**
     * 由指定的文字和权重构造一个新的段落对象
     * 
     * @param text   段落文字
     * @param weight 段落权重
     */
    public Paragraph(String text, double weight) {
        this.text = text;
        this.weight = weight;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * 获取段落的文本
     * 
     * @return 段落的文本
     */
    public String getText() {
        return text;
    }

    /**
     * 获取段落的权重
     * 
     * @return 段落的权重
     */
    public double getWeight() {
        return weight;
    }

}
