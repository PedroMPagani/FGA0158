package org.unbbrasilia.fga0158g5.services;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.unbbrasilia.fga0158g5.Main;
import org.unbbrasilia.fga0158g5.dao.Event;
import org.unbbrasilia.fga0158g5.dao.Logger;
import org.unbbrasilia.fga0158g5.dao.ParkingLot;
import org.unbbrasilia.fga0158g5.dao.base.Access;
import org.unbbrasilia.fga0158g5.services.base.Service;
import org.unbbrasilia.fga0158g5.services.exceptions.*;
import org.unbbrasilia.fga0158g5.util.AccessUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

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
            log("ran");
            try {
                runOption(scanner, option);
            } catch (Exception exception){

            }
            //
        } catch (NumberFormatException exception){
            new InvalidMenuOptionException().printStackTrace();
        }
    }

    private void startSearch(Scanner scanner) throws Exception {
        try {
            log("1 - Pesquisar estacionamento");
            log("2 - Pesquisar Acesso em estacionamento");
            log("3 - Pesquisar evento");
            int option = Integer.parseInt(scanner.nextLine());
            switch (option){
                case 1:{
                    log("Digite o ID do estacionamento.");
                    try {
                        int parkId = Integer.parseInt(scanner.nextLine());
                        BigInteger bigInteger = BigInteger.valueOf(parkId);
                        ParkingLot parkingLot = companyService.parkingLots.get(bigInteger);
                        if(parkingLot == null){
                            throw new RegisterNotFoundException("Estacionamento com ID " + parkId + " não encontrado.");
                        }
                        log("Estacionamento ID: " + parkingLot.getParkingId());
                        log("Número de vagas: " + parkingLot.getMaxVehicleCapacity());
                        log("Número de vagas ocupadas: " + parkingLot.getSpotsUsed());
                    } catch (NumberFormatException exception){
                        throw new WrongValueException("O ID do estacionamento deve ser um número inteiro.");
                    }
                    break;
                }
                case 2:{
                    log("Digite o ID do estacionamento.");
                    try {
                        int parkId = Integer.parseInt(scanner.nextLine());
                        BigInteger bigInteger = BigInteger.valueOf(parkId);
                        ParkingLot parkingLot = companyService.parkingLots.get(bigInteger);
                        if(parkingLot == null){
                            throw new RegisterNotFoundException("Estacionamento com ID " + parkId + " não encontrado.");
                        }
                        log("Estacionamento ID: " + parkingLot.getParkingId());
                        log("Digite a placa que deseja fazer a pesquisa");
                        String plate = scanner.nextLine();
                        // don't use .toList() to avoid wrong JDK version,
                        // since the software doesn't specify which java version should it be ran with.
                        List<Access> accessList = parkingLot.getAcesses().stream().filter(s->s.getVehiclePlate().equals(plate)).collect(Collectors.toList());
                        if(accessList.isEmpty()){
                            throw new RegisterNotFoundException("Não foi encontrado nenhum registro com esta placa. case sensitive");
                        }
                        log("Registros encontrados: " + accessList.size());
                        log("Dados: ");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        for (int i = 0; i < accessList.size(); i++){
                            Access ac = accessList.get(i);
                            log("n " + (i + 1));
                            log("Data de entrada: " + simpleDateFormat.format(ac.getEntryTime()));
                            log("Data de saída: " + simpleDateFormat.format(ac.getLeaveTime()));
                        }

                    } catch (NumberFormatException exception){
                        throw new WrongValueException("O ID do estacionamento deve ser um número inteiro.");
                    }
                    break;
                }
                case 3:{
                    // pesquisa por evento.
                    log("Digite o nome do evento que está procurando.");
                    String eventName = scanner.nextLine();
                    List<Event> events = null;
                    for (Event event : Main.events){
                        if(event.getEventName().equals(eventName)){
                            if(events == null){
                                events = new ArrayList<>();
                            }
                            events.add(event);
                        }
                    }
                    if(events == null){
                        throw new RegisterNotFoundException("Não foi encontrado nenhum evento.");
                    }
                    log("Número de eventos encontrados com este nome: " + events.size());
                    int a = 0;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    for (Event event : events) {
                        log("Registro " + (++a));
                        log("Data de início do evento: " + simpleDateFormat.format(event.getStart()));
                        log("Data de término do evento: " + simpleDateFormat.format(event.getEnd()));
                    }
                    break;
                }
            }
        } catch (NumberFormatException ex){
            throw new InvalidMenuOptionException();
        }
    }

    private void runOption(Scanner scanner,int selectedOption) throws Exception{
        switch (selectedOption){
            case 1:{
                // create new registry.
                startRegistering(scanner);
                break;
            }
            case 2:{
                startSearch(scanner);
                break;
            }
            case 3:{
                changeRegistry(scanner);
                break;
            }
        }
    }

    private boolean confirm(Scanner sca){
        String line = sca.nextLine();
        if(line.toLowerCase().startsWith("y")){
            return true;
        }
        return false;
    }

    private void changeRegistry(Scanner scanner) throws WrongValueException {
        try {
            log("Escolha qual registro você deseja alterar:");
            log("1 - Estacionamento");
            log("2 - Acesso");
            log("3 - Evento");
            int opt = Integer.parseInt(scanner.nextLine());
            if(opt <= 0 || opt > 3) {
                throw new NumberFormatException();
            }
            switch (opt){
                case 1:{
                    log("Digite o ID do estacionamento.");
                    try {
                        changeParkLot(scanner);
                    } catch (RegisterNotFoundException | WrongValueException e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 2:{
                    log("Digite o ID do estacionamento onde o acesso está localizado.");
                    try {
                        changeAccess(scanner);
                    } catch (RegisterNotFoundException | WrongValueException e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 3:{
                    try {
                        changeEvent(scanner);
                    } catch (RegisterNotFoundException | WrongValueException e){
                        e.printStackTrace();
                    }
                }
            }

        } catch (NumberFormatException exception){
            throw new WrongValueException("Tipo de dado não existente.");
        }
    }


    private void changeEvent(Scanner scanner) throws RegisterNotFoundException, WrongValueException {
        try {
            log("Digite o nome do evento.");
            String name = scanner.nextLine();
            List<Event> accessList = Main.events.stream().filter(s->s.getEventName().equals(name)).collect(Collectors.toList());
            if(accessList.isEmpty()){
                throw new RegisterNotFoundException("Não foi encontrado nenhum evento com este nome.");
            }
            int size = accessList.size();
            log("Encontrados " + size + " eventos com este nome neste estacionamento.");
            log("Os eventos serão mostrados 1 por 1, decida qual acesso você quer alterar!");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (int i = 0; i < size; i++){
                log("Número evento "+(i + 1));
                Event ac = accessList.get(i);
                log("Data de inicio: " + simpleDateFormat.format(ac.getStart()));
                log("Data de termino: " + simpleDateFormat.format(ac.getEnd()));
            }
            log("Digite o número do evento que você deseja alterar..");
            try {
                int accessId = Integer.parseInt(scanner.nextLine());
                try {
                    Event ac = accessList.get(accessId);
                    log("Deseja alterar a data de início do evento? (Y/N)");
                    String dateFormat = "dd/MM/yyyy HH:mm";
                    if(confirm(scanner)){
                        log("Formato de data deve ser: " + dateFormat);
                        log("Insira a data de inicio:");
                        Date in = getAcessDate(scanner, dateFormat);
                        ac.setStart(in.getTime());
                    }
                    log("Deseja alterar a data de término do evento? (Y/N)");
                    if(confirm(scanner)){
                        log("Formato de data deve ser: " + dateFormat);
                        log("Insira a data de fim:");
                        Date out = getAcessDate(scanner, dateFormat);
                        Date in = new Date(ac.getStart());
                        if(!out.after(in) || out.equals(in)){
                            throw new WrongValueException("A data de término não pode ser anterior ou igual a data de inicio.");
                        }
                        ac.setEnd(out.getTime());
                    }
                } catch (ArrayIndexOutOfBoundsException exception){
                    throw new RegisterNotFoundException("Não foi encontrado um acesso com este número.");
                }
            } catch (NumberFormatException exception){
                throw new WrongValueException("O número do acesso deveria ter sido um número inteiro.");
            }
        } catch (NumberFormatException exception){
            throw new WrongValueException("O Id do estacionamento deve ser um número inteiro.");
        }

    }

    private void changeAccess(Scanner scanner) throws RegisterNotFoundException, WrongValueException {
        try {
            int parkId = Integer.parseInt(scanner.nextLine());
            BigInteger bigInteger = BigInteger.valueOf(parkId);
            ParkingLot parkingLot = companyService.parkingLots.get(bigInteger);
            if(parkingLot == null){
                throw new RegisterNotFoundException("Estacionamento com ID " + parkId + " não encontrado.");
            }
            log("Digite a placa do acesso.");
            String placa = scanner.nextLine();
            List<Access> accessList = parkingLot.getAcesses().stream().filter(s->s.getVehiclePlate().equals(placa)).collect(Collectors.toList());
            if(accessList.isEmpty()){
                throw new RegisterNotFoundException("Não foi encontrado nenhum acesso com esta placa.");
            }
            int size = accessList.size();
            log("Encontrados " + size + " acessos com esta placa neste estacionamento.");
            log("Os acessos serão mostrados 1 por 1, decida qual acesso você quer alterar!");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (int i = 0; i < size; i++){
                log("Número Acesso "+(i + 1));
                Access ac = accessList.get(i);
                log("Data de entrada: " + simpleDateFormat.format(ac.getEntryTime()));
                log("Data de saída: " + simpleDateFormat.format(ac.getLeaveTime()));
            }
            log("Digite o número do acesso.");
            try {
                int accessId = Integer.parseInt(scanner.nextLine());
                try {
                    Access ac = accessList.get(accessId);
                    log("Deseja alterar a data de entrada do acesso? (Y/N)");
                    String dateFormat = "dd/MM/yyyy HH:mm";
                    boolean rcPrice = false;
                    if(confirm(scanner)){
                        log("Formato de data deve ser: " + dateFormat);
                        log("Insira a data de entrada:");
                        Date in = getAcessDate(scanner, dateFormat);
                        ac.setEntryTime(in.getTime());
                        rcPrice = true;
                    }
                    log("Deseja alterar a data de saída do acesso? (Y/N)");
                    if(confirm(scanner)){
                        log("Formato de data deve ser: " + dateFormat);
                        log("Insira a data de saída:");
                        Date out = getAcessDate(scanner, dateFormat);
                        Date in = new Date(ac.getEntryTime());
                        if(!out.after(in) || out.equals(in)){
                            ac.calculatePrice(parkingLot);
                            throw new WrongValueException("A data de saída não pode ser anterior ou igual a data de entrada.");
                        }
                        ac.setLeaveTime(out.getTime());
                        rcPrice = true;
                    }
                    if(rcPrice){
                        ac.calculatePrice(parkingLot);
                    }
                } catch (ArrayIndexOutOfBoundsException exception){
                    throw new RegisterNotFoundException("Não foi encontrado um acesso com este número.");
                }
            } catch (NumberFormatException exception){
                throw new WrongValueException("O número do acesso deveria ter sido um número inteiro.");
            }
        } catch (NumberFormatException exception){
            throw new WrongValueException("O Id do estacionamento deve ser um número inteiro.");
        }

    }

    private void changeParkLot(Scanner scanner) throws RegisterNotFoundException, WrongValueException {
        try {
            int parkId = Integer.parseInt(scanner.nextLine());
            BigInteger bigInteger = BigInteger.valueOf(parkId);
            ParkingLot parkingLot = companyService.parkingLots.get(bigInteger);
            if(parkingLot == null){
                throw new RegisterNotFoundException("Estacionamento com ID " + parkId + " não encontrado.");
            }
            log("Estacionamento ID: " + parkingLot.getParkingId());
            log("Deseja alterar horário de abertura? (Y/N)");
            if(confirm(scanner)){
                log("Digite o horário no formato hora:minuto, 00:00 para estacionamentos 24h.");
                Date time = getAcessDate(scanner,"HH:mm");
                parkingLot.getPricingInformation().setOpensAt(time.getTime());
            }
            log("Deseja alterar horário de fechamento? (Y/N)");
            if(confirm(scanner)){
                log("Digite o horário no formato hora:minuto, 00:00 para estacionamentos 24h.");
                Date time = getAcessDate(scanner,"HH:mm");
                parkingLot.getPricingInformation().setClosesAt(time.getTime());
            }
            log("Deseja aumentar o número máximo de veículos? (Y/N)");
            if(confirm(scanner)) {
                log("Digite o número máximo de veículos.");
                int total = getInt(scanner.nextLine());
                parkingLot.setMaxVehicleCapacity(total);
            }
            log("Deseja trocar o preço de fração de 15 min? (Y/N)");
            if(confirm(scanner)){
                log("Digite o novo preço, não pode ser menor que 0.");
                double price = getDouble(scanner.nextLine());
                parkingLot.getPricingInformation().setFractionValue(price);
            }
            log("Deseja trocar o desconto de hora cheia? (Y/N)");
            if(confirm(scanner)){
                log("Digite a nova porcentagem de desconto, não se esqueça de inserir %.");
                double percentage = getPercentage(scanner.nextLine());
                if(percentage < 0){
                    throw new WrongValueException("A porcentagem não pode ser menor de 0.");
                }
                parkingLot.getPricingInformation().setFullHourDiscount(percentage/100);
            }
            log("Deseja trocar o valor de acesso de dia? (Y/N)");
            if(confirm(scanner)){
                log("Digite o novo preço de acesso diária.");
                double money = getDouble(scanner.nextLine());
                if(money < 0){
                    throw new WrongValueException("A preço não pode ser menor de 0.");
                }
                parkingLot.getPricingInformation().setFullDayValue(money);
            }
            log("Deseja trocar a porcentagem de desconto da noite? (Y/N)");
            if(confirm(scanner)){
                log("Digite o novo valor de desconto noturno. não se esqueça de por %, ex: 50%");
                double percentage = getPercentage(scanner.nextLine());
                if(percentage < 0){
                    throw new WrongValueException("A porcenteagem não pode ser menor de 0.");
                }
                parkingLot.getPricingInformation().setFullNightPercentage(percentage/100);
            }
            log("Deseja trocar a porcentagem de retorno do contratante? (Y/N)");
            if(confirm(scanner)){
                log("Digite o novo valor da porcentagem. não se esqueça de por %, ex: 50%");
                double percentage = getPercentage(scanner.nextLine());
                if(percentage < 0){
                    throw new WrongValueException("A porcentagem não pode ser menor de 0.");
                }
                parkingLot.getPricingInformation().setPercentReturnHiring(percentage/100);
            }
            log("Deseja trocar a porcentagem de retorno do contratante? (Y/N)");
            if(confirm(scanner)){
                log("Digite o novo valor da porcentagem. não se esqueça de por %, ex: 50%");
                double percentage = getPercentage(scanner.nextLine());
                if(percentage < 0){
                    throw new WrongValueException("A porcentagem não pode ser menor de 0.");
                }
                parkingLot.getPricingInformation().setPercentReturnHiring(percentage/100);
            }
            log("Deseja trocar o preço de acessos em eventos? (Y/N)");
            if(confirm(scanner)){
                log("Digite o novo valor do acesso:");
                double price = getDouble(scanner.nextLine());
                if(price < 0){
                    throw new WrongValueException("O valor não pode ser menor de 0.");
                }
                parkingLot.getPricingInformation().setEventPrice(price);
            }
            log("Deseja trocar o preço de acessos no mes? (Y/N)");
            if(confirm(scanner)){
                log("Digite o novo valor do acesso:");
                double price = getDouble(scanner.nextLine());
                if(price < 0){
                    throw new WrongValueException("O valor não pode ser menor de 0.");
                }
                parkingLot.getPricingInformation().setFullMonthPrice(price);
            }
            log("Deseja alterar horário de início da noite? (Y/N)");
            if(confirm(scanner)){
                log("Digite o horário no formato hora:minuto, 00:00 para estacionamentos 24h.");
                Date time = getAcessDate(scanner,"HH:mm");
                parkingLot.getPricingInformation().setStartNightHour((time.getHours()));
                parkingLot.getPricingInformation().setStartNightMinute((time.getMinutes()));
            }
            log("Deseja alterar horário de término da noite? (Y/N)");
            if(confirm(scanner)){
                log("Digite o horário no formato hora:minuto, 00:00 para estacionamentos 24h.");
                Date time = getAcessDate(scanner,"HH:mm");
                parkingLot.getPricingInformation().setEndNightHour((time.getHours()));
                parkingLot.getPricingInformation().setEndNightMinute((time.getMinutes()));
            }
        } catch (NumberFormatException exception){
            throw new WrongValueException("O ID do estacionamento deve ser um número inteiro.");
        }

    }

    private void showRegisterMenuOptions(){
        log("Operações possíveis:");
        log(" 1 - Cadastrar um estacionamento");
        log(" 2 - Cadastrar um acesso");
        log(" 3 - Cadastrar um evento");
    }

    private void showMenu(){
        log("Operações possíveis: ");
        log(" 1 - Realizar cadastros.");
        log(" 2 - Pesquisar cadastros");
        log(" 3 - Alterar dados de um cadastro em um estacionamento.");
        log(" 4 - Excluir dados.");
        log(" 5 - Debug all parking lots.");
    }

    private void startRegistering(Scanner scanner){
        try {
            showRegisterMenuOptions();
            int opt = readRegisterOption(scanner);
            if(opt == 1){
                registerParkingLot(scanner);
            }
            if(opt == 2){
                registerAccess(scanner);
            }
            if(opt == 3){
                registerEvent(scanner);
            }
        } catch (Exception exception){
            // do nothing here.
        }
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
        log("Insira a placa do acesso.");
        String carPlate = scanner.nextLine();
        String dateFormat = "dd/MM/yyyy HH:mm";
        log("Formato de data deve ser: " + dateFormat);
        log("Insira a data de entrada:");
        Date in = getAcessDate(scanner, dateFormat);
        if(in == null) return;
        log("Insira a data de saída:");
        Date out = getAcessDate(scanner, dateFormat);
        if(out == null) return;
        if(out.equals(in) || !out.after(in)){
            new InvalidAccessValueException("A data de saída não pode " +
                    "ser igual/anterior a data de entrada.").printStackTrace();
            return;
        }
        log("Insira o número do estacionamento: ");
        String parkId = scanner.nextLine();
        try {
            BigInteger id = new BigDecimal(parkId).toBigInteger();
            ParkingLot parkingLot = companyService.parkingLots.get(id);
            if(parkingLot == null){
                throw new RegisterNotFoundException("Não foi encontrado o estacionamento com ID: " + id);
            }
            if(!parkingLot.hasFreeSpot()){
                throw new RegistryUnallowedException("O estacionamento não tem mais vagas livres!");
            }
            // add access ->
            Access access = AccessUtil.returnRegistry(parkingLot, carPlate, in, out);
            access.calculatePrice(parkingLot);
            parkingLot.addAccess(access);
            log("Registro concluído!");
            if(access.getPrice() != null){
                log("");
                log("Access price: ");
                log("Total: " + access.getPrice().getKey());
                log("Contratante: " + access.getPrice().getValue());
                log("");
            }
        } catch (NumberFormatException exception){
            new InvalidAccessValueException("A data de saída não pode ser igual/anterior a data de entrada.").printStackTrace();
        }
    }

    // * Estacionamento: capacidade vagas, valor fracao 15 minutos,
    // * valor hora cheia, valor diaria diurna, valor diaria noturna (em %)
    private void registerParkingLot(Scanner scanner) throws Exception {
        log("Qual a capacidade total de carros?");
        String input = scanner.nextLine();
        int spots = getInt(input);
        if(spots <= 0) return;
        log("Insira preço de fração de 15 minutos");
        input = scanner.nextLine();
        double fractionPrice = getDouble(input);
        log("Insira o valor de desconto na hora:");
        input = scanner.nextLine();
        double fullHourDiscount = getPercentage(input);
        log("Insira o preço diário: ");
        input = scanner.nextLine();
        double fullDayDay = getDouble(input);
        log("Insira o desconto noturno: ");
        input = scanner.nextLine();
        double percentageNight = getPercentage(input);
        log("Insira o preço durante eventos: ");
        input = scanner.nextLine();
        double eventPrice = getDouble(input);
        log("Insira o preço de pacote mensal: ");
        input = scanner.nextLine();
        double fullMonthPrice = getDouble(input);
        log("Insira a porcentagem do retorno, utilize %, exemplo: 30%");
        input = scanner.nextLine();
        double returnPercentage = getPercentage(input);

        log("Insira o horário que o estacionamento abre, para 24 horas, digite 00:00. formato: hora:minuto ");
        LocalTime opensAt = getAcessDate2(scanner, "HH:mm");
        log("Insira o horário que o estacionamento fecha, para 24 horas, digite 00:00. formato: hora:minuto ");
        LocalTime closesAt = getAcessDate2(scanner, "HH:mm");

        log("Insira o horário que o estacionamento começará a considerar acesso noite, formato: hora:minuto");
        LocalTime nightStartAt = getAcessDate2(scanner, "HH:mm");
        log("Insira o horário que o estacionamento para a considerar acesso noite, formato: hora:minuto");
        LocalTime nightStopAt = getAcessDate2(scanner, "HH:mm");

        LocalDate today = LocalDate.now();
        ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(Instant.now());

        ParkingLot.ParkingInformation information = new ParkingLot.ParkingInformation(
                fractionPrice,fullHourDiscount/100,
                fullDayDay, percentageNight/100,
                returnPercentage/100, opensAt.toEpochSecond(today, offset),
                closesAt.toEpochSecond(today, offset),
                eventPrice, fullMonthPrice,
                nightStartAt.getHour(), nightStartAt.getMinute(),
                nightStopAt.getHour(), nightStopAt.getMinute());

        ParkingLot parkingLot = new ParkingLot(companyService.parkingLots.size(), spots, information);

        companyService.parkingLots.put(BigInteger.valueOf(parkingLot.getParkingId()),parkingLot); // added it to cache.
        getLogger().log("Estacionamento registrado com o ID: " + parkingLot.getParkingId());
    }

    //     * Evento: nome do evento, data de inicio e fim, hora de inicio e fim.
    private void registerEvent(Scanner scanner) throws RegistryUnallowedException {
        log("Insira o nome do evento:");
        String eventName = scanner.nextLine();
        String dateFormat = "dd/MM/yyyy HH:mm";
        log("Insira a data de início do evento no formato dd/MM/yyyy HH:mm, ou seja: dia/mes/ano hora:minuto");
        Date start = getAcessDate(scanner, dateFormat);
        log("Insira a data do término do evento no formato dd/MM/yyyy HH:mm, ou seja: dia/mes/ano hora:minuto");
        Date finish = getAcessDate(scanner, dateFormat);
        if(!finish.after(start) || finish.equals(start)){
            throw new RegistryUnallowedException("A data de términou não pode ser anterior ou igual a data de início de evento.");
        }
        Event event = new Event(start.getTime(), finish.getTime(), eventName);
        Main.events.add(event);
        log("Evento registrado!");
    }

    private double getPercentage(String input) throws WrongValueException {
        if(!input.contains("%")){
            throw new WrongValueException("O número inserido precisa ser em porcentagem e conter %.");
        }
        float percentage = 0;
        try {
            percentage = Float.parseFloat(input.substring(0, input.length() - 1));
        } catch (Exception ex){
            throw new WrongValueException("O número inserido deve ser porcentagem, contendo %. ex: 50%");
        }
        return percentage;
    }

    private double getDouble(String input) throws WrongValueException {
        double spots = 0;
        try {
            spots = Double.parseDouble(input);

        } catch (NumberFormatException e){
            throw new WrongValueException("O número inserido deveria ser fracionário.");
        }
        if(Double.isNaN(spots) && Double.isInfinite(spots) ||
        spots < 0){
            throw new WrongValueException("O número inserido deveria ser fracionário.");
        }
        return spots;
    }

    private int getInt(String input) throws WrongValueException {
        int spots;
        try {
            spots = Integer.parseInt(input);
        } catch (NumberFormatException e){
            throw new WrongValueException("O número inserido deveria ser inteiro.");
        }
        if(spots <= 0){
            throw new WrongValueException("O número inteiro não pode ser menor ou igual a 0.");
        }
        return spots;
    }

    public Date getAcessDate(Scanner scanner, String pattern){
        try {
            return new SimpleDateFormat(pattern).parse(scanner.nextLine());
        } catch (Exception ignored){
            ignored.printStackTrace();
            new InvalidAccessValueException("Data digitada incorreta.").printStackTrace();
        }
        return null;
    }


    private LocalTime getAcessDate2(Scanner scanner, String pattern) throws WrongValueException {
        try {
            return LocalTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern(pattern));
        } catch (Exception exception){
            throw new WrongValueException("A data inserida está incorreta, siga as instruções.");
        }
    }

    private void log(String str){
        getLogger().log(str);
    }

}