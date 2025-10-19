import java.io.IOException;

//
// DataManipulation接口
// 我们在这里定义所有要实现的对照组 并且分别用DatabaseManipulation和FileManipulation实例化
// 最终我们在Client类里调用实验
//
public interface DataManipulation {
    //
    // 支持表的创建和基本功能
    //
    public void bustCache();
    public int addOneMovie(String str);
    public String findMovieById(int id);
    //
    // Design some experiments and try to answer the following questions:
    //

    //
    // Q1. What are the unique advantages of a DBMS compared with data operations in files?
    //

    //
    //       实验1.大批量SELECT实验 检索带有‘XXX’(以Star Wars系列为例)的字符串的电影名称 [体现缓存的优势]
    //            组1.调用数据库查询(暖机)      [缓存]
    //            组2.调用数据库查询(清理缓存) [不缓存]
    //            组3.调用Java代码查询        [无优化]
    //       预期: 组1最快 组2略慢 组3慢很多(由数据规模决定)
    //
    public String findMovieByTitleStrict(String title);
    public String findMovieByTitleLike(String like);

    //
    //       实验2.大批量Update实验 将带有‘to’的人名字符串部分替换为'TTOO' [尤其体现并发IO的优势]
    //            组1.调用数据库替换(暖机)      [缓存]
    //            组2.调用数据库替换(清理缓存) [不缓存]
    //            组3.调用Java代码替换        [无优化]
    //       预期: 组1最快 组2略慢 组3慢很多(比实验1更明显)
    //
    public String updatePeopleNamesTTOO();
    public void refreshTTOO();

    //
    //       实验3.使用课上查询优化器例子中本应较差表现的逻辑查询语句
    //            组1.调用数据库查询(好逻辑)[运用查询优化器]
    //            组2.调用数据库查询(差逻辑)[运用查询优化器]
    //            组3.调用Java代码查询    [执行较好的逻辑]
    //            组4.调用Java代码查询    [执行较差的逻辑]
    //       预期: 组1和2不应该有很大差别 组3慢很多 组4再慢很多
    //
    public String findMovieByConstraintNationAndReleaseYear_usingGoodLogic(String nation, int year1, int year2);
    public String findMovieByConstraintNationAndReleaseYear_usingBadLogic(String nation, int year1, int year2);


    //
    // Q2. Which DBMS is better? PostgreSQL or openGauss, and by which standard?
    //

    //
    //       实验4.大批量SELECT实验 检索带有载有4个人的trip
    //            组1. 8K+
    //            组2. 80K+
    //            组3. 800K+
    //            组4. 8M+
    //       作图: 规模-时间图
    //
    public String findTripOf_n_Passengers(int n, int group);


    //
    //       实验5.大批量Update实验 将trip_distance = d的值设为 -d
    //            组1. 8K+
    //            组2. 80K+
    //            组3. 800K+
    //            组4. 8M+
    //       作图: 规模-时间图
    //
    public String updateDistance_mt_d(int d, int group);
    public String refreshDistance_mt_d(int d, int group);

    //
    //       实验6.添加了索引的方法(FileManipulation不需要实现)
    //            组1.不使用索引
    //            组2.使用索引
    //       预期: 组1慢于组2略慢
    //
    public String findMovieByTitleStrict_withIdx(String title);
    public String findMovieByTitleLike_withIdx(String like);
}



