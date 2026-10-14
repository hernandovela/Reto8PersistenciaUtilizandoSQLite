package com.hernandovela.reto8;

import android.test.AndroidTestCase;

/** Integration tests against the real Android SQLite engine. */
public class CompanyDatabaseTest extends AndroidTestCase {
    private CompanyDatabase db;
    @Override protected void setUp() throws Exception {
        super.setUp(); getContext().deleteDatabase("companies.db"); db=new CompanyDatabase(getContext());
    }
    @Override protected void tearDown() throws Exception {
        db.close(); getContext().deleteDatabase("companies.db"); super.tearDown();
    }
    public void testCrudAndPersistence() {
        Company c=new Company(0,"Empresa Uno","https://example.com","+571234567890","info@example.com","Aplicaciones",3);
        c.id=db.save(c); assertTrue(c.id>0);
        db.close(); db=new CompanyDatabase(getContext());
        Company stored=db.list("",0).get(0);
        assertEquals(c.name,stored.name); assertEquals(c.website,stored.website); assertEquals(c.phone,stored.phone);
        assertEquals(c.email,stored.email); assertEquals(c.services,stored.services); assertEquals(3,stored.categories);
        c.name="Empresa actualizada"; c.categories=4; db.save(c);
        assertEquals(1,db.list("actualizada",4).size()); assertEquals(0,db.list("",1).size());
        assertTrue(db.delete(c.id)); assertFalse(db.delete(c.id)); assertTrue(db.list("",0).isEmpty());
    }
    public void testCombinedAndLiteralFilters() {
        db.save(new Company(0,"100%_Software","https://example.com","123456789","a@example.com","Servicios",5));
        db.save(new Company(0,"Otra empresa","https://example.com","123456789","b@example.com","Servicios",2));
        assertEquals(2,db.list("",0).size()); assertEquals(1,db.list("software",1).size());
        assertEquals(1,db.list("%_",4).size()); assertEquals(0,db.list("software",2).size());
        assertEquals(0,db.list("' OR 1=1 --",0).size());
    }
}
