package ru.serega6531.ctf;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    @SuppressWarnings("StringConcatenationInLoop")
    public static void main(String[] args) throws IOException {
        File db = new File("db.txt");

        List<Record> records = new ArrayList<>();

        Files.lines(db.toPath())
                .skip(1) //header
                .map(line -> {
                    String[] spl = new String[9];
                    String s = "";
                    int n = 0;

                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);

                        if (c == '\t' || c == ' ') {
                            if (!s.isEmpty()) {
                                spl[n++] = s;
                                s = "";
                            }
                        } else {
                            s += c;
                        }
                    }

                    if (!s.isEmpty()) {
                        spl[n] = s;
                    }

                    return spl;
                })
                .forEach(spl ->
                        records.add(new Record(Integer.valueOf(spl[0]), Integer.valueOf(spl[1]), Integer.valueOf(spl[2]),
                                Long.valueOf(spl[3]), Long.valueOf(spl[4]), Double.valueOf(spl[5]), Double.valueOf(spl[6]),
                                Double.valueOf(spl[7]), Double.valueOf(spl[8]))));

        Socket socket = new Socket("c1.easyctf.com", 12483);

        InputStream sin = socket.getInputStream();
        OutputStream sout = socket.getOutputStream();

        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);

        String line = null;

        for (int i = 0; i < 9; i++) {
            line = in.readLine();
            System.out.println(line);
        }

        while (true) {
            System.out.println(in.readLine());

            char c = ' ';
            while (c != '?' && c != '\uFFFF') {
                c = (char) in.read();
                line += c;
            }
            in.skipBytes(1);
            System.out.println(line);

            int start = line.lastIndexOf(' ') + 1;
            int end = line.length() - 1;
            Integer zip = Integer.valueOf(line.substring(start, end));

            if (line.contains("water")) {
                Optional<Record> record = records.stream()
                        .filter(r -> r.getGeoid() == zip)
                        .findAny();

                line = record.map(record1 -> String.valueOf(record1.getWater())).orElse("???");
            } else if (line.contains("land")) {
                Optional<Record> record = records.stream()
                        .filter(r -> r.getGeoid() == zip)
                        .findAny();

                line = record.map(record1 -> String.valueOf(record1.getLand())).orElse("???");
            } else if (line.contains("longitude")) {
                Optional<Record> record = records.stream()
                        .filter(r -> r.getGeoid() == zip)
                        .findAny();

                line = record.map(record1 -> String.valueOf(record1.getLongitude())).orElse("???");
            } else if (line.contains("latitude")) {
                Optional<Record> record = records.stream()
                        .filter(r -> r.getGeoid() == zip)
                        .findAny();

                line = record.map(record1 -> String.valueOf(record1.getLatitude())).orElse("???");
            } else {
                line = "???";
            }

            System.out.println(">" + line);
            out.writeBytes(line + '\n');
            out.flush();

            in.readLine();
            line = in.readLine();
            System.out.println(line);

            if (line.contains("incorrect") || line.contains("out of time")) {
                socket.close();
                return;
            }

            in.skipBytes(1);

            line = "";
        }
    }
}
