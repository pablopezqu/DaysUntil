package es.upm.dit.adsw.daysuntil;

import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Calendar today=Calendar.getInstance();
    final Calendar anotherDay= new GregorianCalendar();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView diaHoy=(TextView) findViewById(R.id.diaHoy);
        diaHoy.setText(getResources().getString(R.string.dia_hoy) + today.get(Calendar.DAY_OF_MONTH) + "/" + (today.get(Calendar.MONTH) + 1) + "/" + today.get(Calendar.YEAR));
        EditText seleccionFecha=(EditText) findViewById(R.id.fechaEvento);
        seleccionFecha.setEnabled(false);
    }

    public void sacarCalendario(View v){
        Button seleccionFecha=(Button) findViewById(R.id.sacaCalendario);
        final EditText fecha=(EditText) findViewById(R.id.fechaEvento);
        final DatePickerDialog.OnDateSetListener date=
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        anotherDay.set(year,month,day);
                        fecha.setText(anotherDay.get(Calendar.DAY_OF_MONTH)+"/"+(anotherDay.get(Calendar.MONTH)+1)+"/"+anotherDay.get(Calendar.YEAR));
                        fecha.setEnabled(false);
                    }
                };

        seleccionFecha.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                new DatePickerDialog(MainActivity.this,date,anotherDay.get(Calendar.YEAR),anotherDay.get(Calendar.MONTH),anotherDay.get(Calendar.DAY_OF_MONTH)).show();
            }

        });
    }

    public void compartir(View view){
        TextView textoResultado=(TextView) findViewById(R.id.resultado);
        if(textoResultado.getText().toString().equals("")){
            Toast.makeText(this,R.string.toast_compartir,Toast.LENGTH_LONG).show();
        }
        else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, textoResultado.getText().toString());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }

    public void calcula(View view){
        int tiempo=(int) TimeUnit.MILLISECONDS.toDays(anotherDay.getTimeInMillis() - today.getTimeInMillis());
        TextView resultado=(TextView) findViewById(R.id.resultado);
        EditText nombreEvento=(EditText) findViewById(R.id.nombreEvento);
        EditText seleccionFecha=(EditText) findViewById(R.id.fechaEvento);
        if (seleccionFecha.getText().toString().equals("")||nombreEvento.getText().toString().equals("")){
            Toast.makeText(this, R.string.toast_fecha, Toast.LENGTH_SHORT).show();
            return;
        }else if(tiempo<0){
            resultado.setText(getResources().getString(R.string.str_evento) +" '"+nombreEvento.getText()+"' "+ getResources().getString(R.string.str_pasado));
            return;
        } else if (tiempo==0){
            resultado.setText(getResources().getString(R.string.str_eventoex)+" '"+nombreEvento.getText()+"' " +getResources().getString(R.string.str_hoy));
            return;
        } else if(tiempo==1){
            resultado.setText(getResources().getString(R.string.str_evento) +" '"+nombreEvento.getText()+"' "+getResources().getString(R.string.str_mañana));
            return;
        } else if(tiempo>1){
            resultado.setText(getResources().getString(R.string.str_quedan) + tiempo +getResources().getString(R.string.str_diaspara) + " '" + nombreEvento.getText()+"'");
            return;
        }
    }

    public void guardaEvento(View v){
        EditText seleccionFecha=(EditText) findViewById(R.id.fechaEvento);
        EditText nombreEvento=(EditText) findViewById(R.id.nombreEvento);
        if(seleccionFecha.getText().toString().equals("")||nombreEvento.getText().toString().equals("")){
            Toast.makeText(this, R.string.toast_fecha, Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            int añoEv=anotherDay.get(Calendar.YEAR);
            int mesEv=anotherDay.get(Calendar.MONTH);
            int diaEv=anotherDay.get(Calendar.DAY_OF_MONTH);
            Calendar inicio = new GregorianCalendar();
            inicio.set(añoEv, mesEv , diaEv, 7, 30);
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, inicio.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, nombreEvento.getText().toString())
                    .putExtra(CalendarContract.Events.DESCRIPTION, "")
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, "")
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                    .putExtra(Intent.EXTRA_EMAIL, "");
            startActivity(intent);
        }
    }
}
