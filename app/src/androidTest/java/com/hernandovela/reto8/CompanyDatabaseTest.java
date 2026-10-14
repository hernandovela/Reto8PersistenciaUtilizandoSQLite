package com.hernandovela.reto8;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/** Integration tests against the real Android SQLite engine. */
public class CompanyDatabaseTest {
    private Context getContext() { return InstrumentationRegistry.getInstrumentation().getTargetContext(); }
    private CompanyDatabase db;
    @Before public void setUp() {
        getContext().deleteDatabase("companies-test.db"); db=new CompanyDatabase(getContext(),"companies-test.db");
    }
    @After public void tearDown() {
        db.close(); getContext().deleteDatabase("companies-test.db");
    }
    @Test public void testCrudAndPersistence() {
        Company c=new Company(0,"Empresa Uno","https://example.com","+571234567890","info@example.com","Aplicaciones",3);
        c.id=db.save(c); assertTrue(c.id>0);
        db.close(); db=new CompanyDatabase(getContext(),"companies-test.db");
        Company stored=db.list("",0).get(0);
        assertEquals(c.name,stored.name); assertEquals(c.website,stored.website); assertEquals(c.phone,stored.phone);
        assertEquals(c.email,stored.email); assertEquals(c.services,stored.services); assertEquals(3,stored.categories);
        c.name="Empresa actualizada"; c.categories=4; db.save(c);
        assertEquals(1,db.list("actualizada",4).size()); assertEquals(0,db.list("",1).size());
        assertTrue(db.delete(c.id)); assertFalse(db.delete(c.id)); assertTrue(db.list("",0).isEmpty());
    }
    @Test public void testCombinedAndLiteralFilters() {
        db.save(new Company(0,"100%_Software","https://example.com","123456789","a@example.com","Servicios",5));
        db.save(new Company(0,"Otra empresa","https://example.com","123456789","b@example.com","Servicios",2));
        assertEquals(2,db.list("",0).size()); assertEquals(1,db.list("software",1).size());
        assertEquals(1,db.list("%_",4).size()); assertEquals(0,db.list("software",2).size());
        assertEquals(0,db.list("' OR 1=1 --",0).size());
    }
    @Test public void testSearchEveryFieldAndAccents() {
        db.save(new Company(0,"Árbol Digital","https://portal.example.com","5559876543","ventas@equipo.example","Automatización industrial 100%_",5));
        db.save(new Company(0,"Otra","https://otro.example.com","1112223333","otro@example.com","Aplicaciones",2));
        for(String query:new String[]{"ARBOL", "portal", "9876543", "ventas@equipo", "automatizacion", "CONSULTORIA", "fabrica", "100%_"})
            assertEquals(query,1,db.list(query,0).size());
        assertEquals(1,db.list("ventas",4).size());
        assertEquals(0,db.list("ventas",2).size());
        assertEquals(2,db.list("",0).size());
    }
}
