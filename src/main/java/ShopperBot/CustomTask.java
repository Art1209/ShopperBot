package ShopperBot;


import org.apache.http.cookie.Cookie;

import java.io.Serializable;
import java.util.TimerTask;

public class CustomTask extends TimerTask implements Serializable {
    private static int counter;
    private static TasksKeeper tasksKeeper = TasksKeeper.getTasksKeeper();

    private int id = ++counter;
    private String link;
    private long time;
    private String buttonName;
    private transient Shop shop;
    private transient Cookie cookie;

    CustomTask(){}

    CustomTask(String link, String buttonName){
        this.buttonName = buttonName;
        this.link = link;
    }

    public static ShopTaskBuilder getBuilder(){
        return new ShopTaskBuilder();
    }


    @Override
    public void run() {
        initShop();
        shop.login(this);
        shop.checkJoinedAccount(this);
        shop.doBeforeClick(this);
        shop.click(link,this);
        shop.doAfterClick(this);
        shop.extraFlow(this);


        System.out.println("CustomTask ON_AIR");
        tasksKeeper.unregister(this);
    }

    private void initShop() {
        for (Shop shop:Shop.values()){
            if (link.contains(shop.host)){
                this.shop = shop;
                break;
            }
        }
    }


    void setLink(String link) {
        this.link = link;
    }

    void setTime(long time) {
        this.time = time;
    }

    void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getLink() {
        return link;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public String getButtonName() {
        return buttonName;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return ((CustomTask)obj).getId()==this.getId();
    }

    @Override
    public String toString() {
        return getId()+" " + getTime() + getLink();
    }


    static class ShopTaskBuilder{
        private CustomTask task = new CustomTask(null,null);

        String builderLink;
        long builderTime;

        public CustomTask build(){
            if (task.getButtonName()==null||task.getTime()==0||task.getLink()==null){
                System.out.println("ShopTaskBuilder cannot build wrong task");
                return null;
            } else return task;
        }
        public ShopTaskBuilder setLink(String link){
            builderLink = link;
            task.setLink(link);
            return this;
        }
        public ShopTaskBuilder setButton(String button){
            task.setButtonName(button);
            return this;
        }
        public ShopTaskBuilder setTime(long time){
            builderTime = time;
            task.setTime(time);
            return this;
        }

        public String getBuilderLink() {
            return builderLink;
        }
    }


    private enum Shop{
        Bang(""){
            @Override
            boolean login(CustomTask task) {
                return false;
            }

            @Override
            boolean checkJoinedAccount(CustomTask task) {
                return false;
            }

            @Override
            boolean doBeforeClick(CustomTask task) {
                return false;
            }

            @Override
            boolean click(String link, CustomTask task) {
                return false;
            }

            @Override
            boolean doAfterClick(CustomTask task) {
                return false;
            }

            @Override
            boolean extraFlow(CustomTask task) {
                return false;
            }
        };
        Shop(String host){
            this.host = host;
        }
        abstract boolean  login(CustomTask task);
        abstract boolean checkJoinedAccount(CustomTask task);
        abstract boolean doBeforeClick(CustomTask task);
        abstract boolean click(String link, CustomTask task);
        abstract boolean doAfterClick(CustomTask task);
        abstract boolean extraFlow(CustomTask task);

        private String host;
        private String loginLink;
    }
}
