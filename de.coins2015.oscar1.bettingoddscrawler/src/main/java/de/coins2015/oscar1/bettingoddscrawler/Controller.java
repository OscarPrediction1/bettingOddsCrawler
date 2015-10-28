package de.coins2015.oscar1.bettingoddscrawler;

import org.json.simple.JSONObject;

public class Controller {

    public static void main(String[] args) throws Exception {

	String host = args[0];
	String port = args[1];
	String userName = args[2];
	String password = args[3];
	String database = args[4];

	String[] pages = {
		"http://www.oddschecker.com/awards/oscars/best-picture",
		"http://www.oddschecker.com/awards/oscars/best-director",
		"http://www.oddschecker.com/awards/oscars/best-actor",
		"http://www.oddschecker.com/awards/oscars/best-actress",
		"http://www.oddschecker.com/awards/oscars/best-supporting-actor",
		"http://www.oddschecker.com/awards/oscars/best-supporting-actress" };

	BettingOddsCrawler boc = new BettingOddsCrawler();

	MongoDBHandler mdbh = new MongoDBHandler(host, port, userName,
		database, password);

	for (String page : pages) {
	    JSONObject result = boc.visit(page);
	    mdbh.storeData(result, "bettingOdds");
	}

    }
}
