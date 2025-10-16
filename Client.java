
public class Client {

    public static void main(String[] args) {
        String db_r1;
        String db_r3;
        String db_r5;
        String file_r1;
        String file_r3;
        String file_r5;

        try {
            DataManipulation dm = new DataFactory().createDataManipulation(args[0]);
            FileManipulation fm = new FileManipulation();
            OpenGaussManipulation om = new OpenGaussManipulation();


            //
            // 实验1-1 找到Star Wars电影 对比时间
            //

            db_r1 = dm.findMovieByTitleStrict("Star Wars");
            dm.findMovieByTitleStrict("Star Wars");
            db_r3 = dm.findMovieByTitleStrict("Star Wars");

            file_r1 = fm.findMovieByTitleStrict("Star Wars");
            fm.findMovieByTitleStrict("Star Wars");
            file_r3 = fm.findMovieByTitleStrict("Star Wars");

            compareLog_file_db_withCaches(db_r1, db_r3, file_r1, file_r3);

            //System.out.println(om.findMovieByTitleStrict("Star Wars"));
            //System.out.println(dm.findMovieByTitleStrict("Star Wars"));

            //
            // 实验1-2 检索“Star”开头的电影名 对比时间
            //
            db_r1 = dm.findMovieByTitleLike("Star");
            dm.findMovieByTitleLike("Star");
            db_r3 = dm.findMovieByTitleLike("Star");

            file_r1 = fm.findMovieByTitleLike("Star");
            fm.findMovieByTitleLike("Star");
            file_r3 = fm.findMovieByTitleLike("Star");

            compareLog_file_db_withCaches(db_r1, db_r3, file_r1, file_r3);

            //
            // 实验2 替换人名中的所有“to”为“TTOO” 对比时间
            //

            file_r1 = fm.updatePeopleNamesTTOO();
            fm.refreshTTOO();
            fm.updatePeopleNamesTTOO();
            fm.refreshTTOO();
            file_r3 = fm.updatePeopleNamesTTOO();
            fm.refreshTTOO();

            db_r1 = dm.updatePeopleNamesTTOO();
            dm.refreshTTOO();
            dm.updatePeopleNamesTTOO();
            dm.refreshTTOO();
            db_r3 = dm.updatePeopleNamesTTOO();
            dm.refreshTTOO();

            compareLog_file_db_withCaches(db_r1, db_r3, file_r1, file_r3);


            //
            // 实验3 对于课上的查询优化器例子中的逻辑查询语句进行对比
            //

            System.out.println(dm.findMovieByConstraintNationAndReleaseYear_usingGoodLogic("us", 1940, 1949));

            System.out.println(dm.findMovieByConstraintNationAndReleaseYear_usingBadLogic("us", 1940, 1949));

            System.out.println(fm.findMovieByConstraintNationAndReleaseYear_usingGoodLogic("us", 1940, 1949));

            System.out.println(fm.findMovieByConstraintNationAndReleaseYear_usingBadLogic("us", 1940, 1949));


            //
            //  实验4 大批量SELECT实验 检索带有载有4个人的trip
            //

            System.out.println("Size = 8K");
            compareLog_file_db(dm.findTripOf_n_Passengers(4,1), fm.findTripOf_n_Passengers(4,1));
            System.out.println();

            System.out.println("Size = 80K");
            compareLog_file_db(dm.findTripOf_n_Passengers(4,2), fm.findTripOf_n_Passengers(4,2));
            System.out.println();

            System.out.println("Size = 800K");
            compareLog_file_db(dm.findTripOf_n_Passengers(4,3), fm.findTripOf_n_Passengers(4,3));
            System.out.println();

            System.out.println("Size = 8M");
            compareLog_file_db(dm.findTripOf_n_Passengers(4,4), fm.findTripOf_n_Passengers(4,4));
            System.out.println();


            System.out.println("Size = 8K");
            compareLog_db_og(dm.findTripOf_n_Passengers(4,1), om.findTripOf_n_Passengers(4,1));
            System.out.println();

            System.out.println("Size = 80K");
            compareLog_db_og(dm.findTripOf_n_Passengers(4,2), om.findTripOf_n_Passengers(4,2));
            System.out.println();

            System.out.println("Size = 800K");
            compareLog_db_og(dm.findTripOf_n_Passengers(4,3), om.findTripOf_n_Passengers(4,3));
            System.out.println();

            System.out.println("Size = 8M");
            compareLog_db_og(dm.findTripOf_n_Passengers(4,4), om.findTripOf_n_Passengers(4,4));
            System.out.println();

            //
            //  实验5 大批量Update实验 将trip_distance = d的值设为 -d
            //

            System.out.println("Size = 8K");
            compareLog_file_db(dm.updateDistance_mt_d(4, 1), fm.updateDistance_mt_d(4, 1));
            dm.refreshDistance_mt_d(4,1);
            fm.refreshDistance_mt_d(4,1);
            System.out.println();

            System.out.println("Size = 80K");
            compareLog_file_db(dm.updateDistance_mt_d(4, 2), fm.updateDistance_mt_d(4, 2));
            dm.refreshDistance_mt_d(4,2);
            fm.refreshDistance_mt_d(4,2);
            System.out.println();

            System.out.println("Size = 800K");
            compareLog_file_db(dm.updateDistance_mt_d(4, 3), fm.updateDistance_mt_d(4, 3));
            dm.refreshDistance_mt_d(4,3);
            fm.refreshDistance_mt_d(4,3);
            System.out.println();

            System.out.println("Size = 8M");
            compareLog_file_db(dm.updateDistance_mt_d(4, 4), fm.updateDistance_mt_d(4, 4));
            dm.refreshDistance_mt_d(4,4);
            fm.refreshDistance_mt_d(4,4);
            System.out.println();


            System.out.println("Size = 8K");
            compareLog_db_og(dm.updateDistance_mt_d(4, 1), om.updateDistance_mt_d(4, 1));
            dm.refreshDistance_mt_d(4,1);
            om.refreshDistance_mt_d(4,1);
            System.out.println();

            System.out.println("Size = 80K");
            compareLog_db_og(dm.updateDistance_mt_d(4, 2), om.updateDistance_mt_d(4, 2));
            dm.refreshDistance_mt_d(4,2);
            om.refreshDistance_mt_d(4,2);
            System.out.println();

            System.out.println("Size = 800K");
            compareLog_db_og(dm.updateDistance_mt_d(4, 3), om.updateDistance_mt_d(4, 3));
            dm.refreshDistance_mt_d(4,3);
            om.refreshDistance_mt_d(4,3);
            System.out.println();

            System.out.println("Size = 8M");
            compareLog_db_og(dm.updateDistance_mt_d(4, 4), om.updateDistance_mt_d(4, 4));
            dm.refreshDistance_mt_d(4,4);
            om.refreshDistance_mt_d(4,4);
            System.out.println();


        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    //
    // 数据分析
    //
    public static void compareLog_file_db(String db, String file) {

        long db_ = getLong(db);
        long file_ = getLong(file);

        System.out.println("对比");
        System.out.println(db);
        System.out.println(file);
        System.out.println();
        System.out.printf("相比之下后者比前者慢 %.2f%%%n", (100.0 * (file_ - db_) / db_));
        System.out.println();
    }

    public static void compareLog_file_db_withCaches(String db_cold, String db_warm, String file_cold, String file_warm) {

        long db_c = getLong(db_cold);
        long db_w = getLong(db_warm);
        long file_c = getLong(file_cold);
        long file_w = getLong(file_warm);

        System.out.println("第一次执行(确保无缓存)");
        System.out.println(db_cold);
        System.out.println(file_cold);
        System.out.println();

        System.out.println("执行多次后(充分提高其缓存优先级)");
        System.out.println(db_warm);
        System.out.println(file_warm);
        System.out.println();


        System.out.println("对比DBMS执行和Java初始执行速度:");
        System.out.printf("相比之下后者比前者慢 %.2f%%%n", (100.0 * (file_c - db_c) / db_c));
        System.out.println();

        System.out.println("对比DBMS执行和Java缓存执行速度:");
        System.out.printf("相比之下后者比前者慢 %.2f%%%n", (100.0 * (file_w - db_w) / db_w));
        System.out.println();

        System.out.println("对比DBMS充分利用缓存和未利用缓存:");
        System.out.printf("相比之下后者比前者慢 %.2f%%%n", (100.0 * (db_c - db_w) / db_w));
        System.out.println();

        System.out.println("对比Java充分利用缓存和未利用缓存:");
        System.out.printf("相比之下后者比前者慢 %.2f%%%n", (100.0 * (file_c - file_w) / file_w));
        System.out.println();
    }

    public static void compareLog_db_og(String db, String og) {

        long db_ = getLong(db);
        long og_ = getLong(og);

        System.out.println("对比");
        System.out.println(db);
        System.out.println(og);
        System.out.println();
        System.out.printf("相比之下前者者比后者慢 %.2f%%%n", (100.0 * (db_ - og_) / og_));
        System.out.println();
    }

    public static long getLong(String str) {
        String regex = "(\\d+)ns$";

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(str.trim()); // trim() 避免末尾空格干扰 $ 锚点

        if (matcher.find()) {
            String match = matcher.group(1);

            try {
                return Long.parseLong(match);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Found a non-long number near 'ns': " + match, e);
            }
        } else {
            throw new IllegalStateException("Error: Could not find the expected 'XXXns' pattern in the string: " + str);
        }
    }
}

