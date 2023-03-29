import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sorter{
    private static final int CHUNK_SIZE = 100;

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
        try(BufferedReader bufferedReader1 = new BufferedReader(new FileReader(file1));
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file2));
            BufferedWriter writer = new BufferedWriter(new FileWriter(mergedFile))){
            String line1 = bufferedReader1.readLine();
            String line2 = bufferedReader2.readLine();
            boolean stopRead1 = false;
            boolean stopRead2 = false;
            while (!(stopRead1 && stopRead2)){
                if(!(stopRead1 || stopRead2)) {
                    if (line1.equals(line2)) {
                        writer.write(line1);
                        writer.write(line2);
                        line1 = bufferedReader1.readLine();
                        if (line1 == null) {
                            stopRead1 = true;
                        }
                        line2 = bufferedReader2.readLine();
                        if (line2 == null) {
                            stopRead2 = true;
                        }
                    } else if (Long.parseLong(line1) < Long.parseLong(line2)) {
                        writer.write(line1);
                        writer.newLine();
                        line1 = bufferedReader1.readLine();
                        if (line1 == null) {
                            stopRead1 = true;
                        }
                    } else {
                        writer.write(line2);
                        writer.newLine();
                        line2 = bufferedReader2.readLine();
                        if (line2 == null) {
                            stopRead2 = true;
                        }
                    }
                } else if(!stopRead1){
                    writer.write(line1);
                    writer.newLine();
                    line1 = bufferedReader1.readLine();
                    if (line1 == null) {
                        stopRead1 = true;
                    }
                } else if (!stopRead2){
                    writer.write(line2);
                    writer.newLine();
                    line2 = bufferedReader2.readLine();
                    if (line2 == null) {
                        stopRead2 = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        file1.delete();
        file2.delete();
        return mergedFile;
    }
}
