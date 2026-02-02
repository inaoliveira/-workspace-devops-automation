package br.com.java_api.dto;

public class ContainerInfoDTO {
    private String containerName;
    private String ipAddress;
    private String osName;
    private String appVersion;
    private long uptimeMillis;

    public ContainerInfoDTO() {}

    public ContainerInfoDTO(String containerName, String ipAddress, String osName, String appVersion, long uptimeMillis) {
        this.containerName = containerName;
        this.ipAddress = ipAddress;
        this.osName = osName;
        this.appVersion = appVersion;
        this.uptimeMillis = uptimeMillis;
    }

    // Getters e Setters
    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public long getUptimeMillis() {
        return uptimeMillis;
    }

    public void setUptimeMillis(long uptimeMillis) {
        this.uptimeMillis = uptimeMillis;
    }

    @Override
    public String toString() {
        return "ContainerInfoDTO{" +
                "containerName='" + containerName + "'" +
                ", ipAddress='" + ipAddress + "'" +
                ", osName='" + osName + "'" +
                ", appVersion='" + appVersion + "'" +
                ", uptimeMillis=" + uptimeMillis +
                '}';
    }
}