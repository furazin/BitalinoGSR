package com.furazin.android.mbandgsr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.furazin.android.mbandgsr.FirebaseBD.Experiencia;
import com.furazin.android.mbandgsr.FirebaseBD.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by manza on 12/06/2017.
 */

public class InfoSujeto extends AppCompatActivity {

    public static String id_usuario;
    String EMAIL_USUARIO = MainActivity.EMAIL_USUARIO;
    TextView fecha, nombre, apellidos, sexo, fecha_nacimiento, descripcion, txtTerminada, txtTipoPrueba;
    Button btnStartExperiencia;

    // Variable para almacenar el tipo de prueba
    static public String tipoPrueba = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencia);

        fecha = (TextView) findViewById(R.id.fecha);
        nombre = (TextView) findViewById(R.id.nombre);
        apellidos = (TextView) findViewById(R.id.apellidos);
        sexo = (TextView) findViewById(R.id.sexo);
        fecha_nacimiento = (TextView) findViewById(R.id.fecha_nacimiento);
        descripcion = (TextView) findViewById(R.id.descripcion);
        btnStartExperiencia = (Button) findViewById(R.id.btnStartExperiencia);
        txtTerminada = (TextView) findViewById(R.id.experienciaTerminada);
        txtTipoPrueba = (TextView) findViewById(R.id.tipoPruebaInfoExperiencia);


        id_usuario = getIntent().getExtras().getString("id_usuario");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario user = snapshot.getValue(Usuario.class);
                    final String user_key;
                    if (user.getEmail().equals(EMAIL_USUARIO)) {
                        // Obtenemos la key del usuario logueado
                        user_key = snapshot.getKey();
                        myRef.child(user_key).child("Experiencias").child(UsuariosExperiencia.NOMBRE_EXPERIENCIA).child(id_usuario).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Experiencia e = dataSnapshot.getValue(Experiencia.class);
//                                System.out.println(e.getNombre());
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar c = Calendar.getInstance();
                                String date = sdf.format(c.getTime());
                                fecha.setText(date);
                                nombre.setText(e.getNombre());
                                apellidos.setText( e.getApellidos());
                                sexo.setText(e.getSexo());
                                fecha_nacimiento.setText(e.getFecha_nacimiento());
                                descripcion.setText(e.getDescripcion());
                                txtTipoPrueba.setText(e.getOpcion_multimedia());
                                tipoPrueba = e.getOpcion_multimedia();

                                if (e.getTerminada().equals("si")) {
                                    txtTerminada.setVisibility(View.VISIBLE);
                                    btnStartExperiencia.setEnabled(false);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnStartExperiencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.activity.finish();
                Intent i = new Intent(getApplicationContext(), DatosGSR.class);
                i.putExtra("id_usuario",id_usuario);
                startActivity(i);
                finish();
            }
        });

    }
}