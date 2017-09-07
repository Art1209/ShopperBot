package ShopperBot;


public class ShopTask {
    private static int counter;

    private int id = ++counter;
    private String link;
    private long time;
    private String buttonName;

    private ShopTask(String link, String buttonName){
        this.buttonName = buttonName;
        this.link = link;
    }

    public static ShopTaskBuilder getBuilder(){
        return new ShopTaskBuilder();
    }

    private void setLink(String link) {
        this.link = link;
    }

    private void setTime(long time) {
        this.time = time;
    }

    private void setButtonName(String buttonName) {
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
        return ((ShopTask)obj).getId()==this.getId();
    }

    @Override
    public String toString() {
        return getId()+" " + getTime() + getLink();
    }

    static class ShopTaskBuilder{
        private ShopTask task = new ShopTask(null,null);

        String builderLink;
        long builderTime;
        String builderButton;


        public ShopTask build(){
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
}
