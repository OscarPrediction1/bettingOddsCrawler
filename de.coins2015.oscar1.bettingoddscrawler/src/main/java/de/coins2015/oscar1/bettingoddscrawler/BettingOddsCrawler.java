package de.coins2015.oscar1.bettingoddscrawler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class BettingOddsCrawler {

    private static final Map<String, String> boxOfficeIds;
    static {
	boxOfficeIds = new HashMap<String, String>();
	boxOfficeIds.put("name", "boxOfficeId");

    }

    @SuppressWarnings("unchecked")
    public JSONObject visit(String url) throws IOException {
	String category = url.substring(url.lastIndexOf("/") + 1);
	Date date = new Date();
	Timestamp timestamp = new Timestamp(date.getTime());

	Document doc = Jsoup.connect(url).timeout(10000).userAgent("Mozilla")
		.get();

	JSONObject categoryJson = new JSONObject();

	categoryJson.put("category", category);
	categoryJson.put("timestamp", timestamp.toString());

	String html = doc.html();

	String tableData = html
		.substring(html.indexOf("<tbody id=\"t1\"") + 15);
	tableData = tableData.substring(0, tableData.indexOf("</tbody>"));

	String[] rows = StringUtils.splitByWholeSeparatorPreserveAllTokens(
		tableData, "</tr>");
	JSONArray dataJson = new JSONArray();
	for (String row : rows) {
	    row = StringUtils.trim(row);
	    if (row.startsWith("<tr")) {

		String tr = row.substring(0, row.indexOf(">"));

		String id = tr.substring(tr.indexOf("data-bid=\""));
		id = id.substring(id.indexOf("\"") + 1);
		id = id.substring(0, id.indexOf("\""));

		String name = tr.substring(tr.indexOf("data-bname=\""));
		name = name.substring(name.indexOf("\"") + 1);
		name = name.substring(0, name.indexOf("\""));

		JSONObject itemJson = new JSONObject();
		itemJson.put("name", name);
		itemJson.put("id", id);

		String[] columns = StringUtils
			.splitByWholeSeparatorPreserveAllTokens(
				row.substring(tr.length()), "</td>");
		int j = 0;
		for (String column : columns) {
		    column = StringUtils.trim(column);
		    if (column.startsWith("<td class")) {

			String bettingAgencyId = column.substring(column
				.indexOf("id=\"") + 4);
			bettingAgencyId = bettingAgencyId.substring(
				bettingAgencyId.indexOf("_") + 1,
				bettingAgencyId.indexOf("\""));

			String odds = column.substring(column.indexOf(">") + 1,
				column.length());

			itemJson.put(j, odds);
			j++;
		    }
		}
		dataJson.add(itemJson);
	    }

	    categoryJson.put("data", dataJson);
	}

	return categoryJson;
    }
}
