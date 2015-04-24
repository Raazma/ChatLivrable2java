package Clavardage;
/**
 * Created by Razma on 2015-02-22.
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server
{
    //Global Variable
    public  Socket client = null;
    public  ServerSocket server = null;
    public ArrayList<Connexion> activeCon = new ArrayList<Connexion>();
    public String line;
    final int NB_MAX_CON = 4;
    final int TIME_OUT = 300000000;
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public Server()
    {

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public void LaunchServer(int port)
    {
        try
        {
            Thread schwartzi = new Thread(new Terminateur());
            schwartzi.start();
            server = new ServerSocket(port);
            server.setSoTimeout(500);

            while(schwartzi.isAlive())
            {
                Connexion connexion ;
                try
                {
                    client = server.accept();
                    client.setSoTimeout(TIME_OUT);
                    connexion = new Connexion(client);

                    if(connexion.GetNbConnection() <=  NB_MAX_CON )
                    {
                        Thread tCon = new Thread(connexion);
                        tCon.setDaemon(true);
                        activeCon.add(connexion);
                        tCon.start();
                        WelcomeMessage();
                        System.out.println("Client connecter");
                    } else
                    {
                        System.out.println("Trop de Goblin.........");
                        try
                        {
                            client.close();
                        }
                        catch(IOException e)
                        {

                        }
                    }
                }
                catch(SocketTimeoutException  e)
                {

                }
                catch(NullPointerException e)
                {

                }
                finally
                {
                    Distribution();
                }
            }
            CloseConnection();
            server.close();
            System.out.println("Im Done im going to Africa!");
        }
        catch(IOException e)
        {

        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    private synchronized void Distribute(String msg)
    {
        try
        {
            int i = 0;
            String line = new String();
            //  line = reader.readLine();
            if(msg!=null)
            {
                while(activeCon.size() > i)
                {
                    client = activeCon.get(i).socket;
                    PrintWriter writer = new  PrintWriter( new OutputStreamWriter(client.getOutputStream()));
                    writer.println(msg);
                    writer.flush();
                    i++;
                }
            }
        }
        catch (IOException e)
        {

        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private void CloseConnection()
    {
        if(!activeCon.isEmpty())
            for(int i = 0; i < activeCon.size(); i++)
            {
                try
                {
                    client = activeCon.get(i).socket;
                    client.close();
                }
                catch(IOException e)
                {

                }
            }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    private synchronized void Distribution()
    {
        if(!activeCon.isEmpty())
        {
            for(int i = 0; i < activeCon.size(); i++)
            {
                line = activeCon.get(i).line;
                if(line != null)
                    if(!line.isEmpty())
                    {
                        System.out.print((line));
                        Distribute(line);
                        activeCon.get(i).line = null;
                    }
                    else
                    {
                        line = activeCon.get(i).userName + " A quitter la Conversation";
                        CloseOneConnection(i);
                        Distribute(line);
                    }
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void CloseOneConnection(int i)
    {
        client = activeCon.get(i).socket;
        activeCon.remove(i);
        try
        {
            client.close();
        }
        catch(IOException e)
        {

        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    private void WelcomeMessage()
    {
        try
        {
            PrintWriter writer = new  PrintWriter( new OutputStreamWriter(client.getOutputStream()));
            writer.println("Bienvenue Veuiller Entrer Votre Nom d'usager");
            writer.flush();
        }
        catch(IOException e)
        {

        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String args[] )
    {
        Server chat = new Server();
        chat.LaunchServer(50000);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////
}
