package ShopperBot;

import java.io.*;
import java.util.*;

public class TasksKeeper {
    private Timer timer = new Timer();
    private List<CustomTask> tasks = new ArrayList<>();
    private HttpExecuter httpExecuter = HttpExecuter.getHttpExecuter();

    private static TasksKeeper keeper;
    private TasksKeeper(){
        restore();
    }
    public static TasksKeeper getTasksKeeper(){
        if (keeper ==null)keeper = new TasksKeeper();
        return keeper;
    }

    public synchronized void register(CustomTask task){
        long ping = httpExecuter.pingCounter(task.getLink());
        long newTime = task.getTime()-ping/2;
        task.setTime(newTime);
        tasks.add(task);
        timer.schedule(task,new Date(newTime));
        backup();
    }

    public synchronized void unregister(CustomTask task){
        tasks.remove(task.getTime());
    }

    private void backup(){
        try {
            FileOutputStream fs = new FileOutputStream(new File("backup"));
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void restore(){
        try {
            FileInputStream fs = new FileInputStream(new File("backup"));
            ObjectInputStream is = new ObjectInputStream(fs);
            tasks = (ArrayList<CustomTask>)is.readObject();
            for (CustomTask task:tasks){
                timer.schedule(task,task.getTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }
    public String tasksToString(){
        String result = "";
        for (CustomTask task: tasks) {
            result+=task.toString()+System.lineSeparator();
        }
        return result;
    }
}
