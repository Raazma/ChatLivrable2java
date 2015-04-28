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
    PrintWriter writer = null;
    boolean Connected = false;

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
                if(!fieldAdresse.getText().isEmpty() && !fieldPseudo.getText().isEmpty()) {
                    Connection();
                    StartWorker();
                }
                else
                {
                    JOptionPane.showMessageDialog(pan0, "Veuiller entrer une adresse et un pseudo", "Attention!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton boutonQuitter = new JButton("Quitter");
        boutonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Connected)
                    writer.println("");

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
           if(Connected){
             writer.println("");
               writer.flush();
               Connected = false;

           }
           else {
               socket = new Socket();
               socket.connect(socketAdress);
               conn = new Connexion(socket);
               conn.userName = fieldPseudo.getText();
               boutonConnexion.enableInputMethods(true);
               writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
               writer.println(fieldPseudo.getText());
               writer.flush();
               boutonConnexion.setText("Deconnexion");
               Connected = true;
           }

       }
       catch (IOException u) {
           System.out.println("Problem whileee Conecctinggg");
       }
   }
    private void StartWorker(){
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {

            @Override
            protected Boolean doInBackground()throws Exception {
                boolean end = false;
                String line = new String();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        while(!end) {
                      line =  reader.readLine();
                            if(!line.isEmpty()) {
                                if (line != null )

                                    publish(line);
                                if(cboxResterConnecte.isSelected()) {
                                    Thread.sleep(100);
                                    writer.println("   ");
                                    writer.flush();
                                }
                            }
                            else{System.exit(1);}

                        }

                }
                catch (IOException e){
                   System.out.println("Erreur lors de la lecture");
                }
                return true;
            }
            @Override
            protected void process(List<String> chunks) {

                if(!(chunks.get(chunks.size()-1).trim().isEmpty()))
                zoneMessages.append(chunks.get(chunks.size()-1) +"\n" );
            }
            protected void done() {
                System.exit(1);
            }
        };
        worker.execute();

    }
    private void Ecrire(){

           if(Connected) {
               writer.println(fieldTexte.getText());
               System.out.println(fieldTexte.getText());
               writer.flush();
               fieldTexte.setText("");
           }
    }

}