# **PDSProject1Report**

## 实验代码&实验报告已上传Github
### https://github.com/MasenWen/My-Objects/tree/main
### https://github.com/MasenWen/My-Objects/blob/main/PDSProject1Report/PDSProject1Report.md
#### MarkDown版本更清晰

# BaseLine

## 要求重述

### 实验一 回答这个问题:与文件中的数据操作相比，DBMS 的独特优势是什么？
#### 使用足够大的数据集将数据同时存储在数据库表和csv文件中 进行实验
##### 1.在 SQL 中使用 SELECT 查找（我们使用DatabaseManipulation类的函数调用SQL），在java实现的FileManipulation类中调用select函数做相同的事，都记录执行时间（我们选择分别在DatabaseManipulation类和FileManipulation类中记录，都在java的Client类里中打印并对比），以体现DBMS的优越性。
##### 2.在 SQL 中使用 UPDATE 修改（我们使用DatabaseManipulation类的函数调用SQL），在java实现的FileManipulation类中调用select函数做相同的事，都记录执行时间（我们选择分别在DatabaseManipulation类和FileManipulation类中记录，都在java的Client类里中打印并对比），以体现DBMS的优越性。
### 实验二 回答这个问题: 哪个 DBMS 更好？PostgreSQL 还是 OpenGauss，以及采用什么标准？
#### 使用更大的数据集进行进一步实验
##### 1.重复实验一的实验并做出适当修改，我想我们应该要体现特化了的Opengauss的一些优点。
##### 2.在这个部分分析“数据重组”创建索引的价值
### 重新整理思路

###
# =============== 现在让我们开始思考！=================
###

# **思路**

### BDMS(Database Manage System)的优势是什么呢？我结合已知和自己的经验提几个想法:
1. **[性能]** 并行/批量操作的使用 利用有限的操作可以大大减少IO的等待时间 而对于联网服务器的很多情形（特指并行）调取更多的gpu可以全面提升速度
2. **[性能]** 缓存的运用 根据使用频率（我注意到DataGrip的提示中有上下箭头表示使用频率的标志） 可能数据库在后台类似于类似于@cache的装饰符进行了缓存提速
3. **[性能]** 适配的数据结构 使用了树来存储结构和地址？ 数据库肯定做的更好
4. **[管理]** 多用户操作和权限的管理 安装opengauss时发现没有导入任何数据的数据库里是有表格的: 用户表（有用户名密码权限等）
5. **[性能]** 可能的日志 我不太确定PostgreSQL是否支持日志插件（不做要求） 但是opengauss的日志比较详细（这在安装中提供了很大的帮助）
### 经查询验证我的这几个想法 另外:
6. 并发中锁的运用 并发里的锁很让人头疼 但是如果要处理复杂的操作关系 这是绝对必要的
7. 课上提到过 数据库会对命令进行某种"解释"或"编译" 自动替换为更快执行的形式 **（后来发现这也是数据结构的集中体现）**
### 综上:实验要体现出
0. 并行/批量操作优势 这依赖于足够大的数据量 以及足够大量的读写（我想update操作会体现出端倪）
1. 缓存优势 我会在进行实验之前对数据库进行"暖机"操作 即多次调用某一数据保证其被缓存（重连数据库清楚缓存可以体现其优化）
2. 查询优化器 我们编写一些逻辑较差的查询来体现查询优化器的作用（课上讲过的两个例子）
3. PSQL和OpenGauss实现的索引机制
3. 其他
![image.png](df958484-3ba4-41c8-80a8-f7c12fe54cb7.png)

## OpenGauss的优势是什么呢？要体现这种细微优势是有难度的
### 1. 超大型数据集（？存疑）
### 2. 超大量Client的并发调取（单机实现不现实）
### 3. 索引机制的特化

###
# =============== 现在让我们看看实现 ==================
###

# **实现架构**
### 使用了Lab3的code and data项目
### 从华为官网下载了openGauss的驱动.jar包 添加在项目目录下
### 配置了PostgreSQL和OpenGauss数据库导入了课上的基础表
### 从Kaggle上下载了800万行的 纽约出租车数据 并导入数据库
### 划分为四个文件: Size = 8k，80k, 800k, 8M
![image.png](036709b1-0a8e-480f-9ede-8d5f7f9dfce5.png)

# **具体设计（以接口体现）与工具的实现（示例代码）**
## 实验的设计决定了接口框架有哪些方法
### 代码实现里，@retrurn的字符串是方法名称标记和耗时(nm)


```Java
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
```

## **计时的实现**
### 在DataGrip中每条操作记录中有如下时间信息:
![image.png](15c73837-0363-4e39-908f-00353ed79b03.png)
### 这并不利于对比 我选择直接在Java程序中调取SQL指令，并记录时间(同样是寻找"Star Wars"的代码):
![148fcfe66b5fcb660fc2612d67a70336.png](3fd5c111-7c1c-4dc3-b4b4-2dbc35a15452.png)
### 这两者的数值足够接近
### 下附这段计时对应的代码
#### 一个Timer类 可以选择用ms或ns计时


```Java
//
// 按毫秒进行计时(后续我换成了纳秒)
//

public class MillisecondTimer {
    private long startTime;
    private long stopTime;
    private boolean isRunning = false;

    public void start() {...}

    public long stop() {...}


    public long getElapsedTimeMillis() {...}

    public long getElapsedTimeNanos() {...}

}
```

### 由此在DatabaseManipulation 和 FileManipulation类里对每个方法都包装计时

## **绘图的实现**


```Java
import matplotlib.pyplot as plt
import numpy as np

sizes = ['8K', '80K', '800K', '8M']
database_runtime = [...]
file_runtime = [...]
diff_percent = [...]

x = np.arange(len(sizes))
width = 0.35

fig, ax1 = plt.subplots(figsize=(9,5))
bars1 = ax1.bar(x - width/2, database_runtime, width, label='Database', color='#4B8BBE')
bars2 = ax1.bar(x + width/2, file_runtime, width, label='File', color='#FFD43B')

for bars in [bars1, bars2]:
    for bar in bars:
        height = bar.get_height()
        ax1.text(bar.get_x() + bar.get_width()/2, height * 1.05,
                 f'{height/1e6:.2f}M', ha='center', va='bottom', fontsize=9)

ax1.set_xlabel('Data Size')
ax1.set_ylabel('Runtime (ns)')
ax1.set_title('Runtime Comparison: Database vs File')
ax1.set_xticks(x)
ax1.set_xticklabels(sizes)
ax1.set_yscale('log')
ax1.legend()
plt.tight_layout()
plt.show()
```

# **源码**
### 下载源码安装包
![image.png](f46d68b2-8f11-45a6-926c-1f5c67cae9a5.png)
### PostgreSQL的源码是百万级别的 只应当尝试阅读感兴趣的部分
![image.png](55e1dada-8930-4e88-9b81-4c94485a6c14.png)

###
# =============== 现在让我们开始实验 ==================
###

# **Ⅰ 缓存**

## **实验1** SELECT * FROM movies WHERE title = 'Star Wars'; 和 SELECT * FROM movies WHERE title LIKE 'Star%';
## **实验方法**
![image.png](e6d521b9-5c5b-4446-b438-4731b9df871f.png)
![image.png](0fdc8c53-0609-4f77-a0e1-a77d18eca9b8.png)
## **实验结果**
## 1-1
![image.png](f217db01-2f93-4a4f-a9fc-6de23f86b7d6.png)
## 1-2
![image.png](9cc884ef-1470-4b98-82a1-53abfd2188b7.png)

## **分析**
### 在我最初的设想里，DBMS系统最大的性能优势应该是源于并发和算法（查询优化器），而缓存(Cache)只是次之。
### 但一些在movies和people上的小实验（这两个表都只有10000行左右的数据）和重复调试代码的过程让我注意到了**随着重复次数的增加**同一查询的执行时间明显依次递减。
### 以找到"Star Wars"这一部电影的查询为例，缓存（重复执行三次）提升了效率**100多倍**，显然DBMS的缓存模块“记住”了这部电影。 
### 现实意义: 这样的例子提醒我们要善于利用Cache机制设计和运用数据库调用
### 改进进: 我们也要意识到缓存在重复实验中的毁灭性影响 这是我问什么尝试实现了一个bustCache()函数 （尝试并不成功）

## **bustCache()函数** [失败]

### 如果实验需要多次取平均，那么重复操作会让缓存高度“注意”其相关的行和列
### 一个立刻会有的想法是分散这种“注意力”
### 尝试实现了一个bustCache()函数，轮番(做个几十次)对几个不在实验中使用的数据库进行查询，这里具体指，以下是这个函数实现的代码:


```Java
@Override
    public void bustCache() {
        getConnection();
        try {
            String[] queries = {
                    "SELECT * FROM bus_lines WHERE station_id IN (1,2,7,8,12,17,18,28,38,61)",
                    "SELECT * FROM color_names WHERE name IN ('AliceBlue','AntiqueWhite','Aqua','Aquamarine')",
                    "SELECT * FROM station WHERE district IN ('Luohu','Futian')",
                    "SELECT * FROM station WHERE latitude BETWEEN 22.53 AND 22.55"};
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
            //
        } finally {
            closeConnection();
        }
    }
```

## 使用了**Cache()的实际效果**
![image.png](a4053e6e-f567-4afa-8a00-d5c5bfb12e52.png)
### 效果几乎没有 这是因为数据库有很强大的缓存机制

## **缓存机制的优化和实现思路**
### 这部分内容来自: PostgreSQL源码分析 系列文章十余篇 CSDN
### 内容对学生而言较难 选取了少量内容
![image.png](0ed7ea22-61ba-4e0b-ac23-e95187fbb316.png)
## **看源码[...\cache]**
### 以下出现的源码在文件目录："src/backend/util/cache"

## SysCache用于缓存系统表元组
![image.png](0573d235-ee83-42ff-bd3e-e928a9cba3b9.png)

## RelCache用于表模式信息缓存
![image.png](14ea0b9b-d2eb-4e21-8c54-61ef830522a8.png)
### 源码中进行查询的部分

## 缓冲池
![image.png](ac31f9cc-3b30-4cc5-8290-93524a7f32f6.png)

## 缓冲的争抢
![image.png](7dfc88fc-a091-44ce-bafb-95502976fbfa.png)

## **分析**
## 现在我们可以全面的解释:
### 为什么缓存大大加速了查询？ - 因为缓存豁免了IO时间
### 通过实现了系统表元组和表模式信息缓存的哈希表/桶快速映射 本质是一种索引
### 为什么尤其在中小规模数据中有用？ - 因为缓存豁免的时间是受空间限制的
### 如果缓存空间（如缓存池）被占用殆尽 数据库会通过释放较老的缓存来腾出空间
### 这也解释了为什么我写的cache()函数是失败的 缓存空间很大 而少数几个操作只会占用固定的缓冲区 即使需要被“遗忘”的操作长时间没有被“注意”也不会被影响 因为没有更多的内容尝试挤占其缓存
#### DataGrip中的缓存设定: **缓存空间相对充足**
![image.png](6b0d4f8f-d5cc-4564-8aed-d13556fe1733.png)

## 稳妥的方案:结合以下四个操作
### 1.断开数据库清除数据库缓存或者执行等价的DataGrip操作


```Java
sudo systemctl restart postgresql
```

![image.png](777cdac4-b6cc-4516-8d04-2236d3d710a7.png)

### 2.重启 尤其对于JavaIO 由于Windows不支持缓存清除 必须重启

### 3.设定数据库的缓存大小限制
### 4.重写一个涉及了足够多查询以占用足够多缓存以至于保证造成争抢破坏目标缓存记录的cache()
###

# **高IO**
## **实验2**  UPDATE people SET first_name = replace(first_name,'TTOO','to') WHERE first_name LIKE '%TTOO%';

## **实验方法**
![image.png](4833ea4f-87cf-467f-8da4-9149159ef61d.png)
## **实验结果**
![image.png](a5e8e85b-ef10-450f-9b3c-aff68fa89ba6.png)

## **分析**
### 相较于先前实验，比Java实现提速更多
### 数据库优化大大减少了IO等待浪费的时间
### 效果在数据量越大时越明显
### 改进: 可能需要更大的数据集 仅仅几毫秒的运行时间 即使有成倍的关系也不足以令人信服

###
# **Ⅱ 查询优化器**
## **实验3** 对比等价逻辑的不同查询
### 这两个查询逻辑被认为在执行时是会等价的

### 让我们来尝试验证一下
### 鉴于后者的逻辑被认为”本应该“更好，命名为GoodLogic，另一个是BadLogic

![image.png](c282a1a2-cdac-451a-91a1-52d64987b6a5.png)
![image.png](bd4e018e-0672-4f77-b35b-fabe04d7e4ea.png)

### 总的来看是差不多的（毕竟他们调用的实际算法是一样的）
## **EXPLAIN [Execution Plan][Query Plan]**
![image.png](10c49286-03fa-42fa-aa52-f0a579fef620.png)
## **EXPLAIN ANALYSIS [Execution Plan][Query Plan]**
![image.png](f749bee2-b35e-4941-b755-b7224a1ca12d.png)

### 对比发现用时几乎一样
### 误差可能源于缓存(先执行的慢)

## **查询优化器的优化思路(使用算法找到最佳逻辑)**
## 一些情形:

![image.png](87b0a387-7562-458f-9b05-1b3b7cdd591d.png)
![image.png](c912b858-2ffa-42f9-9a50-097df1731a37.png)
### 这部分实验来自文章: Postgresql中的explain
### https://www.cnblogs.com/flying-tiger/p/8039055.html

## **归纳总结**
### 数据库可以做一些基元操作{$opertaion_1$,...$operation_n$}
### 这些操作具有相对稳定的时间{$runtime_1$,...$runtime_n$}
### 一个实际操作可以由一系列基元操作复合而成
### 复合方式不止一种 有的逻辑效率很高（如利用索引直接找到） 有的则有限（遍历）
### 找到最优的操作组合的搜索时间 << 执行时间
## 设计: 用算法找到(被认为)最高效的查询逻辑 执行这个逻辑

## **设计思想**
### 查询优化提升了**逻辑的质量**
### **衡量逻辑质量的标准**是对诸如并行,缓存,索引等**优化方式的利用程度**
###

## **看源码 [...\Plan]**
### 见: 跟我一起读postgresql源码(五)——Planer(查询规划模块)
### https://www.cnblogs.com/flying-tiger/p/6063709.html
### 下图信息来源于这一文章和实际实验
### 架构和源码所在目录为: "src\backend\optimizer\plan"文件夹
![image.png](82a680c7-2db3-421b-b0bb-2b9329f91e5b.png)
## 根据源码逻辑 优化主要在两个环节里进行
### 1.预处理: 通过 **向上提取(pull_up_)** 减少逻辑中的浪费
### 2.动态规划（或遗传算法）找到最优路径（代价最低和排序最优）
### 最终返回优化了的 **查询计划树**
### **EXPLAIN在这个环节调取 查询计划树** （EXPLAIN并不真正执行）
### 释放中间的过程树结构空间
### 然后这个结果可以执行
### **EXPLAIN ANALYSIS用这个 查询计划实际执行 并返回时间**

## **对照实验**
### 那么如果没有查询优化器呢？会怎么样呢？
### 我按照本来的逻辑写了两段Java代码


```Java
@Override
    public String findMovieByConstraintNationAndReleaseYear_usingGoodLogic(String nation, int year1, int year2) {
        ...
            while ((line = reader.readLine()) != null) {
                String[] movie = line.split(";");
                if (movie[2].equals(nation) && (Integer.parseInt(movie[3]) >= year1 && Integer.parseInt(movie[3]) <= year2)) {
                    //System.out.println(line);
                }
            }
        ...
    }


    @Override
    public String findMovieByConstraintNationAndReleaseYear_usingBadLogic(String nation, int year1, int year2) {
        ...
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
        ...
    }
```

## **实验方法**
![image.png](384971bd-ec13-4f7d-a133-a8db156f7173.png)
## **实验结果**
![image.png](b4323772-22c6-4247-8659-eeafeec68686.png)
![image.png](24ac7dbc-cd09-47e6-af38-88def442d42c.png)

## **分析**
### 差距并不大（就单这个查询来看）
### 查询优化器本身需要花费时间
### 这个优化的效果在数据更大时显然会更明显
### 现实意义: 要有意识的审视自己逻辑是否为最佳 有些情况查询优化器可能不会考虑到
### 改进: 可能需要更大的数据集 体现出查询优化器的价值

###
# ================ 我们需要及时调整 ===================
###

# 实现了更大数据集的实验

### **后续的实验都是整批完成 实现了缓存清理（每轮重连数据库/重启）**
## 从Kaggle获取数据表8-Million-Lines

![image.png](c6ee14f3-1d33-4373-a2ff-e9a729ccbda0.png)

## 导入csv
![image.png](72b7bb9d-ceaf-458d-b6f4-ddd5140703b6.png)

## **\copy COPY 和 pg_dump IMPORT** (在这里走了很多弯路)

## 导入可以使用四种方式
### 直接导入 如上图 要注意先创建有列的表格并一一对应导入 否则会出现失败回滚
### **[bash] \copy** 


```Java
\copy movies FROM '______.csv' WITH CSV HEADER
```

### 具体实现是复杂但可行的


```Java
mm=>
omm=> DROP TABLE IF EXISTS public.taxi_trips;
CREATE TABLE public.taxi_trips (
    vendor_name VARCHAR(10),
    Trip_Pickup_DateTime TIMESTAMP,
    Trip_Dropoff_DateTime TIMESTAMP,
    Passenger_Count INTEGER,
    Trip_Distance DECIMAL(8,2),
    Start_Lon DECIMAL(11,6),
    Start_Lat DECIMAL(11,6),
    Rate_Code INTEGER,
    store_and_forward VARCHAR(10),
    End_Lon DECIMAL(11,6),
    End_Lat DECIMAL(11,6),
    Payment_Type VARCHAR(10),
    Fare_Amt DECIMAL(8,2),
    surcharge DECIMAL(8,2),
    mta_tax DECIMAL(8,2),
    Tip_Amt DECIMAL(8,2),
    Tolls_Amt DECIMAL(8,2),
    Total_Amt DECIMAL(8,2)
);

...
    
omm=> \copy public.taxi_trips FROM '/tmp/yellow_final.csv' WITH (FORMAT csv, HEADER true, NULL '');
omm=> select * from public.taxi_trips where vendor_name = '%DD%';
 vendor_name | trip_pickup_datetime | trip_dropoff_datetime | passenger_count | trip_distance | start_lon | start_lat |
rate_code | store_and_forward | end_lon | end_lat | payment_type | fare_amt | surcharge | mta_tax | tip_amt | tolls_amt
| total_amt
-------------+----------------------+-----------------------+-----------------+---------------+-----------+-----------+-
----------+-------------------+---------+---------+--------------+----------+-----------+---------+---------+-----------
+-----------
(0 rows)
```

### **[SQL] COPY**


```Java
COPY movies FROM '______.csv' WITH CSV HEADER;
```

### **pg_dump**
### 功能和普通Import不同
### 更快更稳定
![image.png](d5414038-a47e-43e9-9bd4-adb7cc78efbf.png)

### **关于内存和时间的调整**

![image.png](936fb094-781e-46fd-bcf3-a16508792e1f.png)

### 要导入的数据大小为2GB+
### 默认的750MB填满后就会回滚
### 除了调整内存还需要调整虚拟机供用内存
### 对于运行时间 超时会回滚
### 保证运行时间上限为0（不设限）

# **大批量实验**
## 关注DBMS的性能
# **Ⅲ 并行 & 批量操作**
### 并行和批量操作是截然不同的优化方式
### 他们在大批量都呈现出较好的表现
![image.png](e410ff1c-c44f-4e72-810c-c8fe6562152b.png)

## **实验方法**
## 查询实验
![image.png](04aa3f6a-928d-4931-aebe-94c3605d0af0.png)

## **实验结果**
![image.png](ed832588-ab0e-4084-9195-4d4b810837d7.png)

## **可视化**
![image.png](b987d5fa-d430-449c-8749-9f327ab242c7.png)

## **分析**
### 对于大型数据集 数据库的优秀表现是可以预见的
### 通过[并行or批量处理]的优化（控制变量:缓存被及时清理限制了）越大的数据集越倾向于表现出优越的性能
### 相比之下 JavaIO的表现并不差 较为稳定的每10倍时间增加3-5倍
### 产生疑问: 在甄别是哪个导致了优化上产生了疑问

## **实验方法**
## 更新实验
![image.png](689ff905-bff7-45b1-a4ca-b144038bfb7b.png)
## **实验结果**
![image.png](b432fe03-d998-4ae9-899a-86dafbe893d7.png)

## **可视化**
![image.png](243fa742-07e6-4d20-b9d1-246435bcf539.png)

## **分析**
### 对于高IO的情况是类似的
## **归因**
#### **批量操作会一次读写一批数据 减少IO时间**
#### **并行操作会解放cpu 减少IO等待的浪费**

### 疑问: 这仍然不能区分两者

# **并行的优化框架**
## 通过数据库并行节点检查判断简单查询的优化原因
### 这部分实验来自文章: Postgresql源码（109）并行框架实例与分析
### https://cloud.tencent.com/developer/article/2339809

![image.png](a59342de-06a6-4e2c-9efe-601d1bab5ec4.png)

## **看源码[...\plan]**

### **并行计划的源代码**
### （查询优化器会决定是否使用并行）
![image.png](3f7fef85-da90-4f67-98b9-e76b687af0c1.png)

## 现在我们有了判断的方法
## **分析**
### **检查并行参数** 并行在实验中被支持（默认值）
### **检查具体使用** Quiery Plan告诉我们并行没有被执行
![image.png](2f1aacbc-c415-4243-bfca-d5ae4d5135b0.png)


## **归因**
### 使用并行有巨大的一次成本
### 因此只有同时满足大数据&较复杂操作（多读写）时才倾向于被使用
# **批量操作**
### 辨析概念: 向量化/pg_扩展是强大的批量化优化方式 但是PSQL并不自带这些效果
### 关于缓存: 控制了查询间的缓存不互相影响 但批量操作本就依赖在一次查询内的快速从缓存找到/读写
### 我不确定是否仍应归为缓存的影响

###
# ================ 关于OpenGauss ===================
###

# OpenGauss和PostgreSQL的大数据集性能对比

## 对OpenGauss的印象
### OpenGauss对格式/输入/操作质量的要求都远远更高
### 例如OpenGauss不允许使用su用户连接DataGrip
### 例如OpenGauss强制要求su用户有高安全度密码
### 例如OpenGauss提供了系统的日志文件和日志输出（驱动会报红）
### 例如OpenGauss报错了psql不报错的.sql文件
## 阅读OpenGauss官方介绍文档
### https://learningvideo.obs.ap-southeast-1.myhuaweicloud.com/openGauss%20%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84.pptx
## 对比OpenGauss和PostgreSQL
![image.png](46453b3b-413c-44c0-ac7b-b3aceabbbaac.png)

### OpenGauss优化了高并发的切换 CheckPoint机制 将很这样的多企业能力特化
### OpenGauss强调使用的稳定性 对于效率的重视程度不高于PostgreSQL
#### **因为OpenGauss是企业数据库（稳定性等-企业场景下）**
#### **而PostgreSQL是通用数据库（通用性能-一般场景下）**


### 由于OpenGauss终始PostgreSQL**基本性能**以外的**企业性能** 其设计逻辑也发生了偏移
## **特化场景**
![image.png](c66c862a-e364-42c3-916e-1bbc219d3abc.png)

### 线程池模型 vs. 进程模型
### 存储引擎：原生支持列存与内存表
### 企业级安全管理体系
### 原生内置的AI能力
### 针对特定硬件平台的深度优化
## 这些优化对于特定场景无疑是巨大的提升 但我们很难设计实验去实现
## **一般场景**
![image.png](6240c556-a22f-47aa-8950-9f6d35ec9294.png)
![image.png](8ff18d2c-afd0-4951-9add-819a778af88a.png)
### 速度性能优化框架和PostgreSQL基本一致
### 一般情况下的表现可以通过简单的实验窥见

###
# ================ OpenGauss的表现 ===================
###

## **实验方法**
## 查询实验
![image.png](5db9eb81-3e5b-49c7-9086-f7a7722c8dd7.png)
## **实验结果**
![image.png](17e2dfb9-489b-4d83-9e3d-37abd953a7e3.png)
## **可视化**
![image.png](161920ed-a7b0-42fd-92eb-19e3d105be6d.png)
## **实验方法**

## 更新实验
![image.png](5b75dc53-8874-4c37-805b-3dbfbae40150.png)
## **实验结果**
![image.png](0c89c66f-c278-4e5d-a137-c597d60b145c.png)
## **可视化**
![image.png](b22d537c-9773-489c-9d33-9ccd561131d3.png)
## **分析**
### 课上曾经提及:尽管OpenGauss优化了psql 但是其表现缺常常不如psql
### 就这一实验来看OpenGauss的表现不如psql
### 可能在**更大**的数据上OpenGauss会更好 以后可以再尝试**更大**的数据集
![image.png](a669bbf8-5837-417d-968f-5abcf0254055.png)
### 在8000万行数据附近 psql还在随着数据集变大而更加胜出
### 这意味着所谓**更大**可能比较遥远

## 更有可能的情况
### 如果OpenGauss添加的额外设计没有优化时间性能
### 那么这些设计的基础开销很可能“拖累”运行时间
### 这样的“拖累”无伤大雅 因为时间是固定的（ 类似O(1) ）
![image.png](1232323d-2766-4313-9499-9a11162832f9.png)!
### OpenGauss在实验中亏损的时间可能反映了这种情况

# **Ⅳ 索引**
## **实验6** 对比索引机制的性能
### 索引的原理
![image.png](b9d13703-bcde-4513-ac63-2c49a709da7f.png)
### 相关思想在缓存和查询优化器中已经涉及
### 本质上是为了更快调用IO
### 对于不同数据类型映射方式不同
## **实验方法**
![image.png](6e6c56f2-f6c9-4135-97fe-2231ee04c6db.png)
## **实验结果**
![image.png](a3d84783-fbad-4c4a-97d4-27e762c02fe5.png)

## **可视化**
![image.png](bc3e1000-109c-4332-baef-cea44d726d98.png)

## **分析**
### **PostgreSQL**
### 添加了索引的列查找起来会更快
### 这也是为什么查询优化器尝试将查询化归为有索引的查询
### 添加索引本身有一个一定规模的开销(大) 调取索引也有一定的损耗(小) 在小数据集会造成影响
### **OpenGauss**
### 相比于PSQL OpenGauss对索引做了很多优化
### 所以不使用这些优化的场景会偏慢 在小数据集会造成较大影响
#### **以下内容来自OpenGauss官方介绍文档-性能**
![image.png](986c0c78-1b93-4ead-a322-65e6f5dc286a.png)

# End of BassLine
#### 这一部分耗时一周 导入表感谢老师的提示给了我更清晰的思路和debug花费我整天的时间 思路兜兜转转 

# Extra Thoughts
#### 我尝试做一些额外的实验（如脚本模拟多用户）但是在设计尝试时就放弃了 我的总结可能并不正确 但这是我初步的经验

#### 我说的尽量简单些 希望我学到的没有错误
# **主题总结 (回答要求提出的问题)**

### **DBMS系统尝试优化数据存储信息在增删改查等操作中的性能**
### **PostgreSQL从缓存/索引/并行&批量操作中优化时间 并通过查询优化器的决策将效率最大化**
####     这些优化节省的时间多数来自IO(费时操作)但节省方式不同
#####         **缓存**根本的避免了被持续调取的数据的IO
#####         **索引**减少了IO读取后被验证无用的读取记录
#####         **并行**通过cpu绕过IO等待时间而减少浪费(或者增加cpu核数量)
#####         **批量操作**通过减少IO的连接次数减少时间
#####         **查询优化器**通过选择最佳优化方案来充分调配以上优化
####     这些设计使用了不同的设计理念
#####         **空间换时间**
#####         **设计算法**
#####         **加强计算力**
####     这些设计本身会增加较小而稳定的**消耗**
#####         如查询优化器的消耗和查询复杂程度(而非数据量)相关
#####         并行启动的时间被认为和模拟为固定的(不考虑锁问题)
####     这也决定了他们被查询优化器调取的可能性
#####         查询优化器不计本身的耗时(Paradox)
#####         查询优化器调取并行的条件较为严苛(大数据+复杂查询+...)因为并行的消耗非常大
####     查询优化器的选择依赖复合的复杂度判断<=>(场景)
#####         数据库算法**不使用O($f(n)$)**因为n并不是唯一的
#####         以查询为例 输入不仅有数据表 还有查询语句 数据库参数 内存状态等
####     **Explain**的模拟调取了所有这些数据才能规划(这远比O($f(n)$)更具体)
####     **Explain Analyze**则直接进行实际操作来验证规划
### **更多的时间性能优化不在这些层面**
####     硬件设计是加强数据库能力最直接的方式
####     数据库使用者对于表的设计同样重要
### **更多的设计和时间无关(如OpenGauss的很多设计)**
####     安全 控制 特殊场景等
### 故而评判性能**不能只看时间**
### 评判性能应该看场景下的表现 **[实验的看]**
### 评判性能设计应该看设计本身 **[设计的看]**

# **项目总结**
### **清晰实验架构**
#### 利用接口设计 方法设计与实现分离 总方法数=比较类型数(File,PSQL,OpenGauss)×实验数(...)
#### 利用getConnection()自动连接 简化连接步骤
#### 依次控制变量: 缓存 查询优化器 清理缓存冷机实验 使用最简语句
### **更有规模的数据库和纵向比较**
#### 800万+条的数据表(纽约出租车)
#### 分为8000~800万的几组实验 关注数据量-性能关系
### **对于无法实现和研究的部分参考他人想法**
#### 利用计算机社区CSDN 腾讯云开发社区等 借鉴理解(尤其是在看源码架构这种困难的尝试上)
### **不足**
#### 应当考虑在查询中直接返回Explain Analysis时间(确保计时时间~执行时间是多余的)
#### 缺乏对数据表的横向利用: 查询依赖少量列 没有实验查询复杂度-性能关系
#### 对源码的理解能力有限(没有办法看完设计片段)
#### 缺乏对OpenGauss特化优势的实验

# **遇到的报错和困难**
### OpenGauss不能使用su用户连接 su密码需要用""保护 防止命令行混淆词义
### 在Java中catch掉OpenGauss的日志返回
### 数据库导入 需要正确设定所有列名 一一对应 需要设定IDE缓存为>总大小（默认750） 同时虚拟机缓存>总大小 否则会回滚
### OpenGauss导入.sql时需要清洗语法 否则会报错（要求比PSQL更高）

###
## 数据库的影响因素不可胜数 越是了解就越是怀疑 场景的尝试是钥匙 一把钥匙开一把锁
## 开发是现实主义的 不追求完整的剖析而追求具体的归因 开发者要站在别人的肩膀上
## 产品开发(此指PostgreSQL和OpenGauss)要有用户意识 产品测试须紧随其后
# Stop Here
