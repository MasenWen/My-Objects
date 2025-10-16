import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//
// FileManipulation.java: 我们选择使用txt文件作为存储文件
// 为了简洁起见我们只记录两个文件
// 第一个保存movies信息 并和数据库保有相同的列 用于查询实验
// 第二个保存people信息 并和数据库保有相同的列 用于update实验
// 我们在这个类里包装计时功能并直接打印打控制台
//

public class FileManipulation implements DataManipulation {

    @Override
    public void bustCache() {
        return;
    }

    @Override
    public int addOneMovie(String str) {
        try (FileWriter writer = new FileWriter("movies.txt", true)) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }


    @Override
    public String findMovieById(int id) {
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        try (FileReader fr = new FileReader("movies.txt");
             BufferedReader reader = new BufferedReader(fr)) {
            String line;
            String[] movie = new String[5];
            while ((line = reader.readLine()) != null) {
                movie = line.split(";");
                if (movie[0].equals(String.valueOf(id))) {
                    //System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown: " + e.getMessage());
            e.printStackTrace();
        }
        return "Runtime of this [File] findMovieById is " + timer.stop() + "ns";
    }


    @Override
    public String findMovieByTitleStrict(String title) {
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        try (FileReader fr = new FileReader("movies.txt");
             BufferedReader reader = new BufferedReader(fr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] movie = line.split(";");
                if (movie[1].equals(title)) {
                    //System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown: " + e.getMessage());
            e.printStackTrace();
        }
        return "Runtime of this [File] findMovieByTitleStrict is " + timer.stop() + "ns";
    }


    @Override
    public String findMovieByTitleLike(String like) {
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        try (FileReader fr = new FileReader("movies.txt");
             BufferedReader reader = new BufferedReader(fr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] movie = line.split(";");
                if (movie[1].contains(like)) {
                    //System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown: " + e.getMessage());
            e.printStackTrace();
        }
        return "Runtime of this [File] findMovieByTitleLike is " + timer.stop() + "ns";
    }


    @Override
    public String updatePeopleNamesTTOO() {
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();

        int totalLines = 0;
        int updatedLines = 0;
        List<String> updatedContent = new ArrayList<>();

        try (FileReader fr = new FileReader("people.txt");
             BufferedReader reader = new BufferedReader(fr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                String[] people = line.split(";");

                if (people.length > 1 && people[1].contains("to")) {
                    String originalName = people[1];
                    String updatedName = originalName.replace("to", "TTOO");
                    people[1] = updatedName;

                    // 重新构建行
                    String updatedLine = String.join(";", people);
                    updatedContent.add(updatedLine);
                    updatedLines++;

                    //System.out.println("Updated: " + originalName + " -> " + updatedName);
                } else {
                    updatedContent.add(line);
                }
            }

            // 将更新后的内容写回文件
            if (updatedLines > 0) {
                try (FileWriter fw = new FileWriter("people.txt");
                     BufferedWriter writer = new BufferedWriter(fw)) {
                    for (String contentLine : updatedContent) {
                        writer.write(contentLine);
                        writer.newLine();
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown: " + e.getMessage());
            e.printStackTrace();
        }

        return "Runtime of this [File] updatePeopleNamesTTOO is " + timer.stop() + "ns";
    }

    @Override
    public void refreshTTOO() {
        String sourceFileName = "people_backup.txt";
        String destinationFileName = "people.txt";

        Path sourcePath = Paths.get(sourceFileName);
        Path destinationPath = Paths.get(destinationFileName);

        try {
            long bytesCopied = Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING).getNameCount();

        } catch (IOException e) {
            // 处理文件不存在、没有权限或复制过程中发生的任何 I/O 错误
            System.err.println("Fail" + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public String findMovieByConstraintNationAndReleaseYear_usingGoodLogic(String nation, int year1, int year2) {
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        try (FileReader fr = new FileReader("movies.txt");
             BufferedReader reader = new BufferedReader(fr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] movie = line.split(";");
                if (movie[2].equals(nation) && (Integer.parseInt(movie[3]) >= year1 && Integer.parseInt(movie[3]) <= year2)) {
                    //System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown: " + e.getMessage());
            e.printStackTrace();
        }
        return "Runtime of this [File] findMovieByConstraintNationAndReleaseYear_usingGoodLogic is " + timer.stop() + "ns";

    }


    @Override
    public String findMovieByConstraintNationAndReleaseYear_usingBadLogic(String nation, int year1, int year2) {
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        try (FileReader fr = new FileReader("movies.txt");
             BufferedReader reader = new BufferedReader(fr)) {
            String line;
            List<String> selectedContent = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] movie = line.split(";");
                if (movie[2].equals(nation)) {
                    selectedContent.add(line);
                }
            }
            for (String contentLine : selectedContent) {
                String[] movie = contentLine.split(";");
                if ((Integer.parseInt(movie[3]) >= year1 && Integer.parseInt(movie[3]) <= year2)) {
                    //System.out.println(contentLine);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown: " + e.getMessage());
            e.printStackTrace();
        }
        return "Runtime of this [File] findMovieByConstraintNationAndReleaseYear_usingBadLogic is " + timer.stop() + "ns";
    }

    @Override
    public String findTripOf_n_Passengers(int n, int group) {
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        String file_dir;
        switch (group) {
            case 1: file_dir = "D:/database_files/yellow_tripdata_2009-01_8K.csv"; break;
            case 2: file_dir = "D:/database_files/yellow_tripdata_2009-01_80K.csv"; break;
            case 3: file_dir = "D:/database_files/yellow_tripdata_2009-01_800K.csv"; break;
            default: file_dir = "D:/database_files/yellow_tripdata_2009-01_8M.csv"; break;
        }

        try (FileReader fr = new FileReader(file_dir);
             BufferedReader reader = new BufferedReader(fr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] trip = line.split(",");
                if (trip[4].equals(n)) {
                    //System.out.println(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown: " + e.getMessage());
            e.printStackTrace();
        }
        return "Runtime of this [File] findTripOf_n_Passengers is " + timer.stop() + "ns";
    }

    @Override
    public String updateDistance_mt_d(int d, int group) {
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();

        String file_dir;
        switch (group) {
            case 1: file_dir = "D:/database_files/yellow_tripdata_2009-01_8K.csv"; break;
            case 2: file_dir = "D:/database_files/yellow_tripdata_2009-01_80K.csv"; break;
            case 3: file_dir = "D:/database_files/yellow_tripdata_2009-01_800K.csv"; break;
            default: file_dir = "D:/database_files/yellow_tripdata_2009-01_8M.csv"; break;
        }

        int totalLines = 0;
        int updatedLines = 0;
        List<String> updatedContent = new ArrayList<>();

        try (FileReader fr = new FileReader(file_dir);
             BufferedReader reader = new BufferedReader(fr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                String[] trip = line.split(",");

                if (trip[5].equals(String.valueOf(d))){
                    String originalDist = trip[5];
                    String updatedDist = String.valueOf(-d);
                    trip[5] = updatedDist;

                    // 重新构建行
                    String updatedLine = String.join(",", trip);
                    updatedContent.add(updatedLine);
                    updatedLines++;

                    //System.out.println("Updated: " + originalDist + " -> " + updatedName);
                } else {
                    updatedContent.add(line);
                }
            }

            // 将更新后的内容写回文件
            if (updatedLines > 0) {
                try (FileWriter fw = new FileWriter("people.txt");
                     BufferedWriter writer = new BufferedWriter(fw)) {
                    for (String contentLine : updatedContent) {
                        writer.write(contentLine);
                        writer.newLine();
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown: " + e.getMessage());
            e.printStackTrace();
        }

        return "Runtime of this [File] updateDistance_mt_d is " + timer.stop() + "ns";
    }

    @Override
    public String refreshDistance_mt_d(int d, int group) {
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();

        String file_dir;
        switch (group) {
            case 1: file_dir = "D:/database_files/yellow_tripdata_2009-01_8K.csv"; break;
            case 2: file_dir = "D:/database_files/yellow_tripdata_2009-01_80K.csv"; break;
            case 3: file_dir = "D:/database_files/yellow_tripdata_2009-01_800K.csv"; break;
            default: file_dir = "D:/database_files/yellow_tripdata_2009-01_8M.csv"; break;
        }

        int totalLines = 0;
        int updatedLines = 0;
        List<String> updatedContent = new ArrayList<>();

        try (FileReader fr = new FileReader(file_dir);
             BufferedReader reader = new BufferedReader(fr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                String[] trip = line.split(",");

                if (trip[5].equals(String.valueOf(-d))){
                    String originalDist = trip[5];
                    String updatedDist = String.valueOf(d);
                    trip[5] = updatedDist;

                    // 重新构建行
                    String updatedLine = String.join(",", trip);
                    updatedContent.add(updatedLine);
                    updatedLines++;

                    //System.out.println("Updated: " + originalDist + " -> " + updatedName);
                } else {
                    updatedContent.add(line);
                }
            }

            // 将更新后的内容写回文件
            if (updatedLines > 0) {
                try (FileWriter fw = new FileWriter("people.txt");
                     BufferedWriter writer = new BufferedWriter(fw)) {
                    for (String contentLine : updatedContent) {
                        writer.write(contentLine);
                        writer.newLine();
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown: " + e.getMessage());
            e.printStackTrace();
        }

        return "Runtime of this [File] updateDistance_mt_d is " + timer.stop() + "ns";
    }

    class FullInformation {
        int runTime;
        String information;

        FullInformation(int runTime, String information) {
            this.runTime = runTime;
            this.information = information;
        }
    }

}
