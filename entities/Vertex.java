package sample.entities;

import javafx.scene.shape.Circle;

import java.util.List;

public class Vertex {
    private int id;
    private List<Vertex> previous;
    private List<Vertex> next;
    public double x;
    public double y;
    public Circle circle;

    public Vertex(){}

    public Vertex(int id, double x, double y, Circle circle){
        this.id = id;
        this.x = x;
        this.y = y;
        this.circle = circle;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setPrevious(List<Vertex> previous){
        this.previous = previous;
    }

    public void setNext(List<Vertex> next){
        this.next = next;
    }

    public int getId(){
        return this.id;
    }

    public List<Vertex> getPrevious(){
        return this.previous;
    }

    public List<Vertex> getNext(){
        return this.next;
    }

}
