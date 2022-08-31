package org.unbbrasilia.fga0158g5.services;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.unbbrasilia.fga0158g5.dao.Logger;
import org.unbbrasilia.fga0158g5.services.base.Service;

import java.util.Scanner;

public class TerminalService extends Service implements Runnable {

    @Getter protected final @NotNull CompanyService companyService; // dependency injection.
    protected final @NotNull Thread thread;

    public TerminalService(@NotNull CompanyService service){
        super(new Logger("Terminal Service Logger"));
        this.companyService = service;
        this.thread = new Thread(this);
    }

    @Override
    public void shutdownService() {
        this.getLogger().log("Terminal Service shutting down..");
        this.thread.stop();
    }

    public void startService(){
        this.thread.setName("Terminal Service");
        this.thread.start();
    }

    public void run(){
        while (true){
            // scan logic.
            try(Scanner scanner = new Scanner(System.in)){
                getLogger().log(scanner.nextLine());
            }
        }
    }

}