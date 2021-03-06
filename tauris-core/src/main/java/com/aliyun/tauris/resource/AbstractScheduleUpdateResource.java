package com.aliyun.tauris.resource;

import com.aliyun.tauris.TResource;
import com.aliyun.tauris.TResourceURI;
import com.aliyun.tauris.utils.TLogger;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by ZhangLei on 2018/4/13.
 */
public abstract class AbstractScheduleUpdateResource extends TResource {

    public static final String P_INTERVAL = "__interval__";

    protected TLogger logger;

    protected ScheduledExecutorService updater;

    protected int updateInterval;

    protected String md5sum;

    public AbstractScheduleUpdateResource() {
        this.logger = TLogger.getLogger(this);
    }

    @Override
    public void setURI(TResourceURI uri) {
        super.setURI(uri);
        Integer interval = this.uri.getIntegerParam(P_INTERVAL);
        if (interval != null) {
            this.updateInterval = interval;
        }
    }

    @Override
    public void watch(Consumer<byte[]> consumer) {
        if (updateInterval > 0) {
            updater = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("resource-update-scheduler-pool-" + id()).daemon(true).build());
            updater.scheduleAtFixedRate(() -> {
                try {
                    byte[] res = fetchIfChanged();
                    if (res != null) {
                        consumer.accept(res);
                        logger.INFO("update resource from %s success", AbstractScheduleUpdateResource.this.toString());
                    }
                } catch (Exception e) {
                    logger.ERROR("update resource %s failed", e, AbstractScheduleUpdateResource.this.toString());
                }
            }, updateInterval, updateInterval, TimeUnit.SECONDS);
        }
    }

    protected byte[] fetchIfChanged() throws Exception {
        byte[] res = fetch();
        if (md5sum == null) {
            md5sum = md5sum(res);
            return res;
        } else {
            String newmd5 = md5sum(res);
            if (!newmd5.equals(md5sum)) {
                md5sum = newmd5;
                return res;
            }
        }
        return null;
    }

    protected static String md5sum(byte[] res) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(res);
        StringBuilder hexString = new StringBuilder();
        byte[] hash = digest.digest();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }

    @Override
    public void release() {
        if (updater != null) {
            updater.shutdownNow();
        }
    }
}
