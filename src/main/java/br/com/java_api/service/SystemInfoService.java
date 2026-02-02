package br.com.java_api.service;

import br.com.java_api.dto.ContainerInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SystemInfoService {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    public ContainerInfoDTO getContainerInfo() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            String ip = InetAddress.getLocalHost().getHostAddress();
            String os = System.getProperty("os.name");
            long uptime = ManagementFactory.getRuntimeMXBean().getUptime();

            return new ContainerInfoDTO(hostname, ip, os, appVersion, uptime);
        } catch (UnknownHostException e) {
            return new ContainerInfoDTO("unknown", "unknown", "unknown", appVersion, 0);
        }
    }

    public String getContainerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    public Map<String, Object> getDetailedStatus() {
        Map<String, Object> status = new HashMap<>();

        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            String ip = InetAddress.getLocalHost().getHostAddress();
            String javaVersion = System.getProperty("java.version");
            String os = System.getProperty("os.name");

            long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
            long uptimeSec = uptimeMs / 1000;

            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heap = memoryBean.getHeapMemoryUsage();
            MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();

            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

            String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date());

            Map<String, Object> memory = new HashMap<>();
            memory.put("heap_used_MB", heap.getUsed() / (1024 * 1024));
            memory.put("heap_max_MB", heap.getMax() / (1024 * 1024));
            memory.put("non_heap_used_MB", nonHeap.getUsed() / (1024 * 1024));

            status.put("status", "UP");
            status.put("hostname", hostname);
            status.put("ip", ip);
            status.put("uptime_sec", uptimeSec);
            status.put("java_version", javaVersion);
            status.put("os", os);
            status.put("cpu_load_avg", osBean.getSystemLoadAverage());
            status.put("memory", memory);
            status.put("timestamp", timestamp);
            status.put("app_version", appVersion);

        } catch (Exception e) {
            status.put("status", "ERROR");
            status.put("error", e.getMessage());
        }

        return status;
    }

    public long getUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }
}