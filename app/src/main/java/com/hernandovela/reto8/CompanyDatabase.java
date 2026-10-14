package com.hernandovela.reto8;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class CompanyDatabase extends SQLiteOpenHelper {
    public CompanyDatabase(Context context) { this(context,"companies.db"); }
    CompanyDatabase(Context context,String name) { super(context,name,null,1); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE companies (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, website TEXT NOT NULL, phone TEXT NOT NULL, email TEXT NOT NULL, services TEXT NOT NULL, categories INTEGER NOT NULL CHECK(categories BETWEEN 1 AND 7))");
    }
    @Override public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        throw new IllegalStateException("Se requiere una migración explícita para conservar los datos");
    }
    public long save(Company c) {
        ContentValues v=new ContentValues(); v.put("name",c.name); v.put("website",c.website); v.put("phone",c.phone); v.put("email",c.email); v.put("services",c.services); v.put("categories",c.categories);
        if(c.id==0) return getWritableDatabase().insertOrThrow("companies",null,v);
        if(getWritableDatabase().update("companies",v,"_id=?",new String[]{String.valueOf(c.id)})!=1) throw new IllegalStateException("La empresa ya no existe");
        return c.id;
    }
    public boolean delete(long id) { return getWritableDatabase().delete("companies","_id=?",new String[]{String.valueOf(id)})==1; }
    public List<Company> list(String name,int category) {
        List<Company> result=new ArrayList<>();
        String escaped=name.replace("\\","\\\\").replace("%","\\%").replace("_","\\_");
        try(Cursor c=getReadableDatabase().query("companies",null,"name LIKE ? ESCAPE '\\' AND (CAST(? AS INTEGER) = 0 OR (categories & ?) != 0)",new String[]{"%"+escaped+"%",String.valueOf(category),String.valueOf(category)},null,null,"name COLLATE NOCASE ASC, _id ASC")) {
            while(c.moveToNext()) result.add(new Company(c.getLong(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getInt(6)));
        }
        return result;
    }
}

