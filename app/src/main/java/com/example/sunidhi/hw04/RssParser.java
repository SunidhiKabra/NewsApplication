package com.example.sunidhi.hw04;

import android.util.Log;
import android.util.Xml;

import org.jsoup.Jsoup;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sunidhi on 17-Jul-18.
 */

public class RssParser {
    public static class RssSAXParser extends DefaultHandler
    {
        ArrayList<Item> itemArrayList;

        int flag = 0 , flag1 =0;
        Item item;
        StringBuilder innerXML;

        static public ArrayList<Item> parseRss(InputStream inputStream) throws IOException, SAXException {
            RssSAXParser parser = new RssSAXParser();
            Xml.parse(inputStream,Xml.Encoding.ISO_8859_1, parser);
            return parser.itemArrayList;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            this.itemArrayList = new ArrayList<>();
            this.innerXML = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            Log.d("demo","localName START " +localName);
            Log.d("demo","qName START " +qName);

            if(localName.equals("item"))
            {
                item = new Item();
                flag = 1;
            }
            if(qName.equals("media:content") && flag1==0)
            {
                item.imageUrl = attributes.getValue("url");
                Log.d("demo","item.imageUrl "+ item.imageUrl);
                flag1=1;
            }
            if(qName.equals("media:thumbnail") && flag1==0)
            {
                item.imageUrl = attributes.getValue("url");
                Log.d("demo","item.imageUrl "+ item.imageUrl);
                flag1=1;
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            Log.d("demo","before if flag "+ flag);
            String text = "";
            text=innerXML.toString();

            Log.d("demo","text "+text);

            if(innerXML.toString() != null)
            {
                text = innerXML.toString().trim();
            }

            if(localName.equals("title") && flag==1)
            {
                item.title = text;
            }

            else if(localName.equals("description") && flag==1)
            {
                String temp = Jsoup.parse(text).text();
                item.description = temp;
                Log.d("demo","item.description "+item.description);
            }

            else if(localName.equals("link")&& flag==1)
            {
                item.link = text;
                Log.d("demo","item.link "+item.link);
            }
            else if(localName.equals("pubDate")&& flag==1)
            {
                item.pubDate = text;
                Log.d("demo","item.pubDate "+item.pubDate);
            }
            else if(qName.equals("media:group")&& flag==1)
            {
                flag1=0;
            }

            else if(qName.equals("media:thumbnail")&& flag==1)
            {
                flag1=0;
            }

            else if(localName.equals("item")&& flag==1)
            {
                itemArrayList.add(item);
            }
            innerXML.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            innerXML.append(ch,start,length);
        }
    }
}
