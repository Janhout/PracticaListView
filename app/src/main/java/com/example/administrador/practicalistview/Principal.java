package com.example.administrador.practicalistview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class Principal extends Activity {

    private ArrayList<Juego> datosJuegos;
    private ListView lista;
    private AdaptadorLista adaptador;
    private int ordenar;

    private ArrayList<String> valoresDificultad;
    private ArrayList<String> valoresPublicacion;
    private ArrayList<String> valoresPuntuacion;
    private ArrayList<String> valoresTipo;

    /**********************************************************************************************/
    /**********************************************************************************************/
    /*                                    Métodos on...                                           */
    /**********************************************************************************************/
    /**********************************************************************************************/

    /*Método que hay que sobreescribir para no perder los datos al girar la pantalla*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*Método que gestiona el clic sobre un elemento del menu contextual*/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = info.position;
        if(id == R.id.contextual_borrar){
            return borrarJuego(index);
        }else if(id == R.id.contextual_editar){
            return editarJuego(index);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_principal);
        initComponents();
    }

    /*Método que crea el menú contextual*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual,menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_nuevo) {
            return nuevoJuego();
        }else if (id == R.id.menu_ordena_nombre){
            ordenar = 1;
            actualizarLista();
            return true;
        }else if (id == R.id.menu_ordena_tipo){
            ordenar = 3;
            actualizarLista();
            return true;
        }else if (id == R.id.menu_ordena_puntuacion){
            ordenar = 2;
            actualizarLista();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*Método para gestionar dinamicamente el menú principal*/
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        switch(ordenar){
            case 1:
                menu.findItem(R.id.menu_ordena_puntuacion).setVisible(true);
                menu.findItem(R.id.menu_ordena_nombre).setVisible(false);
                menu.findItem(R.id.menu_ordena_tipo).setVisible(true);
                break;
            case 2:
                menu.findItem(R.id.menu_ordena_puntuacion).setVisible(false);
                menu.findItem(R.id.menu_ordena_nombre).setVisible(true);
                menu.findItem(R.id.menu_ordena_tipo).setVisible(true);
                break;
            case 3:
                menu.findItem(R.id.menu_ordena_puntuacion).setVisible(true);
                menu.findItem(R.id.menu_ordena_nombre).setVisible(true);
                menu.findItem(R.id.menu_ordena_tipo).setVisible(false);
                break;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/
    /*                                     Auxiliares                                             */
    /**********************************************************************************************/
    /**********************************************************************************************/

    /*Método que actualiza la lista */
    private void actualizarLista(){
        switch(ordenar){
            case 1:
                Collections.sort(datosJuegos);
                break;
            case 2:
                Collections.sort(datosJuegos, new JuegosPorPuntuacion());
                break;
            case 3:
                Collections.sort(datosJuegos, new JuegosPorTipo());
                break;
        }

        adaptador.notifyDataSetChanged();
    }

    /*Método para cargar juegos para hacer las pruebas*/
    private void datosIniciales() {
        datosJuegos.add(new Juego("Senderos de Gloria", 1999, "Juego sobre la Primera Guerra Mundial",
                "Wargame", null, 10, 90, false));
        datosJuegos.add(new Juego("Pandemia", 2007, "¿Serás capaz de salvar el mundo de todas las enfermedades?",
                "Estrategia", null, 5, 77, false));
        datosJuegos.add(new Juego("Pandemia: On The Brick", 2010, "Expansión que añade más dificultad y emoción a Pandemia",
                "Estrategia", null, 6, 82, true));
        datosJuegos.add(new Juego("Bohnanza", 1997, "Vender más alubias que los demás",
                "Otro", null, 3, 65, false));
        datosJuegos.add(new Juego("Un Mundo Sin Fin", 2009, "Si te gusta Los Pilares De La Tierra... Juego parecido pero con unas reglas más pulidas",
                "Eurogame", null, 5, 70, false));
        datosJuegos.add(new Juego("Arkham Horror", 2005, "Un clásico cooperativo y solitario. Si te gusta Lovecraft, este puede ser tu juego",
                "Temático", null, 7, 80, false));
        datosJuegos.add(new Juego("Claustrophobia", 2009, "Sin comentarios",
                "Ameritrash", null, 7, 73, false));
    }

    /*Método para conseguir el drawable correspondiente al tipo de juego*/
    public static int conseguirImagen(Context c, String tipo){
        int resultado = -1;

        if(tipo.equals(c.getString(R.string.abstracto_tipo))){
            resultado = R.drawable.abstracto;
        }else if(tipo.equals(c.getString(R.string.ameritrash_tipo))){
            resultado = R.drawable.ameritrash;
        }else if(tipo.equals(c.getString(R.string.estrategia_tipo))){
            resultado = R.drawable.estrategia;
        }else if(tipo.equals(c.getString(R.string.eurogame_tipo))){
            resultado = R.drawable.eurogame;
        }else if(tipo.equals(c.getString(R.string.otro_tipo))){
            resultado = R.drawable.otro;
        }else if(tipo.equals(c.getString(R.string.tematico_tipo))){
            resultado = R.drawable.tematico;
        }else if(tipo.equals(c.getString(R.string.wargame_tipo))){
            resultado = R.drawable.wargame;
        }

        return resultado;
    }

    /*Método que devuelve la información de cada tipo de juego*/
    private String conseguirTexto(String tipo){
        String resultado = "";

        if(tipo.equals(valoresTipo.get(0))){
            resultado = getString(R.string.abstracto);
        }else if(tipo.equals(valoresTipo.get(1))){
            resultado = getString(R.string.ameritrash);
        }else if(tipo.equals(valoresTipo.get(2))){
            resultado = getString(R.string.estrategia);
        }else if(tipo.equals(valoresTipo.get(3))){
            resultado = getString(R.string.eurogame);
        }else if(tipo.equals(valoresTipo.get(4))){
            resultado = getString(R.string.otro);
        }else if(tipo.equals(valoresTipo.get(5))){
            resultado = getString(R.string.tematico);
        }else if(tipo.equals(valoresTipo.get(6))){
            resultado = getString(R.string.wargame);
        }

        return resultado;
    }

    /*Método auxiliar para inicializar los componentes de la aplicación*/
    private void initComponents(){
        ordenar = 1;
        rellenarDatosSpinners();
        datosJuegos = new ArrayList<Juego>();
        datosIniciales();
        lista = (ListView)findViewById(R.id.listView);
        adaptador = new AdaptadorLista(this, R.layout.elemento, datosJuegos);
        lista.setAdapter(adaptador);
        actualizarLista();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Juego juego = (Juego) adapterView.getItemAtPosition(i);
                mostrarDatos(juego);
            }
        });
        registerForContextMenu(lista);
    }

    /*Rellena los arrayList que se usaran para rellenar los spinners*/
    private void rellenarDatosSpinners(){
        valoresTipo = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.valoresTipo)));
        valoresDificultad = new ArrayList<String>();
        valoresPublicacion = new ArrayList<String>();
        valoresPuntuacion = new ArrayList<String>();
        for(int i=0; i<10; i++){
            valoresDificultad.add(String.valueOf(i + 1));
        }
        for(int i=1979; i<2014; i++){
            valoresPublicacion.add(String.valueOf(i+1));
        }
        valoresPublicacion.add("otro");
        for(int i=0; i<100; i++){
            valoresPuntuacion.add(String.valueOf(i+1));
        }
    }

    /*Método que muestra un Toast con el string s*/
    private void tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/
    /*                                        Menú aplicación                                     */
    /**********************************************************************************************/
    /**********************************************************************************************/

    /*Metodo para añadir un juego nuevo. Esta versión no permite añadir foto desde la galería.*/
    private boolean nuevoJuego(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.nuevo_juego));
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_nuevo, null);
        alert.setView(vista);

        final Spinner spDificultad = (Spinner) vista.findViewById(R.id.spinnerDificultad);
        final Spinner spTipo = (Spinner) vista.findViewById(R.id.spinnerTipo);
        final Spinner spPublicacion = (Spinner) vista.findViewById(R.id.spinnerPublicacion);
        final Spinner spPuntuacion = (Spinner) vista.findViewById(R.id.spinnerPuntuacion);

        spDificultad.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresDificultad));
        spTipo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresTipo));
        spPublicacion.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresPublicacion));
        spPuntuacion.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresPuntuacion));

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText etNombre, etInformacion;
                RadioButton rbSi;
                etNombre = (EditText) vista.findViewById(R.id.nuevoNombre);
                etInformacion = (EditText) vista.findViewById(R.id.nuevoInfo);
                rbSi = (RadioButton)vista.findViewById(R.id.rbSi);

                String nombre = etNombre.getText().toString();
                int publicacion = Integer.valueOf(spPublicacion.getSelectedItem().toString());
                int puntuacion = Integer.valueOf(spPuntuacion.getSelectedItem().toString());
                int dificultad = Integer.valueOf(spDificultad.getSelectedItem().toString());
                String tipo = spTipo.getSelectedItem().toString();
                String informacion = etInformacion.getText().toString();
                boolean expansion = rbSi.isChecked();
                Bitmap foto = null;
                datosJuegos.add(new Juego(nombre, publicacion, informacion, tipo, foto, dificultad, puntuacion, expansion));
                actualizarLista();
                tostada(getString(R.string.elemento_nuevo));
            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();

        return true;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/
    /*                                   Menú Contextual                                          */
    /**********************************************************************************************/
    /**********************************************************************************************/

    /*Método para editar los datos de un juego, en esta version no permite editar la foto
    * desde la galería*/
    private boolean editarJuego(final int posicion){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.editar_juego));
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_nuevo, null);
        alert.setView(vista);

        final Spinner spDificultad = (Spinner) vista.findViewById(R.id.spinnerDificultad);
        final Spinner spTipo = (Spinner) vista.findViewById(R.id.spinnerTipo);
        final Spinner spPublicacion = (Spinner) vista.findViewById(R.id.spinnerPublicacion);
        final Spinner spPuntuacion = (Spinner) vista.findViewById(R.id.spinnerPuntuacion);

        spDificultad.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresDificultad));
        spTipo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresTipo));
        spPublicacion.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresPublicacion));
        spPuntuacion.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresPuntuacion));

        spTipo.setSelection(valoresTipo.indexOf(datosJuegos.get(posicion).getTipo()));
        spPublicacion.setSelection(valoresPublicacion.indexOf(""+datosJuegos.get(posicion).getPublicacion()));
        spPuntuacion.setSelection(valoresPuntuacion.indexOf(""+datosJuegos.get(posicion).getPuntuacion()));
        spDificultad.setSelection(valoresDificultad.indexOf(""+datosJuegos.get(posicion).getDificultad()));
        final EditText etNombre, etInformacion;
        final RadioButton rbSi;
        etNombre = (EditText)vista.findViewById(R.id.nuevoNombre);
        etInformacion = (EditText)vista.findViewById(R.id.nuevoInfo);
        rbSi = (RadioButton)vista.findViewById(R.id.rbSi);

        etNombre.setText(datosJuegos.get(posicion).getNombre());
        etInformacion.setText(datosJuegos.get(posicion).getInformacion());

        if(datosJuegos.get(posicion).isExpansion()){
            rbSi.setChecked(true);
        }else{
            rbSi.setChecked(false);
        }
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                String nombre = etNombre.getText().toString();
                int publicacion = Integer.valueOf(spPublicacion.getSelectedItem().toString());
                int puntuacion = Integer.valueOf(spPuntuacion.getSelectedItem().toString());
                int dificultad = Integer.valueOf(spDificultad.getSelectedItem().toString());
                String tipo = spTipo.getSelectedItem().toString();
                String informacion = etInformacion.getText().toString();
                boolean expansion = rbSi.isChecked();

                datosJuegos.set(posicion, new Juego(nombre, publicacion, informacion, tipo, null, dificultad, puntuacion, expansion));
                actualizarLista();
                tostada(getString(R.string.elemento_modificado));
            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();

        return true;
    }

    /*Método para borrar un juego. pide confirmación*/
    private boolean borrarJuego(final int posicion){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.borrar_juego));
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_borrar, null);
        alert.setView(vista);
        final String nombre = datosJuegos.get(posicion).getNombre() + " (" + datosJuegos.get(posicion).getPublicacion() + ")";
        TextView texto = (TextView)vista.findViewById(R.id.tvBorrar);
        texto.setText(getString(R.string.seguro) + " " + nombre + "?");
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                datosJuegos.remove(posicion);
                actualizarLista();
                tostada(getString(R.string.elemento_borrado) + " " + nombre);
            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/
    /*                                        Clic                                                */
    /**********************************************************************************************/
    /**********************************************************************************************/

    /*Método para mostrar los datos del juego en un diálogo al hacer click en el item de la lista.
    * Muestra la foto según el tipo de juego*/
    public void mostrarDatos(Juego juego){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(juego.getNombre());
        LayoutInflater inflater = LayoutInflater.from(this);
        View vista = inflater.inflate(R.layout.dialogo_mostrar, null);
        alert.setView(vista);

        TextView expansion = (TextView)vista.findViewById(R.id.mostrarExpansion);
        TextView publicacion =(TextView)vista.findViewById(R.id.mostarPublicacion);
        TextView dificultad =(TextView)vista.findViewById(R.id.mostrarDificultad);
        TextView informacion =(TextView)vista.findViewById(R.id.mostrarInfo);
        TextView tipo =(TextView)vista.findViewById(R.id.mostrarTipo);
        TextView puntuacion =(TextView)vista.findViewById(R.id.mostrarPuntuacion);
        ImageView foto = (ImageView)vista.findViewById(R.id.imageMostrar);

        publicacion.setText(String.valueOf(juego.getPublicacion()));
        puntuacion.setText(String.valueOf(juego.getPuntuacion()));
        tipo.setText(juego.getTipo());
        dificultad.setText(String.valueOf(juego.getDificultad()));
        informacion.setText(juego.getInformacion());
        if(juego.isExpansion()){
            expansion.setText(getString(R.string.si));
        }else{
            expansion.setText(getString(R.string.no));
        }
        if(juego.getFoto()!=null) {
            foto.setImageBitmap(juego.getFoto());
        }else{
            String tipo2 = juego.getTipo();
            int imagen = conseguirImagen(this, tipo2);
            foto.setImageResource(imagen);
        }
        alert.show();
    }

    /* Método que mostrará la foto en grande con el con una descripción del tipo de juego.*/
    public void fotoGrande(View v){
        int position = (Integer) v.getTag();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String tipo = datosJuegos.get(position).getTipo();
        alert.setTitle(tipo);
        LayoutInflater inflater = LayoutInflater.from(this);
        View vista = inflater.inflate(R.layout.dialogo_foto, null);
        alert.setView(vista);
        ImageView iv = (ImageView)vista.findViewById(R.id.soloTipo);
        TextView tv = (TextView)vista.findViewById(R.id.tipoTexto);

        iv.setImageResource(conseguirImagen(this, tipo));
        tv.setText(conseguirTexto(tipo));
        alert.show();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/
    /*                                   Clases                                                   */
    /**********************************************************************************************/
    /**********************************************************************************************/

    /*Clases comparator para ordenar los items de la lista por diferentes campos*/
    private class JuegosPorPuntuacion implements Comparator<Juego> {
        public int compare(Juego o1, Juego o2) {
            int neg = o2.getPuntuacion() - o1.getPuntuacion();
            if(neg == 0){
                return o1.compareTo(o2);
            }
            return neg;
        }
    }

    private class JuegosPorTipo implements Comparator<Juego> {
        public int compare(Juego o1, Juego o2) {
            int neg = o1.getTipo().compareTo(o2.getTipo());
            if(neg == 0){
                return o1.compareTo(o2);
            }
            return neg;
        }
    }
}
