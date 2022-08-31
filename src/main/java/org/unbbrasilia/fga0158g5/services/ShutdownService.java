package org.unbbrasilia.fga0158g5.services;

import org.jetbrains.annotations.NotNull;
import org.unbbrasilia.fga0158g5.dao.Logger;
import org.unbbrasilia.fga0158g5.services.base.Service;

public class ShutdownService extends Service {

    protected final @NotNull CompanyService service;
    protected final @NotNull TerminalService terminalService;

    public ShutdownService(@NotNull CompanyService companyService, @NotNull TerminalService service){
        super(new Logger("Shutdown Service Logger"));
        this.terminalService = service;
        this.service = companyService;
    }

    public void startService(){
        Runtime.getRuntime().addShutdownHook(new ShutdownServiceThread(this));
    }

    private static class ShutdownServiceThread extends Thread {

        private final @NotNull ShutdownService service;

        public ShutdownServiceThread(@NotNull ShutdownService shutdownService){
            this.service = shutdownService;
            this.setName("Shutdown Service Thread");
            this.setDaemon(true);
        }

        @Override
        public void run(){
            System.out.println("Stopping service");
            this.service.terminalService.shutdownService();
            this.service.service.shutdownService();
        }
    }

}