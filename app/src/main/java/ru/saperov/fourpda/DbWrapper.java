package ru.saperov.fourpda;

/**
 * Created by saperov on 23.03.16.
 */
public class DbWrapper {
    private int _id;
    private String mTitle;
    private String mHref;
    private String mDesc;
    private String mSrcImg;

    // Пустой констуктор
    public DbWrapper(){

    }

    // Конструктор с параметрами
    public DbWrapper(int id, String title, String href, String desc, String srcImg){
        this._id = id;
        this.mTitle = title;
        this.mHref = href;
        this.mDesc = desc;
        this.mSrcImg = srcImg;
    }

    // Конструктор с параметрами
    public DbWrapper(String title, String href, String desc, String srcImg){
        this.mTitle = title;
        this.mHref = href;
        this.mDesc = desc;
        this.mSrcImg = srcImg;
    }

    // Создание геттеров-сеттеров
    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getTitle(){
        return this.mTitle;
    }

    public void setTitle(String title){
        this.mTitle=title;
    }

    public String getHref(){
        return this.mHref;
    }

    public void setHref(String href){
        this.mHref=href;
    }

    public String getDesc(){
        return this.mDesc;
    }

    public void setDesc(String desc){
        this.mDesc=desc;
    }

    public String getSrcImg(){
        return this.mSrcImg;
    }

    public void setSrcImg(String srcImg){
        this.mSrcImg=srcImg;
    }
}
