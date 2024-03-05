package com.example.iotx.threadPoolConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@ThreadSafe
public class StatMetric implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private static final int MAX_MS_BETWEEN_THROUGHPUT_CALCS = 5000;
	private long callCount = 0L;
	private long total;
	private long min;
	private long max;
	private long last;
	private double avg;
	private int timeBetweenThroughputCalcs = 5000;
	private long lastThroughputCalcTime;
	private long hitsSinceLastThroughputCalc;
	private double throughput;
	private long lastUpdate = -1L;

	private TimeUnit timeUnit = TimeUnit.SECONDS;

	private static int DEFAULT_N = 10;

	double sum = 0.0D;
	final int N;
	final long[] lastValues;

	public StatMetric() {
		this(TimeUnit.SECONDS);
	}

	public StatMetric(TimeUnit timeUnit) {
		this(timeUnit, DEFAULT_N);
	}

	public StatMetric(TimeUnit timeUnit, int nRequiredForAverage) {
		if ((timeUnit != TimeUnit.MILLISECONDS) && (timeUnit != TimeUnit.SECONDS)) {
			timeUnit = TimeUnit.SECONDS;
		}
		this.timeUnit = timeUnit;
		this.N = nRequiredForAverage;

		this.lastValues = new long[this.N];
	}

	public void setTimeBetweenThroughputCalcs(int timeBetweenThroughputCalcs) {
		this.timeBetweenThroughputCalcs = timeBetweenThroughputCalcs;
	}

	public synchronized void update(long startTime) {
		update(startTime, 1);
	}

	public synchronized void update(long startTime, int count) {
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		if (count > 1) {
			duration = Math.round(duration / count);
		}
		this.last = duration;
		if (this.callCount == 0L) {
			this.min = (this.max = duration);
		} else {
			int index = (int) (this.callCount % this.N);

			this.sum -= this.lastValues[index];
			this.lastValues[index] = duration;
			this.sum += this.lastValues[index];

			if (this.callCount >= this.N) {
				this.avg = (this.sum / this.N);
			}

			if (duration > this.max) {
				this.max = duration;
			}

			if (duration < this.min) {
				this.min = duration;
			}
		}

		this.total += count;
		this.callCount += 1L;
		this.hitsSinceLastThroughputCalc += count;

		long timeSinceLast = endTime - this.lastThroughputCalcTime;
		if (timeSinceLast >= this.timeBetweenThroughputCalcs) {
			if (this.lastThroughputCalcTime != 0L) {
				double ms = timeSinceLast;
				this.throughput = (this.hitsSinceLastThroughputCalc / ms * 1000.0D);
				onSetThroughput(this.throughput);
			}
			this.lastThroughputCalcTime = endTime;
			this.hitsSinceLastThroughputCalc = 0L;
		}

		this.lastUpdate = endTime;
	}

	protected void onSetThroughput(double throughput) {
	}

	public synchronized double getAvg() {
		switch (this.timeUnit) {
		case MILLISECONDS:
			return this.avg;
		case SECONDS:
		default:
			break;
		}
		return this.avg / 1000.0D;
	}

	@Deprecated
	public synchronized long getHits() {
		return this.total;
	}

	public long getTotal() {
		return this.total;
	}

	public synchronized double getMax() {
		switch (this.timeUnit) {
		case MILLISECONDS:
			return this.max;
		case SECONDS:
		default:
			break;
		}

		return this.max / 1000.0D;
	}

	public synchronized double getMin() {
		switch (this.timeUnit) {
		case MILLISECONDS:
			return this.min;
		case SECONDS:
		default:
			break;
		}

		return this.min / 1000.0D;
	}

	public synchronized double getLast() {
		switch (this.timeUnit) {
		case MILLISECONDS:
			return this.last;
		case SECONDS:
		default:
			break;
		}

		return this.last / 1000.0D;
	}

	public synchronized double getThroughput() {
		if ((this.lastThroughputCalcTime == 0L) || (System.currentTimeMillis() - this.lastThroughputCalcTime > this.timeBetweenThroughputCalcs * 2)) {
			return -1.0D;
		}
		return this.throughput;
	}

	public synchronized long getLastUpdate() {
		return this.lastUpdate;
	}

	public synchronized TimeUnit getTimeUnit() {
		return this.timeUnit;
	}

	public synchronized void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public synchronized Object getMetric(Metric metric) {
		switch (metric.ordinal()) {
		case 1:
			return Double.valueOf(this.avg);
		case 2:
			return Long.valueOf(this.total);
		case 3:
			return Long.valueOf(this.last);
		case 4:
			return Long.valueOf(this.lastUpdate);
		case 5:
			return Long.valueOf(this.max);
		case 6:
			return Long.valueOf(this.min);
		case 7:
			return Double.valueOf(this.throughput);
		}

		return Integer.valueOf(-1);
	}

	public String toString() {
		switch (this.timeUnit) {
		case MILLISECONDS:
			return String.format("Total[%d], Last[%.1fms], Min/Avg/Max[%.1fms / %.1fms / %.1fms], Throughput[%.1f/sec]", new Object[] { Long.valueOf(getHits()),
					Double.valueOf(getLast()), Double.valueOf(getMin()), Double.valueOf(getAvg()), Double.valueOf(getMax()), Double.valueOf(getThroughput()) });
		case SECONDS:
		default:
			break;
		}

		return String.format("Total[%d], Last[%.1fs], Min/Avg/Max[%.1fs / %.1fs / %.1fs], Throughput[%.1f/sec]", new Object[] { Long.valueOf(getHits()), Double.valueOf(getLast()),
				Double.valueOf(getMin()), Double.valueOf(getAvg()), Double.valueOf(getMax()), Double.valueOf(getThroughput()) });
	}

	public static void main(String[] args) {
		long overallStart = System.currentTimeMillis();
		StatMetric stat = new StatMetric();
		Random rand = new Random();
		List<Integer> sleepValues = new ArrayList<Integer>(200);
		List<Integer> actualValues = new ArrayList<Integer>(200);
		for (int i = 0; i < 200; i++) {
			long start = System.currentTimeMillis();
			try {
				int x = rand.nextInt(100);
				sleepValues.add(Integer.valueOf(x));
				Thread.sleep(x);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			Long end = Long.valueOf(System.currentTimeMillis());
			actualValues.add(Integer.valueOf((int) (end.longValue() - start)));
			stat.update(start);
		}
		System.out.println("Max Value: " + Collections.max(sleepValues));
		System.out.println("Avg Value: " + avg(sleepValues));
		System.out.println("Max Dur: " + Collections.max(actualValues));
		System.out.println("Avg Dur: " + avg(actualValues));
		System.out.println(stat.toString());
		System.out.println("Test took " + (System.currentTimeMillis() - overallStart) / 1000L + " sec");
	}

	private static double avg(List<Integer> list) {
		double sum = 0.0D;
		for (Integer i : list) {
			sum += i.intValue();
		}
		return sum / list.size();
	}

	public static enum Metric {
		Throughput(Double.class), MinDuration(Double.class), MaxDuration(Double.class), AverageDuration(Double.class), LastDuration(Double.class), LastUpdate(Long.class), Count(
				Long.class);

		private Class<?> dataClass;

		private Metric(Class<?> dataClass) {
			this.dataClass = dataClass;
		}

		public Class<?> getDataClass() {
			return this.dataClass;
		}
	}
}