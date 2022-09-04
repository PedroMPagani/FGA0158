package org.unbbrasilia.fga0158g5.services;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.unbbrasilia.fga0158g5.dao.Logger;
import org.unbbrasilia.fga0158g5.dao.ParkingLot;
import org.unbbrasilia.fga0158g5.dao.base.Access;
import org.unbbrasilia.fga0158g5.services.base.Service;
import org.unbbrasilia.fga0158g5.services.exceptions.InvalidAccessValueException;
import org.unbbrasilia.fga0158g5.services.exceptions.InvalidMenuOptionException;
import org.unbbrasilia.fga0158g5.services.exceptions.RegisterNotFoundException;
import org.unbbrasilia.fga0158g5.util.AcessUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TerminalService extends Service implements Runnable {

    @Getter protected final @NotNull CompanyService companyService; // dependency injection.
    protected final @NotNull Thread thread;
    private int selectedOption = -1; // idle!

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
        // scan logic.

        try (Scanner scanner = new Scanner(System.in)){
            while (true){
                showMenu();
                readOption(scanner);
            }
        }

    }

    private void readOption(Scanner scanner){
        String line = scanner.nextLine();
        try {
            int option = Integer.parseInt(line);
            if(option > 4 || option < 1){
                try {
                    throw new InvalidMenuOptionException();
                } catch (InvalidMenuOptionException e){
                    e.printStackTrace();
                }
            }
            System.out.println("ran");
            runOption(scanner, option);
            //
        } catch (NumberFormatException exception){
            new InvalidMenuOptionException().printStackTrace();
        }
    }

    private void runOption(Scanner scanner,int selectedOption){
        switch (selectedOption){
            case 1:{
                // create new registry.
                startRegistering(scanner);
                break;
            }

        }
    }

    private void showMenu(){
        System.out.println("Operações possíveis: ");
        System.out.println(" 1 - Cadastrar uso em um estacionamento");
        System.out.println(" 2 - Pesquisar um cadastro em um estacionamento.");
        System.out.println(" 3 - Alterar dados de um cadastro em um estacionamento.");
        System.out.println(" 4 - Excluir cadastro em um estacionamento.");
    }

    private void startRegistering(Scanner scanner){
        try {
            showRegisterMenuOptions();
            int opt = readRegisterOption(scanner);
            if(opt == 1){

            }
            if(opt == 2){
                registerAccess(scanner);
            }
        } catch (Exception exception){
            // do nothing here.
        }
    }

    private void showRegisterMenuOptions(){
        System.out.println("Operações possíveis:");
        System.out.println(" 1 - Cadastrar um estacionamento");
        System.out.println(" 2 - Cadastrar um acesso");
        System.out.println(" 3 - Cadastrar um evento");
    }

    private int readRegisterOption(Scanner scanner){
        String line = scanner.nextLine();
        try {
            int option = Integer.parseInt(line);
            if(option > 4 || option < 1){
                try {
                    throw new InvalidMenuOptionException();
                } catch (InvalidMenuOptionException e){
                    e.printStackTrace();
                }
            }
            return option;
        } catch (NumberFormatException exception){
            new InvalidMenuOptionException().printStackTrace();
        }
        return 0;
    }

    //     * Acesso: placa, data de entrada e saida, hora de entrada e saida;
    private void registerAccess(Scanner scanner) throws Exception {
        System.out.println("Insira a placa do acesso.");
        String carPlate = scanner.nextLine();
        String dateFormat = "dd/MM/yyyy HH:mm";
        System.out.println("Formato de data deve ser: " + dateFormat);
        System.out.println("Insira a data de entrada:");
        LocalDateTime in = getAcessDate(scanner, dateFormat);
        if(in == null) return;
        System.out.println("Insira a data de saída:");
        LocalDateTime out = getAcessDate(scanner, dateFormat);
        if(out == null) return;
        if(out.isEqual(in) || out.isAfter(in)){
            new InvalidAccessValueException("A data de saída não pode " +
                    "ser igual/anterior a data de entrada.").printStackTrace();
            return;
        }
        System.out.println("Insira o número do estacionamento: ");
        String parkId = scanner.nextLine();
        try {
            BigInteger id = new BigDecimal(parkId).toBigInteger();
            ParkingLot parkingLot = companyService.parkingLots.get(id);
            if(parkingLot == null){
                throw new RegisterNotFoundException("Não foi encontrado o estacionamento com ID: " + id);
            }
            // add access ->
            Access acess = AcessUtil.returnRegistry(parkingLot, carPlate, in, out);
            System.out.println("Registro concluído!");
        } catch (NumberFormatException exception){
            new InvalidAccessValueException("A data de saída não pode " +
                    "ser igual/anterior a data de entrada.").printStackTrace();
        }
    }

    public LocalDateTime getAcessDate(Scanner scanner, String pattern){
        try {
            LocalDateTime in = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern(pattern));
            return in;
        } catch (Exception ignored){
            new InvalidAccessValueException("Data digitada incorreta.").printStackTrace();
        }
        return null;
    }

    //     * Estacionamento: capacidade vagas, valor fracao 15 minutos,
    //     * valor hora cheia, valor diaria diurna, valor diaria noturna (em %)
    private void registerParkingLot(Scanner scanner){

    }

    //     * Evento: nome do evento, data de inicio e fim, hora de inicio e fim.
    private void registerEvent(Scanner scanner){

    }

}