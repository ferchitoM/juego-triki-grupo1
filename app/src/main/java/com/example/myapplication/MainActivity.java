package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[][] tablero = new String[3][3]; //fila, col
    boolean turno = false; //true: X, false: O
    String jugador = "";
    Button botonIniciarPartida;

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

            }
        });

    }

    private void inicializarTablero() {
        for (int fila = 0; fila <= 2; fila++) {
            for (int col = 0; col <= 2; col++) {
                tablero[fila][col] = "_";
            }
        }
    }

    private void mostrarTablero() {
        for (int fila = 0; fila <= 2; fila++) {
            for (int col = 0; col <= 2; col++) {
                System.out.print(tablero[fila][col] + " ");
            }
            System.out.println();
        }
    }

    private void jugada(int fila, int columna) {
        turno = !turno; //invertimos el valor de turno

        if (turno) jugador = "X";
        else jugador = "O";

        tablero[fila][columna] = jugador;

    }

    private void inicializarBotones() {

        int idBoton;
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {

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

                final int f = fila;
                final int c = col;
                boton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (boton.getText().toString().isEmpty()) {
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

        String valor;
        int contX = 0;
        int contO = 0;
        String ganador = "";

        //buscar 3 en linea en las filas
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {

                valor = tablero[fila][col];
                if (!valor.isEmpty()) {
                    if (valor == "X") contX++;
                    if (valor == "O") contO++;
                }

                if (contX == 3) ganador = "X";
                if (contO == 3) ganador = "O";
            }
            contX = 0;
            contO = 0;
        }

        if (ganador == "") {
            //buscar 3 en linea en las columnas
            for (int col = 0; col < 3; col++) {
                for (int fila = 0; fila < 3; fila++) {

                    valor = tablero[fila][col];
                    if (!valor.isEmpty()) {
                        if (valor == "X") contX++;
                        if (valor == "O") contO++;
                    }

                    if (contX == 3) ganador = "X";
                    if (contO == 3) ganador = "O";
                }
                contX = 0;
                contO = 0;
            }
        }

        if (ganador == "") {
            //buscar 3 en línea en la diagonal 1
            int fila;
            for (int col = 0; col < 3; col++) {
                fila = col;

                valor = tablero[fila][col];
                if (!valor.isEmpty()) {
                    if (valor == "X") contX++;
                    if (valor == "O") contO++;
                }

                if (contX == 3) ganador = "X";
                if (contO == 3) ganador = "O";

            }
            contX = 0;
            contO = 0;
        }

        if (ganador == "") {

            //buscar 3 en línea en la diagonal 2
            int fila = 0;
            for (int col = 2; col >= 0; col--) {

                valor = tablero[fila][col];
                if (!valor.isEmpty()) {
                    if (valor == "X") contX++;
                    if (valor == "O") contO++;
                }

                if (contX == 3) ganador = "X";
                if (contO == 3) ganador = "O";

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
        }
    }

}