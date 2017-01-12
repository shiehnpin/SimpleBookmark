package com.enping.simplebookmark;

/**
 * Created by Chjeng-Lun SHIEH on 2017/1/11.
 */

//POJO
public class Bookmark {
    int id = -1;
    String text = "N/A";

    public Bookmark(String text) {
        this.text = text;
    }

    public Bookmark(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Bookmark){
            if(((Bookmark) o).getId() == id){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
