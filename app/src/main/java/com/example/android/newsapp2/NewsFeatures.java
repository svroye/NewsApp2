package com.example.android.newsapp2;

/**
 * Created by Steven on 14/02/2017.
 */

public class NewsFeatures {

    // title of the news feature
    private String mTitle ;

    // url of the news feature
    private String mUrl;

    //section of the news feature
    private String mSection;

    private String mDate;

    /**
     * public constructor to create a new NewsFeature item
     * @param title : title of the article
     * @param url : url of the article
     * @param section : section to which the article belongs
     * @param date : publishing date of the article
     */
    public NewsFeatures(String title, String url, String section,String date ){
        mTitle = title;
        mUrl = url;
        mSection = section;
        mDate = date;
    }

    /**
     * called to get the title of the current NewsFeature object
     * @return : title of the object
     */
    public String getTitle(){
        return mTitle;
    }

    /**
     * called to get the url of the current NewsFeature object
     * @return : url of the object
     */
    public String getUrl(){
        return mUrl;
    }

    /**
     * called to get the section of the current NewsFeature object
     * @return : section of the current object
     */
    public String getSection(){
        return mSection;
    }

    /**
     * called to get the publishing date of the current NewFeature object
     * @return : date when the current object was published
     */
    public String getDate(){
        return mDate;
    }


}
