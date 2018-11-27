package Adapter;

public class SingerItem {
    String id;
    String title;
    String content;
    String date;

    public SingerItem(String id,String title, String content, String date){
        this.title = title;
        this.content = content;
        this.date = date;
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }


    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.content = date;
    }
}
