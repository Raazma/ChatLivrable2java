package Clavardage;

/**
 * Created by 201037629 on 2015-04-20.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class Panneau extends JPanel {
    Connexion conn;
    Socket socket;
   InetSocketAddress socketAdress = null;
    final JLabel labelAdresse;
    final JTextField fieldAdresse;
    JLabel labelPseudo;
    final JTextField fieldPseudo;
    JCheckBox cboxResterConnecte;
    final JTextArea zoneMessages;
    JPanel pan0;
    JTextField fieldTexte;
    JButton boutonConnexion;

    public Panneau() {
        setLayout(new GridLayout(0, 1)); // une seule colonne

        // rangée 0
         labelAdresse = new JLabel("Adresse IP");
        fieldAdresse = new JTextField(16);
        labelPseudo = new JLabel("Pseudo");
        fieldPseudo = new JTextField(16);
        cboxResterConnecte = new JCheckBox("Rester connecté");
        pan0 = new JPanel();
        add(pan0);
        pan0.add(labelAdresse);
        pan0.add(fieldAdresse);
        pan0.add(labelPseudo);
        pan0.add(fieldPseudo);
        pan0.add(cboxResterConnecte);

        // rangée 1
       zoneMessages = new JTextArea(20,40);
        JScrollPane zoneDefilement = new JScrollPane(zoneMessages);
        JPanel pan1 = new JPanel();
        add(pan0);
        pan1.add(zoneDefilement);
        add(pan1);

        // rangée 2
        JLabel labelTexte = new JLabel("Votre texte");
        fieldTexte = new JTextField(40);
        JButton boutonEnvoyer = new JButton("Envoyer");
        JPanel pan2 = new JPanel();
        pan2.add(labelTexte);
        pan2.add(fieldTexte);
        pan2.add(boutonEnvoyer);
        add(pan2);

        // rangée 3
        boutonConnexion  = new JButton("Connecter");
        boutonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection();
                StartWorker();
            }
        });

        JButton boutonQuitter = new JButton("Quitter");
        boutonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        boutonEnvoyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ecrire();
            }
        });
        JPanel pan3 = new JPanel();
        pan3.add(boutonConnexion);
        pan3.add(boutonQuitter);
        add(pan3);
    }

   private void Lecture(){

        while(true)
        {

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            catch (IOException e){}
        }

    }

   private void Connection(){

       socketAdress = new InetSocketAddress(fieldAdresse.getText(),50000);
       try {
           socket = new Socket();
           socket.connect(socketAdress);
           conn = new Connexion(socket);
           conn.userName = fieldPseudo.getText();
          boutonConnexion.enableInputMethods(true);
       }
       catch (IOException u) {
           System.out.print(u);
       }
   }
    private void StartWorker(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {

            @Override
            protected Boolean doInBackground()throws Exception {
                boolean end = false;
                try {
                        while(!end) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            publish(reader.readLine());
                        }

                }
                catch (IOException e){
                    System.out.print("Erreur lors de la lecture");
                }
                return true;
            }
            @Override
            protected void process(List<String> chunks) {

                zoneMessages.setText(chunks.get(chunks.size()-1));
            }
            protected void done() {
                zoneMessages.setText("yolo");
            }
        };
        worker.execute();

    }
    private void Ecrire(){

        try {
            System.out.print("Je sendd !!!");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.print(conn.line = fieldTexte.getText());
            System.out.print(fieldTexte.getText());
        }
        catch (IOException e){}

    }

}