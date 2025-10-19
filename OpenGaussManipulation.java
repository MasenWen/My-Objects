import java.sql.*;


//
// OpenGaussManipulation.java: 我们使用对应的连接分别连接Postgres和OpenGauss
// 我们在这个类里包装计时功能并直接打印打控制台
//

public class OpenGaussManipulation implements DataManipulation {
    private Connection con = null;
    private ResultSet resultSet;

    private String host = "localhost";
    private String dbname = "opengauss";
    private String user = "masen";
    private String pwd = "Masen060214";
    private String port = "15432";



    private void getConnection() {
        try {
            Class.forName("org.opengauss.Driver");

        } catch (Exception e) {
            System.err.println("Cannot find the OpenGauss driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String url = "jdbc:opengauss://" + host + ":" + port + "/" + dbname +
                    "?loggerLevel=OFF&loggerFile=opengauss.log";
            con = DriverManager.getConnection(url, user, pwd);

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    private void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //
    // 缓存重置 大量调取一个无关库(bus_lines)的查询 清理缓存
    //
    @Override
    public void bustCache() {

        getConnection();
        try {
            String[] queries = {
                    "SELECT * FROM bus_lines WHERE station_id IN (1,2,7,8,12,17,18,28,38,61)",
                    "SELECT * FROM color_names WHERE name IN ('AliceBlue','AntiqueWhite','Aqua','Aquamarine')",
                    "SELECT * FROM station WHERE district IN ('Luohu','Futian')",
                    "SELECT * FROM station WHERE latitude BETWEEN 22.53 AND 22.55"
            };
            for (int i = 0; i < 30; i++) {
                for (String query : queries) {
                    try (Statement stmt = con.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {
                        while (rs.next()) {
                            // 读取所有列
                            int cols = rs.getMetaData().getColumnCount();
                            for (int j = 1; j <= cols; j++) {
                                rs.getObject(j);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // 静默处理
        } finally {
            closeConnection();
        }
    }

    @Override
    public int addOneMovie(String str) {
        getConnection();
        int result = 0;
        String sql = "insert into movies (title, country,year_released,runtime) " +
                "values (?,?,?,?)";
        String movieInfo[] = str.split(";");
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, movieInfo[0]);
            preparedStatement.setString(2, movieInfo[1]);
            preparedStatement.setInt(3, Integer.parseInt(movieInfo[2]));
            preparedStatement.setInt(4, Integer.parseInt(movieInfo[3]));
            System.out.println(preparedStatement.toString());

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }


    @Override
    public String findMovieById(int id) {
        getConnection();
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        String result = "";
        String sql = "select * from movies where movieid = " + "?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            result = "Runtime of this [OpenGauss] findMovieById is " + timer.stop() + "ns";
            // 余下的打印极其耗时


            while (resultSet.next()) {
                String str = "";
                str += resultSet.getString("movieid");
                str += ";";
                str += resultSet.getString("title");
                str += ";";
                str += resultSet.getString("country");
                str += ";";
                str += resultSet.getString("year_released");
                str += ";";
                str += resultSet.getString("runtime");
                str += ";";

                //System.out.println(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public String findMovieByTitleStrict(String title) {
        getConnection();
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        String result = "";
        String sql = "select * from movies where title = " + "?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();
            result = "Runtime of this [OpenGauss] findMovieByTitleStrict is " + timer.stop() + "ns";
            // 余下的打印极其耗时

            while (resultSet.next()) {
                String str = "";
                str += resultSet.getString("movieid");
                str += ";";
                str += resultSet.getString("title");
                str += ";";
                str += resultSet.getString("country");
                str += ";";
                str += resultSet.getString("year_released");
                str += ";";
                str += resultSet.getString("runtime");
                str += ";";

                //System.out.println(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public String findMovieByTitleLike(String like) {
        getConnection();
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        String result = "";
        String sql = "select * from movies where title like " + "?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, like + "%");
            resultSet = preparedStatement.executeQuery();
            result = "Runtime of this [OpenGauss] findMovieByTitleLike is " + timer.stop() + "ns";
            // 余下的打印极其耗时


            while (resultSet.next()) {
                String str = "";
                str += resultSet.getString("movieid");
                str += ";";
                str += resultSet.getString("title");
                str += ";";
                str += resultSet.getString("country");
                str += ";";
                str += resultSet.getString("year_released");
                str += ";";
                str += resultSet.getString("runtime");
                str += ";";

                //System.out.println(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public String updatePeopleNamesTTOO() {
        getConnection();
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        String result = "";
        String sql = "UPDATE people " + "SET first_name = REPLACE(first_name, 'to', 'TTOO')\n" +
                "WHERE first_name LIKE '%to%';";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
            result = "Runtime of this [OpenGauss] updatePeopleNamesTTOO is " + timer.stop() + "ns";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public void refreshTTOO() {
        getConnection();
        String sql = "UPDATE people " + "SET first_name = REPLACE(first_name, 'TTOO', 'to')\n" +
                "WHERE first_name LIKE '%TTOO%';";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public String findMovieByConstraintNationAndReleaseYear_usingGoodLogic(String nation, int year1, int year2) {
        getConnection();
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        String result = "";
        String sql = "select * from movies where country = ? and year_released BETWEEN ? and ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, nation);
            preparedStatement.setInt(2, year1);
            preparedStatement.setInt(3, year2);
            resultSet = preparedStatement.executeQuery();
            result = "Runtime of this [OpenGauss] findMovieByConstraintNationAndReleaseYear_usingGoodLogic is " + timer.stop() + "ns";
            // 余下的打印极其耗时


            while (resultSet.next()) {
                String str = "";
                str += resultSet.getString("movieid");
                str += ";";
                str += resultSet.getString("title");
                str += ";";
                str += resultSet.getString("country");
                str += ";";
                str += resultSet.getString("year_released");
                str += ";";
                str += resultSet.getString("runtime");
                str += ";";

                //System.out.println(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public String findMovieByConstraintNationAndReleaseYear_usingBadLogic(String nation, int year1, int year2) {
        getConnection();
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        String result = "";
        String sql = "select * from (select * from movies where country = ?) as us_movies where year_released BETWEEN ? and ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, nation);
            preparedStatement.setInt(2, year1);
            preparedStatement.setInt(3, year2);
            resultSet = preparedStatement.executeQuery();
            result = "Runtime of this [OpenGauss] findMovieByConstraintNationAndReleaseYear_usingBadLogic is " + timer.stop() + "ns";
            // 余下的打印极其耗时


            while (resultSet.next()) {
                String str = "";
                str += resultSet.getString("movieid");
                str += ";";
                str += resultSet.getString("title");
                str += ";";
                str += resultSet.getString("country");
                str += ";";
                str += resultSet.getString("year_released");
                str += ";";
                str += resultSet.getString("runtime");
                str += ";";

                //System.out.println(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public String findTripOf_n_Passengers(int n, int group) {
        getConnection();
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();

        String table;
        switch (group) {
            case 1: table = "\"taxi_8K+\""; break;
            case 2: table = "\"taxi_80K+\""; break;
            case 3: table = "\"taxi_800K+\""; break;
            default: table = "\"taxi_8M+\""; break;
        }

        String result = "";
        String sql = "select * from "+table+" where passenger_count = " + "?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, n);
            resultSet = preparedStatement.executeQuery();
            result = "Runtime of this [OpenGauss] findTripOf_n_Passenger is " + timer.stop() + "ns";
            // 余下的打印极其耗时

//            while (resultSet.next()) {
//                String str = "";
//                str += resultSet.getString("movieid");
//                str += ";";
//                str += resultSet.getString("title");
//                str += ";";
//                str += resultSet.getString("country");
//                str += ";";
//                str += resultSet.getString("year_released");
//                str += ";";
//                str += resultSet.getString("runtime");
//                str += ";";
//
//                //System.out.println(str);
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }


    @Override
    public String updateDistance_mt_d(int d, int group) {
        getConnection();
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();

        String table;
        switch (group) {
            case 1: table = "\"taxi_8K+\""; break;
            case 2: table = "\"taxi_80K+\""; break;
            case 3: table = "\"taxi_800K+\""; break;
            default: table = "\"taxi_8M+\""; break;
        }

        String result = "";
        String sql = "UPDATE "+table + " SET trip_distance = (?)\n" +
                "WHERE trip_distance = ?;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, -d);
            preparedStatement.setInt(2, d);
            preparedStatement.executeUpdate();
            result = "Runtime of this [OpenGauss] updateDistance_mt_d is " + timer.stop() + "ns";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }


    @Override
    public String refreshDistance_mt_d(int d, int group) {
        getConnection();
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();

        String table;
        switch (group) {
            case 1: table = "\"taxi_8K+\""; break;
            case 2: table = "\"taxi_80K+\""; break;
            case 3: table = "\"taxi_800K+\""; break;
            default: table = "\"taxi_8M+\""; break;
        }

        String result = "";
        String sql = "UPDATE "+table + " SET trip_distance = (?)\n" +
                "WHERE trip_distance = ?;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, -d);
            preparedStatement.setInt(2, d);
            preparedStatement.executeUpdate();
            result = "Runtime of this [OpenGauss] updateDistance_mt_d is " + timer.stop() + "ns";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public String findMovieByTitleStrict_withIdx(String title) {
        getConnection();

        // 先创建索引（不计时）- OpenGauss使用标准SQL语法
        try {
            String createIndexSQL = "CREATE INDEX IF NOT EXISTS idx_movies_title ON movies (title)";
            try (Statement stmt = con.createStatement()) {
                stmt.execute(createIndexSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 开始计时（只计时查询操作）
        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        String result = "";

        try {
            String sql = "select * from movies where title = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();
            result = "Runtime of this [OpenGauss] findMovieByTitleStrict_withIdx is " + timer.stop() + "ns";

            while (resultSet.next()) {
                String str = "";
                str += resultSet.getString("movieid");
                str += ";";
                str += resultSet.getString("title");
                str += ";";
                str += resultSet.getString("country");
                str += ";";
                str += resultSet.getString("year_released");
                str += ";";
                str += resultSet.getString("runtime");
                str += ";";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    @Override
    public String findMovieByTitleLike_withIdx(String like) {
        getConnection();

        // 先创建索引（不计时）- OpenGauss支持表达式索引
        try {
            String createIndexSQL = "CREATE INDEX IF NOT EXISTS idx_movies_title_lower ON movies (LOWER(title))";
            try (Statement stmt = con.createStatement()) {
                stmt.execute(createIndexSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        MillisecondTimer timer = new MillisecondTimer();
        timer.start();
        System.out.println();
        String result = "";

        try {
            String sql = "select * from movies where title like ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, like + "%");
            resultSet = preparedStatement.executeQuery();
            result = "Runtime of this [OpenGauss] findMovieByTitleLike_withIdx is " + timer.stop() + "ns";

            while (resultSet.next()) {
                String str = "";
                str += resultSet.getString("movieid");
                str += ";";
                str += resultSet.getString("title");
                str += ";";
                str += resultSet.getString("country");
                str += ";";
                str += resultSet.getString("year_released");
                str += ";";
                str += resultSet.getString("runtime");
                str += ";";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

}
