package com.peas.xinrui.common.spider;

public class ExtractContent {

	public static String getContent(String url) {

		String content = "";
		try {
			String html = Utility.getWebContent(url, true);

			if (html == null || html.equals("")) {
				return content;
			}
			HTMLDocument doc = HTMLDocument.createHTMLDocument(html);

			if (doc == null) {
				return content;
			}
			// int offset = 0;
			double maxweight = 0;

			for (Paragraph p : doc.getParagraphs()) {
				if (p != null) {
					// offset += p.getText().length();
					if (p.getWeight() > maxweight) {
						maxweight = p.getWeight();
						content = p.getText();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public static void main(String[] args) {

	}
}
