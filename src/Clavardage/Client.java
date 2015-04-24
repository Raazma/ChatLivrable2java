package Clavardage;

/**
 * Created by 201037629 on 2015-04-20.
 */
import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class Client  {

    Socket socket;

    public Client() {

        //  Connexion conn = new Connexion(socket);

    }

    private static void creerEtAfficherIug() {
        JFrame frame = new JFrame("Client de clavardage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = frame.getContentPane();
        Panneau panneau = new Panneau();
        c.add(panneau);
        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                creerEtAfficherIug();
            }
        });
    }


    }



