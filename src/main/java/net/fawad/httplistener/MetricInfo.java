package net.fawad.httplistener;

public class MetricInfo {
    private final String name;
    private final long value;
    private final String aggregationType;
    private final String rollupType;
    private final String clusterRollupType;

    public MetricInfo(String name, long value, String aggregationType, String rollupType, String clusterRollupType) {
        assert (name != null);
        assert (aggregationType != null);
        assert (rollupType != null);
        assert (clusterRollupType != null);
        this.name = name;
        this.value = value;
        this.aggregationType = aggregationType;
        this.rollupType = rollupType;
        this.clusterRollupType = clusterRollupType;
    }

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }

    public String getAggregationType() {
        return aggregationType;
    }

    public String getRollupType() {
        return rollupType;
    }

    public String getClusterRollupType() {
        return clusterRollupType;
    }

    @Override
    public String toString() {
        return "MetricInfo{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", aggregationType='" + aggregationType + '\'' +
                ", rollupType='" + rollupType + '\'' +
                ", clusterRollupType='" + clusterRollupType + '\'' +
                '}';
    }
}