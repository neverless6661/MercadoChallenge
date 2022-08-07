package com.lubani.mercadochallenge;

public class ListBusqueda {
    public String id;
    public String titulo;
    public int price;
    public String condition;
    public String shipping;
    public String thumbnail;

    public ListBusqueda(){}

    public ListBusqueda(String id, String titulo, int price, String condition, String shipping, String thumbnail) {
        this.id = id;
        this.titulo = titulo;
        this.price = price;
        this.condition = condition;
        this.shipping = shipping;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public String getShipping() {
        return shipping;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
