package com.hernandovela.reto8;

import android.app.*;
import android.content.Intent;
import android.graphics.Rect;
import android.view.*;
import android.widget.*;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/** Exercises real UI controls and SQLite, removing only its uniquely named record. */
public class RequirementsUiTest {
    private final Instrumentation instrumentation=InstrumentationRegistry.getInstrumentation();
    private final Instrumentation.ActivityMonitor monitor=instrumentation.addMonitor(MainActivity.class.getName(),null,false);
    private View find(View view,String label) {
        if(view instanceof TextView && label.contentEquals(((TextView)view).getText())) return view;
        if(view instanceof ViewGroup) for(int i=0;i<((ViewGroup)view).getChildCount();i++){ View found=find(((ViewGroup)view).getChildAt(i),label); if(found!=null)return found; }
        return null;
    }
    private void edits(View view,List<EditText> result) {
        if(view instanceof EditText)result.add((EditText)view);
        if(view instanceof ViewGroup)for(int i=0;i<((ViewGroup)view).getChildCount();i++)edits(((ViewGroup)view).getChildAt(i),result);
    }
    private AlertDialog dialog(Activity activity) throws Exception {
        java.lang.reflect.Field field=MainActivity.class.getDeclaredField("activeEditor"); field.setAccessible(true);
        return (AlertDialog)field.get(activity);
    }
    private AlertDialog deletion(Activity activity) throws Exception {
        java.lang.reflect.Field field=MainActivity.class.getDeclaredField("activeDelete"); field.setAccessible(true);
        return (AlertDialog)field.get(activity);
    }
    @Test public void classificationsAndCrudMeetRequirements() throws Exception {
        String original="QA-Requisitos-"+System.currentTimeMillis(), updated=original+"-editada";
        Activity activity=instrumentation.startActivitySync(new Intent(instrumentation.getTargetContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        try {
            instrumentation.runOnMainSync(()->find(activity.getWindow().getDecorView(),"+ Nueva empresa").performClick());
            final AlertDialog editor=dialog(activity);
            instrumentation.runOnMainSync(()->{
                Rect visible=new Rect();
                for(String label:Company.LABELS){CheckBox box=(CheckBox)find(editor.getWindow().getDecorView(),label); assertNotNull(label,box); assertTrue(label,box.getGlobalVisibleRect(visible)); assertEquals(label,box.getHeight(),visible.height());box.performClick();assertTrue(box.isChecked());}
                List<EditText> fields=new ArrayList<>(); edits(editor.getWindow().getDecorView(),fields); assertEquals(5,fields.size());
                String[] values={original,"https://example.com","3001234567","qa@example.com","Software de prueba"};
                for(int i=0;i<5;i++)fields.get(i).setText(values[i]);
                editor.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                assertNotNull(find(activity.getWindow().getDecorView(),original));
                List<EditText> search=new ArrayList<>(); edits(activity.getWindow().getDecorView(),search);
                search.get(0).setText(original+"-inexistente");assertNull(find(activity.getWindow().getDecorView(),original));
                find(activity.getWindow().getDecorView(),"Ver todas las empresas").performClick();
                assertEquals("",search.get(0).getText().toString());assertNotNull(find(activity.getWindow().getDecorView(),original));
                search.get(0).setText(original);
                assertFalse(find(activity.getWindow().getDecorView(),"Editar").isShown());
                find(activity.getWindow().getDecorView(),"Ver detalles").performClick();
                assertTrue(find(activity.getWindow().getDecorView(),"Editar").isShown());
            });
            CompanyDatabase db=new CompanyDatabase(instrumentation.getTargetContext());
            try {
                assertEquals(7,db.list(original,0).get(0).categories);
                for(int mask:new int[]{1,2,4})assertEquals(1,db.list(original,mask).size());
                assertEquals(0,db.list(original+"-inexistente",0).size());
            } finally {db.close();}
            instrumentation.runOnMainSync(()->find(activity.getWindow().getDecorView(),"Editar").performClick());
            final AlertDialog editDialog=dialog(activity);
            instrumentation.runOnMainSync(()->{List<EditText> fields=new ArrayList<>();edits(editDialog.getWindow().getDecorView(),fields);fields.get(0).setText(updated);editDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();assertNotNull(find(activity.getWindow().getDecorView(),updated));});
            instrumentation.runOnMainSync(()->{find(activity.getWindow().getDecorView(),"Ver detalles").performClick();find(activity.getWindow().getDecorView(),"Eliminar").performClick();});
            final AlertDialog cancelled=deletion(activity);
            instrumentation.runOnMainSync(()->{assertNotNull(find(cancelled.getWindow().getDecorView(),"¿Quieres eliminar a «"+updated+"»? Esta acción no se puede deshacer."));cancelled.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();assertNotNull(find(activity.getWindow().getDecorView(),updated));});
            instrumentation.runOnMainSync(()->find(activity.getWindow().getDecorView(),"Eliminar").performClick());
            final AlertDialog confirmed=deletion(activity);
            instrumentation.runOnMainSync(()->confirmed.getButton(AlertDialog.BUTTON_POSITIVE).performClick());
            instrumentation.waitForIdleSync();
            instrumentation.runOnMainSync(()->assertNull(find(activity.getWindow().getDecorView(),updated)));
            CompanyDatabase after=new CompanyDatabase(instrumentation.getTargetContext());
            try{assertEquals(0,after.list(original,0).size());}finally{after.close();}
        } finally {
            CompanyDatabase db=new CompanyDatabase(instrumentation.getTargetContext());
            try { for(Company c:db.list(original,0))db.delete(c.id); }finally{db.close();}
            instrumentation.runOnMainSync(activity::finish); instrumentation.removeMonitor(monitor);
        }
    }
}
