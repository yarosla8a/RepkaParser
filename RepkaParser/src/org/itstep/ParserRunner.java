package org.itstep;

import org.itstep.service.PageParserService;

public class ParserRunner {

	public static void main(String[] args) {
		String url ="  ";
		PageParserService pps = new PageParserService(url);
		pps.start();
		
	}

}
