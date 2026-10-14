package com.hernandovela.reto8;
public class Company {
    public long id;
    public String name, website, phone, email, services;
    public int categories;
    public Company(long id, String name, String website, String phone, String email, String services, int categories) {
        this.id=id; this.name=name; this.website=website; this.phone=phone; this.email=email; this.services=services; this.categories=categories;
    }
    public static final String[] LABELS={"Consultoría", "Desarrollo a la medida", "Fábrica de software"};
    public String classification() {
        StringBuilder result=new StringBuilder();
        for(int i=0;i<LABELS.length;i++) if((categories & (1<<i))!=0) { if(result.length()>0) result.append(" • "); result.append(LABELS[i]); }
        return result.toString();
    }
}
