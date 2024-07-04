package com.example.myapplication;

import static java.lang.Thread.sleep;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    boolean turno = false; //true: X, false: O
    String jugador = "";
    String ganador = "";
    Button botonIniciarPartida;
    int contX = 0;
    int contO = 0;
    int t = 3; // tamaño del tablero = 3x3
    String[][] tablero = new String[t][t]; //fila, col

    //col:      0   1   2
    //fila: 0   X   O   O
    //fila: 1   O   X   O
    //fila: 2   O   O   X

    //jugadas ganadoras
    //00 - 01 - 02
    //10 - 11 - 12
    //20 - 21 - 22

    //00 - 10 - 20
    //01 - 11 - 21
    //02 - 12 - 22

    //00 - 11 - 22
    //02 - 11 - 02

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonIniciarPartida = (Button) findViewById(R.id.iniciarPartida);
        botonIniciarPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inicializarTablero();
                inicializarBotones();

                cargarBarraProgreso();

            }
        });

    }

    private void cargarBarraProgreso() {

        ProgressBar barra = findViewById(R.id.progressBar);


        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        int progreso = 0;
                        while (progreso <= 100) {
                            barra.setProgress(progreso);
                            Log.e("msg", progreso + "%");

                            progreso++;

                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        /*do{
                            barra.setProgress(progreso);
                            Log.e("msg", progreso + "%");

                            progreso++;

                            try {
                                sleep(50);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } while(progreso <= 100);*/

                    }
                }
        ).start();

    }

    private void inicializarTablero() {
        for (int fila = 0; fila < t; fila++) {
            for (int col = 0; col < t; col++) {
                tablero[fila][col] = "_";
            }
        }
    }

    private void mostrarTablero() {
        for (int fila = 0; fila < t; fila++) {
            for (int col = 0; col < t; col++) {
                System.out.print(tablero[fila][col] + " ");
            }
            System.out.println();
        }
    }

    private void jugada(int fila, int columna) {
        turno = !turno; //invertimos el valor de turno

        if (turno)
            jugador = "X";
        else
            jugador = "O";

        tablero[fila][columna] = jugador;

    }

    private void inicializarBotones() {

        int idBoton;
        for (int fila = 0; fila < t; fila++) {
            for (int col = 0; col < t; col++) {

                idBoton = getResources()
                        .getIdentifier(
                                "boton" + fila + col,
                                "id",
                                getPackageName()
                        );

                Button boton = (Button) findViewById(idBoton);
                boton.setText("");
                boton.setEnabled(true);

                //Establecemos algunos valores al inicio de la partida
                botonIniciarPartida.setText("Iniciar partida");
                turno = false;
                jugador = "";
                ganador = "";
                contX = 0;
                contX = 0;

                final int f = fila;
                final int c = col;
                boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (boton.getText().toString().isEmpty() && ganador.isEmpty()) {
                            jugada(f, c);
                            mostrarTablero();

                            boton.setText(jugador);
                            botonIniciarPartida.setText("Reiniciar partida");
                            // boton.setEnabled(false);

                            buscarGanador();
                        }

                    }
                });
            }
        }
    }

    private void buscarGanador() {

        //buscar 3 en linea en las filas
        for (int fila = 0; fila < t; fila++) {
            for (int col = 0; col < t; col++) {
                validarJugada(fila, col);
            }
            if (!ganador.isEmpty())
                break;
            contX = 0;
            contO = 0;
        }

        if (ganador == "") {
            //buscar 3 en linea en las columnas
            for (int col = 0; col < t; col++) {
                for (int fila = 0; fila < t; fila++) {
                    validarJugada(fila, col);
                }
                if (!ganador.isEmpty())
                    break;
                contX = 0;
                contO = 0;
            }
        }

        if (ganador == "") {
            //buscar 3 en línea en la diagonal 1
            int fila;
            for (int col = 0; col < t; col++) {
                fila = col;
                validarJugada(fila, col);
            }
            contX = 0;
            contO = 0;
        }

        if (ganador == "") {
            //buscar 3 en línea en la diagonal 2
            int fila = 0;
            for (int col = t - 1; col >= 0; col--) {
                validarJugada(fila, col);
                fila++;
            }
            contX = 0;
            contO = 0;
        }

        if (ganador != "") {
            Toast.makeText(
                    getApplicationContext(),
                    "El ganador es: " + ganador,
                    Toast.LENGTH_LONG
            ).show();
            //ganador = "";
        }
    }

    private void validarJugada(int fila, int col) {
        String valor = tablero[fila][col];

        if (!valor.isEmpty()) {
            if (valor == "X")
                contX++;
            if (valor == "O")
                contO++;
        }

        if (contX == t)
            ganador = "X";
        if (contO == t)
            ganador = "O";
    }

}