package Clavardage;

/**
 * Created by Razma on 2015-02-22.
 */

import  java.io.*;
import java.net.*;

public class Connexion implements Runnable
{
    //Global Variable
    public  Socket socket = null;
    public BufferedReader reader;
    public String line = null;
    public String userName = null;
    final int TAILLE_MAX = 80;
    final int USER_MAX = 8;
    private boolean end = false;
    static int Nb_Connection = 0;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public Connexion(Socket lesocket)
    {
        socket = lesocket;
        Nb_Connection++;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void run()
    {
        try {

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

          //  userName = reader.readLine();
           // ConfirmUserName();
            line = userName + " viens de joindre la conversation";
            while(!end)
            {
               // line = reader.readLine();
                if(!line.isEmpty())
                    line = userName + ": " +  ConfirmLine(line);
                else
                {
                    end = true;
                }
            }
        }
        catch(SocketTimeoutException e)
        {
            line = "";
        }
        catch (IOException e)
        {

        }
        catch(NullPointerException e)
        {
            line = "";
        }
        finally
        {
            try
            {
                reader.close();
                socket.close();
            }
            catch(IOException e)
            {

            }
            Nb_Connection--;
        }

        System.out.println("Client donnecte");
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void ConfirmUserName()
    {
        if(userName.length() >  USER_MAX)
            userName = userName.substring(0, USER_MAX);
        else if(userName.isEmpty())
            userName = socket.getInetAddress().getHostAddress();

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String ConfirmLine(String line)
    {
        if(line.length() > TAILLE_MAX)
            return line.substring(0, TAILLE_MAX);
        else
            return line;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int GetNbConnection()
    {
        return Nb_Connection;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////
}
