package postpc.huji.SandwichApp;

import java.io.Serializable;
import java.util.UUID;

public class Order implements Serializable {
    String id;
    String customerName;
    boolean hummus;
    boolean tahini;
    int pickles;
    String comment;
    String status;

    public Order() {
        this("", false, false, 0, "");
    }

    public Order(String customerName, boolean hummus, boolean tahini, int pickles, String comments){
        this.id = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.hummus = hummus;
        this.tahini = tahini;
        this.pickles = pickles;
        this.comment = comments;
        this.status = "waiting";
    }

    public Order(Order other){
        this.id = other.id;
        this.customerName = other.customerName;
        this.hummus = other.hummus;
        this.tahini = other.tahini;
        this.pickles = other.pickles;
        this.comment = other.comment;
        this.status = other.status;
    }

    public String getId(){
        return this.id;
    }

    public String getCustomerName(){
        return this.customerName;
    }

    public boolean isHummus(){
        return this.hummus;
    }

    public boolean isTahini(){
        return this.tahini;
    }

    public int getPickles(){
        return this.pickles;
    }

    public String getComment(){
        return this.comment;
    }

    public String getStatus(){
        return this.status;
    }

    public void setCustomerName(String name){
        this.customerName = name;
    }

    public void setHummus(boolean hummus){
        this.hummus = hummus;
    }

    public void setTahini(boolean tahini){
        this.tahini = tahini;
    }

    public void setPickles(int pickles){
        this.pickles = pickles;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public void setStatus(String status){
        this.status = status;
    }

}
