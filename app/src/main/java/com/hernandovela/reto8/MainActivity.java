package com.hernandovela.reto8;
import android.app.*;
import android.os.Bundle;
import android.graphics.Color;
import android.text.*;
import android.view.*;
import android.widget.*;
import android.util.Patterns;

public class MainActivity extends Activity {
    private CompanyDatabase db;
    private LinearLayout rows;
    private EditText search;
    private Spinner filter;
    private TextView count;
    private int dp(int n) { return (int)(n*getResources().getDisplayMetrics().density); }
    private LinearLayout column() { LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(dp(20),dp(12),dp(20),dp(12)); return l; }
    private TextView text(String value,int size) { TextView t=new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(Color.rgb(24,42,62)); t.setPadding(0,dp(6),0,dp(6)); return t; }
    private Button button(String label,Runnable action) { Button b=new Button(this); b.setText(label); b.setAllCaps(false); b.setOnClickListener(v->action.run()); return b; }
    @Override public void onCreate(Bundle state) {
        super.onCreate(state); db=new CompanyDatabase(this);
        LinearLayout screen=column(); screen.setBackgroundColor(Color.rgb(244,247,251));
        screen.setOnApplyWindowInsetsListener((v,insets)-> { v.setPadding(dp(20),dp(12)+insets.getSystemWindowInsetTop(),dp(20),dp(12)+insets.getSystemWindowInsetBottom()); return insets; });
        screen.addView(text("Directorio de empresas",26)); screen.addView(text("Software y servicios • SQLite",14));
        screen.addView(button("+ Nueva empresa",()->edit(null)));
        search=new EditText(this); search.setSingleLine(true); search.setHint("Buscar por nombre"); screen.addView(search);
        filter=new Spinner(this); filter.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"Todas las clasificaciones",Company.LABELS[0],Company.LABELS[1],Company.LABELS[2]})); screen.addView(filter);
        count=text("",14); screen.addView(count);
        ScrollView scroll=new ScrollView(this); rows=column(); scroll.addView(rows); screen.addView(scroll,new LinearLayout.LayoutParams(-1,0,1)); setContentView(screen);
        search.addTextChangedListener(new TextWatcher(){ public void beforeTextChanged(CharSequence s,int a,int c,int f){} public void onTextChanged(CharSequence s,int a,int b,int c){refresh();} public void afterTextChanged(Editable e){} });
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){ public void onItemSelected(AdapterView<?> p,View v,int pos,long id){refresh();} public void onNothingSelected(AdapterView<?> p){} });
        if(state!=null){ search.setText(state.getString("search","")); filter.setSelection(state.getInt("filter",0)); } refresh();
    }
    @Override public void onSaveInstanceState(Bundle state) { super.onSaveInstanceState(state); state.putString("search",search.getText().toString()); state.putInt("filter",filter.getSelectedItemPosition()); }
    private void refresh() {
        rows.removeAllViews(); int selected=filter.getSelectedItemPosition();
        java.util.List<Company> companies=db.list(search.getText().toString().trim(),selected==0?0:1<<(selected-1));
        count.setText(companies.size()+" empresa(s)");
        if(companies.isEmpty()) rows.addView(text("No hay empresas para mostrar. Crea una empresa o cambia los filtros.",16));
        for(Company c:companies) {
            LinearLayout card=column(); card.setBackgroundColor(Color.WHITE); LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-2); params.setMargins(0,0,0,dp(12)); rows.addView(card,params);
            card.addView(text(c.name,21)); card.addView(text(c.classification(),14)); card.addView(text(c.website+"\n"+c.phone+"\n"+c.email+"\n\n"+c.services,16));
            card.addView(button("Editar",()->edit(c))); card.addView(button("Eliminar",()->new AlertDialog.Builder(this).setTitle("Eliminar empresa").setMessage("¿Quieres eliminar a «"+c.name+"»? Esta acción no se puede deshacer.").setNegativeButton("Cancelar",null).setPositiveButton("Eliminar",(d,w)->{ try { if(!db.delete(c.id)) throw new IllegalStateException(); refresh(); toast("Empresa eliminada"); } catch(Exception e){toast("No se pudo eliminar la empresa");} }).show()));
        }
    }
    private void toast(String value) { Toast.makeText(this,value,Toast.LENGTH_LONG).show(); }
    private void edit(Company existing) {
        LinearLayout form=column(); ScrollView scroll=new ScrollView(this); scroll.addView(form);
        String[] labels={"Nombre de la empresa","URL de la página web","Teléfono de contacto","Email de contacto","Productos y servicios"};
        String[] values=existing==null?new String[]{"","","","",""}:new String[]{existing.name,existing.website,existing.phone,existing.email,existing.services};
        EditText[] fields=new EditText[5];
        for(int i=0;i<5;i++){form.addView(text(labels[i],14)); fields[i]=new EditText(this); fields[i].setText(values[i]); fields[i].setHint(i==1?"https://empresa.com":labels[i]); fields[i].setInputType(i==1?17:i==2?3:i==3?33:i==4?131073:8193); form.addView(fields[i]);}
        form.addView(text("Clasificación (elige una o varias)",16)); CheckBox[] categories=new CheckBox[3];
        for(int i=0;i<3;i++){categories[i]=new CheckBox(this); categories[i].setText(Company.LABELS[i]); categories[i].setChecked(existing!=null&&(existing.categories&(1<<i))!=0); form.addView(categories[i]);}
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle(existing==null?"Nueva empresa":"Editar empresa").setView(scroll).setNegativeButton("Cancelar",null).setPositiveButton("Guardar",null).create();
        dialog.setOnShowListener(d->dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v->{
            String[] data=new String[5]; boolean valid=true;
            for(int i=0;i<5;i++){data[i]=fields[i].getText().toString().trim(); if(data[i].isEmpty()){fields[i].setError("Campo obligatorio"); valid=false;}}
            if(!data[1].matches("(?i)^https?://.+")||!Patterns.WEB_URL.matcher(data[1]).matches()){ fields[1].setError("Ingresa una URL http:// o https:// válida"); valid=false; }
            if(!Patterns.EMAIL_ADDRESS.matcher(data[3]).matches()){fields[3].setError("Email inválido");valid=false;}
            if(!data[2].matches("[+0-9() .-]{7,30}")){fields[2].setError("Ingresa un teléfono válido");valid=false;}
            int mask=0; for(int i=0;i<3;i++) if(categories[i].isChecked())mask|=1<<i;
            if(mask==0){toast("Selecciona al menos una clasificación");valid=false;} if(!valid)return;
            try{ db.save(new Company(existing==null?0:existing.id,data[0],data[1],data[2],data[3],data[4],mask)); refresh();dialog.dismiss();toast("Empresa guardada"); }catch(Exception e){toast("No se pudo guardar la empresa");}
        })); dialog.show();
    }
    @Override public void onDestroy(){db.close();super.onDestroy();}
}
