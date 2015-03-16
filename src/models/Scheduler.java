package models;

import controllers.HeaderController;

import java.util.TimerTask;

/**
 * Created by sondrehj on 09.03.2015.
 */
public class Scheduler extends TimerTask {


    @Override
    public void run(){
        HeaderController.getController().updateNotifications();
    }

}
