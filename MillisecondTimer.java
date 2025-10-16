//
// 按毫秒进行计时
//
public class MillisecondTimer {
    private long startTime;
    private long stopTime;
    private boolean isRunning = false;

    //
    // 启动计时器
    //

    public void start() {
        if (!isRunning) {
            this.startTime = System.nanoTime();
            this.isRunning = true;
            this.stopTime = 0;
        } else {
            System.out.println("Already running");
        }
    }

    //
    // 停止计时器
    // @return 总耗时
    //
    public long stop() {
        if (isRunning) {
            this.stopTime = System.nanoTime();
            this.isRunning = false;
            return getElapsedTimeNanos();
        } else {
            System.out.println("Stopwatch 未运行。");
            return -1;
        }
    }


    public long getElapsedTimeMillis() {
        if (startTime == 0) {
            return 0;
        }

        long end = isRunning ? System.nanoTime() : stopTime;
        return (end - startTime) / 1_000_000;
    }

    public long getElapsedTimeNanos() {
        if (startTime == 0) {
            return 0;
        }

        long end = isRunning ? System.nanoTime() : stopTime;
        return (end - startTime);
    }

}