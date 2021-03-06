package cn.vanchee.util;

import cn.vanchee.model.User;
import cn.vanchee.service.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author vanchee
 * @date 13-1-30
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class MyFactory {

    private static OutDetailService outDetailService;
    private static InDetailService inDetailService;
    private static OwnerService ownerService;
    private static UserService userService;
    private static ConsumerService consumerService;
    private static FruitService fruitService;
    private static PaidService paidDetailService;
    private static ConsumptionService consumptionService;
    private static ResourceService resourceService;

    private static ExecutorService executorService;

    static {
        executorService = Executors.newCachedThreadPool();
        outDetailService = new OutDetailService();
        inDetailService = new InDetailService();
        ownerService = new OwnerService();
        userService = new UserService();
        consumerService = new ConsumerService();
        fruitService = new FruitService();
        paidDetailService = new PaidService();
        consumptionService = new ConsumptionService();
        resourceService = new ResourceService();
    }

    public void shutdown() {
        executorService.shutdown();
        outDetailService = null;
        inDetailService = null;
        ownerService = null;
        userService = null;
        consumerService = null;
        fruitService = null;
        paidDetailService = null;
        consumptionService = null;
        resourceService = null;
    }

    public static ExecutorService getExecutorService() {
        if (executorService != null) {
            return executorService;
        }
        return Executors.newCachedThreadPool();
    }

    public static User getCurrentUser() {
        return getUserService().getCurrentUser();
    }

    public static int getCurrentUserId() {
        return getUserService().getCurrentUserId();
    }

    public synchronized static OutDetailService getOutDetailService() {
        if (outDetailService == null) {
            outDetailService = new OutDetailService();
        }
        return outDetailService;
    }

    public synchronized static InDetailService getInDetailService() {
        if (inDetailService == null) {
            inDetailService = new InDetailService();
        }
        return inDetailService;
    }

    public synchronized static OwnerService getOwnerService() {
        if (ownerService == null) {
            ownerService = new OwnerService();
        }
        return ownerService;
    }

    public synchronized static UserService getUserService() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public synchronized static ConsumerService getConsumerService() {
        if (consumerService == null) {
            consumerService = new ConsumerService();
        }
        return consumerService;
    }

    public synchronized static FruitService getFruitService() {
        if (fruitService == null) {
            fruitService = new FruitService();
        }
        return fruitService;
    }

    public synchronized static PaidService getPaidDetailService() {
        if (paidDetailService == null) {
            paidDetailService = new PaidService();
        }
        return paidDetailService;
    }

    public synchronized static ConsumptionService getConsumptionService() {
        if (consumptionService == null) {
            consumptionService = new ConsumptionService();
        }
        return consumptionService;
    }

    public synchronized static ResourceService getResourceService() {
        if (resourceService == null) {
            resourceService = new ResourceService();
        }
        return resourceService;
    }
}
