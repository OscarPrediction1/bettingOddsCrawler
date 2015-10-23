package de.coins2015.oscar1.bettingoddscrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    public static String host;
    public static String port;
    public static String userName;
    public static String password;
    public static String database;

    public static void main(String[] args) throws Exception {

	host = args[0];
	port = args[1];
	userName = args[2];
	password = args[3];
	database = args[4];

	String crawlStorageFolder = "src/../data/crawler";

	int numberOfCrawlers = 1;

	CrawlConfig config = new CrawlConfig();

	config.setCrawlStorageFolder(crawlStorageFolder);

	config.setPolitenessDelay(500);

	config.setMaxDepthOfCrawling(1);

	config.setMaxPagesToFetch(1000);

	config.setIncludeBinaryContentInCrawling(false);

	config.setResumableCrawling(false);

	PageFetcher pageFetcher = new PageFetcher(config);
	RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
		pageFetcher);
	CrawlController controller = new CrawlController(config, pageFetcher,
		robotstxtServer);

	controller
		.addSeed("http://www.oddschecker.com/awards/oscars/best-picture");

	controller.start(BettingOddsCrawler.class, numberOfCrawlers);

    }
}
