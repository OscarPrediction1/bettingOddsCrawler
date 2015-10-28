# bettingOddsCrawler
## Info
Scraping betting odds from oddschecker.com for the categories best-picture, best-actor, best-actress, best-director and storing them in the MongoDB specified in the parameters.
## Output
For each category there is a JSONObject as follows:
```javascript
{
  "_id": ObjectId("5629e78c5fc73db9a2c581af"),
  "timestamp": "2015-10-23 09:53:10.186",
  "category": "best-picture",
  "data": [
    {
      "BW": "",
      "BY": "",
      "BX": "",
      "RD": "4",
      "TI": "",
      "BF": "",
      "id": "15214556282",
      "FR": "",
      "VC": "",
      "BD": "",
      "name": "Spotlight",
      "OE": "",
      "MA": "",
      "B3": "",
      "UN": "4",
      "SJ": "",
      "FB": "11/4",
      "SK": "4",
      "WH": "5/2",
      "MR": "",
      "SO": "",
      "WN": "",
      "WA": "",
      "PP": "4",
      "CE": "",
      "EE": "4",
      "LD": "5/2"
    }, ...
}
```
## Build
run `mvn install` and use the ...jar-with-dependencies.jar; or take the bettingOddsCrawler-0.0.1.jar from the repository if you don't want to build it yourself.
## Run
`java -jar bettingOddsCrawler-0.0.1.jar [host] [port] [userName] [password] [database]`

e.g. `java -jar bettingOddsCrawler-0.0.1.jar 23.102.28.222 27017 oscar 123456 oscar`
