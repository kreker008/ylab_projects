import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Sorter{
    private static final int CHUNK_SIZE = 3;

    public  File sortFile(File dataFile) throws IOException {
        List<File> tempFiles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            List<Long> chunk = new ArrayList<>(CHUNK_SIZE);
            String line;
            while ((line = reader.readLine()) != null) {
                chunk.add(Long.parseLong(line));
                if (chunk.size() == CHUNK_SIZE) {
                    Collections.sort(chunk);
                    File tempFile = File.createTempFile("file" + tempFiles.size(), ".txt");
                    writeChunkToFile(chunk, tempFile);
                    tempFiles.add(tempFile);
                    chunk.clear();
                }
            }
            if (!chunk.isEmpty()) {
                Collections.sort(chunk);
                File tempFile = File.createTempFile("file" + tempFiles.size(), ".txt");
                writeChunkToFile(chunk, tempFile);
                tempFiles.add(tempFile);
            }
        }

        return mergeSortedFiles(tempFiles);
    }

    private  void writeChunkToFile(List<Long> chunk, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Long number : chunk) {
                writer.write(number.toString());
                writer.newLine();
            }
        }
    }

    private  File mergeSortedFiles(List<File> fileList) throws IOException {
        List<File> tempList = new ArrayList<>();
        while (fileList.size()/2 > 0){
            int i = 0;
            for(; i + 1 < fileList.size(); i+=2){
                tempList.add(mergeTwoFiles(fileList.get(i), fileList.get(i + 1)));
            }
            if(i + 1 == fileList.size())
                tempList.add(fileList.get(i));
            fileList.clear();
            fileList.addAll(tempList);
            tempList.clear();
        }
        return fileList.get(0);
    }

    private  File mergeTwoFiles(File file1, File file2) throws IOException {
        File mergedFile = File.createTempFile("result", ".txt");
        try(Scanner scanner1 = new Scanner(new FileReader(file1));
            Scanner scanner2 = new Scanner(new FileReader(file2));
            BufferedWriter writer = new BufferedWriter(new FileWriter(mergedFile))){
            long longFile1 = scanner1.nextLong();
            long longFile2 = scanner2.nextLong();
            while (scanner1.hasNextLong() || scanner2.hasNextLong()) {

                if(scanner1.hasNextLong() && scanner2.hasNextLong()){
                    if (longFile1 <= longFile2){
                        writer.write(Long.toString(longFile1));
                        writer.newLine();
                        longFile1 = scanner1.nextLong();
                    }
                    else {
                        writer.write(Long.toString(longFile2));
                        writer.newLine();
                        longFile2 = scanner1.nextLong();
                    }
                }else if (scanner1.hasNextLong()){
                    longFile1 = scanner1.nextLong();
                    writer.write(Long.toString(longFile1));
                    writer.newLine();
                }else if (scanner2.hasNextLong()){
                    longFile2 = scanner2.nextLong();
                    writer.write(Long.toString(longFile2));
                    writer.newLine();
                }
            }
            file1.delete();
            file2.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mergedFile;
    }

}