package com.rssreadertop.rssreader;

/**
 * Created by anilkukreti on 05/02/16.
 */
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Handler;
import android.os.Message;
import android.text.Html;

import com.rssreadertop.pojo.IRSSItem;
import com.rssreadertop.pojo.RSSItem;

public class RSSHandleXml implements IRSSHandler{
    private String title = "title";
    private String link = "link";
    private String description = "description";
    private String urlString = null;

    private RSSItem item;
    private boolean itemInitialized;
    String text=null;
    private ArrayList<IRSSItem> mItemList;


    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    private Handler forRSS;

    public RSSHandleXml(String url,ArrayList<IRSSItem> list,Handler handler){
        this.urlString = url;
        this.mItemList = list;
        this.forRSS = handler;
    }

    public String getTitle(){
        return title;
    }

    public String getLink(){
        return link;
    }

    public String getDescription(){
        return description;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;


        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        if(name.equals("item")){
                            item = new RSSItem();
                            itemInitialized = true;
                        }

                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("item")){
                            itemInitialized = false;
                            mItemList.add(item);
                        } else if (name.equals("title") && itemInitialized) {
                            title = Html.fromHtml(text).toString();
                            item.setTitle(title);
                        } else if (name.equals("link") && itemInitialized) {
                            link = Html.fromHtml(text).toString();
                            item.setLink(link);
                        } else if (name.equals("description") && itemInitialized) {
                            description = text;
                            item.setDescription(description);
                        } else if (name.equals("pubDate") && itemInitialized) {
                            item.setPubDate(Html.fromHtml(text).toString());
                        }
                        break;
                }

                event = myParser.next();
            }

            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type","application/rss+xml");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();



                    /*InputStreamReader inputStreamReader = new InputStreamReader(stream,"UTF-8");
                    BufferedReader r = new BufferedReader(inputStreamReader);

                    StringBuilder total = new StringBuilder();
                    String line;
                    Log.v("Input = ", "start reading");
                    while ((line = r.readLine()) != null) {
                        Log.v("Input = ","\n");
                        Log.v("Input = ",line);
                    }*/



                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

                    myparser.setInput(stream,null);
                    parseXMLAndStoreIt(myparser);
                    stream.close();
                    Message msg = new Message();
                    msg.what = 1;
                    forRSS.sendMessage(msg);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
