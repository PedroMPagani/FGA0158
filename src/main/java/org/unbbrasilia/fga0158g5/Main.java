package org.unbbrasilia.fga0158g5;

import org.jetbrains.annotations.NotNull;
import org.unbbrasilia.fga0158g5.dao.Event;
import org.unbbrasilia.fga0158g5.services.CompanyService;
import org.unbbrasilia.fga0158g5.services.ShutdownService;
import org.unbbrasilia.fga0158g5.services.TerminalService;
import org.unbbrasilia.fga0158g5.services.base.Service;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    private static final @NotNull List<Service> services = new CopyOnWriteArrayList<>();
    public static final List<Event> events = new CopyOnWriteArrayList<>();

    public static void main(String[] args){
        CompanyService companyService = new CompanyService();
        services.add(companyService);
        TerminalService terminalService = new TerminalService(companyService);
        services.add(terminalService);
        services.add(new ShutdownService(companyService, terminalService));
        for (int i = services.size() - 1; i >= 0; i--) {
            services.get(i).startService();
        }
    }

}