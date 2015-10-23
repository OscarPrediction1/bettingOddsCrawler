package de.coins2015.oscar1.bettingoddscrawler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.uwyn.jhighlight.tools.StringUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class BettingOddsCrawler extends WebCrawler {

    private String path;
    private String[] pages = {
	    "http://www.oddschecker.com/awards/oscars/best-director",
	    "http://www.oddschecker.com/awards/oscars/best-actor",
	    "http://www.oddschecker.com/awards/oscars/best-actress" };

    private static final Map<String, String> bettingAgencies;
    static {
	bettingAgencies = new HashMap<String, String>();
	bettingAgencies.put("B3", "bet365");
	bettingAgencies.put("SK", "skybet");
	bettingAgencies.put("BX", "totesport");
	bettingAgencies.put("BY", "boylesports");
	bettingAgencies.put("FR", "betfred");
	bettingAgencies.put("SO", "sportingbet");
	bettingAgencies.put("VC", "bet_victor");
	bettingAgencies.put("PP", "paddy_power");
	bettingAgencies.put("SJ", "stan_james");
	bettingAgencies.put("EE", "888sport");
	bettingAgencies.put("LD", "ladbrokes");
	bettingAgencies.put("CE", "coral");
	bettingAgencies.put("WH", "william_hill");
	bettingAgencies.put("WN", "winner");
	bettingAgencies.put("FB", "betfair_sportsbook");
	bettingAgencies.put("WA", "betway");
	bettingAgencies.put("TI", "titan_bet");
	bettingAgencies.put("UN", "unibet");
	bettingAgencies.put("BW", "bwin");
	bettingAgencies.put("RD", "32red_bet");
	bettingAgencies.put("OE", "10bet");
	bettingAgencies.put("MR", "marathon_bet");
	bettingAgencies.put("BF", "betfair");
	bettingAgencies.put("BD", "betdaq");
	bettingAgencies.put("MA", "matchbook");
    }

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
	String href = url.getURL().toLowerCase();
	boolean result = false;

	for (String page : pages) {
	    if (href.equals(page)) {
		result = true;
	    }
	}
	return result;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
	String url = page.getWebURL().getURL();
	String category = url.substring(url.lastIndexOf("/") + 1);
	Date date = new Date();
	Timestamp timestamp = new Timestamp(date.getTime());

	JSONObject categoryJson = new JSONObject();

	categoryJson.put("category", category);
	categoryJson.put("timestamp", timestamp.toString());

	if (page.getParseData() instanceof HtmlParseData) {
	    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	    String html = htmlParseData.getHtml();

	    String tableData = html
		    .substring(html.indexOf("<tbody id=\"t1\"") + 15);
	    tableData = tableData.substring(0, tableData.indexOf("</tbody>"));

	    ArrayList<String> rows = StringUtils.split(tableData, "</tr>");
	    JSONArray dataJson = new JSONArray();
	    for (String row : rows) {
		if (row.startsWith("<tr")) {

		    String tr = row.substring(0, row.indexOf(">"));

		    String id = tr.substring(tr
			    .indexOf("data-participant-id=\""));
		    id = id.substring(id.indexOf("\"") + 1);
		    id = id.substring(0, id.indexOf("\""));

		    String name = tr.substring(tr.indexOf("data-bname=\""));
		    name = name.substring(name.indexOf("\"") + 1);
		    name = name.substring(0, name.indexOf("\""));

		    JSONObject itemJson = new JSONObject();
		    itemJson.put("name", name);
		    itemJson.put("id", id);

		    ArrayList<String> columns = StringUtils.split(
			    row.substring(tr.length()), "</td>");
		    for (String column : columns) {
			if (column.startsWith("<td id")) {

			    String bettingAgencyId = column.substring(column
				    .indexOf("id=\"") + 4);
			    bettingAgencyId = bettingAgencyId.substring(
				    bettingAgencyId.indexOf("_") + 1,
				    bettingAgencyId.indexOf("\""));

			    String odds = column.substring(
				    column.indexOf(">") + 1, column.length());
			    odds.replace("\\", "");

			    itemJson.put(bettingAgencyId, odds);
			}
		    }
		    dataJson.add(itemJson);
		}

	    }
	    categoryJson.put("data", dataJson);
	}

	MongoDBHandler mdbh = new MongoDBHandler(Controller.host,
		Controller.port, Controller.userName, Controller.database,
		Controller.password);

	mdbh.storeData(categoryJson, "bettingOdds");

    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }
}
