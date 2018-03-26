package org.itstep.service;

import java.util.ArrayList;

import org.itstep.model.Good;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParserService extends Thread {

	private final String pageUrl;

	public PageParserService(String pageToParse) {
		this.pageUrl = pageToParse;
	}

	@Override
	public void run() {
		ArrayList<Good> goods = new ArrayList<>();

		Document pageDocument = null;
		try {
			pageDocument = Jsoup.connect(pageUrl).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element productFilter = pageDocument.getElementsByAttributeValueContaining("class", "product-items").first();
		Elements productContentElements = productFilter.getElementsByAttributeValueMatching("class",
				"product-item-info");
		for (Element productElement : productContentElements) {
			if (!productElement.attr("class").contains("catalog_item_banner")) {
				Good good = new Good();
				/* name + goodUrl */
				Element nameElement = productElement.getElementsByAttributeValueMatching("class", "product-item-name")
						.first().getElementsByTag("a").first();
				String goodName = nameElement.text();
				String goodUrl = nameElement.attr("href");
				good.setName(goodName);
				good.setGoodUrl(goodUrl);

				/* imgUrl */
				Element imgElement = productElement.getElementsByAttributeValueContaining("class", "product-item-photo")
						.first().getElementsByTag("a").first().getElementsByTag("img").first();
				String imgUrl = imgElement.attr("data-src");
				good.setImgUrl(imgUrl);

				/* price */
				Element goodPriceElement = productElement.getElementsByAttributeValueContaining("id", "product-price")
						.first();
				int price = Integer.parseInt(goodPriceElement.attr("data-price-amount"));
				good.setPrice(price);

				goods.add(good);

			}

		}
		System.out.println(goods.size());

		FileWriterService fws = new FileWriterService();
		for (Good good : goods) {
			String textToWrite = good.getName() + " ** " + good.getGoodUrl() + " ** " + good.getPrice() + "UAH\n";
			fws.writeTextToFile(textToWrite);
		}
	}
}
