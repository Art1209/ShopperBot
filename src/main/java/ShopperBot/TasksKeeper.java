package ShopperBot;


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class TasksKeeper {

    private Map<Long,ShopTask> tasks = new HashMap<>();

    private static TasksKeeper keeper;
    private TasksKeeper(){}
    public static TasksKeeper getTasksKeeper(){
        if (keeper ==null)keeper = new TasksKeeper();
        return keeper;
    }

    public void register(ShopTask task){
        tasks.put(task.getTime(),task);
    }

    public boolean unregister(ShopTask task){
        tasks.remove(task);
        return false;
    }
    public String tasksToString(){
        String result = "";
        for (Long l: tasks.keySet()) {
            result+=tasks.get(l).toString()+System.lineSeparator();
        }
        return result;
    }
}
