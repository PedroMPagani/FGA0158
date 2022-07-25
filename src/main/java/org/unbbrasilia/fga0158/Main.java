package org.unbbrasilia.fga0158;

import lombok.Getter;
import org.unbbrasilia.fga0158.services.CompanyService;

public class Main {

    @Getter private static CompanyService service;

    public static void main(String[] args){
        service = new CompanyService();
    }

}