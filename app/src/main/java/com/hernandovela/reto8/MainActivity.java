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
    private AlertDialog activeEditor;
    private AlertDialog activeDelete;
    private ScrollView directoryScroll;
    private int dp(int n) { return (int)(n*getResources().getDisplayMetrics().density); }
    private LinearLayout column() { LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(dp(20),dp(12),dp(20),dp(12)); return l; }
    private TextView text(String value,int size) { TextView t=new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(Color.rgb(24,42,62)); t.setPadding(0,dp(6),0,dp(6)); return t; }
    private android.graphics.drawable.GradientDrawable rounded(int color) {
        android.graphics.drawable.GradientDrawable shape=new android.graphics.drawable.GradientDrawable(); shape.setColor(color); shape.setCornerRadius(dp(12)); return shape;
    }
    private Button button(String label,Runnable action) { Button b=new Button(this); b.setText(label); b.setAllCaps(false); b.setTextColor(Color.rgb(8,100,90)); b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(225,241,238))); b.setOnClickListener(v->action.run()); return b; }
    @Override public void onCreate(Bundle state) {
        super.onCreate(state); db=new CompanyDatabase(this);
        LinearLayout screen=column(); screen.setBackgroundColor(Color.rgb(244,247,251));
        screen.setOnApplyWindowInsetsListener((v,insets)-> { v.setPadding(dp(20),dp(12)+insets.getSystemWindowInsetTop(),dp(20),dp(12)+insets.getSystemWindowInsetBottom()); return insets; });
        LinearLayout heading=new LinearLayout(this);heading.setGravity(Gravity.CENTER_VERTICAL);
        ImageView logo=new ImageView(this);logo.setImageResource(R.drawable.ic_directory);logo.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        heading.addView(logo,new LinearLayout.LayoutParams(dp(48),dp(48)));
        LinearLayout titles=new LinearLayout(this);titles.setOrientation(LinearLayout.VERTICAL);titles.setPadding(dp(12),0,0,0);
        TextView title=text("Directorio de empresas",21);title.setTypeface(null,android.graphics.Typeface.BOLD);titles.addView(title);titles.addView(text("Encuentra tu aliado tecnológico",12));
        heading.addView(titles,new LinearLayout.LayoutParams(0,-2,1));screen.addView(heading);
        Button create=button("+ Nueva empresa",()->edit(null));create.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(18,60,80)));create.setTextColor(Color.WHITE);screen.addView(create);
        search=new EditText(this); search.setSingleLine(true); search.setHint("Buscar en cualquier campo"); screen.addView(search);
        search.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH);
        search.setOnEditorActionListener((v,action,event)->{if(action==android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH){((android.view.inputmethod.InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(search.getWindowToken(),0);search.clearFocus();return true;}return false;});
        screen.addView(button("Ver todas las empresas",()->{
            search.setText(""); filter.setSelection(0); search.clearFocus();
            ((android.view.inputmethod.InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(search.getWindowToken(),0);
            refresh(); directoryScroll.smoothScrollTo(0,0);
        }));
        filter=new Spinner(this); filter.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"Todas las clasificaciones",Company.LABELS[0],Company.LABELS[1],Company.LABELS[2]})); screen.addView(filter);
        count=text("",14); screen.addView(count);
        ScrollView scroll=new ScrollView(this); directoryScroll=scroll; rows=column(); scroll.addView(rows); screen.addView(scroll,new LinearLayout.LayoutParams(-1,0,1)); setContentView(screen);
        search.addTextChangedListener(new TextWatcher(){ public void beforeTextChanged(CharSequence s,int a,int c,int f){} public void onTextChanged(CharSequence s,int a,int b,int c){refresh();} public void afterTextChanged(Editable e){} });
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){ public void onItemSelected(AdapterView<?> p,View v,int pos,long id){refresh();} public void onNothingSelected(AdapterView<?> p){} });
        if(state!=null){ search.setText(state.getString("search","")); filter.setSelection(state.getInt("filter",0)); } refresh();
    }
    @Override public void onSaveInstanceState(Bundle state) { super.onSaveInstanceState(state); state.putString("search",search.getText().toString()); state.putInt("filter",filter.getSelectedItemPosition()); }
    private void refresh() {
        rows.removeAllViews(); int selected=filter.getSelectedItemPosition();
        java.util.List<Company> companies=db.list(search.getText().toString().trim(),selected==0?0:1<<(selected-1));
        count.setText(getResources().getQuantityString(R.plurals.directory_count,companies.size(),companies.size()));
        if(companies.isEmpty()) rows.addView(text(search.length()>0||selected!=0?"Sin coincidencias. Prueba otra palabra o pulsa Ver todas las empresas.":"Tu directorio está listo. Pulsa + Nueva empresa para agregar el primer contacto.",16));
        for(Company c:companies) {
            LinearLayout card=column(); card.setBackground(rounded(Color.WHITE)); card.setElevation(dp(2)); LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-2); params.setMargins(dp(2),dp(2),dp(2),dp(12)); rows.addView(card,params);
            card.addView(text(c.name,19)); card.addView(text(c.classification(),14));
            LinearLayout details=new LinearLayout(this); details.setOrientation(LinearLayout.VERTICAL); details.setVisibility(View.GONE);
            Button toggle=button("Ver detalles",()->{}); toggle.setOnClickListener(v->{boolean open=details.getVisibility()==View.GONE;details.setVisibility(open?View.VISIBLE:View.GONE);toggle.setText(open?"Ocultar detalles":"Ver detalles");}); card.addView(toggle);card.addView(details);
            TextView contact=text("Web: "+c.website+"\nTeléfono: "+c.phone+"\nEmail: "+c.email+"\n\nProductos y servicios: "+c.services,16);contact.setTextIsSelectable(true);details.addView(contact);
            details.addView(button("Editar",()->edit(c))); details.addView(button("Eliminar",()->{
                activeDelete=new AlertDialog.Builder(this).setTitle("Eliminar empresa").setMessage("¿Quieres eliminar a «"+c.name+"»? Esta acción no se puede deshacer.").setNegativeButton("Cancelar",null).setPositiveButton("Eliminar",(d,w)->{ try { if(!db.delete(c.id)) throw new IllegalStateException(); refresh(); toast("Empresa eliminada"); } catch(Exception e){toast("No se pudo eliminar la empresa");} }).show();
            }));
        }
    }
    private void toast(String value) { Toast.makeText(this,value,Toast.LENGTH_LONG).show(); }
    private void edit(Company existing) {
        LinearLayout body=new LinearLayout(this); body.setOrientation(LinearLayout.VERTICAL);
        LinearLayout form=column(); ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true); scroll.addView(form);
        body.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        String[] labels={"Nombre de la empresa","URL de la página web","Teléfono de contacto","Email de contacto","Productos y servicios"};
        String[] values=existing==null?new String[]{"","","","",""}:new String[]{existing.name,existing.website,existing.phone,existing.email,existing.services};
        EditText[] fields=new EditText[5];
        for(int i=0;i<5;i++){form.addView(text(labels[i],14)); fields[i]=new EditText(this); fields[i].setText(values[i]); fields[i].setHint(i==1?"https://empresa.com":labels[i]); fields[i].setInputType(i==1?17:i==2?3:i==3?33:i==4?131073:8193); form.addView(fields[i]);}
        LinearLayout classification=column(); classification.addView(text("Clasificación (elige una o varias)",16)); CheckBox[] categories=new CheckBox[3];
        for(int i=0;i<3;i++){categories[i]=new CheckBox(this); categories[i].setText(Company.LABELS[i]); categories[i].setChecked(existing!=null&&(existing.categories&(1<<i))!=0); classification.addView(categories[i]);}
        body.addView(classification,new LinearLayout.LayoutParams(-1,-2));
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle(existing==null?"Nueva empresa":"Editar empresa").setView(body).setNegativeButton("Cancelar",null).setPositiveButton("Guardar",null).create();
        activeEditor=dialog;
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
        dialog.getWindow().setLayout(-1,(int)(getResources().getDisplayMetrics().heightPixels*0.90));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    @Override public void onDestroy(){db.close();super.onDestroy();}
}
