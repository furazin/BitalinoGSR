package com.furazin.android.mbandgsr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.furazin.android.mbandgsr.Dialog.DateDialog;
import com.furazin.android.mbandgsr.FirebaseBD.Experiencia;
import com.furazin.android.mbandgsr.FirebaseBD.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by manza on 11/05/2017.
 */

public class Formulario extends AppCompatActivity{

    public enum Sexo {
        MASCULINO,
        FEMENINO
    }

    public enum OpcionMultimedia {
        SOLO_VIDEO,
        SOLO_AUDIO,
        AUDIO_Y_VIDEO,
        NO_MULTIMEDIA
    }

    public static EditText txtDate;
    EditText edit_nombre, edit_apellidos, edit_fecha_nacimiento, edit_descripcion;
//    RadioButton radio_sexo_masculino, radio_sexo_femenino;
//    RadioButton radio_video, radio_audio, radio_video_audio, radio_ninguno;
    RadioGroup radio_sexo;
    RadioGroup radio_opcion_multimedia;

    // Variable para recordar las credenciales del usuario
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        // Instanciamos una referencia al Contexto
        Context context = this.getApplicationContext();
        //Instanciamos el objeto SharedPrefere  nces y creamos un fichero Privado bajo el
        //nombre definido con la clave preference_file_key en el fichero string.xml
        sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        txtDate = (EditText)findViewById(R.id.editTextFecha);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    DateDialog dialog = new DateDialog();// = new DateDialog(view);
                    android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });

        ImageButton dateIcon = (ImageButton) findViewById(R.id.dateButton);
        dateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog dialog = new DateDialog(); //= new DateDialog(view);
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });

        edit_nombre = (EditText) findViewById(R.id.editTextNombre);
        edit_apellidos = (EditText) findViewById(R.id.editTextApellidos);
        edit_fecha_nacimiento = (EditText) findViewById(R.id.editTextFecha);
        edit_descripcion = (EditText) findViewById(R.id.text_descripcion);

//        radio_sexo_femenino = (RadioButton) findViewById(R.id.radioButton1) ;
//        radio_sexo_masculino = (RadioButton) findViewById(R.id.radioButton2) ;
//
//        radio_video = (RadioButton) findViewById(R.id.radioVideo);
//        radio_audio = (RadioButton) findViewById(R.id.radioAudio);
//        radio_video_audio = (RadioButton) findViewById(R.id.radioAudioYVideo);
//        radio_ninguno = (RadioButton) findViewById(R.id.radioNoMultimedia);

        radio_sexo = (RadioGroup) findViewById(R.id.radioGenero);
        radio_opcion_multimedia = (RadioGroup) findViewById(R.id.radioMultimedia);

        Button ButtonInicio = (Button) findViewById(R.id.InicioPrueba_Button);
        ButtonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(edit_nombre) && !isEmpty(edit_apellidos) && !isEmpty(edit_fecha_nacimiento) && !isEmpty(edit_descripcion)) {
                    PushFirebase();
                    Intent i = new Intent(getApplicationContext(), DatosGSR.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(), "No puede estar ningún campo del formulario vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void PushFirebase() {

        // Obtenemos email del usuario que se ha logueado
        final String email = sharedPref.getString((getString(R.string.email_key)), "");

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");
//        myRef.push().setValue(email);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario user = snapshot.getValue(Usuario.class);
                    if (user.getEmail().equals(email)) {
                        // Obtenemos la key del usuario logueado
                        String key = snapshot.getKey();
                        // Creamos una experiencia con los datos del formulario para ser almacenada en la base de datos en firabase
                        Experiencia experiencia = ExperienciaFormulario();
                        // Añadimos la informacion del formulario, y en la bd se creara una entrada con la fecha y hora actuales
                        myRef.child(key).child("Experiencias").child(getFechaYHora()).setValue(experiencia);
                    }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    Experiencia ExperienciaFormulario() {
        String nombre = edit_nombre.getText().toString();
        String apellidos = edit_apellidos.getText().toString();
        String fecha_nacimiento = edit_fecha_nacimiento.getText().toString();
        String sexo = ((RadioButton)findViewById(radio_sexo.getCheckedRadioButtonId())).getText().toString();

        String opcion_multimedia = ((RadioButton)findViewById(radio_opcion_multimedia.getCheckedRadioButtonId())).getText().toString();

        String descripcion = edit_descripcion.getText().toString();

        return new Experiencia(nombre,apellidos,fecha_nacimiento, sexo,opcion_multimedia,descripcion);
    }

    String getFechaYHora() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = mdformat.format(calendar.getTime());

        SimpleDateFormat format = new SimpleDateFormat("HHmm", Locale.US);
        String hour = format.format(new Date());

        currentDate+=hour;

        return currentDate;
    }

}
