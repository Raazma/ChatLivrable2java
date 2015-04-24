package Clavardage;

/**
 * Created by Razma on 2015-02-19.
 */
import java.io.*;

public class Terminateur implements Runnable
{
    public String str= new String();
    public void run()
    {

        BufferedReader reader;
        reader = new BufferedReader(
                new InputStreamReader( System.in ) );


        while(!str.toLowerCase().trim().equals("q"))
        {
            try
            {
                str = reader.readLine();
            }
            catch( IOException e )
            {
                System.err.println( e );

            }

        }
    }
}